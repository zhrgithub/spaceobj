package com.spaceobj.common.core.utils;

/**
 * @author zhr_java@163.com
 * @date 2022/9/10 00:30
 */
public class CommonStringUtils {

    public static String getEmailAccountName(String email) {

        return email.substring(0, email.indexOf("@"));
    }

}
