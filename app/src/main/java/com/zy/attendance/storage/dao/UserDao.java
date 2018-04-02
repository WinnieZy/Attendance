package com.zy.attendance.storage.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.zy.attendance.bean.User;

/**
 * Created by lenovo on 2018/3/31.
 */

public class UserDao {

    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;

    public UserDao(Context context) {
        mContext = context;
        getSharedPreferences();
        getEditor();
    }

    public SharedPreferences getSharedPreferences(){
        if (mSharedPreferences == null){
            mSharedPreferences = mContext.getSharedPreferences("USER",Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEditor(){
        if (mEditor == null){
            mEditor = getSharedPreferences().edit();
        }
        return mEditor;
    }

    public void setUser(User user){
        mEditor.putString("username",user.getUsername());
        mEditor.putString("password",user.getPassword());
        mEditor.putInt("staffId",user.getStaff_id());
        mEditor.putString("mac", user.getMac());
        mEditor.putString("lastLoginTime", user.getLast_login_time());
        mEditor.putBoolean("isOnline", user.getIsOnline());
        mEditor.apply();
    }

    public User getUser(){
        return new User(getUsername(),getPassword(), getStaffId(),getMac(),getLastLoginTime(),getIsOnline());
    }

    public void setUsername(String username) {
        mEditor.putString("username",username);
        mEditor.apply();
    }

    public String getUsername() {
        return mSharedPreferences.getString("username","");
    }

    public void setPassword(String password) {
        mEditor.putString("password",password);
        mEditor.apply();
    }

    public String getPassword() {
        return mSharedPreferences.getString("password","");
    }

    public void setStaffID(int staffId) {
        mEditor.putInt("staffId",staffId);
        mEditor.apply();
    }

    public int getStaffId() {
        return mSharedPreferences.getInt("staffId",0);
    }

    public void setMac(String mac) {
        mEditor.putString("mac", mac);
        mEditor.apply();
    }

    public String getMac() {
        return mSharedPreferences.getString("mac","");
    }

    public void setLastLoginTime(String lastLoginTime) {
        mEditor.putString("lastLoginTime", lastLoginTime);
        mEditor.apply();
    }

    public String getLastLoginTime() {
        return mSharedPreferences.getString("lastLoginTime","");
    }

    public void setIsOnline(boolean isOnline) {
        mEditor.putBoolean("isOnline", isOnline);
        mEditor.apply();
    }

    public Boolean getIsOnline() {
        return mSharedPreferences.getBoolean("isOnline",false);
    }
}
