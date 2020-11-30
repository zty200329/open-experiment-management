package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.cas.CasAutoConfig;
import com.swpu.uchain.openexperiment.domain.Role;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.user.GetAllPermissions;
import com.swpu.uchain.openexperiment.mapper.UserRoleMapper;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.security.JwtTokenUtil;
import com.swpu.uchain.openexperiment.security.JwtUser;
import com.swpu.uchain.openexperiment.service.AclService;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.RoleService;
import com.swpu.uchain.openexperiment.service.UserService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/casToHomePage")
public class CASController{

	@Autowired
	private CasAutoConfig autoconfig;
	@Autowired
	private GetUserService getUserService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AclService aclService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleService roleService;

	/**
	 * 点进入业务 系统
	 *
	 * @return
	 */
	@RequestMapping(value = "/inCAS",method = {RequestMethod.GET,RequestMethod.POST})
	public Object list(HttpSession session) {
		// 该方法通过cas获取到 登陆对象
		Assertion assertion=
				(Assertion)
						session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
		/*获取用户的唯一标识信息
		 *由UIA的配置不同可分为两种：
		 *(1)学生：学号；教工：身份证号
		 *(2)学生：学号；教工：教工号*/
		String ssoId = assertion.getPrincipal().getName();
		List<Integer> rolesList = getAllPermissions(ssoId);


		if(rolesList != null){
			Integer role1 = Collections.min(rolesList);
			User user = getUserService.selectByUserCodeAndRole(ssoId,role1);
			//验证用户密码及其角色是否存在
			if (user == null) {
				return Result.error(CodeMsg.USER_NO_EXIST);
			}
			Authentication token = new UsernamePasswordAuthenticationToken(user.getCode(), user.getPassword());
			Authentication authentication = authenticationManager.authenticate(token);
			//认证通过放入容器中
			SecurityContextHolder.getContext().setAuthentication(authentication);
			final UserDetails userDetails;

			User user1 = getUserService.selectByUserCode(user.getCode());
			if (user1==null) {
				throw new UsernameNotFoundException(String.format(" user not exist with stuId ='%s'.", user.getCode()));
			} else {
				//若存在则返回userDetails对象
				List<String> aclUrl = aclService.getUserAclUrl(Long.valueOf(user.getCode()));
				userDetails =new JwtUser(user.getCode(), passwordEncoder.encode(user.getPassword()), aclUrl);
			}
			log.info("加载数据库中的userDetails: {}", userDetails);
			//生成真正的token
			final String realToken = jwtTokenUtil.generateToken(userDetails);
			Role role = roleService.getUserRoles(Long.valueOf(user1.getCode()), Long.valueOf(role1));

			// 重定向前端页面
			return new RedirectView(autoconfig.getUiRrl()+"?userToken="+realToken+"&role="+role1);
		}else{
			return new RedirectView(autoconfig.getUiRrl());
		}




	}
	private  List<Integer> getAllPermissions(String stuId) {
		List<Integer> roles = userRoleMapper.selectUserRolesById(Long.valueOf(stuId));
		return roles;
	}

	/**
	 * 将SSO传递过来的String类型的用户信息转换为List类型
	 * @param str 需要被转换的String
	 * @return List
	 */
	private List<Map<String,String>> parseStringToList(
			String str){
		List<Map<String,String>> list =
				new ArrayList<Map<String,String>>();
		if(str == null || str.equals("")){
			return list;
		}
		String[] array = str.split("-");
		for (String subArray : array) {
			String[] keyResult = subArray.split(",");
			Map<String,String> map =
					new HashMap<String, String>();
			for (String subResult : keyResult) {
				String[] value = subResult.split(":");
				map.put(value[0], value[1]);
			}
			list.add(map);
		}
		return list;
	}


}