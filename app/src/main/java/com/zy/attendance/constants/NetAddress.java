package com.zy.attendance.constants;

/**
 * 网络请求地址常量类
 * Created by lenovo on 2018/3/24.
 */

public class NetAddress {
    private static String ip_address = "http://192.168.23.5";
    public static final String MAC_REQUEST = ip_address + "/AttendanceServer/IMac.php";
    public static final String USER_REQUEST = ip_address + "/AttendanceServer/IUserLogin.php";
    public static final String USER_REGISTER = ip_address + "/AttendanceServer/IUserRegister.php";
    public static final String USER_MODIFY = ip_address + "/AttendanceServer/IUserModify.php";
    public static final String STAFF_MODIFY = ip_address + "/AttendanceServer/IStaffModify.php";
    public static final String APPLY_ADD = ip_address + "/AttendanceServer/IApplyAdd.php";
    public static final String APPLY_REQUEST = ip_address + "/AttendanceServer/IApply.php";
}
