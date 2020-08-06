package com.zen.model.manage.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: xiongjun
 * @Date: 2020/6/11 15:00
 * @description
 * @reviewer
 */
public class ToolClass {
    public static String MD5(String input){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] byteArray = input.getBytes();
        byte[] md5Bytes = md5.digest(byteArray);
        String hexValue = "";
        int i=0;
        for (;i<md5Bytes.length;i++){
            int str = (int)md5Bytes[i]&0xff;
            if (str < 16) {
                hexValue=hexValue+"0";
            }
            hexValue=hexValue+ Integer.toHexString(str);
        }
        return hexValue;
    }
}
