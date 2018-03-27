package com.zy.attendance.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lenovo on 2018/3/23.
 */

/*
 * 网络连接工具类 POST　GET
 */
public class HttpUtil {

    public static void sendGetHttpRequest(final String address,final HttpCallBackListener listener){

        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null) {
                        response.append(line);
                    }
                    Log.e("winnie","response.toString():"+response.toString());
                    if (listener!=null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener!=null) {
                        listener.onError(e);
                    }
                }finally{
                    if (connection!=null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendPostHttpRequest(final String address,final String jsonString,final HttpCallBackListener listener){

        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.connect();
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(jsonString);
                    outputStream.flush();
                    outputStream.close();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null) {
                        response.append(line);
                    }
                    if (listener!=null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener!=null) {
                        listener.onError(e);
                    }
                }finally{
                    if (connection!=null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
