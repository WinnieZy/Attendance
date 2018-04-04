package com.zy.attendance.utils;

/**
 * Created by lenovo on 2018/4/4.
 */

public class FormatCheckUtil {
    public static boolean isMacAddressValid(String mac){
        if (mac.length() != 17){
            return false;
        }
        for (int i = 0; i < 17; i++) {
            if (i == 2 || i == 5 || i == 8 || i == 11 || i == 14){
                if (mac.charAt(i) == ':'){
                    continue;
                }else {
                    return false;
                }
            }
            if (mac.charAt(i) <= 'z' && mac.charAt(i) >= 'a') {
                continue;
            }
            if (mac.charAt(i) <= '9' && mac.charAt(i) >= '0') {
                continue;
            }
            if (mac.charAt(i) <= 'Z' && mac.charAt(i) >= 'A') {
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isUsernameValid(String username) {
        return username.contains("_");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    public static boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }
}
