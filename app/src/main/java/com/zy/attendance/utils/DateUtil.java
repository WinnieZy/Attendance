package com.zy.attendance.utils;

import android.text.format.DateFormat;

import com.zy.attendance.bean.MacRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lenovo on 2018/4/2.
 */

public class DateUtil {

    public static String getFormatDate(String accuracy){
        Date date = new Date();
        SimpleDateFormat formatter;
        if ("second".equals(accuracy)){
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }else if ("day".equals(accuracy)){
            formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }else {
            formatter = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        }
        return formatter.format(date);
    }

    public static String combineMacDate(MacRecord macRecord){
        return macRecord.getYear() + "-" + macRecord.getMonth() + "-" + macRecord.getDay() + "(" + macRecord.getWeekday() + ")";
    }

    public static String convertMillsToString(long mills) {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", mills).toString();
    }
}
