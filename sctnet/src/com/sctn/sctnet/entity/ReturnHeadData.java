package com.sctn.sctnet.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sctn.sctnet.cache.sctnAplication;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 服务端返回的公共的头信息
 * @author wanghao
 *
 */
public class ReturnHeadData {

	public String custId;
	public String userId;
	public String returnCode;
	public String errorMsg; 
	public String bindingPhone;
	public String userName;

	public String staffId;
	public String shopId;
	public String branchId;
	
	public String authId;

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
			String custId = personalJSONObject.getString("custId");
			String userId = personalJSONObject.getString("userId");
			String authId = personalJSONObject.getString("authId");
		//	String userName = personalJSONObject.getString("userName");
		//	String bindingPhone = personalJSONObject.getString("bindingPhone");
			String staffId = personalJSONObject.getString("staffId");
			String shopId = personalJSONObject.getString("shopId");
			String branchId = personalJSONObject.getString("branchId");
			
			returnHeadData.custId = custId;
			returnHeadData.userId = userId;
			returnHeadData.returnCode = code;
			returnHeadData.errorMsg = errormsg;
			returnHeadData.bindingPhone = "123";
			returnHeadData.userName = "adf";

			returnHeadData.staffId = staffId;
			returnHeadData.shopId = shopId;
			returnHeadData.branchId = branchId;

			returnHeadData.authId="123";

			
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(sctnAplication
					.getInstance().getApplicationContext());
			Editor edit = sharedPreferences.edit();
			if(sharedPreferences.getString("userId", "")==null||"".equals(sharedPreferences.getString("userId", ""))) {
				edit.putString("userId", userId);
			}
			if(sharedPreferences.getString("custId", "")==null||"".equals(sharedPreferences.getString("custId", ""))) {
				edit.putString("custId", custId);
			}
			if(sharedPreferences.getString("staffId", "")==null||"".equals(sharedPreferences.getString("staffId", ""))) {
				edit.putString("staffId", staffId);
			}
			if(sharedPreferences.getString("shopId", "")==null||"".equals(sharedPreferences.getString("shopId", ""))) {
				edit.putString("shopId", shopId);
			}
			if(sharedPreferences.getString("branchId", "")==null||"".equals(sharedPreferences.getString("branchId", ""))) {
				edit.putString("branchId", branchId);
			}

			edit.commit();
			
		} catch (JSONException e) {
			returnHeadData.returnCode = "1";
			returnHeadData.errorMsg = "头信息解析json出错";
		}
		return returnHeadData;
		
	}
}
