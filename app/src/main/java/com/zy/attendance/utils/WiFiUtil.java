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
            return info.getMacAddress();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        return "";
    }
}
