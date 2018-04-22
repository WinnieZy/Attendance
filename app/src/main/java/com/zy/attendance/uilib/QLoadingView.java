package com.zy.attendance.uilib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import static com.zy.attendance.uilib.UIConfig.HOST_ATTRIBUTE_NAME_SPACE;

/**
 * 新WiFi转菊花控件
 * xml使用时：wrap_content,  app:loadingtype="1"，其中1为绿色38DP大，2为绿色26DP，3为白色26DP
 * java使用时可以用setType变更
 */
public class QLoadingView extends BaseAnimView {
	private static final String TAG = "QLoadingView";
	private static final int GREEN_COLOR = 0xFF00D196;
	private static final int WHITE_COLOR = 0xFFFFFFFF;
	private static final int NO_COLOR = 0x00FFFFFF;
	private static final float START_ANGLE = 225f;
	private static final float ANGLE = 90f;
	private static final int RADIUS = 15;
	private static final int STROKE_WIDTH = 2;
	private static final long DURATION = 1000l;
	private static final long END_DELAY = 500l;
	private static final int ROTATE_PER_SECOND = 360;
	private static final int SIZE_BIG = 38;
	private static final int SIZE_MINI = 26;
	public static final String ATTRBUTE_TYPE_KEY = "loadingtype";
	private int mState = -1;
	private long mStartTime = -1;
	private Paint mArcPaint;
	private Paint mInvisiblePaint;
	private int mArcWidth = 0;
	private int mArcHeight = 0;
	private Bitmap mBuffer;
	private Canvas mBufferCanvas;
	private Context mContext;
	/**
	 * 菊花样式
	 **/
	private int mType;
	/**
	 * 菊花样式 正常大小，用于弹框等
	 **/
	public static final int TYPE_NORMAL = 1;
	/**
	 * 菊花样式 小图标 用于Button等
	 **/
	public static final int TYPE_MINI = 2;
	/**
	 * 白色菊花样式 小图标 用于小黄条
	 **/
	public static final int TYPE_MINI_WHITE = 3;

	/**
	 * 构造函数，初始化前景图片和背景图片
	 *
	 * @param context
	 */
	public QLoadingView(Context context, int type) {
		super(UIConfig.getUILibContext());
		mContext = context;
		this.init();
		this.setLoadingViewByType(type);
	}

	public QLoadingView(Context context, AttributeSet attrs) {
		super(UIConfig.getUILibContext(), attrs);
		mContext = context;
		int loadingType = attrs.getAttributeIntValue(HOST_ATTRIBUTE_NAME_SPACE, ATTRBUTE_TYPE_KEY, TYPE_NORMAL);
		this.init();
		// 宿主资源处理
		this.setLoadingViewByType(loadingType);
	}

	public void init() {
		setPaintColor(GREEN_COLOR);
		setStrokeWidth(Tools.dip2px(mContext,STROKE_WIDTH));
		mArcPaint = new Paint();
		mArcPaint.setStyle(Paint.Style.STROKE);
		mArcPaint.setAntiAlias(true);
		mArcPaint.setStrokeWidth(Tools.dip2px(mContext, STROKE_WIDTH));

		mInvisiblePaint = new Paint();
		mInvisiblePaint.setStyle(Paint.Style.FILL);
		mInvisiblePaint.setAntiAlias(true);
//        mInvisiblePaint.setColor(PluginResUtil.getInstance().getPluginColor(R.color.session_management_bg));
		mInvisiblePaint.setColor(NO_COLOR);
		mInvisiblePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

	}

	void createBuffer(int w, int h) {
		if (mBuffer != null && (mBuffer.getWidth() != w || mBuffer.getHeight() != h)) {
			mBuffer.recycle();
			mBuffer = null;
			mBufferCanvas = null;
		}
		if (mBuffer == null && w > 0 && h > 0) {
			mBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mBufferCanvas = new Canvas(mBuffer);
		}
	}

	public void recycle(){
		try {
			if (mBuffer != null){
				mBuffer.recycle();
				mBuffer = null;
			}
			mBufferCanvas = null;
		} catch (Exception e) {
		}
	}

	public boolean isAnimRunning() {
		return mDoAnimFlag;
	}

	@Override
	public void initAfterMeasure() {
		super.initAfterMeasure();
		createBuffer(this.mViewWidth , this.mViewHeight);
	}

	public void setLoadingViewByType(int type) {
		mType = type;
		if (mType == TYPE_NORMAL) {
			mArcPaint.setColor(GREEN_COLOR);
			mArcWidth = Tools.dip2px(mContext, SIZE_BIG);
			mArcHeight = Tools.dip2px(mContext, SIZE_BIG);
		} else if (mType == TYPE_MINI) {
			mArcPaint.setColor(GREEN_COLOR);
			mArcWidth = Tools.dip2px(mContext, SIZE_MINI);
			mArcHeight = Tools.dip2px(mContext, SIZE_MINI);
		} else if (mType == TYPE_MINI_WHITE) {
			mArcPaint.setColor(WHITE_COLOR);
			mArcWidth = Tools.dip2px(mContext, SIZE_MINI);
			mArcHeight = Tools.dip2px(mContext, SIZE_MINI);
		} else {
			mArcPaint.setColor(GREEN_COLOR);
			mArcWidth = Tools.dip2px(mContext, SIZE_BIG);
			mArcHeight = Tools.dip2px(mContext, SIZE_BIG);
		}
	}

	@Override
	public void startRotationAnimation() {
		super.startRotationAnimation();
		mStartTime = System.currentTimeMillis();
		this.setHeight(mArcHeight);
		this.setWidth(mArcWidth);
		createBuffer(mArcWidth, mArcHeight);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!mDoAnimFlag) {
			return;
		}
		doAnime(canvas, System.currentTimeMillis());
	}

	private void doAnime(Canvas canvas, long curTime) {
		if (mStartTime == -1) {
			mStartTime = System.currentTimeMillis();
		}
		if (curTime - mStartTime > DURATION + END_DELAY) {
			mStartTime = curTime;
		}

		if(curTime - mStartTime > DURATION){
			//最后的时间不做动画
			doNextFrameTimeSlot(curTime);
			return;
		}

		float cx = getWidth() / 2;
		float cy = getHeight() / 2;
//        Log.i(TAG, "doAnime cx:" + cx+"; cy:"+cy);
		if (mBuffer == null) {
			return;
		} else if (mBuffer.isRecycled() && this.mViewWidth > 0 && this.mViewHeight > 0) {
			mBuffer = Bitmap.createBitmap(this.mViewWidth, this.mViewHeight, Bitmap.Config.ARGB_8888);
			mBufferCanvas = new Canvas(mBuffer);
		}
//		mBuffer.eraseColor(0x7fff0000);
		mBuffer.eraseColor(0);
		Canvas externalCanvas = canvas;
		canvas = mBufferCanvas;


		float density = mContext.getApplicationContext().getResources().getDisplayMetrics().density;
		float originX = (getWidth() - mBuffer.getWidth()) * (1.5f / density) * 0.5f;
		float originY = (getHeight() - mBuffer.getHeight()) * (1.5f / density) * 0.5f;

		canvas.save();
		canvas.translate(-originX, -originY);

		canvas.save();
		RectF rf = new RectF(cx - mArcWidth/2, 1.5f * cy - mArcWidth/2, cx + mArcWidth/2, 1.5f * cy + mArcWidth/2);
		canvas.drawArc(rf, START_ANGLE, ANGLE, true, mArcPaint);
		canvas.save();
		RectF rf2 = new RectF(0, 0,  cx * 2, cy * 2);
//        Log.i(TAG, "doAnime curTime:" + curTime+"; mAnimStartTime :"+mStartTime);
		float t = (float) (curTime - mStartTime) % DURATION / DURATION;
		// 这里注意一个很狗的玩意，我们要算的是没有颜色那一段的起始和终结！
		float r2SwipeAngle = getSwipe(t) * 120 + 240;
		float r2StartAngle = 10 + getSwipe2(t) * 480;
//        float r2StartAngle = t * 360;
//		Log.i(TAG, "doAnime t:" + t+"; r2StartAngle :"+r2StartAngle+"; r2SwipeAngle:"+r2SwipeAngle);
		canvas.drawArc(rf2, r2StartAngle, r2SwipeAngle, true, mInvisiblePaint);
//        canvas.drawArc(rf2, r2StartAngle, ANGLE, true, mInvisiblePaint);
		canvas.restore();
//        canvas.drawCircle(cx, cy, r, mPaint);
		canvas.restore();
		canvas.restore();
		externalCanvas.drawBitmap(mBuffer, originX, originY, null);
		doNextFrameTimeSlot(curTime);
	}

	/**
	 * 获取遮罩扫的角度
	 * @param t
	 * @return
	 */
	private float getSwipe(float t) {
		float swipe;
		double angle = 180 * t;
		if (angle < 0) {
			angle = 0;
		} else if (angle > 180) {
			angle = 180;
		}
		double radians = Math.toRadians(angle);
		swipe = (float) Math.sin(radians);
//		Log.i(TAG, "getSwipe swipe:" + swipe+"; angle :"+angle+"; radians:"+radians);
		return 1 - swipe;
	}

	/**
	 * 获取遮罩扫的角度,sin(x)的积分
	 * @param t
	 * @return
	 */
	private float getSwipe2(float t) {
		float swipe;
		double angle = 180 * t;
		if (angle < 0) {
			angle = 0;
		} else if (angle > 180) {
			angle = 180;
		}
		double radians = Math.toRadians(angle);
		swipe = (1 - (float) Math.cos(radians)) / 2.0f;
//		Log.i(TAG, "getSwipe2 swipe:" + swipe + "; angle :" + angle + "; radians:" + radians);
		return swipe;
	}
}
