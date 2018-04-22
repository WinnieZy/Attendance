package com.zy.attendance.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2018/4/10.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_MAC = "create table mac("
            + "id integer primary key autoincrement,"
            + "mac text not null,"
            + "year text,"
            + "month text,"
            + "day text,"
            + "weekday text,"
            + "first_time text,"
            + "last_time text)";

    private static final String CREATE_APPLY = "create table apply("
            + "id integer primary key autoincrement,"
            + "apply_id integer not null,"
            + "staff_id integer not null,"
            + "staff_name text not null,"
            + "leader_id integer not null,"
            + "type integer not null,"
            + "apply_time_for text,"
            + "apply_time_at text not null,"
            + "reason text,"
            + "result integer not null)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MAC);
        sqLiteDatabase.execSQL(CREATE_APPLY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO:数据库更新操作
        sqLiteDatabase.execSQL("drop table if exists mac");
        sqLiteDatabase.execSQL("drop table if exists apply");
        onCreate(sqLiteDatabase);
    }
}
