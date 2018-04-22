package com.zy.attendance.bean;

/**
 * Created by lenovo on 2018/3/29.
 */

public class Staff {
    private int staff_id;
    private String staff_name;
    private String IDcard;
    private String tel_num;
    private String email;
    private boolean approval_auth;
    private int leader_id;
    private String leader;
    private String department;
    private String entry_date;

    public Staff(int staff_id, String staff_name, String IDcard, String tel_num, String email, boolean approval_auth, int leader_id, String leader, String department, String entry_date) {
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.IDcard = IDcard;
        this.tel_num = tel_num;
        this.email = email;
        this.approval_auth = approval_auth;
        this.leader_id = leader_id;
        this.leader = leader;
        this.department = department;
        this.entry_date = entry_date;
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

    public String getIDcard() {
        return IDcard;
    }

    public void setIDcard(String IDcard) {
        this.IDcard = IDcard;
    }

    public String getTel_num() {
        return tel_num;
    }

    public void setTel_num(String tel_num) {
        this.tel_num = tel_num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getApproval_auth() {
        return approval_auth;
    }

    public void setApproval_auth(boolean approval_auth) {
        this.approval_auth = approval_auth;
    }

    public int getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(int leader_id) {
        this.leader_id = leader_id;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("[")
                .append(staff_id).append(",")
                .append(staff_name).append(",")
                .append(IDcard).append(",")
                .append(tel_num).append(",")
                .append(email).append(",")
                .append(approval_auth).append(",")
                .append(leader_id).append(",")
                .append(leader).append(",")
                .append(department).append(",")
                .append(entry_date).append("]").toString();
    }
}
