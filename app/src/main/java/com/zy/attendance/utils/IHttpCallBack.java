package com.zy.attendance.utils;

public interface IHttpCallBack {

	void onFinish(String response);
	
	void onError(Exception e);
}
