package com.zy.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.zy.attendance.controller.MacRequestCtl;
import com.zy.attendance.storage.dao.UserDao;
import com.zy.attendance.storage.db.DbOperator;
import com.zy.attendance.uilib.BaseDialog;
import com.zy.attendance.uilib.UIConfig;
import com.zy.attendance.utils.IDataCallback;
import com.zy.attendance.view.HomeListView;
import com.zy.attendance.view.IMainView;
import com.zy.attendance.view.ManagementView;
import com.zy.attendance.view.PersonalCenterView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;
    private FloatingActionButton fab;
    private BottomNavigationView mBottomNavigationView;
//    private MaterialCalendarView mMaterialCalendarView;
    final ArrayList<IMainView> viewHandler = new ArrayList<IMainView>();
    private final ArrayList<View> viewContainer = new ArrayList<View>();
    private Context mContext;
    private ManagementView mManagementView;
    private HomeListView mHomeListView;
    private PersonalCenterView mPersonalCenterView;
    private DbOperator mDbOperator;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_manager:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_me:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

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
            ((ViewPager) container).addView(viewContainer.get(position));
            Object obj = null;
            synchronized (viewContainer) {
                obj = viewContainer.get(position);
            }
            return obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(viewContainer.get(position));
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.i(TAG, " onPageScrolled position = " + position);
//            Log.i(TAG, " onPageScrolled positionOffset = " + positionOffset);
//            Log.i(TAG, " onPageScrolled positionOffsetPixels = " + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
//            Log.i(TAG, " onPageSelected position = " + position);
//            Log.i(TAG, " viewpager position = " + mViewPager.getCurrentItem());
            synchronized (viewHandler) {
                for (int i = 0; i < viewHandler.size(); i++) {
                    if(position == i){
                        viewHandler.get(i).onShow();
                        Log.i(TAG, " onPageSelected position = " + position + "viewSub.onPrepareShow()");
                    }
                }
            }
            switchTab(position);
            if (position == 2){
                fab.setVisibility(View.GONE);
            }else {
                fab.setVisibility(View.VISIBLE);
                if (position == 0){
                    fab.setImageResource(R.drawable.editor);
                }else if (position == 1){
                    fab.setImageResource(R.drawable.calendar);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            Log.i(TAG, " onPageScrollStateChanged state = " + state);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        if (!new UserDao(mContext).getIsOnline()){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        mDbOperator = DbOperator.getInstance(mContext);
        UIConfig.initUILib(mContext);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
//        mMaterialCalendarView = new MaterialCalendarView(mContext);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() == 0){
                    Snackbar.make(view, "Current position is managementPage", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else if (mViewPager.getCurrentItem() == 1){
                    Snackbar.make(view, "Current position is ListViewPage", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getDateDialog().show();
                    mHomeListView.onCallback("clearDialogData",null);
                }
            }
        });
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mManagementView = new ManagementView(mContext);

        try {
            mHomeListView = new HomeListView(mContext);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mPersonalCenterView = new PersonalCenterView(mContext);

        synchronized (viewHandler) {
            viewHandler.clear();
            viewHandler.add(mManagementView);
            viewHandler.add(mHomeListView);
            viewHandler.add(mPersonalCenterView);
        }

        synchronized (viewHandler) {
            for (IMainView obj : viewHandler) {
                obj.onCreate(savedInstanceState);
            }
        }

        synchronized (viewContainer) {
            viewContainer.clear();
            viewContainer.add(mManagementView.getContentView());
            viewContainer.add(mHomeListView.getContentView());
            viewContainer.add(mPersonalCenterView.getContentView());
        }

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setSaveEnabled(true);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setCurrentItem(1);
        mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(1).getItemId());

        MacRequestCtl.getInstance().requestMacData(mContext, new IDataCallback() {
            @Override
            public void onCallback(String result, Bundle outBundle) {
                mHomeListView.onCallback("onCreate",null);
            }

            @Override
            public void onHostFail(int errCode, String errMsg, Bundle inBundle) {
                mHomeListView.onCallback("onCreate",null);
            }
        });
    }

    private BaseDialog getDateDialog(){
        final BaseDialog macDialog = new BaseDialog(mContext);
        final MaterialCalendarView materialCalendarView = new MaterialCalendarView(mContext);
        materialCalendarView.state().edit().setMaximumDate(CalendarDay.today()).commit();
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        materialCalendarView.setOnDateChangedListener(mHomeListView);
        materialCalendarView.setOnMonthChangedListener(mHomeListView);
        macDialog.setContentView(materialCalendarView);
        macDialog.setNeutralButton(true,"开始查询", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeListView.onCallback("conditionQuery",null);
                macDialog.dismiss();
            }
        });
        return macDialog;
    }

    private void switchTab(int index){
        if (index < 0 || index > 2) {
            return;
        }
        if (mBottomNavigationView != null) {
            mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(index).getItemId());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        synchronized (viewHandler) {
            for (IMainView obj : viewHandler) {
                obj.onResume();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //遍历并发出
        synchronized (viewHandler) {
            for (IMainView obj : viewHandler) {
                obj.onDestroy();
            }
        }
        synchronized (viewContainer) {
            viewContainer.clear();
        }
    }
}
/**
 public static final String TAG = "MainActivity";
 private String[] mac_result;
 private HandlerThread mHandlerThread;
 private Handler mHandler;
 private Runnable mNetworkTask = new Runnable(){

@Override
public void run() {
//发送用户名密码至网络端，正确则返回所有用户信息存入数据库
String address = NetAddress.MAC_REQUEST + "?mac=0C:8F:FF:82:19:67&&date=2018-04-09";
HttpUtil.sendGetHttpRequest(address, new IHttpCallBack() {

@Override
public void onFinish(String response) {
Log.d("winnie", "response:"+response.toString());
mac_result = JsonUtil.handleMacResponse(response);
if (mac_result!=null){
runOnUiThread(new Runnable() {
@Override
public void run() {
((TextView)findViewById(R.id.mac)).setText(mac_result[0]);
((TextView)findViewById(R.id.date)).setText(mac_result[1]);
((TextView)findViewById(R.id.first_time)).setText(mac_result[2]);
((TextView)findViewById(R.id.last_time)).setText(mac_result[3]);
}
});
}
//                Message message = new Message();
//                int result = JsonUtil.handleGeneralResponse(response);
//                if (result==1) {
//                    //登录成功
////								User user= new User(userName, password,"","","");
////								if (bridsDB.queryUserByName(userName) != null) {
////									bridsDB.updateUser(user);
////									loginUser = bridsDB.queryUser(user);
////								} else {
////									bridsDB.addUser(user);
////									loginUser = bridsDB.queryUser(user);
////								}
////								Intent intent = new Intent(LoginActivity.this,MainActivity.class);
////								startActivity(intent);
////								finish();
//                    loginUser=bridsDB.queryUserByUnameUpass(userName, password);
//                    loginUserImage = ImageUtil.loadImage(loginUser.getUimage());
//                    Log.d("LoginActivity", loginUser.getUimage());
//                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else if (result==2) {
//                    //用户名错误
//                    message.what = UNAME_WRONG;
//                    message.obj = "用户名错误";
//                    handler.sendMessage(message);
//                } else {
//                    //密码错误
//                    message.what = UPASS_WRONG;
//                    message.obj = "密码错误";
//                    handler.sendMessage(message);
//                }
}

@Override
public void onError(Exception e) {
String error = "Login连接服务器失败";
//                Message message = new Message();
//                message.what = CONNECT_ERROR;
//                message.obj = error;
//                handler.sendMessage(message);
Log.d("winnie", "exception:"+e.getMessage());
}
});
}
};

 @Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_main);
 if (mHandlerThread == null) {
 mHandlerThread = new HandlerThread(TAG);
 mHandlerThread.start();
 mHandler = new Handler(mHandlerThread.getLooper());
 }
 mHandler.postDelayed(mNetworkTask, 0);
 Log.e("winnie", WiFiUtil.getLocalMacAddress(getApplicationContext()));
 }

 @Override
 protected void onDestroy() {
 super.onDestroy();
 if(mHandler!=null)
 mHandler.removeCallbacks(mNetworkTask);
 mHandlerThread = null;
 mHandler = null;
 }
 */

/**
private void initializeData() {
    String mac = new UserDao(mContext).getMac();
    if ("".equals(mac)){
        mac = WiFiUtil.getLocalMacAddress(mContext);
    }
    MacRecord macRecord = mDbOperator.getLatestMacRecord();
    String date = "";
    int updateId = -1;
    boolean isAdd = true;
    if (macRecord != null){
        //TODO
        date = DateUtil.combineMacDate(macRecord);
        String date10 = date.substring(0,10);
        if (date10.equals(DateUtil.getFormatDate(false))){
            isAdd = false;
            updateId = macRecord.getId();
        }
    }
//        String address = NetAddress.MAC_REQUEST + "?mac="+mac+"&&date="+date+"&&isAdd="+isAdd;

    String isAddStr = String.valueOf(isAdd);
    String address = NetAddress.MAC_REQUEST ;
    Log.d(TAG, "MainActivity getMac address:" + address);
    String[] key = {"mac","date","isAdd"};
    String[] value = {mac,date,isAddStr};
    String jsonString = JsonUtil.createJSONString(key,value);
    Log.d(TAG, "jsonString:"+jsonString);
    Log.d(TAG, "address:"+address);
    final boolean finalIsAdd = isAdd;
    final int finalUpdateId = updateId;
    HttpUtil.sendPostHttpRequest(address, jsonString,new IHttpCallBack() {

        @Override
        public void onFinish(String response) {
            Log.d(TAG, "response:" + response);
            String result = JsonUtil.handleGeneralResponse(response);
            String code = result.substring(0,3);
            String message = result.substring(3);
            Log.e(TAG,"code:"+code+",message:"+message);
            if ("200".equals(code)){
                if ("success".equals(message)){
                    ArrayList<MacRecord> macList= JsonUtil.handleMacResponse(response);
                    if (macList != null){
                        if (finalIsAdd){
                            Log.e(TAG,"isAdd mac");
                            for (MacRecord macRecord : macList){
                                Log.e(TAG,macRecord.toString());
                                mDbOperator.addMacRecord(macRecord);
                            }
                        }else {
                            Log.e(TAG,"update mac");
                            mDbOperator.updateMacRecord(finalUpdateId,macList.get(0));
                        }
                    }
                }else {
                    Log.e(TAG,"200 and message:"+message);
                }
            }else{
                Log.e(TAG,"code:"+code+",message:"+message);
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.e(TAG,"thread sleep catch,message:"+e.getMessage());
            }
            mHomeListView.getListViewCallback().onCallback(null,null);
        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, "error:" + e.getMessage());
            mHomeListView.getListViewCallback().onCallback(null,null);
        }
    });
}
 */