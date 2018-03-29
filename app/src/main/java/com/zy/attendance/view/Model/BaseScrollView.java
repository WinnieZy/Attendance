package com.zy.attendance.view.Model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by lenovo on 2018/3/28.
 */

public class BaseScrollView extends ScrollView {
    private static final String TAG = "BaseScrollView";

    protected Context mContext;
    private float xDistance, yDistance, xLast, yLast;

    public BaseScrollView(Context context) {
        super(context);
        mContext = context;
    }

    public BaseScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);

//        try { // 某些机器上会出现 pointerIndex out of range，这里catch住
//            return super.onInterceptTouchEvent(ev);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        try { // 某些机器上会出现 pointerIndex out of range，这里catch住
//            return super.onTouchEvent(ev);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
}
