package com.sctn.sctnet.entity;

import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import com.sctn.sctnet.cache.SctnAplication;
import com.sctn.sctnet.contants.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

/**
 * 请求数据的公共信息头
 * @author wanghao
 *
 */
public class RequestHeadData {

	public static JSONObject getRequestHeadData() throws JSONException {
		
		JSONObject requestJSONObject = new JSONObject();
		JSONObject mobileHeadJSONObject = new JSONObject();
		
		JSONObject equipmentJSONObject = new JSONObject();		
		TelephonyManager tm = (TelephonyManager)SctnAplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();//手机的唯一标识  
		String name = android.os.Build.MODEL;//手机型号
		String version = android.os.Build.VERSION.RELEASE;//手机版本号
		String osVersion = android.os.Build.VERSION.SDK_INT+"";//手机Android系统版本号
		String osName = "Android";//操作系统名称
		
		if(deviceId==null||"".equals(deviceId)) {//手机唯一标识取不到，随机生成一串数字，并存放到本地文件中
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SctnAplication
					.getInstance().getApplicationContext());
			deviceId = sharedPreferences.getString("deviceId", "");
			if("".equals(deviceId)) {
				deviceId =java.util.UUID.randomUUID().toString();
				sharedPreferences.edit().putString("deviceId", deviceId).commit();
			}
		}
		equipmentJSONObject.put("deviceId", deviceId);
		equipmentJSONObject.put("name", name);
		equipmentJSONObject.put("version", version);
		equipmentJSONObject.put("osVersion", osVersion);
		equipmentJSONObject.put("osName", osName);	
		mobileHeadJSONObject.put("equipment", equipmentJSONObject);
		
		
		JSONObject clientJSONObject = new JSONObject();		
		ConnectivityManager connManager = (ConnectivityManager) SctnAplication
				.getInstance().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
		String networkType = ""; // 网络类型
		if (networkinfo != null) {
			networkType = networkinfo.getTypeName();
		}
		int versionCode = 0;//app版本号
		String versionName = "";//app版本名称
		try {
			versionCode = SctnAplication.getInstance().getApplicationContext()
					.getPackageManager()
					.getPackageInfo(Constant.packageName, 0).versionCode;
			versionName = SctnAplication.getInstance().getApplicationContext()
					.getPackageManager()
					.getPackageInfo(Constant.packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    clientJSONObject.put("netType", networkType);
	    clientJSONObject.put("versionCode", versionCode);
	    clientJSONObject.put("versionName", versionName);
	    mobileHeadJSONObject.put("client", clientJSONObject);

	    JSONObject personalJSONObject = new JSONObject();	    
	    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SctnAplication.getInstance().getApplicationContext());
	    personalJSONObject.put("userName", sharedPreferences.getString("userId", ""));   
	    personalJSONObject.put("userType", Constant.jobSeekerType);//用户类型	    
	    mobileHeadJSONObject.put("personal", personalJSONObject);
	    
	    JSONObject otherJSONObject = new JSONObject();	    
	    String timezone=TimeZone.getDefault().getDisplayName();//时区
	    String country=tm.getNetworkCountryIso(); //取得运营商所在国家
	    String lan=Locale.getDefault().getLanguage(); //所使用语言
	    
	    otherJSONObject.put("timeZone", timezone);
	    otherJSONObject.put("country", country);
	    otherJSONObject.put("lan", lan);
	    mobileHeadJSONObject.put("other", otherJSONObject);
	    requestJSONObject.put("mobileHead", mobileHeadJSONObject);
	    
		return requestJSONObject;
		
	}
	
}