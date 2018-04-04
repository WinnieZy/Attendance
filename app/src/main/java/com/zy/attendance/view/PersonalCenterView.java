package com.zy.attendance.view;

import android.app.Activity;
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

import com.zy.attendance.LoginActivity;
import com.zy.attendance.R;
import com.zy.attendance.bean.Staff;
import com.zy.attendance.bean.User;
import com.zy.attendance.storage.dao.StaffDao;
import com.zy.attendance.storage.dao.UserDao;
import com.zy.attendance.uilib.Tools;
import com.zy.attendance.uilib.UIConfig;
import com.zy.attendance.view.Model.ItemModel;
import com.zy.attendance.view.Model.ItemModelView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/3/26.
 */

public class PersonalCenterView extends ScrollView implements IMainView {

    private static final String TAG = "PersonalCenterView";
    private Context mContext;
    private LinearLayout mContentLayout;
    private LinearLayout mMacLayout;
    private LinearLayout mPersonLayout;
    private LinearLayout mStaffLayout;
    private LinearLayout mSignOutLayout;
    private StaffDao mStaffDao;
    private UserDao mUserDao;

    private ArrayList<ItemModel> mItemModelList = new ArrayList<>();
    private ArrayList<ItemModelView> mItemModelViewList = new ArrayList<>();

    public PersonalCenterView(Context context){
        super(context);
        mContext = context;
        mStaffDao = new StaffDao(mContext);
        mUserDao = new UserDao(mContext);
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

        mMacLayout = new LinearLayout(mContext);
        mMacLayout.setBackgroundResource(R.drawable.common_cards_bg);
        mMacLayout.setOrientation(LinearLayout.VERTICAL);
        mPersonLayout = new LinearLayout(mContext);
        mPersonLayout.setBackgroundResource(R.drawable.common_cards_bg);
        mPersonLayout.setOrientation(LinearLayout.VERTICAL);
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
                Log.e(TAG,"mSignOutLayout onclick isOnline:"+ mUserDao.getIsOnline());
                if (mUserDao.getIsOnline()){
                    mUserDao.setIsOnline(false);
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    if (mContext instanceof Activity){
                        ((Activity)mContext).finish();
                    }
                }else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    if (mContext instanceof Activity){
                        ((Activity)mContext).finish();
                    }
                }
            }
        });
        initializeData();
        mContentLayout.addView(mMacLayout);
        mContentLayout.addView(mPersonLayout);
        mContentLayout.addView(mStaffLayout);
        mContentLayout.addView(mSignOutLayout);
    }

    private ItemModelView createModelView(ItemModel model) {
        ItemModelView itemView = (ItemModelView) LayoutInflater.from(mContext).inflate(R.layout.item_model_view,null);
        ImageView icon = itemView.findViewById(R.id.iv_icon);
        TextView title = itemView.findViewById(R.id.tv_title_personal);
        TextView content = itemView.findViewById(R.id.tv_content_personal);
        icon.setImageResource(model.getIconId());
        title.setText(model.getTitle());
        content.setText(model.getContent());
        itemView.setIconView(icon);
        itemView.setTitleView(title);
        itemView.setContentView(content);
        return itemView;
    }

    private void updateModelView(ItemModelView itemModelView, ItemModel model) {
        TextView content = itemModelView.getContentView();
        content.setText(model.getContent());
        Log.i(TAG,model.getContent());
    }

    private void initializeData(){
        storeModelItem(new ItemModel(R.drawable.icon_mac,"设备mac"));
        storeModelItem(new ItemModel(R.drawable.icon_name,"姓名"));
        storeModelItem(new ItemModel(R.drawable.icon_mobile,"手机号"));
        storeModelItem(new ItemModel(R.drawable.icon_email,"邮箱"));
        storeModelItem(new ItemModel(R.drawable.icon_no,"工号"));
        storeModelItem(new ItemModel(R.drawable.icon_leader,"直属上级"));
        storeModelItem(new ItemModel(R.drawable.icon_calendar,"入职日期"));
        mMacLayout.addView(mItemModelViewList.get(0));
        mPersonLayout.addView(mItemModelViewList.get(1));
        mPersonLayout.addView(mItemModelViewList.get(2));
        mPersonLayout.addView(mItemModelViewList.get(3));
        mStaffLayout.addView(mItemModelViewList.get(4));
        mStaffLayout.addView(mItemModelViewList.get(5));
        mStaffLayout.addView(mItemModelViewList.get(6));
    }

    private void updateItemData(){
        if (mItemModelList != null){
            User user = mUserDao.getUser();
            Staff staff = mStaffDao.getStaff();
            Log.e(TAG,user.toString());
            Log.e(TAG,staff.toString());
            mItemModelList.get(0).setContent(user.getMac());
            mItemModelList.get(1).setContent(staff.getStaffName());
            mItemModelList.get(2).setContent(staff.getTel_num());
            mItemModelList.get(3).setContent(staff.getEmail());
            mItemModelList.get(4).setContent(staff.getStaff_id() != 0 ? String.valueOf(staff.getStaff_id()) : "");
            mItemModelList.get(5).setContent(staff.getLeader());
            mItemModelList.get(6).setContent(staff.getEntry_date());
        }
        if (mItemModelViewList != null){
            for (int i = 0; i < mItemModelViewList.size(); i++) {
                updateModelView(mItemModelViewList.get(i),mItemModelList.get(i));
            }
        }
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
        updateItemData();
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
