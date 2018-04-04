package com.zy.attendance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.attendance.bean.Staff;
import com.zy.attendance.bean.User;
import com.zy.attendance.constants.NetAddress;
import com.zy.attendance.storage.dao.StaffDao;
import com.zy.attendance.storage.dao.UserDao;
import com.zy.attendance.uilib.BaseDialog;
import com.zy.attendance.utils.DateUtil;
import com.zy.attendance.utils.FormatCheckUtil;
import com.zy.attendance.utils.HttpUtil;
import com.zy.attendance.utils.IHttpCallBack;
import com.zy.attendance.utils.JsonUtil;
import com.zy.attendance.utils.WiFiUtil;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,OnClickListener {

    private static final String TAG = "LoginActivity";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private Context mContext;
    // UI references.
    private EditText mAccountView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private StaffDao mStaffDao;
    private UserDao mUserDao;
    private String mUsername;
    private String mPassword;
    private String mReviewMac = "";
    private boolean mIsMacChecked;
    private boolean mPwVisible;
    private boolean mNotSameUser;
    // Handler交互的信息
    private static final int MSG_DO_LOGIN = 1;
    private static final int MSG_LOGIN_SUCCESS = 2;
    private static final int MSG_LOGIN_FAIL = 3;
    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                // 窗口不存在，不处理消息
                return;
            }
            Log.i(TAG, "handleMessage what = " + msg.what);
            switch (msg.what) {
                case MSG_DO_LOGIN:{
                    doLogin();
                    break;
                }
                case MSG_LOGIN_SUCCESS:{
                    String response = msg.obj.toString();
                    int first_login = msg.arg1;
                    Staff staff = JsonUtil.handleLoginData(response);
                    if (staff != null){
                        mStaffDao.setStaff(staff);
                        if (first_login == 1){
                            mIsMacChecked = !"".equals(mReviewMac);
                            User user = new User(mUsername,mPassword,staff.getStaff_id(), mReviewMac, DateUtil.getFormatDate(),mIsMacChecked);
                            mUserDao.setUser(user);
                            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                            startActivity(intent);
                        }else {
                            //需判断本次登录用户是否为上次登录用户
                            if (mNotSameUser){//用户登录非绑定设备,直接覆盖原用户信息
                                Log.e(TAG,"用户登录非绑定设备");
                                mUserDao.setUsername(mUsername);
                                mUserDao.setPassword(mPassword);
                                mUserDao.setStaffID(staff.getStaff_id());
                            }
                            mUserDao.setLastLoginTime(DateUtil.getFormatDate());
                            mUserDao.setIsOnline(true);
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        Log.e(TAG,staff.toString());
                        Log.e(TAG,mUserDao.getUser().toString());
                        finish();
                    }else {
                        Toast.makeText(mContext,"获取用户信息失败，请稍后重试",Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case MSG_LOGIN_FAIL:{
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
        setContentView(R.layout.activity_login);
        mContext = this;
        mStaffDao = new StaffDao(mContext);
        mUserDao = new UserDao(mContext);
        // Set up the login form.
        mAccountView = (EditText) findViewById(R.id.et_account);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.et_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.btn_login);
        mSignInButton.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        Button clearAccountBtn = (Button) findViewById(R.id.btn_clearAccount);
        clearAccountBtn.setOnClickListener(this);
        Button seePasswordBtn = (Button) findViewById(R.id.btn_seePassword);
        seePasswordBtn.setOnClickListener(this);
        mIsMacChecked = mUserDao.getIsMacChecked();
        Log.e(TAG,"user:"+mUserDao.getUser().toString());
        if (!mIsMacChecked){
            TextView registerLinkTv = (TextView) findViewById(R.id.tv_register_link);
            registerLinkTv.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
            registerLinkTv.setVisibility(View.VISIBLE);
            registerLinkTv.setOnClickListener(this);
        }
        //test
        mAccountView.setText("周泳_14");
        mPasswordView.setText("111111");
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mAccountView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mUsername = mAccountView.getText().toString();
        mPassword = mPasswordView.getText().toString();
        mNotSameUser = !mUsername.equals(mUserDao.getUsername());

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(mPassword)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        }else if (!FormatCheckUtil.isPasswordValid(mPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(mUsername)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        } else if (!FormatCheckUtil.isUsernameValid(mUsername)) {
            mAccountView.setError(getString(R.string.error_invalid_username));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
//            mAuthTask = new UserLoginTask(username, password,this);
//            mAuthTask.execute((Void) null);
            mHandler.sendEmptyMessage(MSG_DO_LOGIN);
        }
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

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

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
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                attemptLogin();
                break;
            case R.id.btn_clearAccount:
                mAccountView.setText("");
                break;
            case R.id.btn_seePassword:
                Log.e(TAG,"password visible:"+mPwVisible);
                if (mPwVisible){
                    mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mPwVisible = false;
                }else {
                    mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mPwVisible = true;
                }
                break;
            case R.id.tv_register_link:
                final BaseDialog macDialog = new BaseDialog(mContext);
                macDialog.setTitle("设备mac校验");
                View macView = LayoutInflater.from(mContext).inflate(R.layout.dialog_mac,null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                macDialog.setContentView(macView, params);
                final EditText macEdt = macView.findViewById(R.id.edt_mac);
                macEdt.setText(WiFiUtil.getLocalMacAddress(mContext));
                TextView macTv = macView.findViewById(R.id.tv_mac);
                macTv.setText(getString(R.string.mac_review_login));
                macDialog.setPositiveButton("确认无误", new OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       String reviewMac = macEdt.getText().toString();
                       Log.e(TAG,"isMac:"+FormatCheckUtil.isMacAddressValid(reviewMac));
                       if (!"".equals(reviewMac) && FormatCheckUtil.isMacAddressValid(reviewMac)){
                            mReviewMac = reviewMac;
                       }
                       macDialog.dismiss();
                   }
                });
                macDialog.setNegativeButton("稍后确认", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        macDialog.dismiss();
                    }
                });
                macDialog.show();
                break;
            default:
                break;
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final Context mContext;

        UserLoginTask(String username, String password, Context context) {
            mUsername = username;
            mPassword = password;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
//                doLogin(new IResultCallback() {
//                    @Override
//                    public void onSuccess(String result) {
//
//                    }
//
//                    @Override
//                    public void onFail(String failReason) {
//
//                    }
//                });
                Thread.sleep(2000);
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"登录超时",Toast.LENGTH_LONG).show();
                    }
                },5000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


    }
    private void doLogin() {
        //发送用户名密码至网络端，正确则返回用户信息
        String address = NetAddress.USER_REQUEST ;
        String[] key = {"username","password","reviewMac","getMac"};
        String[] value = {mUsername,mPassword,mReviewMac,String.valueOf(mNotSameUser)};
        String jsonString = JsonUtil.createJSONString(key,value);
        Log.d(TAG, "jsonString:"+jsonString);
        Log.d(TAG, "address:"+address);
        HttpUtil.sendPostHttpRequest(address, jsonString,new IHttpCallBack() {

            @Override
            public void onFinish(String response) {
                Log.d(TAG, "response:"+response.toString());
                String result = JsonUtil.handleGeneralResponse(response);
                String code = result.substring(0,3);
                String message = result.substring(3);
                Log.e(TAG,"code:"+code+",message:"+message);
                if ("200".equals(code)){
                    Message msg = new Message();
                    msg.what = MSG_LOGIN_SUCCESS;
                    msg.obj = response;
                    if ("first_login".equals(message)){
                        msg.arg1 = 1;
                    }else if ("success".equals(message)){
                        msg.arg1 = 0;
                    }else {
                        msg.arg1 = 0;
                        if (!"".equals(message) && FormatCheckUtil.isMacAddressValid(message)){
                            mUserDao.setMac(message);
                            mUserDao.setIsMacChecked(true);
                        }
                        Log.e(TAG,"用户登录非绑定设备，重置mac:"+message);
                    }
                    mHandler.sendMessage(msg);
                }else {
                    Message msg = new Message();
                    msg.what = MSG_LOGIN_FAIL;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Exception e) {
                String error = "Login连接服务器失败";
                Message message = new Message();
                message.what = MSG_LOGIN_FAIL;
                message.obj = error;
                mHandler.sendMessage(message);
                Log.d(TAG, "exception:"+e.getMessage());
            }
        });
    }
}

