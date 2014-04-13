package com.sctn.sctnet.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.PhoneUtil;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.ItemView;

/**
 * 订阅界面
 * 
 * @author xueweiwei
 * 
 */
public class SubscribeActivity extends BaicActivity {

	private EditText subsribeNameEdit;
	private EditText keyWordsEdit;
	private ItemView pushSetItem;
	private Builder builder;
	private Dialog dialog;// 弹出框
	private String[] pushItem = { "每天", "每三天", "每周", "不推送" };
	private boolean subscribe;// 是否开启消息订阅
	private boolean jobInformationPushAuto;// 是否开启职业信息自动推送
	private String result;// 服务端返回的数据
	private HashSet<String> keywords;// 设置tags用的
	String logs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribe_activity);
		setTitleBar(getString(R.string.subscribeActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);

		builder = new AlertDialog.Builder(SubscribeActivity.this);

		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {

		subsribeNameEdit = (EditText) findViewById(R.id.subscribe_name_edit);
		keyWordsEdit = (EditText) findViewById(R.id.keywords_edit);
		pushSetItem = (ItemView) findViewById(R.id.push_set);
		pushSetItem.setBackground(R.drawable.item_up_bg);
		pushSetItem.setIconImageViewResource(R.drawable.home_btn_normal);
		pushSetItem.setLabel("推送设置");
		pushSetItem.setValue("不推送");
		pushSetItem.setDetailImageViewResource(R.drawable.detail);
		pushSetItem.setIconImageVisibility(View.GONE);
	}

	@Override
	protected void reigesterAllEvent() {

		pushSetItem.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				builder.setTitle("推送设置");
				builder.setSingleChoiceItems(pushItem, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						pushSetItem.setValue(pushItem[which]);

						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});
		// 确定按钮
		titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (StringUtil.isBlank(subsribeNameEdit.getText().toString())) {
					Toast.makeText(getApplicationContext(), "请输入订阅名称", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(keyWordsEdit.getText().toString())) {
					Toast.makeText(getApplicationContext(), "请输入关键字", Toast.LENGTH_SHORT).show();
				} else {
					
					String[] temp = keyWordsEdit.getText().toString().split(",");
					keywords = new HashSet<String>();
					for(String keyword:temp){
						keywords.add(keyword);
					}
					requestDataThread();
					
//					Intent intent = getIntent();
//					intent.putExtra("tags", "总经理");
//					setResult(RESULT_OK,intent);
//					finish();
					
				}
			}

		});

	}

	/**
	 * 请求数据线程
	 * 
	 */
	private void requestDataThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestData();
					}
				});
		mThread.start();
	}

	private void requestData() {
		String url = "appMessageSend.app";

		Message msg = new Message();

		jobInformationPushAuto = SharePreferencesUtils.getSharedBooleanData("jobInformationPushAuto");
		subscribe = SharePreferencesUtils.getSharedBooleanData("subscribe");

		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("DeviceId", PhoneUtil.DEVICE_ID));
			params.add(new BasicNameValuePair("RegisterrationId", JPushInterface.getRegistrationID(getApplicationContext())));
			params.add(new BasicNameValuePair("DeviceName", "默认设备名称"));
			params.add(new BasicNameValuePair("DeviceModel", PhoneUtil.MODEL));
			params.add(new BasicNameValuePair("flag", Constant.PUSH_BY_TAGS));
			params.add(new BasicNameValuePair("Tags", keyWordsEdit.getText().toString()));
			if (jobInformationPushAuto) {// 这个true表示按钮时开着的，这块没有关闭的动作，所以传送的是 Y
				params.add(new BasicNameValuePair("AuthPush", "Y"));
			} else {
				params.add(new BasicNameValuePair("AuthPush", "N"));
			}
			
			if(subscribe){// true 表示按钮时开着的，正在关闭
				params.add(new BasicNameValuePair("UserDefinedPush", "N"));
			} else {
				params.add(new BasicNameValuePair("UserDefinedPush", "Y"));
			}

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);// 返回结果存放在该json对象中
			if ("0".equals(responseJsonObject.getString("resultCode"))) {
				msg.what = Constant.USER_DEFINED_PUSH_SUCCESS;
				handler.sendMessage(msg);
			} else {
				String errorResult = responseJsonObject.getString("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			sendExceptionMsg(err);
		}
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.USER_DEFINED_PUSH_SUCCESS:
				
				finishCurrentActivity();
				break;
				
//			case Constant.USER_DEFINED_PUSH_SUCCESS_MODIFY_WIDGET:
//				finishCurrentActivity();
//				break;
			}
		}
	};
	
	private void finishCurrentActivity(){
//		Toast.makeText(getApplicationContext(), logs, Toast.LENGTH_SHORT).show();
		
		ArrayList<String> tagList = new ArrayList<String>();
		for(String keyword:keywords){
			tagList.add(keyword);
		}
		
		Intent intent = getIntent();
		intent.putStringArrayListExtra("tags",tagList);
		setResult(RESULT_OK,intent);
		finish();
		
	}	
//	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
//
//		@Override
//		public void gotResult(int code, String alias, Set<String> tags) {
//			Message msg = new Message();
//			switch (code) {
//			case 0:
//				logs = "订阅成功";
//				msg.what = Constant.USER_DEFINED_PUSH_SUCCESS_MODIFY_WIDGET;
//				handler.sendMessage(msg);
//				break;
//
//			case 6002:
//				logs = "网络连接超时，请60秒之后重新设置";
//				break;
//
//			default:
//				logs = "设置失败，错误代码：" + code;
//				break;
//			}
//
//		}
//
//	};
}
