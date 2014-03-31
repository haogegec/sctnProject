package com.sctn.sctnet.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
/**
 * 用户反馈界面
 * @author 姜勇男
 *
 */
public class FeedBackActivity extends BaicActivity{
	
	private Button btn_submit ;// 提交按钮
	private EditText feedbackEdit;
	private EditText emailEdit;
	private long userId;
	private String result;// 服务端返回的结果
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_activity);
		setTitleBar(getString(R.string.feedback), View.VISIBLE, View.GONE);
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		userId = SharePreferencesUtils.getSharedlongData("userId");
		
		btn_submit = (Button)findViewById(R.id.btn_submit);
		feedbackEdit = (EditText) findViewById(R.id.feedback);
		emailEdit = (EditText) findViewById(R.id.email);
		
		feedbackEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 提交按钮
		btn_submit.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(StringUtil.isBlank(feedbackEdit.getText().toString())){
					Toast.makeText(getApplicationContext(), "请填写您的反馈意见再提交吧~~", Toast.LENGTH_SHORT).show();
				}else{
					 requestDataThread();
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

		String url = "appInfo!insert.app";

		Message msg = new Message();
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("USER_ID", userId+""));
		params.add(new BasicNameValuePair("FEED_DESC",feedbackEdit.getText().toString()));
		params.add(new BasicNameValuePair("EMAIL", emailEdit.getText().toString()));
		
		result = getPostHttpContent(url, params);
		if (StringUtil.isExcetionInfo(result)) {
			FeedBackActivity.this.sendExceptionMsg(result);
			return;
		}
		
		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			FeedBackActivity.this.sendExceptionMsg(result);
			return;
		}
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultCode").toString().equals("0")) {
			msg.what = 00;
			handler.sendMessage(msg);
		}else{
			msg.what = 1;
			handler.sendMessage(msg);
		}
	}
	// 处理线程发送的消息
		private Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 00:
					Toast.makeText(getApplicationContext(), "您反馈的意见已提交，我们会继续改进~~", Toast.LENGTH_SHORT).show();
					finish();
					break;
				case 11:
					Toast.makeText(getApplicationContext(), "反馈意见失败，要不重新提交一下吧", Toast.LENGTH_SHORT).show();
					break;
				}
				
			}
		};
}
