package com.zy.attendance.utils;

import android.util.Log;

import com.google.gson.Gson;
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
//        int weekday = -1;
//        switch (weekdayStr){
//            case "一":
//                weekday = 1;
//                break;
//            case "二":
//                weekday = 2;
//                break;
//            case "三":
//                weekday = 3;
//                break;
//            case "四":
//                weekday = 4;
//                break;
//            case "五":
//                weekday = 5;
//                break;
//            case "六":
//                weekday = 6;
//                break;
//            case "日":
//                weekday = 0;
//                break;
//            default:
//                break;
//        }
        MacRecord macRecord = new MacRecord(mac,year,month,day,weekday,first_time,last_time);
        Log.e(TAG,macRecord.toString());
        return macRecord;
    }

    public static Staff handleLoginData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONArray("data");
            if (data != null){
                JSONObject jsonArrayObject = data.getJSONObject(0);
                Gson gson = new Gson();
                return gson.fromJson(jsonArrayObject.toString(),Staff.class);
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
