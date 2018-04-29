package com.zy.attendance.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.attendance.R;
import com.zy.attendance.bean.ApplyRecord;
import com.zy.attendance.controller.ApplyRequestCtl;
import com.zy.attendance.storage.dao.StaffDao;
import com.zy.attendance.storage.db.DbOperator;
import com.zy.attendance.uilib.QLoadingView;
import com.zy.attendance.utils.IDataCallback;
import com.zy.attendance.utils.IResultCallback;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/4/16.
 */

public class ApplyListView extends FrameLayout implements IMainView,IResultCallback,IDataCallback,SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ApplyListView";

    private Context mContext;
    private View mLoadingParent;
    private QLoadingView mLoadingView;
    private TextView mLoadingText;
    private View mContentView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private ApplyListViewAdapter mListViewAdapter;
    private StaffDao mStaffDao;
    private DbOperator mDbOperator;
    private int mLastId;//用于ListView分页展示的间隔id
    private ArrayList<ApplyRecord> mApplyRecordList = new ArrayList<>();

    // Handler交互的信息
    private static final int MSG_TIME_OUT = 1;
    private static final int MSG_FAIL_REASON = 2;
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
                    if (mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    break;
                case MSG_FAIL_REASON:
                    Toast.makeText(mContext,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    if (mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public ApplyListView(Context context) {
        super(context);
        mContext = context;
        mStaffDao = new StaffDao(mContext);
        mDbOperator = DbOperator.getInstance(mContext);
        initUi();
    }

    private void initUi() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_apply_list_view,null);
        mLoadingParent = mContentView.findViewById(R.id.list_is_loading);
        mLoadingView = mContentView.findViewById(R.id.loading_view);
        mLoadingText = mContentView.findViewById(R.id.loading_text);
        mSwipeRefreshLayout = mContentView.findViewById(R.id.srl_apply);
        mListView = mContentView.findViewById(R.id.lv_apply);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListViewAdapter = new ApplyListViewAdapter(mContext);
        mListView.setAdapter(mListViewAdapter);
        mLoadingParent.setVisibility(View.VISIBLE);
        mLoadingView.setLoadingViewByType(1);
        mLoadingView.startRotationAnimation();

//        loadingData();
        mHandler.sendEmptyMessageDelayed(MSG_TIME_OUT,8000);
    }

    private void loadingData() {
//        for (int i = 0; i < 5; i++){
//            ApplyRecord applyRecord = new ApplyRecord(mStaffDao.getStaffId(),mStaffDao.getStaffName(),mStaffDao.getLeaderId(),1,"2018-04-15上午~2018-04-16下午","2018-04-15 10:12:00","请假请假请假请假请假请假请假请假请假请假请假请假请假",0);
//            mApplyRecordList.add(applyRecord);
//        }
        //由于后期申请记录越来越多，每次查询的条数需要由第二个参数LimitCount控制
        //此处传值0表示暂时不控制条数，初期处理方式：refresh与初始化均将整个列表进行更新
        //TODO:整体更新需要优化
        mApplyRecordList = mDbOperator.queryApplyRecordById(0,0,false);
        updateListView(mApplyRecordList);
//        mApplyRecordList = mDbOperator.queryApplyRecord("staff_id",String.valueOf(mStaffDao.getStaffId()));
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
//                        mLoadingParent.setVisibility(View.GONE);
                        mLoadingText.setText("当前暂无申请记录");
                        mLoadingView.setVisibility(INVISIBLE);
                    }else {
                        mListViewAdapter.setListViewData(arrayList);
                        if (mSwipeRefreshLayout != null) {
                            mLoadingView.stopRotationAnimation();
                            mLoadingParent.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(VISIBLE);
                        }
                        if (mListViewAdapter != null) {
                            mListViewAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()){
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
    public void onSuccess(String result) {
        mHandler.removeMessages(MSG_TIME_OUT);
        loadingData();
//        mApplyRecordList = mDbOperator.queryApplyRecordById(mLastApplyId,0);
//        updateListView(mApplyRecordList);
//        if (mSwipeRefreshLayout.isRefreshing()){
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
    }

    @Override
    public void onFail(String failReason) {
        //初始化的场景网络失败后还是要记得在本地数据库中获取数据
        if ("onCreate".equals(failReason)){
            mHandler.removeMessages(MSG_TIME_OUT);
            loadingData();
        }else {//非初始化的情况则只需弹出错误提示
            Message message = new Message();
            message.what = MSG_FAIL_REASON;
            message.obj = failReason;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onRefresh() {
        //TODO:申请单信息更新
//        ApplyRequestCtl.getInstance().requestDataByApplyId(mContext, this);
        if (!ApplyRequestCtl.getInstance().updateResult(mStaffDao.getStaffId(), this)) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        } else{
            mHandler.sendEmptyMessageDelayed(MSG_TIME_OUT, 8000);
        }
    }

    //IDataCallback用在添加一条新的申请的情况
    @Override
    public void onCallback(String result, Bundle outBundle) {
        ArrayList<ApplyRecord> applyList = mDbOperator.queryApplyRecord("apply_id", result);
        mApplyRecordList.addAll(applyList);
        updateListView(mApplyRecordList);
    }

    @Override
    public void onHostFail(int errCode, String errMsg, Bundle inBundle) {
        Message message = new Message();
        message.what = MSG_FAIL_REASON;
        message.obj = errMsg;
        mHandler.sendMessage(message);
    }
}
