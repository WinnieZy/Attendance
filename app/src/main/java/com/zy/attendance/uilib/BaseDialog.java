package com.zy.attendance.uilib;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zy.attendance.R;

/**
 * Created by lenovo on 2018/4/2.
 */

public class BaseDialog extends Dialog {

    protected Context mContext;
    /** 整个对话框的布局视图 **/
    private RelativeLayout mDialogLayout;
    private TextView mTitleText;

    private LinearLayout mContainerViewLayout;
    /** 内容区域 **/
    private LinearLayout mContentViewLayout;
    private TextView mMessageView;
    /** 按钮区域 **/
    private LinearLayout mButtonLayout;
    private Button mButtonOne;
    private Button mButtonTwo;

    private RelativeLayout mContentRootLayout;
    private RelativeLayout mRealContentLayout;

    /**
     * 按钮之间的间隙
     */
    private View mButtonGap;

    /**
     * 标题栏下面分割线条
     */
    private View mDivider;

    private boolean mUseSoftInputModePan = false;

    /**
     * 是否显示标题
     */
    private boolean mIsShowTitle;

    /**
     * 是否显示按钮
     */
    private boolean mIsShowButtons;
    /**
     * 是否底部显示
     */
    private boolean mIsShowBottom;

    public BaseDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(
                UIConfig.getUILibDrawable(context, R.color.transparent));
        mDialogLayout = (RelativeLayout) UIConfig.inflateUILibResource(
                R.layout.layout_dialog, null);
        mContentRootLayout = mDialogLayout.findViewById(R.id.dialog_content_root_view);
        mRealContentLayout = mDialogLayout.findViewById(R.id.dialog_layout);
        mContainerViewLayout = mDialogLayout.findViewById(R.id.dialog_container_layout);
        mTitleText = mDialogLayout.findViewById(R.id.dialog_title_text);
        mContentViewLayout = mDialogLayout.findViewById(R.id.dialog_content_layout);
        mButtonOne = mDialogLayout.findViewById(R.id.dialog_button_one);
        mButtonTwo = mDialogLayout.findViewById(R.id.dialog_button_two);
        mButtonOne.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        mButtonLayout = mDialogLayout.findViewById(R.id.dialog_button_layout);
        mButtonGap = mDialogLayout.findViewById(R.id.dialog_button_gap);
        mDivider = mDialogLayout.findViewById(R.id.dialog_title_divider);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        lp.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        getWindow().setAttributes(lp);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIConfig.canvasWidth, LinearLayout.LayoutParams.FILL_PARENT);
        super.setContentView(mDialogLayout, params);
    }

    /**
     * 设置按钮绿色
     */
    public void setButtonTwoHighlight(Context context) {
        mButtonTwo.setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 设置按钮无色
     */
    public void setButtonOneDarklight(Context context) {
        mButtonOne.setTextColor(context.getResources().getColor(R.color.text_black));
    }

    /**
     * 设置弹框展示的位置在底部
     */
    public void setIsShowBottom(boolean showBottom) {
        mIsShowBottom = showBottom;
    }

    /**
     * 设置对话框标题文字
     *
     * @param title
     *            标题文字
     */
    public void setTitle(CharSequence title) {
        mTitleText.setText(title);
    }

    /**
     * 设置标题的位置
     * @param gravity
     */
    public void setTitlePosition(int gravity) {
        mTitleText.setGravity(gravity);
        if (gravity == Gravity.CENTER || gravity == Gravity.CENTER_HORIZONTAL) {
            mTitleText.setPadding(0, mTitleText.getPaddingTop(), 0, mTitleText.getPaddingBottom());
        }
    }

    /**
     * 设置对话框的提示信息
     *
     * @param message
     *            提示信息
     */
    public void setMessage(CharSequence message) {
        if (mMessageView == null) {
            mMessageView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT;

            setContentView(mMessageView, params);
        }
        mMessageView.setText(message);
    }

    /**
            * 添加PositiveButton按钮
	 *
             * @param str
	 *            按钮语句
	 * @param onClickListener
	 *            按钮事件
	 */
    public void setPositiveButton(String str,
                                  View.OnClickListener onClickListener) {
        mButtonOne.setText(str);
        mButtonOne.setOnClickListener(onClickListener);
    }
    /**
     * 添加NegativeButton按钮
     *
     * @param str
     *            提示语句资源
     * @param onClickListener
     *            按钮事件
     */
    public void setNegativeButton(String str,
                                  View.OnClickListener onClickListener) {
        mButtonTwo.setText(str);
        mButtonTwo.setOnClickListener(onClickListener);
    }

    /**
     * 添加NeutralButton按钮
     *
     * @param str
     *            提示语句资源
     * @param onClickListener
     *            按钮事件
     */
    public void setNeutralButton(boolean positive, String str,
                                 View.OnClickListener onClickListener) {
        if (positive){
            setPositiveButton(str,onClickListener);
        }else {
            setNegativeButton(str, onClickListener);
        }
    }

    /**
     * 设置嵌入的View到对话框
     *
     * @param view
     *            嵌入的View
     */
    public void setContentView(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(view, params);
    }

    /**
     * 设置嵌入的View到对话框
     *
     * @param view
     *            嵌入的View
     */
    public void setContentView(View view, LinearLayout.LayoutParams params) {
        setContentView(view, params, false);
    }

    public void setContentView(View view, LinearLayout.LayoutParams params, boolean clearPadding) {
        if (clearPadding) {
            mContentViewLayout.setPadding(0, 0, 0, 0);
        }
        mContentViewLayout.removeAllViews();
        mContentViewLayout.addView(view, params);
    }

    protected void showDividerView(boolean isShow) {
        mDivider.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示对话框
     */
    public void onShow() {
        if (mTitleText.getText() == null || mTitleText.getText().equals("")) {
            mTitleText.setVisibility(View.GONE);
            mIsShowTitle = false;
        } else {
            mTitleText.setVisibility(View.VISIBLE);
            mIsShowTitle = true;
        }

        int buttonNum = 0;
        if (mButtonOne.getText() == null || mButtonOne.getText().equals("")) {
            mButtonOne.setVisibility(View.GONE);
        } else {
            buttonNum++;
        }
        if (mButtonTwo.getText() == null || mButtonTwo.getText().equals("")) {
            mButtonTwo.setVisibility(View.GONE);
        } else {
            buttonNum++;
        }

        if (buttonNum == 0) {
            mButtonLayout.setVisibility(View.GONE);
            mIsShowButtons = false;
        } else if (buttonNum == 1) {
            mIsShowButtons = true;
            mButtonLayout.setVisibility(View.VISIBLE);
            mButtonGap.setVisibility(View.GONE);
        } else if (buttonNum == 2) {
            mIsShowButtons = true;
            mButtonLayout.setVisibility(View.VISIBLE);
            mButtonGap.setVisibility(View.VISIBLE);
        }

        showDividerView(mIsShowButtons);

        // 只有默认布局才设置状态
        mContainerViewLayout.setBackgroundDrawable(UIConfig.getUILibDrawable(mContext, R.drawable.common_cards_bg));

        int radius = UIConfig.getUILibResources(mContext)
                .getDimensionPixelSize(R.dimen.dialog_radius);
        if (mIsShowTitle) {

            // 标题上边要圆角
            GradientDrawable topGradientDrawable = new GradientDrawable();
            topGradientDrawable.setShape(GradientDrawable.RECTANGLE);
            topGradientDrawable
                    .setCornerRadii(new float[] { radius, radius, radius, radius, 0, 0, 0, 0 });
            mTitleText.setBackgroundDrawable(topGradientDrawable);

            if (mIsShowButtons) {
                // 显示标题，显示按钮---标题上边和按钮下边要圆角，与默认的一样。
            } else {
                // 显示标题，不显示按钮---内容view下边要圆角
                GradientDrawable bottomGradientDrawable = new GradientDrawable();
                bottomGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                bottomGradientDrawable.setCornerRadii(
                        new float[] { 0, 0, 0, 0, radius, radius, radius, radius });
                mContentViewLayout.setBackgroundDrawable(bottomGradientDrawable);
            }
        } else {
            int contentTop = (int) UIConfig.getUILibResources(mContext).getDimension(R.dimen.dialog_content_margin_top_when_no_title);
            mContentViewLayout.setPadding(mContentViewLayout.getPaddingLeft(), contentTop,
                    mContentViewLayout.getPaddingRight(),
                    mContentViewLayout.getPaddingBottom());
            if (mIsShowButtons) {
                // 不显示标题 ，显示按钮---内容view上边要圆角
                GradientDrawable topGradientDrawable = new GradientDrawable();
                topGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                topGradientDrawable.setCornerRadii(
                        new float[] { radius, radius, radius, radius, 0, 0, 0, 0 });
                mContentViewLayout.setBackgroundDrawable(topGradientDrawable);
            } else {
                // 不显示标题和按钮---内容view上下边都要圆角
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                gradientDrawable.setCornerRadius(radius);
                mContentViewLayout.setBackgroundDrawable(gradientDrawable);
            }
        }

        if (mIsShowBottom){
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.gravity = Gravity.BOTTOM;
            getWindow().setAttributes(lp);
        }else {
            int margin = (int) UIConfig.getUILibResources(mContext)
                    .getDimension(R.dimen.dialog_margin);
            RelativeLayout.LayoutParams realContentLayoutParams = (android.widget.RelativeLayout.LayoutParams) mRealContentLayout
                    .getLayoutParams();
            realContentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);

            RelativeLayout.LayoutParams contentRootLayoutParams = (RelativeLayout.LayoutParams) mContentRootLayout.getLayoutParams();
            contentRootLayoutParams.leftMargin = margin;
            contentRootLayoutParams.rightMargin = margin;
            contentRootLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            contentRootLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.gravity = Gravity.CENTER;
            getWindow().setAttributes(lp);
        }
    }

    @Override
    public void show() {
        onShow();
        super.show();
    }
}
