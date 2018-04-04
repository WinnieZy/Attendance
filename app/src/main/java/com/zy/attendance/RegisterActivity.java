package com.zy.attendance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.attendance.constants.NetAddress;
import com.zy.attendance.storage.dao.StaffDao;
import com.zy.attendance.storage.dao.UserDao;
import com.zy.attendance.uilib.BaseDialog;
import com.zy.attendance.utils.FormatCheckUtil;
import com.zy.attendance.utils.HttpUtil;
import com.zy.attendance.utils.IHttpCallBack;
import com.zy.attendance.utils.JsonUtil;
import com.zy.attendance.utils.WiFiUtil;

public class RegisterActivity extends Activity {

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private View mProgressView;
    private EditText mPasswordEt;
    private EditText mReviewPwEt;
    private EditText mReviewMacEt;
    private EditText mPhoneEt;
    private EditText mEmailEt;
    private String mPassword;
    private String mReviewPw;
    private String mReviewMac;
    private String mPhone;
    private String mEmail;
    private UserDao mUserDao;
    private StaffDao mStaffDao;
    // Handler交互的信息
    private static final int MSG_DO_REGISTER = 1;
    private static final int MSG_REGISTER_SUCCESS = 2;
    private static final int MSG_REGISTER_FAIL = 3;
    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                // 窗口不存在，不处理消息
                return;
            }
            Log.i(TAG, "handleMessage what = " + msg.what);
            switch (msg.what) {
                case MSG_DO_REGISTER:{
                    doRegister();
                    break;
                }
                case MSG_REGISTER_SUCCESS:{
                    String message = msg.obj.toString();
                    if (!"".equals(message)){
                        mUserDao.setPassword(mPassword);
                        mUserDao.setLastLoginTime(message);
                        if (mReviewMac != null&& !"".equals(mReviewMac)){
                            mUserDao.setMac(mReviewMac);
                            mUserDao.setIsMacChecked(true);
                        }
                        mStaffDao.setTelNum(mPhone);
                        mStaffDao.setEmail(mEmail);
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                }
                case MSG_REGISTER_FAIL:{
                    Toast.makeText(mContext,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    showProgress(false);
                    break;
                }
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        mUserDao = new UserDao(mContext);
        mStaffDao = new StaffDao(mContext);
        mProgressView = findViewById(R.id.register_progress);
        mPasswordEt = findViewById(R.id.et_rPW);
        mReviewPwEt = findViewById(R.id.et_rSurePW);
        if (!mUserDao.getIsMacChecked()){
            findViewById(R.id.mac_ll).setVisibility(View.VISIBLE);
            mReviewMacEt = findViewById(R.id.et_mac);
            mReviewMacEt.setText(WiFiUtil.getLocalMacAddress(mContext));
        }
        mPhoneEt = findViewById(R.id.et_phone);
        mEmailEt = findViewById(R.id.et_email);
        Button btn_attention = findViewById(R.id.btn_attention);
        btn_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"attention click");
                final BaseDialog macDialog = getMacDialog();
                macDialog.setNeutralButton("我知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        macDialog.dismiss();
                    }
                });
                macDialog.show();
            }
        });
        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {
        // Reset errors.
        mPasswordEt.setError(null);
        mReviewPwEt.setError(null);
        if (mReviewMacEt != null){
            mReviewMacEt.setError(null);
        }
        mPhoneEt.setError(null);
        mEmailEt.setError(null);

        mPassword = mPasswordEt.getText().toString();
        mReviewPw = mReviewPwEt.getText().toString();
        if (mReviewMacEt != null){
            mReviewMac = mReviewMacEt.getText().toString();
        }else {
            mReviewMac = "";
        }
        mPhone = mPhoneEt.getText().toString();
        mEmail = mEmailEt.getText().toString();
        Log.e(TAG,"mPassword:"+mPassword+",mReviewPw:"+mReviewPw+",mReviewMac:"+ mReviewMac +",mPhone:"+mPhone+",mEmail:"+mEmail);

        boolean cancel = false;
        View focusView = null;

        // Check for field no null.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordEt.setError(getString(R.string.error_field_required));
            focusView = mPasswordEt;
            cancel = true;
        }else if (TextUtils.isEmpty(mReviewPw)) {
            mReviewPwEt.setError(getString(R.string.error_field_required));
            focusView = mReviewPwEt;
            cancel = true;
        }else if (mReviewMacEt != null && TextUtils.isEmpty(mReviewMac)) {
            mReviewMacEt.setError(getString(R.string.error_field_required));
            focusView = mReviewMacEt;
            cancel = true;
        }else if (TextUtils.isEmpty(mPhone)) {
            mPhoneEt.setError(getString(R.string.error_field_required));
            focusView = mPhoneEt;
            cancel = true;
        }else if (TextUtils.isEmpty(mEmail)) {
            mEmailEt.setError(getString(R.string.error_field_required));
            focusView = mEmailEt;
            cancel = true;
        }

        // no field is null
        if (!cancel){
            //check if field is valid
            if (!FormatCheckUtil.isPasswordValid(mPassword)) {
                mPasswordEt.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordEt;
                cancel = true;
            }else if (isPasswordSame(mPassword)) {
                mPasswordEt.setError(getString(R.string.error_same_password));
                focusView = mPasswordEt;
                cancel = true;
            }else if (!TextUtils.isEmpty(mReviewPw) && !mPassword.equals(mReviewPw)) {
                mReviewPwEt.setError(getString(R.string.error_invalid_reviewpw));
                focusView = mReviewPwEt;
                cancel = true;
            }else if (mReviewMacEt != null && !FormatCheckUtil.isMacAddressValid(mReviewMac)) {
                mReviewMacEt.setError(getString(R.string.error_invalid_mac));
                focusView = mReviewMacEt;
                cancel = true;
            }else if (FormatCheckUtil.isPhoneValid(mPhone)){
                mPhoneEt.setError(getString(R.string.error_invalid_phone));
                focusView = mPhoneEt;
                cancel = true;
            }else if (!FormatCheckUtil.isEmailValid(mEmail)) {
                mEmailEt.setError(getString(R.string.error_invalid_email));
                focusView = mEmailEt;
                cancel = true;
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (!mUserDao.getIsMacChecked()){
                final BaseDialog macDialog = getMacDialog();
                macDialog.setPositiveButton("确认无误", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        macDialog.dismiss();
                        showProgress(true);
                        doRegister();

                    }
                });
                macDialog.setNegativeButton("返回修改", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        macDialog.dismiss();
                    }
                });
                macDialog.show();
            }
        }
    }

    private boolean isPasswordSame(String password) {
        //TODO: Replace this with your own logic
        if (password.equals(mUserDao.getPassword())) {
            return true;
        }
        return false;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private BaseDialog getMacDialog(){
        final BaseDialog macDialog = new BaseDialog(mContext);
        macDialog.setTitle("设备mac校验");
        TextView macContentTv = new TextView(mContext);
        macContentTv.setText(getString(R.string.mac_register_content));
        TextView macPathTv = new TextView(mContext);
        macPathTv.setText(getString(R.string.mac_register_path));
        macPathTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        LinearLayout macLL = new LinearLayout(mContext);
        macLL.setOrientation(LinearLayout.VERTICAL);
        macLL.addView(macContentTv);
        macLL.addView(macPathTv);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        macDialog.setContentView(macLL, params);
        return macDialog;
    }

    private void doRegister() {
        String address = NetAddress.USER_REGISTER;
        String[] key = {"username","password","mac","phone","email"};
        String[] value = {mUserDao.getUsername(),mPassword, mReviewMac,mPhone,mEmail};
        String jsonString = JsonUtil.createJSONString(key,value);
        Log.d(TAG, "jsonString:"+jsonString);
        Log.d(TAG, "address:"+address);
        HttpUtil.sendPostHttpRequest(address, jsonString, new IHttpCallBack() {
            @Override
            public void onFinish(String response) {
                Log.e(TAG,"register response:"+response);
                String result = JsonUtil.handleGeneralResponse(response);
                String code = result.substring(0,3);
                String message = result.substring(3);
                Log.e(TAG,"code:"+code+",message:"+message);
                Message msg = new Message();
                msg.obj = message;
                if ("200".equals(code)){
                    msg.what = MSG_REGISTER_SUCCESS;
                }else {
                    msg.what = MSG_REGISTER_FAIL;
                }
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                String error = "Register连接服务器失败";
                Message message = new Message();
                message.what = MSG_REGISTER_FAIL;
                message.obj = error;
                mHandler.sendMessage(message);
                Log.d(TAG, "exception:"+e.getMessage());
            }
        });
    }
}
