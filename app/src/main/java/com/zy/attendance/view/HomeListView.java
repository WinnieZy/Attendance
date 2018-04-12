package com.zy.attendance.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zy.attendance.R;
import com.zy.attendance.bean.MacRecord;
import com.zy.attendance.storage.dao.UserDao;
import com.zy.attendance.storage.db.DbOperator;
import com.zy.attendance.uilib.QLoadingView;
import com.zy.attendance.utils.IDataCallback;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/3/26.
 */

public class HomeListView extends LinearLayout implements IMainView,IDataCallback {

    private static final String TAG = "HomeListView";

    private View mLoadingParent;
    private QLoadingView mLoadingView;
    private TextView mLoadingText;
    private ListView mListView;
    private HomeListViewAdapter mListViewAdapter;
    private Context mContext;
    private View mContentView;
    private LinearLayout mTitle_ll;
    private ArrayList mMacRecordList;
    private UserDao mUserDao;
    private DbOperator mDbOperator;

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

    public IDataCallback getListViewCallback(){
        return this;
    }

    private void initUi() throws InterruptedException {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_list_view,null);
        mTitle_ll = mContentView.findViewById(R.id.ll_title);
        mListView = mContentView.findViewById(R.id.list_view_home);
        mLoadingParent = mContentView.findViewById(R.id.list_is_loading);
        mLoadingView = mContentView.findViewById(R.id.loading_view);
        mLoadingText = mContentView.findViewById(R.id.loading_text);

        mListViewAdapter = new HomeListViewAdapter(mContext);
        mListView.setAdapter(mListViewAdapter);
        mLoadingParent.setVisibility(View.VISIBLE);
        mLoadingView.setLoadingViewByType(1);
        mLoadingView.startRotationAnimation();

        mHandler.sendEmptyMessageDelayed(MSG_TIME_OUT,8000);
    }

    public void loadingData() {
        Log.i(TAG,"loadingData");
        ArrayList<MacRecord> arrayList = mDbOperator.queryMacRecord("mac",mUserDao.getMac());
        for (int i = arrayList.size() - 1; i >= 0; i--) {
            Log.i(TAG,"HomeListView loadingData:"+arrayList.get(i).toString());
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
                        mLoadingText.setText("当前用户查无考勤记录");
                        mLoadingView.setVisibility(INVISIBLE);
                    }else {
                        mListViewAdapter.setListViewData(arrayList);
                        mTitle_ll.setVisibility(VISIBLE);
                        if (mListView != null) {
                            mLoadingView.stopRotationAnimation();
                            mLoadingParent.setVisibility(View.GONE);
                            mListView.setVisibility(VISIBLE);
                            View view = new View(mContext);
                            LayoutParams lp = new LayoutParams(1, 1);
                            view.setLayoutParams(lp);
                            mListView.addHeaderView(view);
//                    mListView.addFooterView(view);
                        }
                        if (mListViewAdapter != null) {
                            mListViewAdapter.notifyDataSetChanged();
                        }
                    }
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
        mHandler.removeMessages(MSG_TIME_OUT);
        loadingData();
    }

    @Override
    public void onHostFail(int errCode, String errMsg, Bundle inBundle) {

    }
}
