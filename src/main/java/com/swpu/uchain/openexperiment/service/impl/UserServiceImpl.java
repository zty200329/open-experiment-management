package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.VerifyCode;
import com.swpu.uchain.openexperiment.VO.permission.RoleInfoVO;
import com.swpu.uchain.openexperiment.VO.user.UserInfoVO;
import com.swpu.uchain.openexperiment.VO.user.UserManageInfo;
import com.swpu.uchain.openexperiment.domain.Teacher;
import com.swpu.uchain.openexperiment.mapper.*;
import com.swpu.uchain.openexperiment.domain.Role;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.domain.UserProjectGroup;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.JoinStatus;
import com.swpu.uchain.openexperiment.enums.MemberRole;
import com.swpu.uchain.openexperiment.enums.UserType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.user.LoginForm;
import com.swpu.uchain.openexperiment.form.user.UserUpdateForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.UserKey;
import com.swpu.uchain.openexperiment.redis.key.UserProjectGroupKey;
import com.swpu.uchain.openexperiment.redis.key.VerifyCodeKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.security.JwtTokenUtil;
import com.swpu.uchain.openexperiment.security.JwtUser;
import com.swpu.uchain.openexperiment.service.*;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 用户登录实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;
    private RedisService redisService;
    private RoleService roleService;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private AclService aclService;
    private PasswordEncoder passwordEncoder;
    private UserProjectGroupMapper userProjectGroupMapper;
    private ConvertUtil convertUtil;
    private GetUserService getUserService;
    private UserRoleMapper userRoleMapper;
    private TeacherMapper teacherMapper;

    private static String PASSWORD = "open_experiment";

    @Autowired
    public UserServiceImpl(UserMapper userMapper, RedisService redisService,
                           RoleService roleService, GetUserService getUserService,
                           AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                           AclService aclService, UserProjectGroupMapper userProjectGroupMapper,
                           PasswordEncoder passwordEncoder, ConvertUtil convertUtil,
                           UserRoleMapper userRoleMapper, TeacherMapper teacherMapper
                           ) {
        this.userMapper = userMapper;
        this.redisService = redisService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.aclService = aclService;
        this.passwordEncoder = passwordEncoder;
        this.userProjectGroupMapper = userProjectGroupMapper;
        this.convertUtil = convertUtil;
        this.getUserService = getUserService;
        this.userRoleMapper = userRoleMapper;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public boolean insert(User user) {
        if (userMapper.insert(user) == 1){
            redisService.set(UserKey.getByUserId, Long.valueOf(user.getCode()) + "", user);
            redisService.set(UserKey.getUserByUserCode, user.getCode(), user);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        if (userMapper.updateByPrimaryKey(user) == 1){
            redisService.set(UserKey.getByUserId, Long.valueOf(user.getCode()) + "", user);
            redisService.set(UserKey.getUserByUserCode, user.getCode(), user);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return;
        }
        redisService.delete(UserKey.getByUserId, id + "");
        redisService.delete(UserKey.getUserByUserCode, user.getCode());
        userMapper.deleteByPrimaryKey(id);
    }


    @Override
    public Result login(String clientIp, LoginForm loginForm) {
        log.info("当前请求ip : {}",clientIp);
        User user = getUserService.selectByUserCodeAndRole(loginForm.getUserCode(),loginForm.getRole());

        //验证用户密码及其角色是否存在
        if (user == null) {
            return Result.error(CodeMsg.USER_NO_EXIST);
        }
        log.info("=============校验用户的密码================");
        Authentication token = new UsernamePasswordAuthenticationToken(loginForm.getUserCode(), PASSWORD);
        Authentication authentication = authenticationManager.authenticate(token);
        //认证通过放入容器中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails;
       List<String> aclUrl = aclService.getUserAclUrl(Long.valueOf(user.getCode()));
         userDetails =new JwtUser(loginForm.getUserCode(), passwordEncoder.encode(user.getPassword()), aclUrl);
        log.info("加载数据库中的userDetails: {}", userDetails);
        //生成真正的token
        final String realToken = jwtTokenUtil.generateToken(userDetails);
        //获取用户的所有角色
        List<Integer> roles = userRoleMapper.selectUserRolesById(Long.valueOf(loginForm.getUserCode()));
        Map<String, Object> map = new HashMap<>(8);
        map.put("token",realToken);
        map.put("roles",roles);
        map.put("userId",user.getCode());
        map.put("name",user.getRealName());
        redisService.delete(VerifyCodeKey.getByClientIp, clientIp);
        return Result.success(map);
    }

    @Override
    public String sendVerifyCode(String clientIp) throws IOException {
        VerifyCode code = new VerifyCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        StringBuffer randomCode = new StringBuffer();
        randomCode.append(text);
        redisService.set(VerifyCodeKey.getByClientIp, clientIp, text.toLowerCase());
        log.info("redis-signCode =======> {}", randomCode.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String jpg_base64 = encoder.encode(bytes).trim();
        jpg_base64 = jpg_base64.replaceAll("\n", "").replaceAll("\r", "");
        return jpg_base64;
    }

    @Override
    public boolean checkVerifyCode(String clientIp, String code) {
        code = code.toLowerCase();
        String verifyCode = redisService.get(VerifyCodeKey.getByClientIp, clientIp, String.class);
        if (code.equals(verifyCode)){
            return true;
        }
        return false;
    }


    @Override
    public User selectByUserId(Long userId) {
        User user = redisService.get(UserKey.getByUserId, userId + "", User.class);
        if (user == null){
            user = userMapper.selectByUserCode(String.valueOf(userId));
            if (user != null){
                redisService.set(UserKey.getByUserId, Long.valueOf(user.getCode()) + "", user);
            }
        }
        return user;
    }

    @Override
    public List<User> selectProjectJoinedUsers(Long projectId) {
        return userMapper.selectProjectJoinedUsers(projectId);
    }

    /**
     *
     * @param userCodes  用户学工号
     * @param projectGroupId 项目组ID
     * @param userType 用户类型
     * @return 方法调用结果
     */
    @Override
    public Result createUserJoin(String[] userCodes, Long projectGroupId, UserType userType) {
        for (String userCode : userCodes) {
            User user = getUserService.selectByUserCode(userCode);
            if (user == null){
                return Result.error(CodeMsg.USER_NO_EXIST);
            }
            UserProjectGroup userProjectGroup = new UserProjectGroup();
            userProjectGroup.setUserId(Long.valueOf(user.getCode()));
            if (userType.getValue() != UserType.STUDENT.getValue().intValue()){
                userProjectGroup.setMemberRole(MemberRole.GUIDANCE_TEACHER.getValue());
                userProjectGroup.setStatus(JoinStatus.JOINED.getValue());
            }else {
                userProjectGroup.setMemberRole(MemberRole.NORMAL_MEMBER.getValue());
                userProjectGroup.setStatus(JoinStatus.JOINED.getValue());
            }
            userProjectGroup.setUpdateTime(new Date());
            userProjectGroup.setJoinTime(new Date());
            userProjectGroup.setProjectGroupId(projectGroupId);
            Result result = addUserProject(userProjectGroup);
            if (result.getCode() != 0){
                return result;
            }
        }
        return Result.success();
    }

    public Result addUserProject(UserProjectGroup userProjectGroup) {
        if (insert(userProjectGroup)){
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    public boolean insert(UserProjectGroup userProjectGroup) {
        int result = userProjectGroupMapper.insert(userProjectGroup);
        if (result == 1){
            redisService.set(UserProjectGroupKey.getByProjectGroupIdAndUserId,
                    userProjectGroup.getId() + "_" + userProjectGroup.getUserId(),
                    userProjectGroup);
            return true;
        }
        return false;
    }

    @Override
    public User selectGroupLeader(Long projectGroupId) {
        return userMapper.selectGroupLeader(projectGroupId);
    }

    @Override
    public Result updateUserInfo(UserUpdateForm userUpdateForm) {
        User user = selectByUserId(userUpdateForm.getUserId());
        if (user == null){
            return Result.error(CodeMsg.USER_NO_EXIST);
        }
        User currentUser = getUserService.getCurrentUser();
        if (!Long.valueOf(user.getCode()).equals(Long.valueOf(currentUser.getCode()))){
            return Result.error(CodeMsg.PERMISSION_DENNY);
        }
        BeanUtils.copyProperties(userUpdateForm, user);
        if (update(user)) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public Result getMyInfo() {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        User userInfo = userMapper.selectByUserCode(currentUser.getCode());
        if (userInfo != null) {
            return Result.success(userInfo);
        }else {
            return Result.success(teacherMapper.selectByUserCode(currentUser.getCode()));
        }
    }

    @Override
    public Result getManageUsersByKeyWord(String keyWord) {
        if (StringUtils.isEmpty(keyWord)){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        List<User> users = selectByKeyWord(keyWord, true);
        List<UserManageInfo> userList = convertUtil.convertUsers(users);
        return Result.success(userList);
    }

    @Override
    public Result getUserInfoByUserId(Long userId) {
        return Result.success(userMapper.selectByUserCode(String.valueOf(userId)));
    }

    @Override
    public List<User> selectByKeyWord(String keyWord, boolean isTeacher) {

        //用户更新无须修改缓存,因为用户无法修改基本信息(这里前端只需要基本信息)
        List<User> users = (List<User>) redisService.getList(UserKey.getByKeyWord, keyWord + isTeacher);
        if (users == null || users.size() == 0){
            users = userMapper.selectByRandom(keyWord, isTeacher);
            if (users != null && users.size() != 0){
                redisService.set(UserKey.getByKeyWord, keyWord + isTeacher, users);
                redisService.setList(UserKey.getByKeyWord, keyWord + isTeacher, users);
            }
        }
        return users;
    }
}
