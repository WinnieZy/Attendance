package com.zy.attendance.utils;

/**
 * Created by lenovo on 2018/3/31.
 */

public interface IResultCallback {
    public void onSuccess(String result);
    public void onFail(String failReason);
}
