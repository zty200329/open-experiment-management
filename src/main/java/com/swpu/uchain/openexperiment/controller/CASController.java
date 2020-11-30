package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.cas.CasAutoConfig;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/casToHomePage")
public class CASController{

	@Autowired
	private CasAutoConfig autoconfig;




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
		String ssoid = assertion.getPrincipal().getName();
		/*获取用户扩展信息
		 *扩展信息由UIA的SSO配置决定
		 *其中，由于用户可能拥有多个角色，岗位，部门
		 *所以角色，岗位，部门需要被转换为List<Map<String,String>类型
		 */
		String sessionId = session.getId();
		Map<String, Object> map = assertion.getPrincipal().getAttributes();
		// 通过该账号去组装我们与前端交互使用的token

		//教工号
		String teachingNumber = (String)map.get("comsys_teaching_number");
		//学生号
		String studentNumber = (String)map.get("comsys_student_number");
		//用户姓名
		String Name = (String)map.get("comsys_name");
		String token = "1231";
		log.info(ssoid);
		// 重定向前端页面
		return new RedirectView(autoconfig.getUiRrl()+"?12312431231231");
	}

	@RequestMapping(value = "/test", method = { RequestMethod.GET, RequestMethod.POST })
	public Object test(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		// 重定向前端页面
		return new RedirectView("http://www.cdshtc.cn");
	}




}