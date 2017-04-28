package com.compus.second.Utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * Created by cai on 29/01/2017.
 */
public class EncryptUtil {

    private static  int[] numbers = {0,1,2,3,4,5,6,7,8,9};

    /*生成一个6位数字字符串*/
    public static String  getVerifyCode(){

        Random random =new Random();
        String verifyCode="";
        for (int i=0;i<6;i++){
            verifyCode+= numbers[random.nextInt(numbers.length)];
        }
        return verifyCode;
    }

    public static String encrypt(String salt,String password){

        return DigestUtils.md5Hex(password+salt);
    }
    /*生成一个16个字符的字符串*/
    public static String userId(){
        return RandomStringUtils.randomNumeric(16);
    }

    public static  String randomString(){
        return RandomStringUtils.randomAlphabetic(16);
    }
    public static  String randomString(int len){
        return RandomStringUtils.randomAlphabetic(len);
    }

    public static String salt(){
        return RandomStringUtils.randomAlphabetic(32);
    }

    /*生成一个UUID*/
    public static  String getUUID(){

        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
