package com.swpu.uchain.openexperiment.filter;

import org.jasig.cas.client.authentication.AuthenticationFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * @author: panghu
 * @Description:
 * @Date: Created in 14:48 2020/2/3
 * @Modified By:
 */
public class SSOFilter extends AuthenticationFilter {

    @Override
    protected void initInternal(FilterConfig filterConfig) throws ServletException {
        super.initInternal(filterConfig);
    }
}
