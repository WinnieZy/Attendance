package com.zy.attendance.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by lenovo on 2018/3/24.
 */

public class WiFiUtil {

    public static final String TAG = "WiFiUtil";

    public static String getLocalMacAddress(Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = mWifiManager.getConnectionInfo();
            return info.getMacAddress().toUpperCase();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        return "";
    }

    public static boolean isMacAddress(String mac){
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
}
