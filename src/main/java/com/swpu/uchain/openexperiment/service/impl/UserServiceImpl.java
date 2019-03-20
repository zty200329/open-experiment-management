package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.VerifyCode;
import com.swpu.uchain.openexperiment.VO.permission.RoleInfoVO;
import com.swpu.uchain.openexperiment.VO.user.UserInfoVO;
import com.swpu.uchain.openexperiment.VO.user.UserManageInfo;
import com.swpu.uchain.openexperiment.dao.AclMapper;
import com.swpu.uchain.openexperiment.dao.RoleMapper;
import com.swpu.uchain.openexperiment.dao.UserMapper;
import com.swpu.uchain.openexperiment.domain.Role;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.domain.UserProjectGroup;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.JoinStatus;
import com.swpu.uchain.openexperiment.enums.MemberRole;
import com.swpu.uchain.openexperiment.enums.UserType;
import com.swpu.uchain.openexperiment.form.user.LoginForm;
import com.swpu.uchain.openexperiment.form.user.UserUpdateForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.UserKey;
import com.swpu.uchain.openexperiment.redis.key.VerifyCodeKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.security.JwtTokenUtil;
import com.swpu.uchain.openexperiment.service.RoleService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import com.swpu.uchain.openexperiment.service.UserService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 用户登录实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserProjectService userProjectService;
    @Autowired
    private AclMapper aclMapper;
    @Override
    public boolean insert(User user) {
        if (userMapper.insert(user) == 1){
            redisService.set(UserKey.getByUserId, user.getId() + "", user);
            redisService.set(UserKey.getUserByUserCode, user.getCode(), user);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        if (userMapper.updateByPrimaryKey(user) == 1){
            redisService.set(UserKey.getByUserId, user.getId() + "", user);
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
        if (!checkVerifyCode(clientIp, loginForm.getVerifyCode())){
            return Result.error(CodeMsg.VERIFY_CODE_ERROR);
        }
        User user = selectByUserCode(loginForm.getUserCode());
        if (user == null){
            return Result.error(CodeMsg.USER_NO_EXIST);
        }
        log.info("=============校验用户的密码================");
        Authentication token = new UsernamePasswordAuthenticationToken(loginForm.getUserCode(), loginForm.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        //认证通过放入容器中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginForm.getUserCode());
        log.info("加载数据库中的userDetails: {}", userDetails);
        //生成真正的token
        final String realToken = jwtTokenUtil.generateToken(userDetails);
        redisService.delete(VerifyCodeKey.getByClientIp, clientIp);
        return Result.success(realToken);
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
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        if (authentication != null && !"anonymousUser".equals(name)){
            return selectByUserCode(name);
        }
        return null;
    }

    @Override
    public User selectByUserCode(String userCode) {
        User user = redisService.get(UserKey.getUserByUserCode, userCode, User.class);
        if (user != null){
            return user;
        }
        user = userMapper.selectByUserCode(userCode);
        if (user != null){
            redisService.set(UserKey.getUserByUserCode, userCode, user);
        }
        return user;
    }

    @Override
    public User selectByUserId(Long userId) {
        User user = redisService.get(UserKey.getByUserId, userId + "", User.class);
        if (user == null){
            user = userMapper.selectByPrimaryKey(userId);
            if (user != null){
                redisService.set(UserKey.getByUserId, user.getId() + "", user);
            }
        }
        return user;
    }

    @Override
    public List<User> selectProjectJoinedUsers(Long projectId) {
        return userMapper.selectProjectJoinedUsers(projectId);
    }

    @Override
    public Result createUserJoin(String[] teacherCodes, Long projectGroupId, UserType userType) {
        for (String teacherCode : teacherCodes) {
            User user = selectByUserCode(teacherCode);
            if (user == null){
                return Result.error(CodeMsg.USER_NO_EXIST);
            }
            UserProjectGroup userProjectGroup = new UserProjectGroup();
            userProjectGroup.setUserId(user.getId());
            if (userType != UserType.STUDENT){
                userProjectGroup.setTechnicalRole(ConvertUtil.getTechnicalRole(user.getUserType()));
                userProjectGroup.setMemberRole(MemberRole.GUIDANCE_TEACHER.getValue());
            }else {
                userProjectGroup.setMemberRole(MemberRole.NORMAL_MEMBER.getValue());
            }
            userProjectGroup.setStatus(JoinStatus.APPLYING.getValue());
            userProjectGroup.setUpdateTime(new Date());
            userProjectGroup.setJoinTime(new Date());
            userProjectGroup.setProjectGroupId(projectGroupId);
            Result result = userProjectService.addUserProject(userProjectGroup);
            if (result.getCode() != 0){
                return result;
            }
        }
        return Result.success();
    }

    @Override
    public User selectGroupLeader(Long projectGroupId) {
        User user = redisService.get(UserKey.getLeaderByGroupId, projectGroupId + "", User.class);
        if (user == null){
            user = userMapper.selectGroupLeader(projectGroupId);
            if (user != null){
                redisService.set(UserKey.getLeaderByGroupId, projectGroupId + "", user);
            }
        }
        return user;
    }

    @Override
    public Result updateUserInfo(UserUpdateForm userUpdateForm) {
        User user = selectByUserId(userUpdateForm.getUserId());
        if (user == null){
            return Result.error(CodeMsg.USER_NO_EXIST);
        }
        User currentUser = getCurrentUser();
        if (user.getId().intValue() != currentUser.getId()){
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
        User currentUser = getCurrentUser();
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(currentUser, userInfoVO);
        List<Role> roles = roleService.getUserRoles(currentUser.getId());
        List<RoleInfoVO> roleInfoVOS = ConvertUtil.convertRoles(roles, aclMapper);
        userInfoVO.setRoleInfoVOS(roleInfoVOS);
        return Result.success(userInfoVO);
    }

    @Override
    public Result getManageUsersByKeyWord(String keyWord) {
        if (StringUtils.isEmpty(keyWord)){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        List<User> users = selectByKeyWord(keyWord, true);
        List<UserManageInfo> userList = ConvertUtil.convertUsers(users, roleMapper);
        return Result.success(userList);
    }

    @Override
    public List<User> selectByKeyWord(String keyWord, boolean isTeacher) {
        List<User> users = redisService.getArraylist(UserKey.getByKeyWord, keyWord + isTeacher, User.class);
        if (users == null || users.size() == 0){
            users = userMapper.selectByRandom(keyWord, isTeacher);
            if (users != null && users.size() != 0){
                redisService.set(UserKey.getByKeyWord, keyWord + isTeacher, users);
            }
        }
        return users;
    }
}
