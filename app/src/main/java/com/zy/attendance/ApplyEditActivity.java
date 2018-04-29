package com.zy.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.zy.attendance.bean.ApplyRecord;
import com.zy.attendance.storage.dao.StaffDao;
import com.zy.attendance.uilib.BaseDialog;
import com.zy.attendance.utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ApplyEditActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ApplyEditActivity";
    private Context mContext;
    private Toolbar mToolbar;
    private EditText mEt_apply_detail;
    private TextView mTv_apply_item;
    private TextView mTv_apply_start_time;
    private TextView mTv_apply_end_time;
    private ImageView mIv_apply_item;
    private ImageView mIv_apply_start_time;
    private ImageView mIv_apply_end_time;
    private int mApplyItem;//请假==1;加班==2;出差==3;其它==4
    private String[] mApplyItems = {"请假","加班","出差","其它"};
    private String mApplyStartTime;
    private String mApplyEndTime;
    private String mApplyTime;
    private String mApplyDetail;
    private StaffDao mStaffDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_edit);
        mContext = this;
        mStaffDao = new StaffDao(mContext);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView iv_apply_yes = (ImageView) findViewById(R.id.iv_apply_yes);
        RelativeLayout rl_apply_item = (RelativeLayout) findViewById(R.id.rl_apply_item);
        RelativeLayout rl_apply_start_time = (RelativeLayout) findViewById(R.id.rl_apply_start_time);
        RelativeLayout rl_apply_end_time = (RelativeLayout) findViewById(R.id.rl_apply_end_time);
        mEt_apply_detail = (EditText) findViewById(R.id.et_apply_detail);
        mTv_apply_item = (TextView) findViewById(R.id.tv_apply_item);
        mTv_apply_start_time = (TextView) findViewById(R.id.tv_apply_start_time);
        mTv_apply_end_time = (TextView) findViewById(R.id.tv_apply_end_time);
        mIv_apply_item = (ImageView) findViewById(R.id.iv_apply_item);
        mIv_apply_start_time = (ImageView) findViewById(R.id.iv_apply_start_time);
        mIv_apply_end_time = (ImageView) findViewById(R.id.iv_apply_end_time);
        iv_apply_yes.setOnClickListener(this);
        rl_apply_item.setOnClickListener(this);
        rl_apply_start_time.setOnClickListener(this);
        rl_apply_end_time.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://返回箭头回首页
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_apply_yes:
                if (isApplyValid()){
                    String now_second = DateUtil.getFormatDate("second");
                    if(mApplyStartTime != null && mApplyEndTime != null) {
                        if (mApplyStartTime.equals(mApplyEndTime)) {
                            mApplyTime = mApplyStartTime;
                        } else {
                            mApplyTime = mApplyStartTime + "~" + mApplyEndTime;
                        }
                    }else {
                        mApplyTime = now_second;
                    }
                    ApplyRecord applyRecord = new ApplyRecord(mStaffDao.getStaffId(),mStaffDao.getStaffName(),mStaffDao.getLeaderId(),mApplyItem,mApplyTime,now_second,mApplyDetail,0);
                    Log.e(TAG,applyRecord.toString());
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("applyRecord",applyRecord);
                    intent.putExtra("bundle",bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                break;
            case R.id.rl_apply_item:
                getApplyItemDialog().show();
                break;
            case R.id.rl_apply_start_time:
                getDateDialog(true).show();
                break;
            case R.id.rl_apply_end_time:
                getDateDialog(false).show();
                break;
            default:
                break;
        }
    }

    private boolean isApplyValid() {
        if (mApplyItem == 0){
            Snackbar.make(mEt_apply_detail, "请选择申请事项", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }
        if (mApplyItem != 4 && mApplyStartTime == null){
            Snackbar.make(mEt_apply_detail, "请选择开始时间", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }
        if (mApplyItem != 4 && mApplyEndTime == null){
            Snackbar.make(mEt_apply_detail, "请选择结束时间", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }
        mApplyDetail = mEt_apply_detail.getText().toString();
        if (mApplyItem == 4 && "".equals(mApplyDetail)){
            Snackbar.make(mEt_apply_detail, "请说明申请事由", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }
        return true;
    }

    private AppCompatDialog getApplyItemDialog(){
        final AppCompatDialog itemDialog = new AppCompatDialog(mContext);
        ListView listView = new ListView(mContext);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ApplyEditActivity.this,android.R.layout.simple_list_item_1,mApplyItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIv_apply_item.setVisibility(View.GONE);
                mTv_apply_item.setText(mApplyItems[position]);
                mTv_apply_item.setVisibility(View.VISIBLE);
                mApplyItem = position + 1;
                itemDialog.dismiss();
            }
        });
        itemDialog.setContentView(listView);
        return itemDialog;
    }

    private BaseDialog getDateDialog(final boolean start){
        final BaseDialog dateDialog = new BaseDialog(mContext);
        final MaterialCalendarView materialCalendarView = new MaterialCalendarView(mContext);
        if (!start && mApplyStartTime != null){
            materialCalendarView.state().edit().setMinimumDate(CalendarDay.from(Integer.parseInt(mApplyStartTime.substring(0,4)),Integer.parseInt(mApplyStartTime.substring(5,7))-1,Integer.parseInt(mApplyStartTime.substring(8,10)))).commit();
        }
        if (start && mApplyEndTime != null){
            materialCalendarView.state().edit().setMaximumDate(CalendarDay.from(Integer.parseInt(mApplyEndTime.substring(0,4)),Integer.parseInt(mApplyEndTime.substring(5,7))-1,Integer.parseInt(mApplyEndTime.substring(8,10)))).commit();
        }
        materialCalendarView.setTitleAnimationOrientation(MaterialCalendarView.HORIZONTAL);
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                if (start){
                    mApplyStartTime = sdf.format(date.getCalendar().getTime());
                }else {
                    mApplyEndTime = sdf.format(date.getCalendar().getTime());
                }
            }
        });
        dateDialog.setContentView(materialCalendarView);
        dateDialog.setButtonTwoHighlight(mContext);
        dateDialog.setPositiveButton("上午", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start){
                    mApplyStartTime += "-上午";
                    mIv_apply_start_time.setVisibility(View.GONE);
                    mTv_apply_start_time.setText(mApplyStartTime);
                    mTv_apply_start_time.setVisibility(View.VISIBLE);
                }else {
                    mApplyEndTime += "-上午";
                    mIv_apply_end_time.setVisibility(View.GONE);
                    mTv_apply_end_time.setText(mApplyEndTime);
                    mTv_apply_end_time.setVisibility(View.VISIBLE);
                }
                Log.e(TAG,mApplyStartTime+"~"+mApplyEndTime);
                dateDialog.dismiss();
            }
        });
        dateDialog.setNegativeButton("下午", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start){
                    mApplyStartTime += "-下午";
                    mIv_apply_start_time.setVisibility(View.GONE);
                    mTv_apply_start_time.setText(mApplyStartTime);
                    mTv_apply_start_time.setVisibility(View.VISIBLE);
                }else {
                    mApplyEndTime += "-下午";
                    mIv_apply_end_time.setVisibility(View.GONE);
                    mTv_apply_end_time.setText(mApplyEndTime);
                    mTv_apply_end_time.setVisibility(View.VISIBLE);
                }
                Log.e(TAG,mApplyStartTime+"~"+mApplyEndTime);
                dateDialog.dismiss();
            }
        });
        return dateDialog;
    }
}
