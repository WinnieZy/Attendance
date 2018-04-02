package com.zy.attendance.utils;

import android.os.Bundle;

/**
 * Created by lenovo on 2018/3/26.
 */

public interface IDataCallback {
    public void onCallback(Bundle inBundle, Bundle outBundle);
    public void onHostFail(int errCode, String errMsg, Bundle inBundle);
}
