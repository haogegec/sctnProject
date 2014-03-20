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
	public void initJobSearchLogInSharedPreferences(Context context,Map<String,String> map){
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor= sp.edit();
		
		for(int i=4;i>0;i--) {			
	    	editor.putString("list_"+(i-1)+"area", sp.getString("list_" + i+"area", ""));
	    	editor.putString("list_"+(i-1)+"JobsClass", sp.getString("list_" + i+"JobsClass", ""));
	    	editor.putString("list_"+(i-1)+"NeedProfession", sp.getString("list_" + i+"NeedProfession", ""));
	    	
	    	editor.putString("list_"+(i-1)+"areaName", sp.getString("list_" + i+"areaName", ""));
	    	editor.putString("list_"+(i-1)+"JobsClassName", sp.getString("list_" + i+"JobsClassName", ""));
	    	editor.putString("list_"+(i-1)+"NeedProfessionName", sp.getString("list_" + i+"NeedProfessionName", ""));
	    	
	    	editor.putString("list_"+(i-1)+"count", sp.getString("list_" + i+"count", ""));
	    }
		editor.putString("list_"+4+"area", map.get("area"));
    	editor.putString("list_"+4+"JobsClass", map.get("JobsClass"));
    	editor.putString("list_"+4+"NeedProfession",  map.get("NeedProfession"));
    	
    	editor.putString("list_"+4+"areaName", map.get("areaName"));
    	editor.putString("list_"+4+"JobsClassName", map.get("JobsClassName"));
    	editor.putString("list_"+4+"NeedProfessionName", map.get("NeedProfessionName"));
    	
    	editor.putString("list_"+4+"count", map.get("count"));
        editor.commit();
	}
	
	public List<Map<String,String>> JobSearchLogInSharedPreferences(Context context){
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	    
	    for(int i=0;i<5;i++) {
	    	Map<String,String> map = new HashMap<String,String>();
	    	map.put("area", sp.getString("list_" + i+"area", ""));
	    	map.put("JobsClass", sp.getString("list_" + i+"JobsClass", ""));
	    	map.put("NeedProfession", sp.getString("list_" + i+"NeedProfession", ""));
	    	
	    	map.put("areaName", sp.getString("list_" + i+"areaName", ""));
	    	map.put("JobsClassName", sp.getString("list_" + i+"JobsClassName", ""));
	    	map.put("NeedProfessionName", sp.getString("list_" + i+"NeedProfessionName", ""));
	    	
	    	map.put("count", sp.getString("list_" + i+"count", ""));
	    	if(!sp.getString("list_" + i+"area", "").equals("")){
	    		list.add(map);  
	    	}
	    	
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
