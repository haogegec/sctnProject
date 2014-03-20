package com.sctn.sctnet.Utils;

import android.content.Context;
import android.content.Intent;
/***
 * 
 *CameraUtil 回调函数 接口
 * */
public interface CameraCallBack {
	/**
	 * 剪切结果 回调 函数
	 * */
	public void cropResult(Context context,Intent data);
	/***
	 * 
	 * 上传图片回调 函数
	 * */
	public void upLoadImageResult(Context context,Intent data);
}
