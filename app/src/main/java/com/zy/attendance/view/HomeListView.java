package com.zy.attendance.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zy.attendance.R;
import com.zy.attendance.bean.MacRecord;
import com.zy.attendance.uilib.QLoadingView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/3/26.
 */

public class HomeListView extends LinearLayout implements IMainView,IMainCallback{

    private View mLoadingParent;
    private QLoadingView mLoadingView;
    private TextView mLoadingText;
    private ListView mListView;
    private HomeListViewAdapter mListViewAdapter;
    private Context mContext;
    private View mContentView;
    private LinearLayout mTitle_ll;
    private ArrayList mMacRecordList;

    public HomeListView(Context context) throws InterruptedException {
        super(context);
        mContext = context;
        initUi();
    }

    private void initUi() throws InterruptedException {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_list_view,null);
        mTitle_ll = mContentView.findViewById(R.id.ll_title);
        mListView = mContentView.findViewById(R.id.list_view_home);
        mLoadingParent = mContentView.findViewById(R.id.list_is_null_loading);
        mLoadingView = mContentView.findViewById(R.id.loading_view);
        mLoadingText = mContentView.findViewById(R.id.loading_text);

        mListViewAdapter = new HomeListViewAdapter(mContext);
        mListView.setAdapter(mListViewAdapter);
        mLoadingParent.setVisibility(View.VISIBLE);
        mLoadingView.setLoadingViewByType(1);
        mLoadingView.startRotationAnimation();
        final IMainCallback callback = this;
        Log.i("winnie","Thread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("winnie","try");
                    loadingData(callback);
                } catch (InterruptedException e) {
                    Log.i("winnie","catch");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void loadingData(IMainCallback callback) throws InterruptedException {
        Log.i("winnie","loadingData");
        ArrayList<MacRecord> arrayList = new ArrayList<>(5);
        for (int i = 0; i < 50; i++) {
            MacRecord macRecord = new MacRecord("03-21","10:45:12","22:32:15");
            arrayList.add(macRecord);
        }
        new Thread().sleep(3000);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("mac",arrayList);
        callback.onCallback(null,bundle);
    }

    public void updateListView(final ArrayList arrayList){
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("winnie","updateListView");
                mLoadingView.stopRotationAnimation();
                if (arrayList == null){
                    mLoadingText.setText("哎呀，加载失败了，请稍后重试~");
                    mLoadingView.setVisibility(INVISIBLE);
                }else {
                    mListViewAdapter.setListViewData(arrayList);
                    mLoadingParent.setVisibility(View.GONE);
                }
                mTitle_ll.setVisibility(VISIBLE);
                if (mListView != null){
                    mListView.setVisibility(VISIBLE);
                    View view = new View(mContext);
                    LayoutParams lp = new LayoutParams(1, 1);
                    view.setLayoutParams(lp);
                    mListView.addHeaderView(view);
                    mListView.addFooterView(view);
                }
                if (mListViewAdapter != null){
                    mListViewAdapter.notifyDataSetChanged();
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
    public void onCallback(Bundle inBundle, Bundle outBundle) {
        Log.i("winnie","onCallback");
        mMacRecordList = outBundle.getParcelableArrayList("mac");
        updateListView(mMacRecordList);
    }

    @Override
    public void onHostFail(int errCode, String errMsg, Bundle inBundle) {

    }
}
