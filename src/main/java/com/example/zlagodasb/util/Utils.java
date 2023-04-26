package com.example.zlagodasb.util;

import java.sql.Date;
import java.util.Calendar;

public class Utils {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isExpired(Date date) {
        return date.compareTo(new Date(Calendar.getInstance().getTime().getTime())) <= 0;
    }

    public static String parseIdFrom(String input) throws Exception {
        int beginIndex = input.indexOf('(');
        int endIndex = input.indexOf(')');
        if(beginIndex == -1 || endIndex == -1 || (beginIndex + 1) > endIndex)
            throw new Exception("Utils.parseIdFrom(String) exception");

        return input.substring(beginIndex + 1, endIndex);
    }
}
