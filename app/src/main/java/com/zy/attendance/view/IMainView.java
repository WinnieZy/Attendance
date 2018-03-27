package com.zy.attendance.view;

import android.os.Bundle;
import android.view.View;


/**
 * 模拟生命周期
 * @author rabbywang
 *
 */
public abstract interface IMainView extends IActivityLifeCycle{
	/**
	 * 类似于表示View 当前正在显示
	 */
	public abstract void onShow();

	/**
	 * 表示当前页面已经发生改变
	 */
	public abstract void onHide(int code);

	/**
	 * 用来Add
	 * @return 返回对象实例
	 */
	public abstract View getContentView();
	
	public void onSaveInstanceState(Bundle outState);

	public interface HideTabCode {
		/** 切换TAB **/
		int CODE_SWITCH_TAB = 0x01;
		int CODE_LEAVE_MAIN_VIEW = 0x02;
	}
}
