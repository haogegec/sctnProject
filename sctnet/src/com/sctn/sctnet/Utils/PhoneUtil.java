package com.sctn.sctnet.Utils;



import com.sctn.sctnet.cache.SctnAplication;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class PhoneUtil   {

	/**
	 * 获取手机号 获取不到则返回0
	 * */
	public static String getPhoneNumber() {
		TelephonyManager manager = (TelephonyManager) SctnAplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum=manager.getLine1Number();
		return StringUtil.isBlank(phoneNum)?"0":phoneNum;
	}
	
	/***
	 * 获取手机屏幕宽度
	 * */
	public static int getPhoneScreenWidth(Context context) {
		SctnAplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;// 获取屏幕宽度
	}
	/***
	 * 获取手机屏幕高度
	 * */
	public static int getPhoneScreenHeght(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;// 获取屏幕高度
	}
	
	/***
	 * 获取手机屏幕宽度  无参数
	 * */
	public static int getPhoneScreenWidth() {
		WindowManager wm = (WindowManager)SctnAplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;// 获取屏幕宽度
	}
	/***
	 * 获取手机屏幕高度 无参数
	 * */
	public static int getPhoneScreenHeght() {
		WindowManager wm = (WindowManager)SctnAplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;// 获取屏幕高度
	}
	
	
	

}
