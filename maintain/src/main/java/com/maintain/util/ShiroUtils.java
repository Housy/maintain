package com.maintain.util;

import org.apache.shiro.crypto.hash.SimpleHash;

public class ShiroUtils {

    /**
     * 加盐加密
     * @param srcPwd    原始密码
     * @param saltValue 盐值
     */
    public static String salt(Object srcPwd, String saltValue){
        return new SimpleHash("MD5", srcPwd, saltValue, 1024).toString();
    }
}
