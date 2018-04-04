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

public class StaffModifyCtl {

    private static final String TAG = "StaffModifyCtl";
    private String[] modifyKey = {"staff_id","tel_num","email"};
    private String[] modifyValue = new String[3];

    private StaffModifyCtl() {
    }

    public static StaffModifyCtl getInstance() {
        return UserModifyCtlHolder.instance;
    }

    private static class UserModifyCtlHolder{
        private static StaffModifyCtl instance = new StaffModifyCtl();
    }

    public void modifyPhone(int staff_id , String phone, final IResultCallback callback){
        modifyValue[0] = String.valueOf(staff_id);
        modifyValue[1] = phone;
        modifyValue[2] = "";
        String jsonString = JsonUtil.createJSONString(modifyKey,modifyValue);
        Log.e(TAG,"modifyPhone jsonString:"+jsonString);
        HttpUtil.sendPostHttpRequest(NetAddress.STAFF_MODIFY, jsonString, new IHttpCallBack() {
            @Override
            public void onFinish(String response) {
                Log.e(TAG,"modifyPhone response:"+response);
                String result = JsonUtil.handleGeneralResponse(response);
                String code = result.substring(0,3);
                String message = result.substring(3);
                Log.e(TAG,"modifyPhone code:"+code+",message:"+message);
                if ("200".equals(code)){
                    callback.onSuccess(message);
                }else {
                    callback.onFail(message);
                }
            }

            @Override
            public void onError(Exception e) {
                callback.onFail("修改失败，请稍后重试");
                Log.e(TAG,"modifyPhone onError:"+e.getMessage());
            }
        });
    }

    public void modifyEmail(int staff_id, String email, final IResultCallback callback) {
        modifyValue[0] = String.valueOf(staff_id);
        modifyValue[1] = "";
        modifyValue[2] = email;
        String jsonString = JsonUtil.createJSONString(modifyKey, modifyValue);
        Log.e(TAG,"modifyEmail jsonString:"+jsonString);
        HttpUtil.sendPostHttpRequest(NetAddress.STAFF_MODIFY, jsonString, new IHttpCallBack() {
            @Override
            public void onFinish(String response) {
                Log.e(TAG,"modifyEmail response:"+response);
                String result = JsonUtil.handleGeneralResponse(response);
                String code = result.substring(0,3);
                String message = result.substring(3);
                Log.e(TAG,"modifyEmail code:"+code+",message:"+message);
                if ("200".equals(code)){
                    callback.onSuccess(message);
                }else {
                    callback.onFail(message);
                }
            }

            @Override
            public void onError(Exception e) {
                callback.onFail("修改失败，请稍后重试");
                Log.e(TAG, "modifyEmail onError:"+e.getMessage());
            }
        });
    }
}
