package com.swpu.uchain.openexperiment.cas;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// springboot自动配置cas客户端
@Configuration
public class CasConfig {
	
	@Autowired
	private CasAutoConfig autoconfig;

	private static boolean casEnabled = true;

	/**
	 * 用于实现单点登出功能
	 */
	@Bean
	public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
		ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
		listener.setEnabled(casEnabled);
		listener.setListener(new SingleSignOutHttpSessionListener());
		listener.setOrder(1);
		return listener;
	}


	/**
	 * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
	 */
	@Bean
	public FilterRegistrationBean singleSignOutFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new SingleSignOutFilter());
		filterRegistration.setEnabled(casEnabled);
		filterRegistration.setUrlPatterns(autoconfig.getSignOutFilters());
		filterRegistration.addInitParameter("casServerUrlPrefix", autoconfig.getCasServerUrlPrefix());
		filterRegistration.addInitParameter("serverName", autoconfig.getServerName());
		filterRegistration.setOrder(3);
		return filterRegistration;
	}

	/**
	 * 该过滤器负责用户的认证工作
	 */
	@Bean
	public FilterRegistrationBean authenticationFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new AuthenticationFilter());
		filterRegistration.setEnabled(casEnabled);
		filterRegistration.setUrlPatterns(autoconfig.getAuthFilters());
		filterRegistration.addInitParameter("casServerLoginUrl", autoconfig.getCasServerLoginUrl());
		filterRegistration.addInitParameter("serverName", autoconfig.getServerName());
		filterRegistration.addInitParameter("useSession", autoconfig.isUseSession() ? "true" : "false");
		filterRegistration.addInitParameter("redirectAfterValidation",
				autoconfig.isRedirectAfterValidation() ? "true" : "false");
		filterRegistration.setOrder(4);
		return filterRegistration;
	}

	/**
	 * 该过滤器负责对Ticket的校验工作
	 */
	@Bean
	public FilterRegistrationBean cas20ProxyReceivingTicketValidationFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		Cas20ProxyReceivingTicketValidationFilter cas20ProxyReceivingTicketValidationFilter = new Cas20ProxyReceivingTicketValidationFilter();
		cas20ProxyReceivingTicketValidationFilter.setServerName(autoconfig.getServerName());
		filterRegistration.setFilter(cas20ProxyReceivingTicketValidationFilter);
		filterRegistration.setEnabled(casEnabled);
		filterRegistration.setUrlPatterns(autoconfig.getValidateFilters());
		filterRegistration.addInitParameter("casServerUrlPrefix", autoconfig.getCasServerUrlPrefix());
		filterRegistration.addInitParameter("serverName", autoconfig.getServerName());
		filterRegistration.setOrder(5);
		return filterRegistration;
	}

	/**
	 * 该过滤器对HttpServletRequest请求包装，
	 * 可通过HttpServletRequest的getRemoteUser()方法获得登录用户的登录名
	 *
	 */
	@Bean
	public FilterRegistrationBean httpServletRequestWrapperFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new HttpServletRequestWrapperFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.setUrlPatterns(autoconfig.getRequestWrapperFilters());
		filterRegistration.setOrder(6);
		return filterRegistration;
	}

	/**
	 * 该过滤器使得可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
	 * 比如AssertionHolder.getAssertion().getPrincipal().getName()。
	 * 这个类把Assertion信息放在ThreadLocal变量中，这样应用程序不在web层也能够获取到当前登录信息
	 */
	@Bean
	public FilterRegistrationBean assertionThreadLocalFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new AssertionThreadLocalFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.setUrlPatterns(autoconfig.getAssertionFilters());
		filterRegistration.setOrder(7);
		return filterRegistration;
	}

}