package com.zy.attendance.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2018/4/10.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_MAC = "create table mac("
            + "id integer primary key autoincrement,"
            + "mac text not null,"
            + "year text,"
            + "month text,"
            + "day text,"
            + "weekday text,"
            + "first_time text,"
            + "last_time text)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MAC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO:数据库更新操作
        sqLiteDatabase.execSQL("drop table if exists mac");
        onCreate(sqLiteDatabase);
    }
}
