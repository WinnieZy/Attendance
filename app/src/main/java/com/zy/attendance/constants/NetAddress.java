package com.zy.attendance.constants;

/**
 * Created by lenovo on 2018/3/24.
 */

public class NetAddress {
    private static String ip_address = "http://192.168.191.2";
    public static final String MAC_REQUEST = ip_address + "/AttendanceServer/IMac.php";
    public static final String USER_REQUEST = ip_address + "/AttendanceServer/IUserLogin.php";
    public static final String USER_REGISTER = ip_address + "/AttendanceServer/IUserRegister.php";
    public static final String USER_MODIFY = ip_address + "/AttendanceServer/IUserModify.php";
    public static final String STAFF_MODIFY = ip_address + "/AttendanceServer/IStaffModify.php";
}
