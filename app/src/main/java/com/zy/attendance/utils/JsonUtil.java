package com.zy.attendance.utils;

import android.util.Log;

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
}
