package com.zy.attendance.controller;

import android.content.Context;
import android.util.Log;

import com.zy.attendance.bean.ApplyRecord;
import com.zy.attendance.constants.NetAddress;
import com.zy.attendance.storage.dao.StaffDao;
import com.zy.attendance.storage.db.DbOperator;
import com.zy.attendance.utils.HttpUtil;
import com.zy.attendance.utils.IDataCallback;
import com.zy.attendance.utils.IHttpCallBack;
import com.zy.attendance.utils.IResultCallback;
import com.zy.attendance.utils.JsonUtil;

import java.util.ArrayList;

import static com.zy.attendance.utils.JsonUtil.handleGeneralResponse;

/**
 * 用于申请数据的网络传输及本地存储逻辑
 * Created by lenovo on 2018/4/18.
 */

public class ApplyRequestCtl {

    private static final String TAG = "ApplyRequestCtl";
    private String[] modifyKey = {"staff_id","password","mac"};
    private String[] modifyValue = new String[3];
    private DbOperator mDbOperator;

    private ApplyRequestCtl() {
    }

    public static ApplyRequestCtl getInstance() {
        return UserModifyCtlHolder.instance;
    }

    private static class UserModifyCtlHolder{
        private static ApplyRequestCtl instance = new ApplyRequestCtl();
    }

    public void sendApplyData(final Context context, final ApplyRecord applyRecord, final IDataCallback callback){
        String address = NetAddress.APPLY_ADD;
        String[] key = {"staff_id","staff_name","leader_id","type","apply_time_for","apply_time_at","reason"};
        String[] value = {String.valueOf(applyRecord.getStaff_id()),applyRecord.getStaff_name(),
                String.valueOf(applyRecord.getLeader_id()),String.valueOf(applyRecord.getType()),
                applyRecord.getApply_time_for(),applyRecord.getApply_time_at(),applyRecord.getReason()};
        String jsonString = JsonUtil.createJSONString(key,value);
        Log.d(TAG, "jsonString:"+jsonString);
        HttpUtil.sendPostHttpRequest(address, jsonString, new IHttpCallBack() {
            @Override
            public void onFinish(String response) {
                Log.e(TAG,"add apply response:"+response.toString());
                String result = JsonUtil.handleGeneralResponse(response);
                String code = result.substring(0,3);
                Log.d(TAG,"code:"+code);
                if ("200".equals(code)) {
                    String apply_id = JsonUtil.handleApplyAddResponse(response);
                    applyRecord.setApply_id(Integer.parseInt(apply_id));
                    mDbOperator = DbOperator.getInstance(context);
                    mDbOperator.addApplyRecord(applyRecord);
                    callback.onCallback(apply_id,null);
                }else {
                    String message = result.substring(3);
                    callback.onHostFail(Integer.parseInt(code),message,null);
                    Log.d(TAG,"message:"+message);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: "+e.getMessage());
                callback.onHostFail(400,e.getMessage(),null);
            }
        });
    }

    public void requestDataByApplyId(Context context, final IResultCallback callback) {
        mDbOperator = DbOperator.getInstance(context);
        int apply_id = mDbOperator.getLatestApplyRecordApplyId();
        int staff_id = new StaffDao(context).getStaffId();
        Log.d(TAG, "apply_id:"+apply_id+",staff_id:"+staff_id);
        String address = NetAddress.APPLY_REQUEST ;
        Log.d(TAG, "MainActivity getMac address:" + address);
        String[] key = {"apply_id","staff_id"};
        String[] value = {String.valueOf(apply_id),String.valueOf(staff_id)};
        String jsonString = JsonUtil.createJSONString(key,value);
        Log.d(TAG, "jsonString:"+jsonString);
        HttpUtil.sendPostHttpRequest(address, jsonString,new IHttpCallBack() {

            @Override
            public void onFinish(String response) {
                Log.d(TAG, "response:" + response);
                String result = handleGeneralResponse(response);
                String code = result.substring(0,3);
                String message = result.substring(3);
                Log.e(TAG,"code:"+code+",message:"+message);
                if ("200".equals(code)){
                    if ("success".equals(message)){
                        //TODO:接口接收数据处理逻辑
                        ArrayList<ApplyRecord> applyList= JsonUtil.handleApplyResponse(response);
                        if (applyList != null){
                            for (ApplyRecord applyRecord : applyList) {
                                Log.e(TAG, applyRecord.toString());
                                mDbOperator.addApplyRecord(applyRecord);
                            }
                        }
                    }else {
                        Log.e(TAG,"200 and message:"+message);
                    }
                }else{
                    Log.e(TAG,"code:"+code+",message:"+message);
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Log.e(TAG,"thread sleep catch,message:"+e.getMessage());
                }
                callback.onSuccess(message);
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "error:" + e.getMessage());
                callback.onFail("on Error Exception");
            }
        });
    }
}
