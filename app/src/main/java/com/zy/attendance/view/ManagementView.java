package com.zy.attendance.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zy.attendance.R;
import com.zy.attendance.storage.dao.StaffDao;
import com.zy.attendance.utils.IDataCallback;
import com.zy.attendance.utils.IResultCallback;
import com.zy.attendance.view.Model.MyViewPager;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/3/28.
 */

public class ManagementView extends LinearLayout implements IMainView,IResultCallback,IDataCallback {

    private static final String TAG = "ManagementView";
    private Context mContext;
    private View mContentView;
    private LinearLayout mLL_title;
    private TextView mTitle_apply;
    private TextView mTitle_approval;
    private ViewPager mViewPager;
    private ApplyListView mApplyListView;
    private ApprovalListView mApprovalListView;
    private final ArrayList<View> viewContainer = new ArrayList<View>();
    private StaffDao mStaffDao;

    public ManagementView(Context context) {
        super(context);
        mContext = context;
        mStaffDao = new StaffDao(mContext);
        initUi();
    }

    private void initUi() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_management,null);
        mLL_title = mContentView.findViewById(R.id.ll_title);
        mViewPager = mContentView.findViewById(R.id.viewPager_manage);
        mTitle_apply = mLL_title.findViewById(R.id.tv_apply);
        mTitle_approval = mLL_title.findViewById(R.id.tv_approval);
        View apply_divider = mLL_title.findViewById(R.id.apply_divider);
        mTitle_apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"mTitle_apply onclick");
                mViewPager.setCurrentItem(0);
                mTitle_apply.setTextColor(getResources().getColor(R.color.colorPrimary));
                mTitle_approval.setTextColor(getResources().getColor(R.color.text_gray));
            }
        });
        //TODO:加入条件判断当前用户是否有审批权限
        if (mStaffDao.getApproval_auth()){
            mTitle_apply.setText(getResources().getString(R.string.apply));
            mTitle_approval.setVisibility(VISIBLE);
            apply_divider.setVisibility(VISIBLE);
            mTitle_approval.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG,"mTitle_approval onclick");
                    mViewPager.setCurrentItem(1);
                    mTitle_apply.setTextColor(getResources().getColor(R.color.text_gray));
                    mTitle_approval.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            });
        }
        mApplyListView = new ApplyListView(mContext);
        mApprovalListView = new ApprovalListView(mContext);
        synchronized (viewContainer) {
            viewContainer.clear();
            viewContainer.add(mApplyListView.getContentView());
            viewContainer.add(mApprovalListView.getContentView());
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
        mApplyListView.onDestroy();
        mApprovalListView.onDestroy();
        //遍历并发出
        synchronized (viewContainer) {
            viewContainer.clear();
        }
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

    @Override
    public void onSuccess(String result) {
        mApplyListView.onSuccess(result);
        mApprovalListView.onSuccess(result);
    }

    @Override
    public void onFail(String failReason) {
        mApplyListView.onFail(failReason);
        mApprovalListView.onFail(failReason);
    }

    @Override
    public void onCallback(String result, Bundle outBundle) {
        mApplyListView.onCallback(result,outBundle);
    }

    @Override
    public void onHostFail(int errCode, String errMsg, Bundle inBundle) {
        mApplyListView.onHostFail(errCode,errMsg,null);
    }
}
