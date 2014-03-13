package com.sctn.sctnet.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;

/**
 * 信息咨询详情页面
 * @author xueweiwei
 *
 */
public class InformationDetailActivity extends BaicActivity{

	private String title;
	private String id;
	private String result;
	private JSONObject responseJsonObject = null;// 返回结果存放在该json对象中
	private String informationContent;
	private TextView informationContentText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_detail);
		initBundle();
		initAllView();
		reigesterAllEvent();
		requestDataThread();
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
   private void requestData(){
		
		String url = "appInfo!find.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("id", id));
		//	params.add(new BasicNameValuePair("pageSize", pageSize + ""));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				InformationDetailActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {
				result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
				InformationDetailActivity.this.sendExceptionMsg(result);
				return;
			}
			responseJsonObject = new JSONObject(result);
			Message m=new Message();
            if(responseJsonObject.get("resultCode").toString().equals("0")) {
            	informationContent = (String) responseJsonObject.getJSONObject("result").get("content");				
				m.what = 0;
				handler.sendMessage(m);
			}else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				InformationDetailActivity.this.sendExceptionMsg(err);
				return;
			}
					
			
			
	}catch (JSONException e) {
		String err = StringUtil.getAppException4MOS("解析json出错！");
		InformationDetailActivity.this.sendExceptionMsg(err);
	}
 }
   private void initUI(){
	   informationContentText.setText(Html.fromHtml(informationContent));
   }
//处理线程发送的消息
		private Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					initUI();
					break;

				}
				closeProcessDialog();
			}
		};
	private void initBundle(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("title");
		id = bundle.getString("id");
	}
	@Override
	protected void initAllView() {
		
		setTitleBar(title, View.VISIBLE, View.GONE);
		informationContentText = (TextView) findViewById(R.id.information_detail_text);
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}

}
