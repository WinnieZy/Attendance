package com.zy.attendance.uilib;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * UI库环境配置 职责：负责资源环境的切换，例如插件环境到宿主环境
 * 
 * @author potatoxu
 * 
 */
public class UIConfig {
	// 这个常量是在编译时由脚本自动替换的，写成常量是为了使编译出来的包更小
	public static final boolean KEEP_TEST_CODE = true;
	
	public static final boolean IS_STATUSBAR_TRANSPARENT = Build.VERSION.SDK_INT >= 19;

	/** 系统资源属性定义命名空间 **/
	public static final String ANDROID_ATTRIBUTE_NAME_SPACE = "http://schemas.android.com/apk/res/android";

	/** 管家宿主资源属性定义命名空间 **/
	public static final String HOST_ATTRIBUTE_NAME_SPACE = "com.zy.attendance";
	
	/** 管家资源包名 **/
	public static final String RES_PKG_NAME = "com.tencent.wifimanager";
	
	/** UI库内部定义的View附带的key **/
	public static final int UILIB_INTERNAL_KEY = Integer.MAX_VALUE;
	
	/** UI库内部定义的View附带的tag **/
	public static final String UILIB_INTERNAL_TAG = "uilib.internal.tag";
	
	public static final String PAGE_TRANSITION_STYLE = "page_transition_style";
	public static final int TRANSITION_STYLE_SLIDE = 0;
	public static final int TRANSITION_STYLE_SCALE = 1;
	public static final int TRANSITION_STYLE_NONE  = 2;
	public static final int TRANSITION_STYLE_BOTTOM_SLIDE  = 3;

	/** 宿主环境 **/
	private static Context mUILibContext;

	/**
	 * 屏幕宽度
	 */
	public static int canvasWidth;

	/**
	 * 屏幕高度
	 */
	public static int canvasHeight;
	
	public static int sStatusBarHeight = 0;
	/**
	 * 获取UI库相关的Context
	 */
	public static Context getUILibContext() {
		return mUILibContext;
	}

	/**
	 * 初始化UI库
	 */
	public static void initUILib(Context context) {
		mUILibContext = context;
	}

	/**
	 * 获取UI库资源
	 */
	public static Resources getUILibResources(Context context) {
		return mUILibContext.getResources();
	}

	/**
	 * 获取UI库定义的字符串资源
	 */
	public static String getUILibString(Context context, int id) {
		return mUILibContext.getString(id);
	}

	/**
	 * 获取UI库定义的颜色资源
	 */
	public static int getUILibColor(Context context, int id) {
		return mUILibContext.getResources().getColor(id);
	}

	/**
	 * 获取UI库定义的Drawable资源
	 */
	public static Drawable getUILibDrawable(Context context, int id) {
		return mUILibContext.getResources().getDrawable(id);			
	}
	
//	/**
//	 * 释放UI库图片资源
//	 */
//	public static void destroyUILibDrawable(){
//		if (mUILibContext instanceof IDrawableHolderOwner){
//			((IDrawableHolderOwner)mUILibContext).destroyHolder();
////			System.gc();
//		}
//	}

	/**
	 * 利用UILib定义的资源setTextAppearance
	 */
	public static void setTextAppearanceByUILib(Context context,
                                                TextView textView, int resid) {
		try {
			textView.setTextAppearance(mUILibContext, resid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * inflate UI库定义的资源
	 */
	public static View inflateUILibResource(int resource, ViewGroup root,
                                            boolean attachToRoot) {
		View v = LayoutInflater.from(mUILibContext).inflate(resource, root,
				attachToRoot);
		initUILibInternalTag(v);
		return v;
	}

	/**
	 * inflate UI库定义的资源
	 */
	public static View inflateUILibResource(int resource, ViewGroup root) {
		return inflateUILibResource(resource, root, root != null);
	}

	/**
	 * 判断是否为UI库资源
	 */
	public static boolean isUILibView(Object view) {
		if (view == null) {
			return false;
		}

		boolean inUILib = view.getClass().getPackage().getName()
				.startsWith("uilib.");
		return inUILib;
	}

	/**
	 * 指定使用宿主定义的资源来设置背景
	 * 
	 * @param view
	 * @param backgroundResourceId
	 */
	public static void setBackgroundUseUILibRes(View view,
			int backgroundResourceId) {
		view.setBackgroundDrawable(getUILibDrawable(view.getContext(),
				backgroundResourceId));
	}

	/**
	 * 初始化宿主View及其子View的tag标志
	 * 只供UI库内部使用,递归实现
	 * @hide
	 */
	public static void initUILibInternalTag(View v){
		if (v == null){
			return;
		}
		v.setTag(UILIB_INTERNAL_KEY, UILIB_INTERNAL_TAG);
		if (v instanceof ViewGroup){
			ViewGroup parent = (ViewGroup) v;
			View child;
			int count = parent.getChildCount();
			for(int index = 0; index < count; index++){
				child = parent.getChildAt(index);
				if (child != null){
					initUILibInternalTag(child);
				}
			}
		}
	}
	
	public static int getStatusBarHeight() {
		if (sStatusBarHeight == 0) {
			 sStatusBarHeight = Tools.initStatusBarHeight(mUILibContext);
		 }
		return sStatusBarHeight;
	}
}
