package com.zy.attendance.utils;

public interface HttpCallBackListener {

	void onFinish(String response);
	
	void onError(Exception e);
}