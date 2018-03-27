package com.zy.attendance.uilib;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.RelativeLayout;

import com.zy.attendance.R;

/**
 * uilib from wifimanager
 */
public abstract class BaseAnimView extends RelativeLayout {

	private Context mContext;
	/** 默认动画帧间隔 ms **/
	public final long DEFAULT_FRAME_INTERVAL = 16;

	protected boolean mDoAnimFlag = false;
	/**
	 * 圆弧的显示区域
	 */
	public RectF mDrawRect = null;
	/**
	 * 控件的尺寸
	 */
	public int mViewHeight = 0;
	public int mViewWidth = 0;
	/**
	 * 中心坐标
	 */
	public int mCenterX = 0;
	public int mCenterY = 0;
	
	protected IDrawCallBack mCallBack;
	
	protected int mStrokeWidth = 12;
	protected int mPaintColor = Color.parseColor("#00D196");
	protected Paint mPaint = new Paint();

	protected final int MSG_DRAW_NEXT = 0x01;
	protected Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_DRAW_NEXT:
				invalidate();
				break;
			default:
				break;
			}
		}
	};

	public BaseAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		setWillNotDraw(false);
		
		setupPaint();
	}

	public BaseAnimView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setWillNotDraw(false);
		
		setupPaint();
	}

	public BaseAnimView(Context context) {
		super(context);
		mContext = context;
		setWillNotDraw(false);
		
		setupPaint();
	}
	
	private void setupPaint() {
		mStrokeWidth = mContext.getResources().getDimensionPixelSize(R.dimen.q_loading_view_stroke_width);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(mPaintColor);
	}
	

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				// 尺寸变化之后，一些参数需要重新设置
				initAfterMeasure();

				getViewTreeObserver().removeOnPreDrawListener(this);
				return false;
			}
		});
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mDrawRect == null) {
			mDrawRect = new RectF();
		}
		mDrawRect.set(mStrokeWidth, mStrokeWidth, right - left - mStrokeWidth, bottom - top - mStrokeWidth);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//canvas.clipRect(mDrawRect);
	}
	
	/**
	 * 这里的初始化基本上都需要知道当前的view的尺寸
	 */
	public void initAfterMeasure() {
		this.mViewWidth = getWidth();
		this.mViewHeight = getHeight();
		
		updateCenter();
	}

	public void updateCenter() {
		this.mCenterX = mViewWidth / 2;
		this.mCenterY = mViewHeight / 2;
	}
	
	public void startRotationAnimation() {
		this.mDoAnimFlag = true;
		mHandler.sendEmptyMessage(MSG_DRAW_NEXT);
	}

	public void stopRotationAnimation() {
		this.mDoAnimFlag = false;
		mHandler.removeMessages(MSG_DRAW_NEXT);
		invalidate();
	}

	public void setHeight(int height) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.height = height;
		requestLayout();
	}

	public void setWidth(int width) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.width = width;
		requestLayout();
	}
	
	public void doNextFrameTimeSlot(long startTime) {
		long frameCostTime = System.currentTimeMillis() - startTime;
		long delayTime = 0;
		if (DEFAULT_FRAME_INTERVAL > frameCostTime) {
			delayTime = DEFAULT_FRAME_INTERVAL - frameCostTime;
		}
		mHandler.sendEmptyMessageDelayed(MSG_DRAW_NEXT, delayTime);
	}
	
	public void setCallBack(IDrawCallBack callBack) {
		this.mCallBack = callBack;
	}
	
	public interface IDrawCallBack {
		public void onDrawStateChanged(int state);
		public void onDrawFinish();
	}
	
	public void setStrokeWidth(int strokeWidth) {
		this.mStrokeWidth = strokeWidth;
		mPaint.setStrokeWidth(mStrokeWidth);
		requestLayout();
	}
	
	public void setPaintColor(int color) {
		this.mPaintColor = color;
		mPaint.setColor(color);
	}
}
