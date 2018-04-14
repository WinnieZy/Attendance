package com.zy.attendance.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.zy.attendance.R;
import com.zy.attendance.bean.MacRecord;
import com.zy.attendance.controller.MacRequestCtl;
import com.zy.attendance.storage.dao.UserDao;
import com.zy.attendance.storage.db.DbOperator;
import com.zy.attendance.uilib.QLoadingView;
import com.zy.attendance.utils.DateUtil;
import com.zy.attendance.utils.IDataCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by lenovo on 2018/3/26.
 */

public class HomeListView extends LinearLayout implements IMainView,IDataCallback,
                OnDateSelectedListener,OnMonthChangedListener {

    private static final String TAG = "HomeListView";

    private View mLoadingParent;
    private QLoadingView mLoadingView;
    private TextView mLoadingText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private HomeListViewAdapter mListViewAdapter;
    private Context mContext;
    private View mContentView;
    private LinearLayout mTitle_ll;
    private ArrayList mMacRecordList;
    private UserDao mUserDao;
    private DbOperator mDbOperator;
    private String mQueryDate;
    private String mQueryMonth;
    private String mQueryMonday;
    private String mQuerySunday;

    // Handler交互的信息
    private static final int MSG_TIME_OUT = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                // 窗口不存在，不处理消息
                return;
            }
            Log.i(TAG, "handleMessage what = " + msg.what);
            switch (msg.what) {
                case MSG_TIME_OUT:
                    mLoadingView.stopRotationAnimation();
                    mLoadingText.setText("哎呀，加载失败了，请稍后重试~");
                    mLoadingView.setVisibility(INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    public HomeListView(Context context) throws InterruptedException {
        super(context);
        mContext = context;
        mUserDao = new UserDao(mContext);
        mDbOperator = DbOperator.getInstance(mContext);
        initUi();
    }

    private void initUi() throws InterruptedException {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_list_view,null);
        mSwipeRefreshLayout = mContentView.findViewById(R.id.ll_refresh);
        mTitle_ll = mContentView.findViewById(R.id.ll_title);
        mListView = mContentView.findViewById(R.id.list_view_home);
        mLoadingParent = mContentView.findViewById(R.id.list_is_loading);
        mLoadingView = mContentView.findViewById(R.id.loading_view);
        mLoadingText = mContentView.findViewById(R.id.loading_text);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO:下拉刷新
                MacRequestCtl.getInstance().requestMacData(mContext, new IDataCallback() {
                    @Override
                    public void onCallback(String result, Bundle outBundle) {
                        loadingData();
                    }

                    @Override
                    public void onHostFail(int errCode, String errMsg, Bundle inBundle) {
                        loadingData();
                    }
                });
            }
        });
        mListViewAdapter = new HomeListViewAdapter(mContext);
        mListView.setAdapter(mListViewAdapter);
        View view = new View(mContext);
        LayoutParams lp = new LayoutParams(1, 1);
        view.setLayoutParams(lp);
        mListView.addHeaderView(view);
//        mListView.addFooterView(view);
        mLoadingParent.setVisibility(View.VISIBLE);
        mLoadingView.setLoadingViewByType(1);
        mLoadingView.startRotationAnimation();

        mHandler.sendEmptyMessageDelayed(MSG_TIME_OUT,8000);
    }

    public void loadingData() {
        Log.i(TAG,"loadingData");
        ArrayList<MacRecord> arrayList = mDbOperator.queryMacRecordByMonth(DateUtil.getFormatDate("month"));
        if (null != arrayList && arrayList.size() > 0) {
            for (int i = arrayList.size() - 1; i >= 0; i--) {
                Log.i(TAG, "HomeListView loadingData:" + arrayList.get(i).toString());
            }
        }
        updateListView(arrayList);
//        new Thread().sleep(5000);
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("macList",arrayList);
//        callback.onCallback(null,bundle);
    }

    public void updateListView(final ArrayList arrayList){
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"updateListView");
                if (arrayList == null){
                    mLoadingView.stopRotationAnimation();
                    mLoadingText.setText("哎呀，加载失败了，请稍后重试~");
                    mLoadingView.setVisibility(INVISIBLE);
                }else {
                    if (arrayList.size()==0){
                        mLoadingView.stopRotationAnimation();
                        mLoadingParent.setVisibility(View.GONE);
                        mLoadingText.setText("当前查无考勤记录");
                        mLoadingView.setVisibility(INVISIBLE);
                    }else {
                        mListViewAdapter.setListViewData(arrayList);
                        mTitle_ll.setVisibility(VISIBLE);
                        if (mListView != null) {
                            mLoadingView.stopRotationAnimation();
                            mLoadingParent.setVisibility(View.GONE);
                            mListView.setVisibility(VISIBLE);
                        }
                        if (mListViewAdapter != null) {
                            mListViewAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if (mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

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
        if (null != mLoadingView) {
            mLoadingView.stopRotationAnimation();
            mLoadingView.recycle();
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
    public void onCallback(String result, Bundle outBundle) {
        Log.i(TAG,"onCallback");
        if ("onCreate".equals(result)) {
            mHandler.removeMessages(MSG_TIME_OUT);
            loadingData();
        }else if ("conditionQuery".equals(result)){
            ArrayList<MacRecord> arrayList = null;
            if (mQueryMonth == null && mQueryDate == null){
                Log.e(TAG, "mQueryMonth == null && mQueryDate == null");
                arrayList = mDbOperator.queryMacRecordByMonth(DateUtil.getFormatDate("month"));
            }else {
                if (mQueryDate != null) {
                    Log.e(TAG, "mQueryDate != null");
                    arrayList = mDbOperator.queryMacRecordByDate(mQueryMonday, mQuerySunday);
                } else if (mQueryMonth != null) {
                    Log.e(TAG, "mQueryMonth != null");
                    arrayList = mDbOperator.queryMacRecordByMonth(mQueryMonth);
                }
            }
            if (null != arrayList && arrayList.size() > 0) {
                mListView.setVisibility(GONE);
                mLoadingParent.setVisibility(View.VISIBLE);
                mLoadingView.setLoadingViewByType(1);
                mLoadingView.startRotationAnimation();
                for (int i = arrayList.size() - 1; i >= 0; i--) {
                    Log.i(TAG, "HomeListView conditionQuery:" + arrayList.get(i).toString());
                }
                updateListView(arrayList);
            } else {
                Snackbar.make(mContentView, "查无数据", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }else if ("clearDialogData".equals(result)){
            mQueryDate = null;
            mQueryMonday = null;
            mQuerySunday = null;
            mQueryMonth = null;
        }
    }

    @Override
    public void onHostFail(int errCode, String errMsg, Bundle inBundle) {

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-E", Locale.CHINA);

        Calendar cal = date.getCalendar();
        mQueryDate = sdf.format(cal.getTime());
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        mQueryMonday = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        mQuerySunday = sdf.format(cal.getTime());
        Log.e(TAG,"select date:"+mQueryDate+",mQueryMonday:"+mQueryMonday+",mQuerySunday:"+mQuerySunday);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        mQueryMonth = df.format(date.getCalendar().getTime());
        Log.e(TAG,"select month:"+mQueryMonth);
    }
}
