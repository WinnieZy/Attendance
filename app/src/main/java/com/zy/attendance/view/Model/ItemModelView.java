package com.zy.attendance.view.Model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lenovo on 2018/3/27.
 */

public class ItemModelView {

    private Context mContext;
    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mContentView;

    public ItemModelView(Context context, Drawable icon, String title, String content) {
        init(context,icon,title,content);
    }

    public ItemModelView(Context context,Drawable icon, String title) {
        init(context,icon,title,"");
    }

    private void init(Context context,Drawable icon, String title, String content) {
        if (icon != null){
            if (mIconView == null){
                mIconView = new ImageView(context);
            }
            mIconView.setImageDrawable(icon);
        }
        if (mTitleView == null){
            mTitleView = new TextView(context);
        }
        mTitleView.setText(title);
        if (mContentView == null){
            mContentView = new TextView(context);
        }
        mContentView.setText(content);
    }

    public void setmContentView(TextView mContentView) {
        this.mContentView = mContentView;
    }

}
