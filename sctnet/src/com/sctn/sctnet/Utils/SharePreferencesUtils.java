package com.sctn.sctnet.Utils;

import com.sctn.sctnet.cache.SctnAplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/*
 *SharePreferences 工具类  sharePreferences 只能存储Int,Long,Float,String,Boolean 基本类型
 * */
public class SharePreferencesUtils {

	private static SharedPreferences sp;
	private static Context context;

	private static void init() {
		if (sp == null) {
			context = SctnAplication.getInstance().getBaseContext();
			sp = PreferenceManager.getDefaultSharedPreferences(context);

		}
	}

	public static void setSharedIntData(String key, int value) {
		if (sp == null) {
			init();
		}
		sp.edit().putInt(key, value).commit();
	}

	public static int getSharedIntData(String key) {
		if (sp == null) {
			init();
		}
		return sp.getInt(key, 0);
	}

	public static void setSharedlongData(String key, long value) {
		if (sp == null) {
			init();
		}
		sp.edit().putLong(key, value).commit();
	}

	public static long getSharedlongData(String key) {
		if (sp == null) {
			init();
		}
		return sp.getLong(key, 0l);
	}

	public static void setSharedFloatData(String key, float value) {
		if (sp == null) {
			init();
		}
		sp.edit().putFloat(key, value).commit();
	}

	public static Float getSharedFloatData(String key) {
		if (sp == null) {
			init();
		}
		return sp.getFloat(key, 0f);
	}

	public static void setSharedBooleanData(String key, boolean value) {
		if (sp == null) {
			init();
		}
		sp.edit().putBoolean(key, value).commit();
	}

	public static Boolean getSharedBooleanData(String key) {
		if (sp == null) {
			init();
		}
		return sp.getBoolean(key, false);
	}

	public static void setSharedStringData(String key, String value) {
		if (sp == null) {
			init();
		}
		sp.edit().putString(key, value).commit();
	}

	public static String getSharedStringData(String key) {
		if (sp == null) {
			init();
		}
		return sp.getString(key, "");
	}

	public static void clearSharedPreferenceData() {
		if (sp == null) init();
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

}