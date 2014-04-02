package com.sctn.sctnet.entity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.sctn.sctnet.Utils.SharePreferencesUtils;

public class LoginInfo {
	
	public static final String LOGIN_SUCCESS_FLAG = "isLogin"; // 登录成功标志位
	
	/**
	 * 判断是否登录，已登录 返回true；没登录返回false
	 * */
	public static boolean isLogin() {
		return SharePreferencesUtils.getSharedBooleanData(LOGIN_SUCCESS_FLAG);
	}
	
	/**
	 * 退出登录
	 * */
	public static void logOut() {
		//清空 请求头信息
		long userId = 0;
		SharePreferencesUtils.setSharedlongData("userId",userId);
		SharePreferencesUtils.setSharedStringData("userName", "");
		SharePreferencesUtils.setSharedStringData("password", "");
		SharePreferencesUtils.setSharedBooleanData(LOGIN_SUCCESS_FLAG, false);
		SharePreferencesUtils.setSharedBooleanData("isRememberPassword",false);
		SharePreferencesUtils.setSharedBooleanData("isAutoLogin", false);
	}
	/**
	 * 用来判断用户是否有简历
	 * @return
	 */
	public static boolean hasResume(String userId) {
		return SharePreferencesUtils.getSharedBooleanData(userId);
	}
}
