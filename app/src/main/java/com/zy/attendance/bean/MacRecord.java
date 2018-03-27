package com.zy.attendance.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2018/3/26.
 */

public class MacRecord implements Parcelable{

    private String date;
    private String first_time;
    private String last_time;

    public MacRecord(){}

    public MacRecord(String date, String first_time, String last_time) {
        this.date = date;
        this.first_time = first_time;
        this.last_time = last_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        parcel.writeString(date);
        parcel.writeString(first_time);
        parcel.writeString(last_time);
    }

    public final static Parcelable.Creator<MacRecord> CREATOR = new Creator<MacRecord>(){

        @Override
        public MacRecord createFromParcel(Parcel parcel) {
            MacRecord macRecord = new MacRecord();
            macRecord.date = parcel.readString();
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
                .append(date).append(",")
                .append(first_time).append(",")
                .append(last_time).append("]").toString();
    }
}
