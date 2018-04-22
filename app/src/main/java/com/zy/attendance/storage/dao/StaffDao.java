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
        mEditor.putString("staffName",staff.getStaff_name());
        mEditor.putString("idCard",staff.getIDcard());
        mEditor.putString("telNum", staff.getTel_num());
        mEditor.putString("email", staff.getEmail());
        mEditor.putBoolean("approval_auth", staff.getApproval_auth());
        mEditor.putInt("leader_id", staff.getLeader_id());
        mEditor.putString("leader", staff.getLeader());
        mEditor.putString("department", staff.getDepartment());
        mEditor.putString("entryDate", staff.getEntry_date());
        mEditor.apply();
    }

    public Staff getStaff(){
        return new Staff(getStaffId(),getStaffName(),getIDCard(),getTelNum(),getEmail(),getApproval_auth(),getLeaderId(),getLeader(),getDepartment(),getEntryDate());
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

    public void setApproval_auth(boolean auth){
        mEditor.putBoolean("approval_auth", auth);
        mEditor.apply();
    }

    public boolean getApproval_auth(){
        return mSharedPreferences.getBoolean("approval_auth",false);
    }

    public void setLeaderId(int leader_id) {
        mEditor.putInt("leader_id",leader_id);
        mEditor.apply();
    }

    public int getLeaderId() {
        return mSharedPreferences.getInt("leader_id",0);
    }

    public void setLeader(String leader) {
        mEditor.putString("leader", leader);
        mEditor.apply();
    }

    public String getLeader() {
        return mSharedPreferences.getString("leader","");
    }

    public void setDepartment(String department) {
        mEditor.putString("department", department);
        mEditor.apply();
    }

    public String getDepartment() {
        return mSharedPreferences.getString("department","");
    }

    public void setEntryDate(String entryDate) {
        mEditor.putString("entryDate", entryDate);
        mEditor.apply();
    }

    public String getEntryDate() {
        return mSharedPreferences.getString("entryDate","");
    }
}
