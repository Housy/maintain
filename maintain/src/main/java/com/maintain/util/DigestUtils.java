package com.maintain.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;

/**
 * @Title: DigestUtils
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/9/11 15:09
 */
public class DigestUtils {

    /**
     * 加密
     * @param salt 盐
     * @param password 明文密码
     * @return
     */
    public static String Md5(String salt,String password){
        Md5Hash hash = new Md5Hash(password, ByteSource.Util.bytes(salt), 2);
        return hash.toString();
    }
}
