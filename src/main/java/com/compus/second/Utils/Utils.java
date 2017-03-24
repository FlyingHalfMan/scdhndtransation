package com.compus.second.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cai on 29/01/2017.
 */
public class Utils {

    /*
        1.手机号的验证
        2.邮箱号验证
        3.中文验证

    * */
    // 获取当前的时间(日期+时间)
    public static String getCurrentTime(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return simpleDateFormat.format(new Date());
    }

    public static String getTimeWithDuration(long time){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return simpleDateFormat.format(new Date(new Date().getTime() +time));
    }

    public static Date parseStringToDate(String date,String dateFormatter) throws ParseException {

        if (dateFormatter == null || dateFormatter.length() <4) dateFormatter = "yyyy-MM-dd HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatter);
        return simpleDateFormat.parse(date);
    }

    public static String parseDateToString(Date date,String dateFormatter){

        if (dateFormatter == null || dateFormatter.length() <4) dateFormatter = "yyyy-MM-dd HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatter);
        return simpleDateFormat.format(date);
    }

    public static boolean isRightEmail(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /* 判断是否手机号码格式是否正确*/
    public static  boolean isRightMobile(String mobile){
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    // 判断用户名是否正确
     /*符合规则 包括 中文字符,英文字符,下划线, 数字*/
    public static  boolean isRightName(String name){

        String regex ="[\\u4e00-\\u9fa5\\w]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private static double PI = 3.14159265;
    private static double EARTH_RADIUS = 6378137;
    private static double RAD = Math.PI / 180.0;


    public static double[] GetAround(double lat, double lon, double raidus)
    {

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901 * 1609) / 360.0;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidus;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidus;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        return new double[] { minLat, minLng, maxLat, maxLng };
    }
}
