package com.sctn.sctnet.Utils;

import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.sctn.sctnet.cache.SctnAplication;

public class PhoneUtil {

	// 手机的唯一标识
	public static final String DEVICE_ID = ((TelephonyManager) SctnAplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

	// 手机型号
	public static final String MODEL = android.os.Build.MODEL;

	// 手机版本号
	public static final String VERSION = android.os.Build.VERSION.RELEASE;

	// 手机Android系统版本号
	public static final String OS_VERSION = android.os.Build.VERSION.SDK_INT + "";

	// 操作系统名称
	public static final String OS_NAME = "Android";

	// 网络类型
//	public static final String NETWORK_TYPE = ((ConnectivityManager) SctnAplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().getTypeName();

	// 时区
	public static final String TIME_ZONE = TimeZone.getDefault().getDisplayName();

	// 取得运营商所在国家
	public static final String COUNTRY = ((TelephonyManager) SctnAplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkCountryIso();

	// 所使用语言
	public static final String LANGUAGE = Locale.getDefault().getLanguage();

	/**
	 * 获取手机号 获取不到则返回0
	 * */
	public static String getPhoneNumber() {
		TelephonyManager manager = (TelephonyManager) SctnAplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum = manager.getLine1Number();
		return StringUtil.isBlank(phoneNum) ? "0" : phoneNum;
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
	 * 获取手机屏幕宽度 无参数
	 * */
	public static int getPhoneScreenWidth() {
		WindowManager wm = (WindowManager) SctnAplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;// 获取屏幕宽度
	}

	/***
	 * 获取手机屏幕高度 无参数
	 * */
	public static int getPhoneScreenHeght() {
		WindowManager wm = (WindowManager) SctnAplication.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;// 获取屏幕高度
	}

}
