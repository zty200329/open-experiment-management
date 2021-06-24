package com.swpu.uchain.openexperiment.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: clf
 * @Date: 19-1-18
 * @Description:
 * 客户端工具类
 */
public class ClientUtil {

    /**
     * 获取客户端请求的ip
     * @param request
     * @return
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String clientIp = request.getHeader("x-forwarded-for");
        if(clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if(clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if(clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }


}
