package com.happy.english.support.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.happy.english.R;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * 
 * @author xmt
 * 
 */
public class UIUtils
{
	
    private static Toast mToast;
    public static void showToast(Context context,String msg, int pic) {
    	if (mToast != null) {
    		View view = mToast.getView();
    		TextView tv = (TextView) view.findViewById(R.id.tv);
    		ImageView iv = (ImageView) view.findViewById(R.id.iv);
    		tv.setText(msg);
    		iv.setImageResource(pic);
    	} else{
    		mToast = getToast(context, pic, msg);
    	}
    	mToast.show();
    }
    
    public static void showToast(Context context,String msg, int pic,int gravity) {
    	if (mToast != null) {
    		View view = mToast.getView();
    		TextView tv = (TextView) view.findViewById(R.id.tv);
    		ImageView iv = (ImageView) view.findViewById(R.id.iv);
    		tv.setText(msg);
    		iv.setImageResource(pic);
    	} else{
    		mToast = getToast(context, pic, msg);
    	}
    	mToast.setGravity(gravity, 0,0);
    	mToast.show();
    }
    
    public static  void showToast(Context context,String msg) {
		if (mToast != null) {
			View view = mToast.getView();
			TextView tv = (TextView) view.findViewById(R.id.tv);
			tv.setText(msg);
		} else
			mToast = getToast(context, 0, msg);
		mToast.show();
	}

	public static  Toast getToast(Context context, int r, String str) {
		Toast toast = new Toast(context);
		View v = LayoutInflater.from(context).inflate(R.layout.cutsomtoast, null);
		TextView tv = (TextView) v.findViewById(R.id.tv);
		ImageView iv = (ImageView) v.findViewById(R.id.iv);
		tv.setText(str);
		if(r != 0){
			iv.setImageResource(r);
		}
		toast.setView(v);
		toast.setGravity(Gravity.CENTER, 0,300);
		return toast;
	}
    
    /**
     * 显示/隐藏键盘
     */
    @SuppressWarnings("static-access")
    public static void hideSoftInputMode(EditText showSoftEditText,
            Context ctx, boolean isHide)
    {
        InputMethodManager imm = (InputMethodManager) ctx
                .getSystemService(ctx.INPUT_METHOD_SERVICE);
        if (isHide)
        {
            imm.hideSoftInputFromWindow(showSoftEditText.getWindowToken(), 0);
        }
        else
        {
            imm.showSoftInput(showSoftEditText, InputMethodManager.SHOW_FORCED);
        }
    }
    
    public static void openActivity(Context mContext, Class<?> cls)
    {
        Intent mIntent = new Intent(mContext, cls);
        mContext.startActivity(mIntent);
    }
    
    /**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
            if (width <= height) {
                    roundPx = width / 2;
                    top = 0;
                    bottom = width;
                    left = 0;
                    right = width;
                    height = width;
                    dst_left = 0;
                    dst_top = 0;
                    dst_right = width;
                    dst_bottom = width;
            } else {
                    roundPx = height / 2;
                    float clip = (width - height) / 2;
                    left = clip;
                    right = width - clip;
                    top = 0;
                    bottom = height;
                    width = height;
                    dst_left = 0;
                    dst_top = 0;
                    dst_right = height;
                    dst_bottom = height;
            }
             
            Bitmap output = Bitmap.createBitmap(width,
                            height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
             
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
            final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
            final RectF rectF = new RectF(dst);

            paint.setAntiAlias(true);
             
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            
            paint.setColor(0xcccccccc);
            paint.setStrokeWidth(1f);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(roundPx, roundPx, roundPx, paint);
            return output;
    }

}
