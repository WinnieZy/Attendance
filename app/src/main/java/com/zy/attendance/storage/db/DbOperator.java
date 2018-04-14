package com.zy.attendance.storage.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.zy.attendance.bean.MacRecord;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/4/10.
 */

public class DbOperator {

    public static final String TAG = "DbOperator";

    public static final String DB_NAME = "AttendanceDB";
    public static final int VERSION = 1;
    private static DbOperator dbOperator;
    private SQLiteDatabase db;

    private DbOperator(Context context) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context,DB_NAME , null, VERSION);
        db = dbHelper.getWritableDatabase();
    }
    /*
     * 获取实例
     */
    public synchronized static DbOperator getInstance(Context context) {
        if (dbOperator == null) {
            dbOperator = new DbOperator(context);
        }
        return dbOperator;
    }

    /*
	 * 添加一条MAC记录
	 */
    public boolean addMacRecord(MacRecord macRecord) {
        try {
            String sql = "insert into mac(mac,year,month,day,weekday,first_time,last_time) values(?,?,?,?,?,?,?)";
            db.execSQL(sql,new String[]{macRecord.getMac(),macRecord.getYear(), macRecord.getMonth(),
                    macRecord.getDay(),String.valueOf(macRecord.getWeekday()),
                    macRecord.getFirst_time(),macRecord.getLast_time()});
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
	 * 更新一条MAC记录
	 */
    public boolean updateMacRecord(int updateId, MacRecord macRecord) {
        try {
            String sql = "update mac set last_time = ? where id = ?";
            db.execSQL(sql,new String[]{macRecord.getLast_time(),String.valueOf(updateId)});
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据一个参数查找mac记录
     * @param param
     * @return
     */
    public ArrayList<MacRecord> queryMacRecord(String param, String value) {
        try {
            ArrayList<MacRecord> macList = new ArrayList<MacRecord>();
            Cursor cursor = db.rawQuery("select * from mac where " +param+ "=?", new String[]{value});
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String mac = cursor.getString(cursor.getColumnIndex("mac"));
                    String year = cursor.getString(cursor.getColumnIndex("year"));
                    String month = cursor.getString(cursor.getColumnIndex("month"));
                    String day = cursor.getString(cursor.getColumnIndex("day"));
                    String weekday = cursor.getString(cursor.getColumnIndex("weekday"));
                    String first_time = cursor.getString(cursor.getColumnIndex("first_time"));
                    String last_time = cursor.getString(cursor.getColumnIndex("last_time"));
                    macList.add(new MacRecord(id,mac,year,month,day,weekday,first_time,last_time));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return macList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取Mac表中最新的一条记录
     * @return
     */
    public MacRecord getLatestMacRecord(){
        Cursor cursor = db.rawQuery("select * from mac order by id desc LIMIT 1", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String mac = cursor.getString(cursor.getColumnIndex("mac"));
                String year = cursor.getString(cursor.getColumnIndex("year"));
                String month = cursor.getString(cursor.getColumnIndex("month"));
                String day = cursor.getString(cursor.getColumnIndex("day"));
                String weekday = cursor.getString(cursor.getColumnIndex("weekday"));
                String first_time = cursor.getString(cursor.getColumnIndex("first_time"));
                String last_time = cursor.getString(cursor.getColumnIndex("last_time"));
                return new MacRecord(id,mac,year,month,day,weekday,first_time,last_time);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }

    /**
     * 根据年月查找mac月记录
     * @return
     */
    public ArrayList<MacRecord> queryMacRecordByMonth(String monthStr) {
        try {
            ArrayList<MacRecord> macList = new ArrayList<MacRecord>();
            Cursor cursor = db.rawQuery("select * from mac where year = ? and month = ?", new String[]{monthStr.substring(0,4),monthStr.substring(5,7)});
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String mac = cursor.getString(cursor.getColumnIndex("mac"));
                    String year = cursor.getString(cursor.getColumnIndex("year"));
                    String month = cursor.getString(cursor.getColumnIndex("month"));
                    String day = cursor.getString(cursor.getColumnIndex("day"));
                    String weekday = cursor.getString(cursor.getColumnIndex("weekday"));
                    String first_time = cursor.getString(cursor.getColumnIndex("first_time"));
                    String last_time = cursor.getString(cursor.getColumnIndex("last_time"));
                    macList.add(new MacRecord(id,mac,year,month,day,weekday,first_time,last_time));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return macList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据年月日查找mac周记录
     * @return
     */
    public ArrayList<MacRecord> queryMacRecordByDate(String startStr, String endStr) {
        try {
            ArrayList<MacRecord> macList = new ArrayList<MacRecord>();
            boolean special = false;
            String startDay = startStr.substring(8,10);
            String endDay = endStr.substring(8,10);
            if (Integer.parseInt(startDay) > Integer.parseInt(endDay)){
                endDay = String.valueOf(31);
                special = true;
            }
            Cursor cursor = db.rawQuery("select * from mac where year = ? and month = ? and day between ? and ?",
                    new String[]{startStr.substring(0,4),startStr.substring(5,7),startDay,endDay});
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String mac = cursor.getString(cursor.getColumnIndex("mac"));
                    String year = cursor.getString(cursor.getColumnIndex("year"));
                    String month = cursor.getString(cursor.getColumnIndex("month"));
                    String day = cursor.getString(cursor.getColumnIndex("day"));
                    String weekday = cursor.getString(cursor.getColumnIndex("weekday"));
                    String first_time = cursor.getString(cursor.getColumnIndex("first_time"));
                    String last_time = cursor.getString(cursor.getColumnIndex("last_time"));
                    macList.add(new MacRecord(id,mac,year,month,day,weekday,first_time,last_time));
                } while (cursor.moveToNext());
            }
            if (special){
                cursor = db.rawQuery("select * from mac where year = ? and month = ? and day between 1 and ?",
                        new String[]{endStr.substring(0,4),endStr.substring(5,7),endStr.substring(8,10)});
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String mac = cursor.getString(cursor.getColumnIndex("mac"));
                        String year = cursor.getString(cursor.getColumnIndex("year"));
                        String month = cursor.getString(cursor.getColumnIndex("month"));
                        String day = cursor.getString(cursor.getColumnIndex("day"));
                        String weekday = cursor.getString(cursor.getColumnIndex("weekday"));
                        String first_time = cursor.getString(cursor.getColumnIndex("first_time"));
                        String last_time = cursor.getString(cursor.getColumnIndex("last_time"));
                        macList.add(new MacRecord(id,mac,year,month,day,weekday,first_time,last_time));
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            return macList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void dropMac(){
        db.execSQL("drop table if exists mac");
    }

}
//                String weekdayStr = null;
//                switch (weekday){
//                    case 1:
//                        weekdayStr = "一";
//                        break;
//                    case 2:
//                        weekdayStr = "二";
//                        break;
//                    case 3:
//                        weekdayStr = "三";
//                        break;
//                    case 4:
//                        weekdayStr = "四";
//                        break;
//                    case 5:
//                        weekdayStr = "五";
//                        break;
//                    case 6:
//                        weekdayStr = "六";
//                        break;
//                    case 0:
//                        weekdayStr = "日";
//                        break;
//                    default:
//                        break;
//                }