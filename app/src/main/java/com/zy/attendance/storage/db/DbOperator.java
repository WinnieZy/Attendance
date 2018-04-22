package com.zy.attendance.storage.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zy.attendance.bean.ApplyRecord;
import com.zy.attendance.bean.MacRecord;
import com.zy.attendance.storage.dao.StaffDao;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/4/10.
 */

public class DbOperator {

    private static final String TAG = "DbOperator";

    private static final String DB_NAME = "AttendanceDB";
    private static final int VERSION = 1;
    private static DbOperator mDbOperator;
    private SQLiteDatabase db;
    private StaffDao mStaffDao;

    private DbOperator(Context context) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context,DB_NAME , null, VERSION);
        db = dbHelper.getWritableDatabase();
        mStaffDao = new StaffDao(context);
    }
    /*
     * 获取实例
     */
    public synchronized static DbOperator getInstance(Context context) {
        if (mDbOperator == null) {
            mDbOperator = new DbOperator(context);
        }
        return mDbOperator;
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
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String mac = cursor.getString(cursor.getColumnIndex("mac"));
            String year = cursor.getString(cursor.getColumnIndex("year"));
            String month = cursor.getString(cursor.getColumnIndex("month"));
            String day = cursor.getString(cursor.getColumnIndex("day"));
            String weekday = cursor.getString(cursor.getColumnIndex("weekday"));
            String first_time = cursor.getString(cursor.getColumnIndex("first_time"));
            String last_time = cursor.getString(cursor.getColumnIndex("last_time"));
            return new MacRecord(id,mac,year,month,day,weekday,first_time,last_time);
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

    /*
	 * 添加一条Apply记录
	 */
    public boolean addApplyRecord(ApplyRecord applyRecord) {
        try {
            String sql = "insert into apply(apply_id,staff_id,staff_name,leader_id,type,apply_time_for,apply_time_at,reason,result) values(?,?,?,?,?,?,?,?,?)";
            db.execSQL(sql,new String[]{String.valueOf(applyRecord.getApply_id()),String.valueOf(applyRecord.getStaff_id()),applyRecord.getStaff_name(),
                    String.valueOf(applyRecord.getLeader_id()),String.valueOf(applyRecord.getType()),applyRecord.getApply_time_for(),
                    applyRecord.getApply_time_at(),applyRecord.getReason(),String.valueOf(applyRecord.getResult())});
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取Apply表中最新的一条记录的apply_id
     * @return
     */
    public int getLatestApplyRecordApplyId(){
        Cursor cursor = db.rawQuery("select apply_id from apply order by id desc LIMIT 1", null);
        if (cursor.moveToFirst()) {
            int apply_id = cursor.getInt(cursor.getColumnIndex("apply_id"));
            Log.e("ApplyRequestCtl","apply_id from db:"+apply_id);
            return apply_id;
        }
        cursor.close();
        return 0;
    }

    /**
     * 获取Apply表中最新的一条记录的id
     * @return
     */
    public int getLatestApplyRecordId(){
        Cursor cursor = db.rawQuery("select id from apply order by id desc LIMIT 1", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return 0;
    }

    /**
     * 根据一个参数查找apply记录
     * @param param
     * @return
     */
    public ArrayList<ApplyRecord> queryApplyRecord(String param, String value) {
        try {
            ArrayList<ApplyRecord> applyList = new ArrayList<ApplyRecord>();
            Cursor cursor = db.rawQuery("select * from apply where " +param+ "=?", new String[]{value});
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    int apply_id = cursor.getInt(cursor.getColumnIndex("apply_id"));
                    int staff_id = cursor.getInt(cursor.getColumnIndex("staff_id"));
                    String staff_name = cursor.getString(cursor.getColumnIndex("staff_name"));
                    int leader_id = cursor.getInt(cursor.getColumnIndex("leader_id"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    String apply_time_for = cursor.getString(cursor.getColumnIndex("apply_time_for"));
                    String apply_time_at = cursor.getString(cursor.getColumnIndex("apply_time_at"));
                    String reason = cursor.getString(cursor.getColumnIndex("reason"));
                    int result = cursor.getInt(cursor.getColumnIndex("result"));
                    applyList.add(new ApplyRecord(id,apply_id,staff_id,staff_name,leader_id,type,apply_time_for,apply_time_at,reason,result));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return applyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据指定id及指定数量查询apply/approval记录
     * @param queryId
     * @param countLimit
     * @return
     */
    public ArrayList<ApplyRecord> queryApplyRecordById(int queryId,int countLimit,boolean approval) {
        try {
            ArrayList<ApplyRecord> applyList = new ArrayList<ApplyRecord>();
//            String sql = "select * from apply where apply_id > ?";
            String sql = "select * from apply where id > ?";
            String param2 = String.valueOf(mStaffDao.getStaffId());
            if (approval){
                sql += " and leader_id = ? and result = 0";
                param2 = String.valueOf(mStaffDao.getLeaderId());
            }else {
                sql += " and staff_id = ?";
            }
            if (countLimit > 0) {
                sql += " Limit "+countLimit;
            }
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(queryId),param2});
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    int apply_id = cursor.getInt(cursor.getColumnIndex("apply_id"));
                    int staff_id = cursor.getInt(cursor.getColumnIndex("staff_id"));
                    String staff_name = cursor.getString(cursor.getColumnIndex("staff_name"));
                    int leader_id = cursor.getInt(cursor.getColumnIndex("leader_id"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    String apply_time_for = cursor.getString(cursor.getColumnIndex("apply_time_for"));
                    String apply_time_at = cursor.getString(cursor.getColumnIndex("apply_time_at"));
                    String reason = cursor.getString(cursor.getColumnIndex("reason"));
                    int result = cursor.getInt(cursor.getColumnIndex("result"));
                    applyList.add(new ApplyRecord(id,apply_id,staff_id,staff_name,leader_id,type,apply_time_for,apply_time_at,reason,result));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return applyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}