package com.swpu.uchain.openexperiment.util;

import com.swpu.uchain.openexperiment.domain.Acl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 */
public class ConvertUtil {
    public static List<String> fromAclsTogetUrls(List<Acl> acls){
        List<String> urls = new ArrayList<>();
        for (Acl acl : acls) {
            urls.add(acl.getUrl());
        }
        return urls;
    }
}
