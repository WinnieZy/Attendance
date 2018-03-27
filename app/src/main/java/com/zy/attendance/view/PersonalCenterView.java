package com.zy.attendance.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zy.attendance.R;

/**
 * Created by lenovo on 2018/3/26.
 */

public class PersonalCenterView extends LinearLayout implements IMainView {

    private Context mContext;
    private View mContentView;

    public PersonalCenterView(Context context){
        super(context);
        mContext = context;
//        initUi();
    }
    @Override
    public void onCreate(Bundle bundle) {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_personal_center,null);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide(int code) {

    }

    @Override
    public View getContentView() {
        return mContentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
