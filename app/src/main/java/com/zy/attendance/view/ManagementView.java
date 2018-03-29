package com.zy.attendance.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zy.attendance.R;
import com.zy.attendance.view.Model.MyViewPager;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/3/28.
 */

public class ManagementView extends LinearLayout implements IMainView {

    private static final String TAG = "ManagementView";
    private Context mContext;
    private View mContentView;
    private TextView mTitle_apply;
    private TextView mTitle_approval;
    private LinearLayout mLL_title;
    private MyViewPager mViewPager;
    private final ArrayList<View> viewContainer = new ArrayList<View>();

    public ManagementView(Context context) {
        super(context);
        mContext = context;
        initUi();
    }

    private void initUi() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_management,null);
        mLL_title = mContentView.findViewById(R.id.ll_title);
        mViewPager = mContentView.findViewById(R.id.viewPager_manage);
        mTitle_apply = mLL_title.findViewById(R.id.tv_apply);
        mTitle_approval = mLL_title.findViewById(R.id.tv_approval);
        mTitle_apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"mTitle_apply onclick");
                mViewPager.setCurrentItem(0);
            }
        });
        mTitle_approval.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"mTitle_approval onclick");
                mViewPager.setCurrentItem(1);
            }
        });
        synchronized (viewContainer) {
            viewContainer.clear();
            TextView tv1 = new TextView(mContext);
            TextView tv2 = new TextView(mContext);
            tv1.setText("申请");
            tv2.setText("审批");
            viewContainer.add(tv1);
            viewContainer.add(tv2);
        }
        mViewPager.setAdapter(mPagerAdapter);
    }

    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return viewContainer.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("winnie","instantiateItem position:"+position);
            ((MyViewPager) container).addView(viewContainer.get(position));
            Object obj = null;
            synchronized (viewContainer) {
                obj = viewContainer.get(position);
            }
            return obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((MyViewPager) container).removeView(viewContainer.get(position));
        }
    };

    @Override
    public void onCreate(Bundle bundle) {

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
