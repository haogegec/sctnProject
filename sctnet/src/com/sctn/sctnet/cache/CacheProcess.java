package com.sctn.sctnet.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CacheProcess{
	
	
	/**
	 * 将用户的搜索记录存储到SharedPreferences
	 * @param context
	 * @param list
	 */
	public void initJobSearchLogInSharedPreferences(Context context,List<Map<String,String>> list){
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor= sp.edit();
		editor.putInt("listSize",list.size()); 

		    for(int i=0;i<list.size();i++) {
		    	
	    		editor.remove("list_" + i+"condition");
		    	editor.putString("list_"+ i+"condition", list.get(i).get("condition"));
		    	
		    	editor.remove("list_" + i+"count");
		    	editor.putString("list_"+ i+"count", list.get(i).get("count"));
		   		    	  
		    }
        editor.commit();
	}
	
	public List<Map<String,String>> JobSearchLogInSharedPreferences(Context context){
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		int size = sp.getInt("listSize", 0);  

	    for(int i=0;i<size;i++) {
	    	Map<String,String> map = new HashMap<String,String>();
	    	map.put("condition", sp.getString("list_" + i+"condition", ""));
	    	map.put("count", sp.getString("list_" + i+"count", ""));
	    	list.add(map);  
	    }
	    return list;
	}
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
