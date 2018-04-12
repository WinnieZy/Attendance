package com.zy.attendance.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2018/3/26.
 */

public class MacRecord implements Parcelable{

    private int id;
    private String mac;
    private String year;
    private String month;
    private String day;
    private String weekday;
    private String first_time;
    private String last_time;

    public MacRecord() {
    }

    public MacRecord(String mac, String year, String month, String day, String weekday, String first_time, String last_time) {
        this.mac = mac;
        this.year = year;
        this.month = month;
        this.day = day;
        this.weekday = weekday;
        this.first_time = first_time;
        this.last_time = last_time;
    }

    public MacRecord(int id, String mac, String year, String month, String day, String weekday, String first_time, String last_time) {
        this.id = id;
        this.mac = mac;
        this.year = year;
        this.month = month;
        this.day = day;
        this.weekday = weekday;
        this.first_time = first_time;
        this.last_time = last_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getFirst_time() {
        return first_time;
    }

    public void setFirst_time(String first_time) {
        this.first_time = first_time;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(mac);
        parcel.writeString(year);
        parcel.writeString(month);
        parcel.writeString(day);
        parcel.writeString(weekday);
        parcel.writeString(first_time);
        parcel.writeString(last_time);
    }

    public final static Parcelable.Creator<MacRecord> CREATOR = new Creator<MacRecord>(){

        @Override
        public MacRecord createFromParcel(Parcel parcel) {
            MacRecord macRecord = new MacRecord();
            macRecord.id = parcel.readInt();
            macRecord.mac = parcel.readString();
            macRecord.year = parcel.readString();
            macRecord.month = parcel.readString();
            macRecord.day = parcel.readString();
            macRecord.weekday = parcel.readString();
            macRecord.first_time = parcel.readString();
            macRecord.last_time = parcel.readString();
            return macRecord;
        }

        @Override
        public MacRecord[] newArray(int i) {
            return new MacRecord[i];
        }
    };

    @Override
    public String toString() {
        return new StringBuilder().append("[")
                .append(id).append(",")
                .append(mac).append(",")
                .append(year).append(",")
                .append(month).append(",")
                .append(day).append(",")
                .append(weekday).append(",")
                .append(first_time).append(",")
                .append(last_time).append("]").toString();
    }
}
