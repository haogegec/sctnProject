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

	public String resultCode;//成功失败的标志
	public String resultMsg; //返回信息

	/**
	 * 将服务端返回的结果解析成json格式，返回头信息
	 * @param result
	 * @return
	 */
	public static ReturnHeadData parserReturnHeadFromStr(String result) {
		ReturnHeadData returnHeadData = new ReturnHeadData();
		try {
			JSONObject resultJSONObject = new JSONObject(result);
			String resultMsg = resultJSONObject.getString("resultMsg");
			String resultCode = resultJSONObject.getString("resultCode");
			
			returnHeadData.resultCode = resultCode;
			returnHeadData.resultMsg = resultMsg;
			
		} catch (JSONException e) {
			returnHeadData.resultCode = "1";
			returnHeadData.resultMsg = "头信息解析json出错";
		}
		return returnHeadData;
		
	}
}
