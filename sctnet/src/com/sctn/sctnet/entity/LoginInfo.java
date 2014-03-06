package com.sctn.sctnet.entity;

import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;

public class LoginInfo {
	
	private static final String LOGIN_SUCCESS_FLAG = "isLogin"; // 登录成功标志位
	
	/**
	 * 判断是否登录，已登录 返回true；没登录返回false
	 * */
	public static boolean isLogin() {
		return SharePreferencesUtils.getSharedBooleanData(LOGIN_SUCCESS_FLAG);
	}
	
	
}
