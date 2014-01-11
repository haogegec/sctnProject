package com.sctn.sctnet.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sctn.sctnet.cache.SctnAplication;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 服务端返回的公共的头信息
 * @author wanghao
 *
 */
public class ReturnHeadData {

	public String returnCode;//成功失败的标志
	public String errorMsg; //错误信息
	public String userName;//用户名

	/**
	 * 将服务端返回的结果解析成json格式，返回头信息
	 * @param result
	 * @return
	 */
	public static ReturnHeadData parserReturnHeadFromStr(String result) {
		ReturnHeadData returnHeadData = new ReturnHeadData();
		try {
			JSONObject resultJSONObject = new JSONObject(result);
			JSONObject mobileHeadJSONObject = resultJSONObject.getJSONObject("mobileHead");
			String errormsg = mobileHeadJSONObject.getString("errormsg");
			String code = mobileHeadJSONObject.getString("code");
			JSONObject personalJSONObject = mobileHeadJSONObject.getJSONObject("personal");
			String userName = personalJSONObject.getString("userName");
			
			returnHeadData.returnCode = code;
			returnHeadData.errorMsg = errormsg;
			returnHeadData.userName = userName;
		
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SctnAplication
					.getInstance().getApplicationContext());
			Editor edit = sharedPreferences.edit();
			if(sharedPreferences.getString("userName", "")==null||"".equals(sharedPreferences.getString("userName", ""))) {
				edit.putString("userName", userName);
			}
			edit.commit();
			
		} catch (JSONException e) {
			returnHeadData.returnCode = "1";
			returnHeadData.errorMsg = "头信息解析json出错";
		}
		return returnHeadData;
		
	}
}
