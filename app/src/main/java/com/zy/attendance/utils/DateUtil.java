package com.zy.attendance.utils;

import com.zy.attendance.bean.MacRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lenovo on 2018/4/2.
 */

public class DateUtil {

    public static String getFormatDate(boolean second){
        Date date = new Date();
        SimpleDateFormat formatter;
        if (second){
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }else {
            formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }
        return formatter.format(date);
    }

    public static String combineMacDate(MacRecord macRecord){
        return macRecord.getYear() + "-" + macRecord.getMonth() + "-" + macRecord.getDay() + "(" + macRecord.getWeekday() + ")";
    }

}
