package com.zy.attendance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zy.attendance.uilib.UIConfig;
import com.zy.attendance.view.HomeListView;
import com.zy.attendance.view.IMainView;
import com.zy.attendance.view.ManagementView;
import com.zy.attendance.view.PersonalCenterView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    final ArrayList<IMainView> viewHandler = new ArrayList<IMainView>();
    private final ArrayList<View> viewContainer = new ArrayList<View>();
    private Context mContext;
    private ManagementView mManagementView;
    private HomeListView mHomeListView;
    private PersonalCenterView mPersonalCenterView;

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
            Log.d("winnie","instantiateItem position:"+position);
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
            Log.i("winnie", " onPageScrolled position = " + position);
            Log.i("winnie", " onPageScrolled positionOffset = " + positionOffset);
            Log.i("winnie", " onPageScrolled positionOffsetPixels = " + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            Log.i("winnie", " onPageSelected position = " + position);
            Log.i("winnie", " viewpager position = " + mViewPager.getCurrentItem());
            synchronized (viewHandler) {
                for (int i = 0; i < viewHandler.size(); i++) {
                    if(position == i){
                        viewHandler.get(i).onShow();
                        Log.i("winnie", " onPageSelected position = " + position + "viewSub.onPrepareShow()");
                    }
                }
            }
            switchTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.i("winnie", " onPageScrollStateChanged state = " + state);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        UIConfig.initUILib(mContext);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
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
String address = NetAddress.MAC_REQUEST + "?mac=0C:8F:FF:82:19:67&&date=2018-03-21";
HttpUtil.sendGetHttpRequest(address, new HttpCallBackListener() {

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
//                int result = JsonUtil.handleLoginResponse(response);
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