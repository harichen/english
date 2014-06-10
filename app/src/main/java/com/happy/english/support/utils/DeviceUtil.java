package com.happy.english.support.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class DeviceUtil {

	/**
	 * 获取版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "1.0.0";
	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * 获取 IMEI
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceKey(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String strKey = tm.getDeviceId();
		if (strKey == null) {
			strKey = String.valueOf(System.currentTimeMillis());
		}
		return strKey;
	}

	/**
	 * 获取屏幕密度，分辨率等
	 * 
	 * @param context
	 */
	public static void getDisplay(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0/3.0）
		int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320/480）
		Log.e("DisplayMetrics", "xdpi=" + dm.xdpi + "; ydpi=" + dm.ydpi);
		Log.e("DisplayMetrics", "widthPixels=" + dm.widthPixels + "; heightPixels=" + dm.heightPixels);
		Log.e("DisplayMetrics", "density=" + density + "; densityDPI="
				+ densityDPI);
	}

	// 显示全屏
	public static void setFullScreen(Activity context) {
		context.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	// 退出全屏：
	public static void quitFullScreen(Activity context) {
		final WindowManager.LayoutParams attrs = context.getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		context.getWindow().setAttributes(attrs);
		context.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}
	public static int getDeviceWidth(Activity context){
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		return screenWidth ;
	}
	
	public static int getDeviceHeight(Activity context){
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenHeigh = dm.heightPixels;
		return screenHeigh;
	}
	
	
	/**get status bur height 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        return statusBarHeight;
    }
}
