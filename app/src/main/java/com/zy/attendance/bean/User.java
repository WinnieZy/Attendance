package com.zy.attendance.bean;

/**
 * Created by lenovo on 2018/3/29.
 */

public class User {
    private String username;
    private String password;
    private int staff_id;
    private String mac;
    private String last_login_time;
    private boolean isMacChecked;

    public User(String username, String password, int staff_id, String mac, String last_login_time, boolean isMacChecked) {
        this.username = username;
        this.password = password;
        this.staff_id = staff_id;
        this.mac = mac;
        this.last_login_time = last_login_time;
        this.isMacChecked = isMacChecked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public boolean getIsMacChecked() {
        return isMacChecked;
    }

    public void setIsMacChecked(boolean isMacChecked) {
        this.isMacChecked = isMacChecked;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("[")
                .append(username).append(",")
                .append(password).append(",")
                .append(staff_id).append(",")
                .append(mac).append(",")
                .append(last_login_time).append(",")
                .append(isMacChecked ? "checked" : "unchecked")
                .append("]").toString();
    }
}
