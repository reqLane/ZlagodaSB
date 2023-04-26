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
}
