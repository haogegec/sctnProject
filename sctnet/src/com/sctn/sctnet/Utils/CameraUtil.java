package com.sctn.sctnet.Utils;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract.Constants;
import android.widget.Toast;
import com.sctn.sctnet.contants.Constant;

public class CameraUtil {

	private String[] items = new String[]{"选择本地图片", "拍照"};

	private String IMAGE_FILE_NAME = "sctnetTempImage.png";
	private int zoomWidth;
	private int zoomHight;
	private String dialogTitle = "上传图片";
	private CameraCallBack cameraCallBack; // 声明一个回调函数

	private Activity mActivity;

	public CameraUtil(Activity activity, CameraCallBack cameraCallBack) {
		this.mActivity = activity;
		this.cameraCallBack = cameraCallBack;
		zoomWidth = Constant.IMAGE_CROP_WIDTH;
		zoomHight = Constant.IMAGE_CROP_HIGHT;
	}

	public CameraUtil(Activity activity) {
		this.mActivity = activity;
		zoomWidth = Constant.IMAGE_CROP_WIDTH;
		zoomHight = Constant.IMAGE_CROP_HIGHT;
	}

	public CameraUtil(Activity activity, CameraCallBack cameraCallBack, int zoomWidth, int zoomHight) {
		this.mActivity = activity;
		this.cameraCallBack = cameraCallBack;
		this.zoomWidth = zoomWidth;
		this.zoomHight = zoomHight;
	}

	public void showUplaodImageDialog() {
		new AlertDialog.Builder(mActivity).setTitle(getDialogTitle()).setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0 :
						Intent intentFromGallery = new Intent();
						intentFromGallery.setType("image/*"); // 设置文件类型
						intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
						mActivity.startActivityForResult(intentFromGallery, Constant.IMAGE_REQUEST_CODE);
						break;
					case 1 :
						Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						// 判断存储卡是否可以用，可用进行存储
						if (SDCardUtil.IsSDCardExist()) {
							intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
						}
						mActivity.startActivityForResult(intentFromCapture, Constant.CAMERA_REQUEST_CODE);
						break;
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
	/*
	 * 相应结果码
	 */
 
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}
		// 结果码不等于取消时候
		switch (requestCode) {
			case Constant.IMAGE_REQUEST_CODE :
				startPhotoZoom(data.getData());
				break;
			case Constant.CAMERA_REQUEST_CODE :
				if (SDCardUtil.IsSDCardExist()) {
					File tempFile = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(mActivity, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
				}

				break;
			case Constant.CAOP_RESULT_REQUEST_CODE :
				if (data != null) {
					if (cameraCallBack != null) {
						cameraCallBack.cropResult(mActivity, data);
					}
				}
				break;

			case Constant.UPLAOD_RESULT_REQUEST_CODE :
				Toast.makeText(mActivity, "上传成功", Toast.LENGTH_SHORT).show();
				if (cameraCallBack != null) {
					cameraCallBack.upLoadImageResult(mActivity, data);
				}
				break;
		}
	}

	/**
	 * 剪切图片
	 * */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例 按照系统默认 的 5:4 的比例
		intent.putExtra("aspectX", Constant.IMAGE_CROP_WIDTH_SCALE);
		intent.putExtra("aspectY", Constant.IMAGE_CROP_HIGHT_SCALE);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", getZoomWidth());
		intent.putExtra("outputY", getZoomHight());
		intent.putExtra("return-data", true);
		mActivity.startActivityForResult(intent, Constant.CAOP_RESULT_REQUEST_CODE);// 截切图片相应结果
	}
	public int getZoomWidth() {
		return zoomWidth;
	}

	public void setZoomWidth(int zoomWidth) {
		this.zoomWidth = zoomWidth;
	}

	public int getZoomHight() {
		return zoomHight;
	}

	public void setZoomHight(int zoomHight) {
		this.zoomHight = zoomHight;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public void setCameraCallBack(CameraCallBack cameraCallBack) {
		this.cameraCallBack = cameraCallBack;
	}

}
