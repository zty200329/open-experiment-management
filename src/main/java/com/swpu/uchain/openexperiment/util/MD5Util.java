package com.swpu.uchain.openexperiment.util;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * @author dgh
 */
public class MD5Util {

    private static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String SALT = "uchainc1108";

    /**
     *          第一次MD5加密，用于网络传输
     * @param inputPass
     * @return
     */
    private static String inputPassToFormPass(String inputPass){
        //避免在网络传输被截取然后反推出密码，所以在md5加密前先打乱密码
        //注意这里要和前端保持一致   加引号和不加引号是有区别的
        String str = ""+SALT.charAt(0)+SALT.charAt(2)+inputPass+SALT.charAt(5)+SALT.charAt(4);
        return md5(str);
    }

    /**
     *              第二次md5加密，用于数据库存储     登录验证使用
     * @param inputPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String inputPass,String salt){
        String str = ""+ salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5);
        return md5(str);
    }

    /**
     *
     * @param inputPass  直接两次加密
     * @param DBsalt
     * @return
     */
    public static String inputPassToDBPass(String inputPass,String DBsalt){
        String formPass = inputPassToFormPass(inputPass);
        return formPassToDBPass(formPass, DBsalt);
    }

    public static void main(String[] args) {
        //38a41052bb6fd70aeddb5370aafcbecc
        //4756a51e387552db1fa1dfc64cb6b194
        System.out.println(inputPassToDBPass("123456","4995bd91"));
    }

}
