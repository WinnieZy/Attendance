package com.zy.attendance.controller;

import android.util.Log;

import com.zy.attendance.constants.NetAddress;
import com.zy.attendance.utils.HttpUtil;
import com.zy.attendance.utils.IHttpCallBack;
import com.zy.attendance.utils.IResultCallback;
import com.zy.attendance.utils.JsonUtil;

/**
 * Created by lenovo on 2018/4/4.
 */

public class UserModifyCtl {

    private static final String TAG = "UserModifyCtl";
    private String[] modifyKey = {"staff_id","password","mac"};
    private String[] modifyValue = new String[3];

    private UserModifyCtl() {
    }

    public static UserModifyCtl getInstance() {
        return UserModifyCtlHolder.instance;
    }

    private static class UserModifyCtlHolder{
        private static UserModifyCtl instance = new UserModifyCtl();
    }

    public void modifyMac(int staff_id , String mac, final IResultCallback callback){
        modifyValue[0] = String.valueOf(staff_id);
        modifyValue[1] = "";
        modifyValue[2] = mac;
        String jsonString = JsonUtil.createJSONString(modifyKey,modifyValue);
        HttpUtil.sendPostHttpRequest(NetAddress.USER_MODIFY, jsonString, new IHttpCallBack() {
            @Override
            public void onFinish(String response) {
                String result = JsonUtil.handleGeneralResponse(response);
                String code = result.substring(0,3);
                String message = result.substring(3);
                Log.e(TAG,"code:"+code+",message:"+message);
                if ("200".equals(code)){
                    callback.onSuccess(message);
                }else {
                    callback.onFail(message);
                }
            }

            @Override
            public void onError(Exception e) {
                callback.onFail("修改失败，请稍后重试");
                Log.e(TAG,e.getMessage());
            }
        });
    }

    public void modifyPassword(int staff_id, String password, final IResultCallback callback) {
        modifyValue[0] = String.valueOf(staff_id);
        modifyValue[1] = "";
        modifyValue[2] = password;
        String jsonString = JsonUtil.createJSONString(modifyKey, modifyValue);
        HttpUtil.sendPostHttpRequest(NetAddress.USER_MODIFY, jsonString, new IHttpCallBack() {
            @Override
            public void onFinish(String response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callback.onFail(e.getMessage());
            }
        });
    }
}
