package com.zy.attendance.storage.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.zy.attendance.bean.Staff;

/**
 * Created by lenovo on 2018/3/31.
 */

public class StaffDao {

    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;

    public StaffDao(Context context) {
        mContext = context;
        getSharedPreferences();
        getEditor();
    }

    public SharedPreferences getSharedPreferences(){
        if (mSharedPreferences == null){
            mSharedPreferences = mContext.getSharedPreferences("STAFF",Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEditor(){
        if (mEditor == null){
            mEditor = getSharedPreferences().edit();
        }
        return mEditor;
    }

    public void setStaff(Staff staff){
        mEditor.putInt("staffId",staff.getStaff_id());
        mEditor.putString("staffName",staff.getStaffName());
        mEditor.putString("idCard",staff.getIDcard());
        mEditor.putString("telNum", staff.getTel_num());
        mEditor.putString("email", staff.getEmail());
        mEditor.putString("leader", staff.getLeader());
        mEditor.putString("entryDate", staff.getEntry_date());
        mEditor.apply();
    }

    public Staff getStaff(){
        return new Staff(getStaffId(),getStaffName(),getIDCard(),getTelNum(),getEmail(),getLeader(),getEntryDate());
    }

    public void setStaffId(int staff_id) {
        mEditor.putInt("staffId",staff_id);
        mEditor.apply();
    }

    public int getStaffId() {
        return mSharedPreferences.getInt("staffId",0);
    }

    public void setStaffName(String staffName) {
        mEditor.putString("staffName",staffName);
        mEditor.apply();
    }

    public String getStaffName() {
        return mSharedPreferences.getString("staffName","");
    }

    public void setIDCard(String idCard) {
        mEditor.putString("idCard",idCard);
        mEditor.apply();
    }

    public String getIDCard() {
        return mSharedPreferences.getString("idCard","");
    }

    public void setTelNum(String telNum) {
        mEditor.putString("telNum", telNum);
        mEditor.apply();
    }

    public String getTelNum() {
        return mSharedPreferences.getString("telNum","");
    }

    public void setEmail(String email) {
        mEditor.putString("email", email);
        mEditor.apply();
    }

    public String getEmail() {
        return mSharedPreferences.getString("email","");
    }

    public void setLeader(String leader) {
        mEditor.putString("leader", leader);
        mEditor.apply();
    }

    public String getLeader() {
        return mSharedPreferences.getString("leader","");
    }

    public void setEntryDate(String entryDate) {
        mEditor.putString("entryDate", entryDate);
        mEditor.apply();
    }

    public String getEntryDate() {
        return mSharedPreferences.getString("entryDate","");
    }
}
