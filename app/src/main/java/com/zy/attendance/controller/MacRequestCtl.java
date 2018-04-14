package com.zy.attendance.controller;

import android.content.Context;
import android.util.Log;

import com.zy.attendance.bean.MacRecord;
import com.zy.attendance.constants.NetAddress;
import com.zy.attendance.storage.dao.UserDao;
import com.zy.attendance.storage.db.DbOperator;
import com.zy.attendance.utils.DateUtil;
import com.zy.attendance.utils.HttpUtil;
import com.zy.attendance.utils.IDataCallback;
import com.zy.attendance.utils.IHttpCallBack;
import com.zy.attendance.utils.JsonUtil;
import com.zy.attendance.utils.WiFiUtil;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/4/4.
 */

public class MacRequestCtl {

    private static final String TAG = "MacRequestCtl";
    private String[] modifyKey = {"staff_id","password","mac"};
    private String[] modifyValue = new String[3];
    private DbOperator mDbOperator;

    private MacRequestCtl() {
    }

    public static MacRequestCtl getInstance() {
        return UserModifyCtlHolder.instance;
    }

    private static class UserModifyCtlHolder{
        private static MacRequestCtl instance = new MacRequestCtl();
    }

    public void requestMacData(Context context, final IDataCallback callback) {
        String mac = new UserDao(context).getMac();
        if ("".equals(mac)){
            mac = WiFiUtil.getLocalMacAddress(context);
        }
        mDbOperator = DbOperator.getInstance(context);
        MacRecord macRecord = mDbOperator.getLatestMacRecord();
        String date = "";
        int updateId = -1;
        boolean isAdd = true;
        if (macRecord != null){
            //TODO
            date = DateUtil.combineMacDate(macRecord);
            String date10 = date.substring(0,10);
            if (date10.equals(DateUtil.getFormatDate("day"))){
                isAdd = false;
                updateId = macRecord.getId();
            }
        }
//        String address = NetAddress.MAC_REQUEST + "?mac="+mac+"&&date="+date+"&&isAdd="+isAdd;

        String isAddStr = String.valueOf(isAdd);
        String address = NetAddress.MAC_REQUEST ;
        Log.d(TAG, "MainActivity getMac address:" + address);
        String[] key = {"mac","date","isAdd"};
        String[] value = {mac,date,isAddStr};
        String jsonString = JsonUtil.createJSONString(key,value);
        Log.d(TAG, "jsonString:"+jsonString);
        Log.d(TAG, "address:"+address);
        final boolean finalIsAdd = isAdd;
        final int finalUpdateId = updateId;
        HttpUtil.sendPostHttpRequest(address, jsonString,new IHttpCallBack() {

            @Override
            public void onFinish(String response) {
                Log.d(TAG, "response:" + response);
                String result = JsonUtil.handleGeneralResponse(response);
                String code = result.substring(0,3);
                String message = result.substring(3);
                Log.e(TAG,"code:"+code+",message:"+message);
                if ("200".equals(code)){
                    if ("success".equals(message)){
                        ArrayList<MacRecord> macList= JsonUtil.handleMacResponse(response);
                        if (macList != null){
                            if (finalIsAdd){
                                Log.e(TAG,"isAdd mac");
                                for (MacRecord macRecord : macList){
                                    Log.e(TAG,macRecord.toString());
                                    mDbOperator.addMacRecord(macRecord);
                                }
                            }else {
                                Log.e(TAG,"update mac");
                                mDbOperator.updateMacRecord(finalUpdateId,macList.get(0));
                            }
                        }
                    }else {
                        Log.e(TAG,"200 and message:"+message);
                    }
                }else{
                    Log.e(TAG,"code:"+code+",message:"+message);
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Log.e(TAG,"thread sleep catch,message:"+e.getMessage());
                }
                callback.onCallback(null,null);
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "error:" + e.getMessage());
                callback.onCallback(null,null);
            }
        });
    }
}
