package com.zy.attendance.view.Model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lenovo on 2018/3/27.
 */

public class ItemModelView extends LinearLayout{

    private ImageView iconView;
    private TextView titleView;
    private TextView contentView;

    public ItemModelView(Context context) {
        super(context);
    }

    public ItemModelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView getIconView() {
        return iconView;
    }

    public void setIconView(ImageView iconView) {
        this.iconView = iconView;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public void setTitleView(TextView titleView) {
        this.titleView = titleView;
    }

    public TextView getContentView() {
        return contentView;
    }

    private void init(Context context, Drawable icon, String title, String content) {
        if (icon != null){
            if (iconView == null){
                iconView = new ImageView(context);
            }
            iconView.setImageDrawable(icon);
        }
        if (titleView == null){
            titleView = new TextView(context);
        }
        titleView.setText(title);
        if (contentView == null){
            contentView = new TextView(context);
        }
        contentView.setText(content);
    }

    public void setContentView(TextView contentView) {
        this.contentView = contentView;
    }

}
