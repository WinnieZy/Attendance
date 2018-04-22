package com.zy.attendance.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 申请记录数据体
 * Created by lenovo on 2018/4/15.
 */

public class ApplyRecord implements Parcelable {
    private int id;
    private int apply_id;
    private int staff_id;
    private String staff_name;
    private int leader_id;
    private int type;//请假==1;加班==2;出差==3;其它==4
    private String apply_time_for;//yyyy-mm-dd 上/下午~yyyy-mm-dd 上/下午,当申请项为其它时申请时间可空
    private String apply_time_at;//yyyy-mm-dd HH:mm:ss
    private String reason;
    private int result;//0:waiting;1:pass;2:rejected

    public ApplyRecord(){

    }

    public ApplyRecord(int id, int apply_id, int staff_id, String staff_name, int leader_id, int type, String apply_time_for, String apply_time_at, String reason, int result) {
        this.id = id;
        this.apply_id = apply_id;
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.leader_id = leader_id;
        this.type = type;
        this.apply_time_for = apply_time_for;
        this.apply_time_at = apply_time_at;
        this.reason = reason;
        this.result = result;
    }

    public ApplyRecord(int apply_id, int staff_id, String staff_name, int leader_id, int type, String apply_time_for, String apply_time_at, String reason, int result) {
        this.apply_id = apply_id;
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.leader_id = leader_id;
        this.type = type;
        this.apply_time_for = apply_time_for;
        this.apply_time_at = apply_time_at;
        this.reason = reason;
        this.result = result;
    }

    public ApplyRecord(int staff_id, String staff_name, int leader_id, int type, String apply_time_for, String apply_time_at, String reason, int result) {
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.leader_id = leader_id;
        this.type = type;
        this.apply_time_for = apply_time_for;
        this.apply_time_at = apply_time_at;
        this.reason = reason;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApply_id() {
        return apply_id;
    }

    public void setApply_id(int apply_id) {
        this.apply_id = apply_id;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public int getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(int leader_id) {
        this.leader_id = leader_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getApply_time_for() {
        return apply_time_for;
    }

    public void setApply_time_for(String apply_time_for) {
        this.apply_time_for = apply_time_for;
    }

    public String getApply_time_at() {
        return apply_time_at;
    }

    public void setApply_time_at(String apply_time_at) {
        this.apply_time_at = apply_time_at;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("[")
                .append(id).append(",")
                .append(apply_id).append(",")
                .append(staff_id).append(",")
                .append(staff_name).append(",")
                .append(leader_id).append(",")
                .append(type).append(",")
                .append(apply_time_for).append(",")
                .append(apply_time_at).append(",")
                .append(reason).append(",")
                .append(result).append("]").toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeInt(apply_id);
        parcel.writeInt(staff_id);
        parcel.writeString(staff_name);
        parcel.writeInt(leader_id);
        parcel.writeInt(type);
        parcel.writeString(apply_time_for);
        parcel.writeString(apply_time_at);
        parcel.writeString(reason);
        parcel.writeInt(result);
    }

    public final static Parcelable.Creator<ApplyRecord> CREATOR = new Creator<ApplyRecord>(){

        @Override
        public ApplyRecord createFromParcel(Parcel parcel) {
            ApplyRecord applyRecord = new ApplyRecord();
            applyRecord.id = parcel.readInt();
            applyRecord.apply_id = parcel.readInt();
            applyRecord.staff_id = parcel.readInt();
            applyRecord.staff_name = parcel.readString();
            applyRecord.leader_id = parcel.readInt();
            applyRecord.type = parcel.readInt();
            applyRecord.apply_time_for = parcel.readString();
            applyRecord.apply_time_at = parcel.readString();
            applyRecord.reason = parcel.readString();
            applyRecord.result = parcel.readInt();
            return applyRecord;
        }

        @Override
        public ApplyRecord[] newArray(int i) {
            return new ApplyRecord[i];
        }
    };
}
