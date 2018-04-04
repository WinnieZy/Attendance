package com.zy.attendance.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.zy.attendance.bean.Staff;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 2018/3/23.
 */

public class JsonUtil {

    public static String[] handleMacResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonArrayObject = data.getJSONObject(i);
                String mac = jsonArrayObject.getString("mac");
                String date = jsonArrayObject.getString("date");
                String first_time = jsonArrayObject.getString("first_time");
                String last_time = jsonArrayObject.getString("last_time");
                Log.e("winnie","mac:"+mac+",date:"+date+",first_time:"+first_time+",last_time:"+last_time);
                String[] result = new String[4];
                result[0] = mac;
                result[1] = date;
                result[2] = first_time;
                result[3] = last_time;
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String handleLoginResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("message");
            return code+message;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "400未知错误";
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
            e.printStackTrace();
        }
        return null;
    }

    public static String handleRegisterResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("message");
            return code+message;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "400未知错误";
    }

    //用于生成Json字符串post到服务器端
    public static String createJSONString(String[] key, String[] value){
        try {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < key.length; i++) {
                Log.e("LoginActivity",key[i]+value[i]);
                jsonObject.put(key[i],value[i]);
            }
            String json = String.valueOf(jsonObject);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
