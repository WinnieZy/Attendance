package com.zy.attendance.view;

import android.content.Intent;
import android.os.Bundle;

/**
 * 模拟activity生命周期几个函数的基类
 * 
 * @author yanfuchen
 *
 */
public abstract interface IActivityLifeCycle {
	public abstract void onCreate(Bundle bundle);

	public abstract void onResume();

	public abstract void onPause();

	public abstract void onDestroy();

	public abstract void onStart();

	public abstract void onStop();

	public void onNewIntent(Intent intent);

	public void onRestart();
}
