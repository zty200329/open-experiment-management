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
	//配置地址可以用逗号分隔
	static final String separator = ",";

	private String validateFilters = "/casToHomePage/inCAS";
	private String signOutFilters = "/logout";
	private String authFilters = "/casToHomePage/inCAS";
	private String assertionFilters = "/casToHomePage/inCAS";
	private String requestWrapperFilters = "/casToHomePage/inCAS";
	private String casServerUrlPrefix = "http://ids.swpu.edu.cn/sso";
	private String casServerLoginUrl = "http://ids.swpu.edu.cn/sso/login";
	private String serverName = "http://uoep.swpu.edu.cn/";
	private String uiRrl = "http://uoep.swpu.edu.cn/";
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