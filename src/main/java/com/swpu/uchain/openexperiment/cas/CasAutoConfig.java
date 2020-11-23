package com.swpu.uchain.openexperiment.cas;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author zty
 */
// cas 配置注入到对象 方便其它地方使用
// 省略get set 方法
// 几个路径get 方法使用 , 分隔转为集合
@Component
@Data
public class CasAutoConfig {
	static final String separator = ","; //配置地址可以用逗号分隔 

	private String validateFilters = "/casToBusiness/inBusiness";
	private String signOutFilters = "/logout";
	private String authFilters = "/casToBusiness/inBusiness";
	private String assertionFilters = "/casToBusiness/inBusiness";
	private String requestWrapperFilters = "/casToBusiness/inBusiness";
	private String casServerUrlPrefix = "http://ids.swpu.edu.cn/sso";
	private String casServerLoginUrl = "http://ids.swpu.edu.cn/sso/login";
	private String serverName = "http://192.168.109.88";
	private String uiRrl = "http://192.168.109.88";
	private boolean useSession = true;
	private boolean redirectAfterValidation = true;

	public List<String> getValidateFilters() {
		return Arrays.asList(validateFilters.split(separator));
	}

	public List<String> getSignOutFilters() {
		return Arrays.asList(signOutFilters.split(separator));
	}

	public List<String> getAuthFilters() {
		return Arrays.asList(authFilters.split(separator));
	}

	public List<String> getAssertionFilters() {
		return Arrays.asList(assertionFilters.split(separator));
	}

	public List<String> getRequestWrapperFilters() {
		return Arrays.asList(requestWrapperFilters.split(separator));
	}

}