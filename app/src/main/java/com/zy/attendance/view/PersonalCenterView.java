package com.zy.attendance.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zy.attendance.R;
import com.zy.attendance.uilib.Tools;
import com.zy.attendance.uilib.UIConfig;
import com.zy.attendance.utils.WiFiUtil;
import com.zy.attendance.view.Model.ItemModel;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/3/26.
 */

public class PersonalCenterView extends ScrollView implements IMainView {

    private static final String TAG = "PersonalCenterView";
    private Context mContext;
    private LinearLayout mContentLayout;
    private LinearLayout mUserLayout;
    private LinearLayout mStaffLayout;
    private LinearLayout mSignOutLayout;

    private ArrayList<ItemModel> mItemModelList = new ArrayList<>();
    private ArrayList<View> mItemModelViewList = new ArrayList<>();

    public PersonalCenterView(Context context){
        super(context);
        mContext = context;
        initUi();
    }

    private void initUi() {
        mContentLayout = new LinearLayout(mContext);
        mContentLayout.setPadding(0, Tools.dip2px(mContext, 5), 0, Tools.dip2px(mContext, 5));
        mContentLayout.setOrientation(LinearLayout.VERTICAL);

//        mScrollView = new ScrollView(mContext);
//        mScrollView.setOverScrollMode(2); // ScrollView.OVER_SCROLL_NEVER
        this.setVerticalScrollBarEnabled(false);
        UIConfig.setBackgroundUseUILibRes(this, R.color.personal_view_bg);
        this.addView(mContentLayout);

        mUserLayout = new LinearLayout(mContext);
        mUserLayout.setBackgroundResource(R.drawable.common_cards_bg);
        mUserLayout.setOrientation(LinearLayout.VERTICAL);
        mStaffLayout = new LinearLayout(mContext);
        mStaffLayout.setBackgroundResource(R.drawable.common_cards_bg);
        mStaffLayout.setOrientation(LinearLayout.VERTICAL);
        mSignOutLayout = new LinearLayout(mContext);
        mSignOutLayout.setBackgroundResource(R.drawable.common_cards_bg);
        mSignOutLayout.setOrientation(LinearLayout.VERTICAL);
        mSignOutLayout.addView(createModelView(new ItemModel(R.drawable.icon_quit,"退出账号")));
        mSignOutLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"mSignOutLayout onclick");
            }
        });
        initializeData();
        mContentLayout.addView(mUserLayout);
        mContentLayout.addView(mStaffLayout);
        mContentLayout.addView(mSignOutLayout);
    }

    private View createModelView(ItemModel model) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_model_view,null);
        ImageView icon = itemView.findViewById(R.id.iv_icon);
        TextView title = itemView.findViewById(R.id.tv_title_personal);
        TextView content = itemView.findViewById(R.id.tv_content_personal);
        icon.setImageResource(model.getIconId());
        title.setText(model.getTitle());
        content.setText(model.getContent());
        return itemView;
    }

    private void initializeData(){
        storeModelItem(new ItemModel(R.drawable.icon_name,"姓名"));
        storeModelItem(new ItemModel(R.drawable.icon_mobile,"手机号"));
        storeModelItem(new ItemModel(R.drawable.icon_email,"邮箱"));
        storeModelItem(new ItemModel(R.drawable.icon_no,"工号"));
        storeModelItem(new ItemModel(R.drawable.icon_mac,"设备mac", WiFiUtil.getLocalMacAddress(mContext)));
        storeModelItem(new ItemModel(R.drawable.icon_leader,"直属上级"));
        storeModelItem(new ItemModel(R.drawable.icon_calendar,"入职日期"));
        mUserLayout.addView(mItemModelViewList.get(0));
        mUserLayout.addView(mItemModelViewList.get(1));
        mUserLayout.addView(mItemModelViewList.get(2));
        mStaffLayout.addView(mItemModelViewList.get(3));
        mStaffLayout.addView(mItemModelViewList.get(4));
        mStaffLayout.addView(mItemModelViewList.get(5));
        mStaffLayout.addView(mItemModelViewList.get(6));
    }

    private void updateItemData(){

    }

    private void storeModelItem(ItemModel model) {
        mItemModelList.add(model);
        mItemModelViewList.add(createModelView(model));
    }

    /**
     * 清空页面中的所有View
     */
    public void clearPage() {
        mItemModelList.clear();
        mItemModelViewList.clear();
        mContentLayout.removeAllViews();
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
        clearPage();
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
        return this;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
