package com.sctn.sctnet.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CacheProcess{
	
	/**
	 * 根据键获取缓存在SharedPreference的String类型的值
	 * @param context
	 * @param key
	 */
	public String getStringCacheValueInSharedPreferences(Context context,String key){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		String val=sharedPreferences.getString(key, "");
		return val;
	}
	
	/**
	 * 根据键获取缓存在SharedPreference的int类型的值
	 * @param context
	 * @param key
	 * @return
	 */
	public int getIntCacheValueInSharedPreferences(Context context,String key){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		int val=sharedPreferences.getInt(key, 0);
		return val;
	}
	
	/**
	 * 根据键获取缓存在SharedPreference的long类型的值
	 * @param context
	 * @param key
	 * @return
	 */
	public long getLongCacheValueInSharedPreferences(Context context,String key){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		long val=sharedPreferences.getLong(key, 0);
		return val;
	}
	
	/**
	 * 根据键获取缓存在SharedPreference的boolean类型的值
	 * @param context
	 * @param key
	 * @return
	 */
	public boolean getBooleanCacheValueInSharedPreferences(Context context,String key){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean val=sharedPreferences.getBoolean(key, false);
		return val;
	}
}
