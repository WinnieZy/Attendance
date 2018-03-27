package com.zy.attendance.view;

import android.os.Bundle;

/**
 * Created by lenovo on 2018/3/26.
 */

public interface IMainCallback {
    public void onCallback(Bundle inBundle, Bundle outBundle);
    public void onHostFail(int errCode, String errMsg, Bundle inBundle);
}
