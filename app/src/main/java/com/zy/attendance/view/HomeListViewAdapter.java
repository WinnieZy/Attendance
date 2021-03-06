package com.zy.attendance.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zy.attendance.R;
import com.zy.attendance.bean.MacRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/3/26.
 */

public class HomeListViewAdapter extends BaseAdapter {

    protected List<MacRecord> list_mac = new ArrayList<>();
    private Context mContext;

    public HomeListViewAdapter(Context context) {
        this.mContext = context;
    }

    public void setListViewData(ArrayList data){
        list_mac = data;
        for (int i = list_mac.size() - 1; i >= 0; i--) {
            Log.i("winnie","HomeListViewAdapter macList:"+list_mac.get(i).toString());
        }
    }

    @Override
    public int getCount() {
        return list_mac.size();
    }

    @Override
    public Object getItem(int i) {
        return list_mac.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MacRecord data = list_mac.get(list_mac.size()-1-position);
        Log.e("winnie","getView:"+data.toString()+position);
        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_home_list_view, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tv_title_date = (TextView) convertView.findViewById(R.id.tv_title_date);
            viewHolder.tv_title_signIn = (TextView) convertView.findViewById(R.id.tv_title_signIn);
            viewHolder.tv_title_signOut = (TextView) convertView.findViewById(R.id.tv_title_signOut);
            viewHolder.tv_title_date.setText(data.getMonth()+"-"+data.getDay()+"("+data.getWeekday()+")");
            viewHolder.tv_title_signIn.setText(data.getFirst_time());
            viewHolder.tv_title_signOut.setText(data.getLast_time());
            convertView.setTag(viewHolder);
        } else {
            ViewHolder viewHolder = (ViewHolder)convertView.getTag();
			viewHolder.tv_title_date.setText(data.getMonth()+"-"+data.getDay()+"("+data.getWeekday()+")");
            viewHolder.tv_title_signIn.setText(data.getFirst_time());
            viewHolder.tv_title_signOut.setText(data.getLast_time());
        }
        return convertView;

    }

    class ViewHolder{
        TextView tv_title_date;
        TextView tv_title_signIn;
        TextView tv_title_signOut;
    }
}
