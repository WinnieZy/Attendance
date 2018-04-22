package com.zy.attendance.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.zy.attendance.bean.ApplyRecord;
import com.zy.attendance.bean.MacRecord;
import com.zy.attendance.bean.Staff;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/3/23.
 */

public class JsonUtil {

    private static final String TAG = "JsonUtil";

    public static ArrayList<MacRecord> handleMacResponse(String response){
        ArrayList<MacRecord> macList = new ArrayList<MacRecord>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonArrayObject = data.getJSONObject(i);
                String mac = jsonArrayObject.getString("mac");
                String date = jsonArrayObject.getString("date");
                String first_time = jsonArrayObject.getString("first_time");
                String last_time = jsonArrayObject.getString("last_time");
                Log.e(TAG,"handleMacResponse,mac:"+mac+",date:"+date+",first_time:"+first_time+",last_time:"+last_time);
                macList.add(macToMacRecord(mac,date,first_time,last_time));
            }
            return macList;
        } catch (JSONException e) {
            Log.e(TAG,"handleMacResponse,exception:"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static MacRecord macToMacRecord(String mac, String date, String first_time, String last_time){
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);
        String weekday = date.substring(11,12);
        MacRecord macRecord = new MacRecord(mac,year,month,day,weekday,first_time,last_time);
        Log.e(TAG,macRecord.toString());
        return macRecord;
    }

    public static ArrayList<ApplyRecord> handleApplyResponse(String response){
        ArrayList<ApplyRecord> applyList = new ArrayList<ApplyRecord>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonArrayObject = data.getJSONObject(i);
                int apply_id = jsonArrayObject.getInt("apply_id");
                int staff_id = jsonArrayObject.getInt("staff_id");
                String staff_name = jsonArrayObject.getString("staff_name");
                int leader_id = jsonArrayObject.getInt("leader_id");
                int type = jsonArrayObject.getInt("type");
                String apply_time_for = jsonArrayObject.getString("apply_time_for");
                String apply_time_at = jsonArrayObject.getString("apply_time_at");
                String reason = jsonArrayObject.getString("reason");
                int result = jsonArrayObject.getInt("result");
                Log.e(TAG,"handleApplyResponse,apply_id:"+apply_id+",staff_id:"+staff_id+",staff_name:"+staff_name+",leader_id:"+leader_id+",type:"+type+",apply_time_for:"+apply_time_for+",apply_time_at:"+apply_time_at+",reason:"+reason+",result:"+result);
                applyList.add(new ApplyRecord(apply_id,staff_id,staff_name,leader_id,type,apply_time_for,apply_time_at,reason,result));
            }
            return applyList;
        } catch (JSONException e) {
            Log.e(TAG,"handleMacResponse,exception:"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String handleApplyAddResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getJSONArray("data").get(0).toString();
        } catch (JSONException e) {
            Log.e(TAG,"handleApplyAddResponse,exception:"+e.getMessage());
        }
        return null;
    }

    public static Staff handleLoginData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONArray("data");
            if (data != null){
                JSONObject jsonArrayObject = data.getJSONObject(0);
                Staff staff = new Gson().fromJson(jsonArrayObject.toString(),Staff.class);
                String approval_auth = jsonArrayObject.getString("approval_auth");
                staff.setApproval_auth("1".equals(approval_auth));
                return staff;
            }else {
                return null;
            }
        } catch (JSONException e) {
            Log.e(TAG,"handleLoginData,exception:"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String handleGeneralResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("message");
            return code+message;
        } catch (JSONException e) {
            Log.e(TAG,"handleGeneralResponse,exception:"+e.getMessage());
            e.printStackTrace();
        }
        return "400未知错误";
    }

    //用于生成Json字符串post到服务器端
    public static String createJSONString(String[] key, String[] value){
        try {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < key.length; i++) {
                Log.e(TAG,"createJSON:"+key[i]+":"+value[i]);
                jsonObject.put(key[i],value[i]);
            }
            return String.valueOf(jsonObject);
        } catch (Exception e) {
            Log.e(TAG,"createJSON,exception:"+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
