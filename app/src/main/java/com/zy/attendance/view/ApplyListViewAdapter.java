package com.zy.attendance.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zy.attendance.R;
import com.zy.attendance.bean.ApplyRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/3/26.
 */

public class ApplyListViewAdapter extends BaseAdapter {

    private static final String TAG = "ApplyListViewAdapter";

    protected List<ApplyRecord> applyList = new ArrayList<>();
    private Context mContext;

    public ApplyListViewAdapter(Context context) {
        this.mContext = context;
    }

    public void setListViewData(ArrayList data){
        applyList = data;
        for (int i = applyList.size() - 1; i >= 0; i--) {
            Log.i(TAG,"approvalList:"+ applyList.get(i).toString());
        }
    }

    @Override
    public int getCount() {
        return applyList.size();
    }

    @Override
    public Object getItem(int i) {
        return applyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ApplyRecord data = applyList.get(applyList.size()-1-position);
        Log.e(TAG,"getView:"+data.toString()+position);
        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_apply_list_view, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.iv_type = convertView.findViewById(R.id.iv_type);
            viewHolder.tv_apply_time = convertView.findViewById(R.id.tv_time);
            viewHolder.tv_reason = convertView.findViewById(R.id.tv_reason);
            viewHolder.iv_result = convertView.findViewById(R.id.iv_result);
            if (data.getType() == 1){
                viewHolder.iv_type.setImageResource(R.drawable.apply_leave);
            }else if (data.getType() == 2){
                viewHolder.iv_type.setImageResource(R.drawable.apply_over);
            }else if (data.getType() == 3){
                viewHolder.iv_type.setImageResource(R.drawable.apply_out);
            }else {
                viewHolder.iv_type.setImageResource(R.drawable.apply_other);
            }
            if ("null~null".equals(data.getApply_time_for())){
                viewHolder.tv_apply_time.setText(data.getApply_time_at());
            }else {
                viewHolder.tv_apply_time.setText(data.getApply_time_for());
            }
            if (data.getReason() == null || "".equals(data.getReason())){
                viewHolder.tv_reason.setText("无");
            }else {
                viewHolder.tv_reason.setText(data.getReason());
            }
            if (data.getResult() == 1){
                viewHolder.iv_result.setImageResource(R.drawable.pass);
            }else if (data.getResult() == 2){
                viewHolder.iv_result.setImageResource(R.drawable.rejected);
            }else {
                viewHolder.iv_result.setImageResource(R.drawable.waiting);
            }
            convertView.setTag(viewHolder);
        } else {
            ViewHolder viewHolder = (ViewHolder)convertView.getTag();
            if (data.getType() == 1){
                viewHolder.iv_type.setImageResource(R.drawable.apply_leave);
            }else if (data.getType() == 2){
                viewHolder.iv_type.setImageResource(R.drawable.apply_over);
            }else if (data.getType() == 3){
                viewHolder.iv_type.setImageResource(R.drawable.apply_out);
            }else {
                viewHolder.iv_type.setImageResource(R.drawable.apply_other);
            }
            if ("null~null".equals(data.getApply_time_for())){
                viewHolder.tv_apply_time.setText(data.getApply_time_at());
            }else {
                viewHolder.tv_apply_time.setText(data.getApply_time_for());
            }
            if (data.getReason() == null || "".equals(data.getReason())){
                viewHolder.tv_reason.setText("无");
            }else {
                viewHolder.tv_reason.setText(data.getReason());
            }
            if (data.getResult() == 1){
                viewHolder.iv_result.setImageResource(R.drawable.pass);
            }else if (data.getResult() == 2){
                viewHolder.iv_result.setImageResource(R.drawable.rejected);
            }else {
                viewHolder.iv_result.setImageResource(R.drawable.waiting);
            }
        }
        return convertView;

    }

    class ViewHolder{
        ImageView iv_type;
        TextView tv_apply_time;
        TextView tv_reason;
        ImageView iv_result;
    }
}
