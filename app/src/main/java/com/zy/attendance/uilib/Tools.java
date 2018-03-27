package com.zy.attendance.uilib;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * 工具类
 * 
 * @author boyliang
 * 
 */
public class Tools {	
	/**在左上合并顶层图片**/
	public final static int LEFT_TOP = 1;
	/**在右下合并顶层图片**/
	public final static int RIGHT_BOTTOM = 2;
	
	/* Maximum pixels size for created bitmap. */
	public static final int UNCONSTRAINED = -1;
	/* Options used internally. */
	public static final int OPTIONS_NONE = 0x0;
	public static final int OPTIONS_SCALE_UP = 0x1;
	public static final int OPTIONS_RECYCLE_INPUT = 0x2;
	private final static int STATUS_BAR_DEFAULT_HEIGHT_DP = 25;
	
	/**
	 * dip转换成px值
	 * 
	 * @author aringbei.
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {

		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转成dip
	 * 
	 * @author aringbei.
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {

		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 合并两张图片
	 * @param background
	 *        底层图片，size最大那张
	 * @param foreground
	 *        顶层图片
	 * @param position
	 *        顶层图片的位置：  LEFT_TOP  RIGHT_BOTTOM
	 * @return
	 */
	public static Bitmap conformBitmap(Drawable background, Drawable foreground, int position, int conformWidth, int conformHeight) {
		if (background == null || foreground == null) {
			return null;
		}
		Bitmap backgroundBitmap = drawableToBitmap(background);
		Bitmap foregroundBitmap = drawableToBitmap(foreground);
		return conformBitmap(backgroundBitmap, foregroundBitmap, position, conformWidth, conformHeight);
	}
	
	/**
	 * 合并两张图片
	 * @param background
	 *        底层图片，size最大那张
	 * @param foreground
	 *        顶层图片
	 * @param position
	 *        顶层图片的位置：  LEFT_TOP  RIGHT_BOTTOM
	 * @return
	 */
	public static Bitmap conformBitmap(Drawable background, Drawable foreground, int position) {
		if (background == null || foreground == null) {
			return null;
		}
		Bitmap backgroundBitmap = drawableToBitmap(background);
		Bitmap foregroundBitmap = drawableToBitmap(foreground);
		return conformBitmap(backgroundBitmap, foregroundBitmap, position, 70, 70);
	}
	
	/**
	 * 合并两张图片
	 * @param backgroundBitmap
	 *        底层图片，size最大那张
	 * @param foregroundBitmap
	 *        顶层图片
	 * @param position
	 *        顶层图片的位置：  LEFT_TOP  RIGHT_BOTTOM
	 * @param conformWidth
	 *        图片合成后的宽度
	 * @param conformHeight
	 *        图片合成后的高度
	 * @return
	 */
	public static Bitmap conformBitmap(Bitmap backgroundBitmap, Bitmap foregroundBitmap, int position, int conformWidth, int conformHeight) {
		if (backgroundBitmap == null || foregroundBitmap == null || backgroundBitmap.isRecycled() || foregroundBitmap.isRecycled()) {
			return Bitmap.createBitmap(10, 10, Config.ARGB_8888);
		}
		try{//防止Bitmap已经被回收
			   //获取 这个图片的宽和高
	        int width = backgroundBitmap.getWidth();
	        int height = backgroundBitmap.getHeight();
	        //定义 预转换成的图片的宽度和高度
	        int newWidth = conformWidth;
	        int newHeight = conformHeight;
	        //计算缩放率，新尺寸除原始尺寸
	        float scaleWidth = ((float) newWidth) / width;
	        float scaleHeight = ((float) newHeight) / height;
	        // 创建操作图片用的matrix对象
	        Matrix matrix = new Matrix();
	        // 缩放图片动作
	        matrix.postScale(scaleWidth, scaleHeight);
	        // 创建新的图片
	        Bitmap resizedBitmap = Bitmap.createBitmap(backgroundBitmap, 0, 0,
	                          width, height, matrix, true);
			
			
			int bgWidth = resizedBitmap.getWidth();
			int bgHeight = resizedBitmap.getHeight();
			// 创建一个新的和SRC长度宽度一样的位图
			Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
			Canvas cv = new Canvas(newbmp);
			cv.drawBitmap(resizedBitmap, 0, 0, null);// 在 0，0坐标开始画入bg
			switch (position) {
			case RIGHT_BOTTOM:
				cv.drawBitmap(foregroundBitmap, bgWidth - foregroundBitmap.getWidth()
						, bgHeight - foregroundBitmap.getHeight(), null);// 在右下坐标开始画入fg ，可以从任意位置画入
				break;
			case LEFT_TOP:
				cv.drawBitmap(foregroundBitmap, 0, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
				break;
			}
			cv.save(Canvas.ALL_SAVE_FLAG);// 保存
			cv.restore();// 存储
			return newbmp;
		}catch(Exception e){
			return Bitmap.createBitmap(10, 10, Config.ARGB_8888);
		}
	}

	/**
	 * 合并两张图片
	 * @param backgroundBitmap 底层图片，size最大那张
	 * @param foregroundBitmap 顶层图片
	 * @param position 顶层图片的位置：  LEFT_TOP  RIGHT_BOTTOM
	 * @return
	 */
	public static Bitmap conformBitmap(Bitmap backgroundBitmap, Bitmap foregroundBitmap, int position) {
		return conformBitmap(backgroundBitmap, foregroundBitmap, position, Tools.dip2px(UIConfig.getUILibContext(), 70), Tools.dip2px(UIConfig.getUILibContext(), 70));
	}
	
	/**
	 * Drawable转Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		try{//防止drawable已经被回收
	        // 取 drawable 的长宽  
	        int w = drawable.getIntrinsicWidth();  
	        int h = drawable.getIntrinsicHeight();  
	        Bitmap a = null;
	        // 取 drawable 的颜色格式  
	        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
	                : Bitmap.Config.RGB_565;
	        // 建立对应 bitmap
	        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
	        // 建立对应 bitmap 的画布
	        Canvas canvas = new Canvas(bitmap);
	        drawable.setBounds(0, 0, w, h);
        	 // 把 drawable 内容画到画布中
            drawable.draw(canvas);
            return bitmap;
        }catch(Exception e){
        	return Bitmap.createBitmap(10, 10, Config.ARGB_8888);
        }
    }

	/**
	 * Bitmap转Drawable
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		if(bitmap == null || bitmap.isRecycled()){ // fix crash 避免空指针
			Bitmap tempBitmap = Bitmap.createBitmap(10, 10, Config.ARGB_8888);
			BitmapDrawable bd= new BitmapDrawable(UIConfig.getUILibResources(null), tempBitmap);
			return bd;
		}
		BitmapDrawable bd= new BitmapDrawable(UIConfig.getUILibResources(null), bitmap);
        return bd;
    }

	/**
	 * josephguo 把图片的方角变成圆角
	 * @param bitmap 待转化为圆角的图片
	 * @param pixels 圆角的弯曲程度，数值越大越弯
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}


	/**
	 * josephguo 把图片变成灰色
	 * @param bitmap 待转化图片
	 * @return
	 */
	public static final Bitmap toGreyBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if(width == 0 || height == 0){
			Bitmap tempBitmap = Bitmap.createBitmap(10, 10, Config.ARGB_8888);
			return tempBitmap;
		}

		Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(faceIconGreyBitmap);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
				colorMatrix);
		paint.setColorFilter(colorMatrixFilter);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		return faceIconGreyBitmap;
	}

	public static Bitmap scaleBitmap(Bitmap source, int width, int height) {
		return scaleBitmap(source, width, height, OPTIONS_NONE);
	}

	public static Bitmap scaleBitmap(Bitmap source, int width, int height, int options) {
		if (source == null) {
			return null;
		}
		if(width == 0 || height == 0){
			Bitmap tempBitmap = Bitmap.createBitmap(10, 10, Config.ARGB_8888);
			return tempBitmap;
		}

		float scale;
		if (source.getWidth() < source.getHeight()) {
			scale = width / (float) source.getWidth();
		} else {
			scale = height / (float) source.getHeight();
		}
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap thumbnail = transform(matrix, source, width, height, OPTIONS_SCALE_UP | options);
		return thumbnail;
	}

	/**
	 * Transform source Bitmap to targeted width and height.
	 */
	private static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, int options) {
		if(targetWidth == 0 || targetWidth == 0){
			Bitmap tempBitmap = Bitmap.createBitmap(10, 10, Config.ARGB_8888);
			return tempBitmap;
		}
		boolean scaleUp = (options & OPTIONS_SCALE_UP) != 0;
		boolean recycle = (options & OPTIONS_RECYCLE_INPUT) != 0;

		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf + Math.min(targetWidth, source.getWidth()), deltaYHalf + Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY);
			c.drawBitmap(source, src, dst, null);
			if (recycle) {
				source.recycle();
			}
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}

		if (recycle && b1 != source) {
			source.recycle();
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);

		if (b2 != b1) {
			if (recycle || b1 != source) {
				b1.recycle();
			}
		}

		return b2;
	}
	
	/**
	 * 获取字符串宽度
	 * @param paint
	 *        画笔对象
	 * @param str
	 *        字符串
	 * @return
	 *        字符串宽度
	 */
	public static int getStringWidth(Paint paint, String str){
		int strWidth = 0;
		if(str != null && str.length() > 0){
			int length = str.length();
			float[] widths = new float[length];
			paint.getTextWidths(str, widths);
			for(int k = 0;k < length;k++){
				strWidth += (int) Math.ceil(widths[k]);
			}
		}
		return strWidth;
	}
	
	/**
	 * 获取字符串高度
	 * @param paint
	 *        画笔对象
	 * @return
	 *        字符串高度
	 */
	public static int getStringHeight(Paint paint){
		FontMetrics fm = paint.getFontMetrics();
		return (int)(Math.ceil(fm.descent - fm.top) / 1.5);
	}
	
	public static int initStatusBarHeight(Context context) {
		Class<?> clazz = null;
		Object obj = null;
		Field field = null;
		int statusBarHeight = Tools.dip2px(context,
				STATUS_BAR_DEFAULT_HEIGHT_DP);
		try {
			clazz = Class.forName("com.android.internal.R$dimen");
			obj = clazz.newInstance();
			if (isMeizu()) {
				try {
					field = clazz.getField("status_bar_height_large");
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (field == null) {
				field = clazz.getField("status_bar_height");
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (field != null && obj != null) {
			try {
				int id = Integer.parseInt(field.get(obj).toString());
				statusBarHeight = context.getResources().getDimensionPixelSize(
						id);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		if (statusBarHeight <= 0
				|| statusBarHeight > Tools.dip2px(context,
						STATUS_BAR_DEFAULT_HEIGHT_DP * 2)) {
			// 安卓默认状态栏高度为25dp，如果获取的状态高度大于2倍25dp的话，这个数值可能有问题，用回默认值。出现这种可能性较低，只有小部分手机出现
			float density = context.getResources().getDisplayMetrics().density;
			if (density == -1) {
				statusBarHeight = Tools.dip2px(context,
						STATUS_BAR_DEFAULT_HEIGHT_DP);
			} else {
				statusBarHeight = (int) (STATUS_BAR_DEFAULT_HEIGHT_DP * density + 0.5f);
			}
		}
		return statusBarHeight;
	}
	
	
	public static boolean isMeizu() {
		String brand = Build.BRAND.toLowerCase();
		if (brand.contains("meizu")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取RippleDrawable
	 * @param contentDrawable 内容drawable
	 * @return 5.0系统及其以上返回RippleDrawable，5.0以下系统，如果传入的contentDrawable为selector则直接返回，不是则返回组装好的selector
	 */
	public static Drawable getRippleDrawable(Drawable contentDrawable) {
		if (Build.VERSION.SDK_INT >= 21) {
			try {
				Class<?> cls = Class.forName("android.graphics.drawable.RippleDrawable");
				Class<?>[] parTypes = {ColorStateList.class, Drawable.class, Drawable.class};
	            Object[] params = {ColorStateList.valueOf(Color.parseColor("#0C000000")), contentDrawable, null};
	            Constructor<?> con = cls.getConstructor(parTypes);
	            Object obj = con.newInstance(params);
	            Drawable dr = (Drawable) obj;
	            if (dr != null) {
	    			return dr;
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (contentDrawable instanceof StateListDrawable) {
			return contentDrawable;
		} else {
			Drawable drawable = new ColorDrawable(Color.parseColor("#0C000000"));
			StateListDrawable sd = new StateListDrawable();
			sd.addState(new int[]{android.R.attr.state_pressed}, drawable);
		    sd.addState(new int[]{android.R.attr.state_focused}, drawable);
		    sd.addState(new int[]{}, contentDrawable);
			return sd;
		}
	}
}
