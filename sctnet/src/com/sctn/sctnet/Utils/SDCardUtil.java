package com.sctn.sctnet.Utils;

import android.os.Environment;

public class SDCardUtil {
	/**
	 * 判断SD卡 是否存在
	 * */
	public static Boolean IsSDCardExist(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
}
