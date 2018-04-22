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

public class ApprovalListViewAdapter extends BaseAdapter {

    private static final String TAG = "ApprovalListViewAdapter";

    protected List<ApplyRecord> approvalList = new ArrayList<>();
    private Context mContext;

    public ApprovalListViewAdapter(Context context) {
        this.mContext = context;
    }

    public void setListViewData(ArrayList data){
        approvalList = data;
        for (int i = approvalList.size() - 1; i >= 0; i--) {
            Log.i(TAG,"approvalList:"+ approvalList.get(i).toString());
        }
    }

    @Override
    public int getCount() {
        return approvalList.size();
    }

    @Override
    public Object getItem(int i) {
        return approvalList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ApplyRecord data = approvalList.get(approvalList.size()-1-position);
        Log.e(TAG,"getView:"+data.toString()+position);
        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_apply_list_view, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.iv_type = convertView.findViewById(R.id.iv_type);
            viewHolder.tv_staff_name = convertView.findViewById(R.id.tv_time);
            viewHolder.tv_apply_time = convertView.findViewById(R.id.tv_reason);
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
            viewHolder.tv_staff_name.setText(data.getStaff_name());
            if ("null~null".equals(data.getApply_time_for())){
                viewHolder.tv_apply_time.setText(data.getApply_time_at());
            }else {
                viewHolder.tv_apply_time.setText(data.getApply_time_for());
            }
            viewHolder.iv_result.setVisibility(View.GONE);
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
            viewHolder.tv_staff_name.setText(data.getStaff_name());
            if ("null~null".equals(data.getApply_time_for())){
                viewHolder.tv_apply_time.setText(data.getApply_time_at());
            }else {
                viewHolder.tv_apply_time.setText(data.getApply_time_for());
            }
            viewHolder.iv_result.setVisibility(View.GONE);
        }
        return convertView;

    }

    class ViewHolder{
        ImageView iv_type;
        TextView tv_staff_name;
        TextView tv_apply_time;
        ImageView iv_result;
    }
}
