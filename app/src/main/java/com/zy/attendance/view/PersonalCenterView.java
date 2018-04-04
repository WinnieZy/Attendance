package com.zy.attendance.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.attendance.LoginActivity;
import com.zy.attendance.R;
import com.zy.attendance.bean.Staff;
import com.zy.attendance.bean.User;
import com.zy.attendance.controller.StaffModifyCtl;
import com.zy.attendance.controller.UserModifyCtl;
import com.zy.attendance.storage.dao.StaffDao;
import com.zy.attendance.storage.dao.UserDao;
import com.zy.attendance.uilib.BaseDialog;
import com.zy.attendance.uilib.Tools;
import com.zy.attendance.uilib.UIConfig;
import com.zy.attendance.utils.FormatCheckUtil;
import com.zy.attendance.utils.IResultCallback;
import com.zy.attendance.utils.WiFiUtil;
import com.zy.attendance.view.Model.ItemModel;
import com.zy.attendance.view.Model.ItemModelView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/3/26.
 */

public class PersonalCenterView extends ScrollView implements IMainView{

    private static final String TAG = "PersonalCenterView";
    private Context mContext;
    private LinearLayout mContentLayout;
    private LinearLayout mMacLayout;
    private LinearLayout mPersonLayout;
    private LinearLayout mStaffLayout;
    private LinearLayout mSignOutLayout;
    private StaffDao mStaffDao;
    private UserDao mUserDao;
    private boolean mIsMacChecked;

    private ArrayList<ItemModel> mItemModelList = new ArrayList<>();
    private ArrayList<ItemModelView> mItemModelViewList = new ArrayList<>();

    // Handler交互的信息
    private static final int MSG_MODIFY_SUCCESS = 1;
    private static final int MSG_MODIFY_FAIL = 2;
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                // 窗口不存在，不处理消息
                return;
            }
            Log.i(TAG, "handleMessage what = " + msg.what);
            switch (msg.what) {
                case MSG_MODIFY_SUCCESS:
                    //如果是设备mac绑定成功的话需要将文本颜色改回
                    if (msg.arg1 == 0){
                        mItemModelViewList.get(0).getContentView().setTextColor(getResources().getColor(R.color.text_gray));
                    }
                    updateModelView(mItemModelViewList.get(msg.arg1), mItemModelList.get(msg.arg1));
                    Toast.makeText(mContext,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;

                case MSG_MODIFY_FAIL:
                    Toast.makeText(mContext,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    public PersonalCenterView(Context context){
        super(context);
        mContext = context;
        mStaffDao = new StaffDao(mContext);
        mUserDao = new UserDao(mContext);
        mIsMacChecked = mUserDao.getIsMacChecked();
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
        if (!mIsMacChecked && "设备mac".equals(model.getTitle())){
            content.setTextColor(getResources().getColor(R.color.text_red));
        }
        itemView.setContentView(content);
        addClickListenerIfNeed(model.getTitle(),itemView);
        return itemView;
    }

    private void addClickListenerIfNeed(String title, ItemModelView itemView) {
        Log.i(TAG,"title:"+title);
        switch (title){
            case "设备mac":
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = mIsMacChecked ? "申请修改绑定mac" : "设备mac校验";
                        String editStr = mIsMacChecked ? "": WiFiUtil.getLocalMacAddress(mContext);
                        getDialog(title,editStr,true,mIsMacChecked);
                        Log.i(TAG,"mac click");
                    }
                });
                break;
            case "手机号":
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = "修改手机号";
                        getDialog(title,"",false,false);
                        Log.i(TAG,"phone click");
                    }
                });
                break;
            case "邮箱":
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = "修改邮箱";
                        getDialog(title,"",false,false);
                        Log.i(TAG,"email click");
                    }
                });
                break;
            case "姓名"://test
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mUserDao.setIsMacChecked(false);
                        Log.i(TAG,"name click");
                    }
                });
                break;
            default:
                break;
        }
    }

    private BaseDialog getDialog(final String title, String editStr, boolean text, final boolean apply){
        Log.i(TAG,"getDialog title:"+title);
        final BaseDialog dialog = new BaseDialog(mContext);
        dialog.setTitle(title);
        View macView = LayoutInflater.from(mContext).inflate(R.layout.dialog_mac,null);
        final EditText edt = macView.findViewById(R.id.edt_mac);
        TextView macTv = macView.findViewById(R.id.tv_mac);
        if (!"".equals(editStr)) {
            edt.setText(editStr);
        }
        if (text){
            if (apply){
                macTv.setText(getResources().getString(R.string.mac_modify_attention));
            }else {
                macTv.setText(getResources().getString(R.string.mac_review_login));
            }
        }else {
            macTv.setVisibility(GONE);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        dialog.setContentView(macView, params);
        dialog.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(View view) {
                edt.setError(null);
                if (apply){//申请修改mac，需发消息给leader
                    final String reviewMac = edt.getText().toString().toUpperCase();
                    if ("".equals(reviewMac)){
                        edt.setError(getResources().getString(R.string.error_field_required));
                        edt.requestFocus();
                    }else if (!FormatCheckUtil.isMacAddressValid(reviewMac)){
                        edt.setError(getResources().getString(R.string.error_invalid_mac));
                        edt.requestFocus();
                    }else if (reviewMac.equals(mUserDao.getMac())){
                        edt.setError(getResources().getString(R.string.error_modify_same));
                        edt.requestFocus();
                    }else {
                        //TODO：发送申请给leader逻辑
                        dialog.dismiss();
                    }
                }else {
                    switch (title){
                        case "设备mac校验":
                            final String reviewMac = edt.getText().toString().toUpperCase();
                            if ("".equals(reviewMac)){
                                edt.setError(getResources().getString(R.string.error_field_required));
                                edt.requestFocus();
                            }else if (!FormatCheckUtil.isMacAddressValid(reviewMac)){
                                edt.setError(getResources().getString(R.string.error_invalid_mac));
                                edt.requestFocus();
                            }else if (reviewMac.equals(mUserDao.getMac())){
                                edt.setError(getResources().getString(R.string.error_modify_same));
                                edt.requestFocus();
                            }else {
                                Log.e(TAG, "设备mac校验点击确认，isMac:" + FormatCheckUtil.isMacAddressValid(reviewMac));
                                UserModifyCtl.getInstance().modifyMac(mUserDao.getStaffId(),
                                        reviewMac, new IResultCallback() {
                                            @Override
                                            public void onSuccess(String result) {
                                                mItemModelList.get(0).setContent(reviewMac);
                                                mUserDao.setMac(reviewMac);
                                                mUserDao.setIsMacChecked(true);
                                                mIsMacChecked = true;
                                                Message msg = new Message();
                                                msg.what = MSG_MODIFY_SUCCESS;
                                                msg.obj = result;
                                                msg.arg1 = 0;
                                                mHandler.sendMessage(msg);
                                            }

                                            @Override
                                            public void onFail(String failReason) {
                                                Message msg = new Message();
                                                msg.what = MSG_MODIFY_FAIL;
                                                msg.obj = failReason;
                                                mHandler.sendMessage(msg);
                                            }
                                        });
                                dialog.dismiss();
                            }
                            break;
                        case "修改手机号":
                            Log.e(TAG,"修改手机号点击确认");
                            final String phone = edt.getText().toString();
                            if ("".equals(phone)){
                                edt.setError(getResources().getString(R.string.error_field_required));
                                edt.requestFocus();
                            }else if (!FormatCheckUtil.isPhoneValid(phone)){
                                edt.setError(getResources().getString(R.string.error_invalid_phone));
                                edt.requestFocus();
                            }else if (phone.equals(mStaffDao.getTelNum())){
                                edt.setError(getResources().getString(R.string.error_modify_same));
                                edt.requestFocus();
                            }else {
                                StaffModifyCtl.getInstance().modifyPhone(mStaffDao.getStaffId(),
                                        phone, new IResultCallback() {
                                            @Override
                                            public void onSuccess(String result) {
                                                mItemModelList.get(2).setContent(phone);
                                                mStaffDao.setTelNum(phone);
                                                Message msg = new Message();
                                                msg.what = MSG_MODIFY_SUCCESS;
                                                msg.obj = result;
                                                msg.arg1 = 2;
                                                mHandler.sendMessage(msg);
                                            }

                                            @Override
                                            public void onFail(String failReason) {
                                                Message msg = new Message();
                                                msg.what = MSG_MODIFY_FAIL;
                                                msg.obj = failReason;
                                                mHandler.sendMessage(msg);
                                            }
                                        });
                                dialog.dismiss();
                            }
                            break;
                        case "修改邮箱":
                            Log.e(TAG,"修改邮箱点击确认");
                            final String email = edt.getText().toString();
                            if ("".equals(email)){
                                edt.setError(getResources().getString(R.string.error_field_required));
                                edt.requestFocus();
                            }else if (!FormatCheckUtil.isEmailValid(email)){
                                edt.setError(getResources().getString(R.string.error_invalid_email));
                                edt.requestFocus();
                            }else if (email.equals(mStaffDao.getEmail())){
                                edt.setError(getResources().getString(R.string.error_modify_same));
                                edt.requestFocus();
                            }else {
                                StaffModifyCtl.getInstance().modifyEmail(mStaffDao.getStaffId(),
                                        email, new IResultCallback() {
                                            @Override
                                            public void onSuccess(String result) {
                                                mItemModelList.get(3).setContent(email);
                                                mStaffDao.setEmail(email);
                                                Message msg = new Message();
                                                msg.what = MSG_MODIFY_SUCCESS;
                                                msg.obj = result;
                                                msg.arg1 = 3;
                                                mHandler.sendMessage(msg);
                                            }

                                            @Override
                                            public void onFail(String failReason) {
                                                Message msg = new Message();
                                                msg.what = MSG_MODIFY_FAIL;
                                                msg.obj = failReason;
                                                mHandler.sendMessage(msg);
                                            }
                                        });
                                dialog.dismiss();
                            }
                            break;
                        default:
                            break;
                    }
                }
                Log.i(TAG,"点击确认，apply:"+apply);
            }
        });
        dialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"点击取消");
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
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
            if (mIsMacChecked){
                mItemModelList.get(0).setContent(user.getMac());
            }else {
                mItemModelList.get(0).setContent(getResources().getString(R.string.mac_review_attention));
            }
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
