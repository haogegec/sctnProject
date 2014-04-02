package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;

/**
 * 编辑联系方式
 * @author xueweiwei
 *
 */
public class ContactWayEditActivity extends BaicActivity{
	
	private EditText contactsnameValue;
	private String contactsnameStr = "";//第二联系人
	
	private EditText contactsphoneValue;
	private String contactsphoneStr = "";//第二联系人电话
	
	private EditText userphoneValue;
	private String userphoneStr = "";//第二联系人电话
	
	private EditText emailValue;
	private String emailStr = "";//email
	
	private EditText postalcodeValue;
	private String postalcodeStr = "";//邮政编码
	
	private EditText qqmsnValue;
	private String qqmsnStr = "";//QQ号
	
	private String result;// 服务端返回的结果
	private long userId;
	
	private HashMap<String, String> contactWayMap;
	
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> newContactWayMap = new HashMap<String, String>();//基本信息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_way_edit_activity);
		setTitleBar(getString(R.string.ContactWayEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		
		initIntent();
		initAllView();
		reigesterAllEvent();
	}

	protected void initIntent(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		userId = SharePreferencesUtils.getSharedlongData("userId");
		if(bundle!=null&&bundle.getSerializable("contactWayList")!=null){
			List<HashMap<String, String>> contactWayList = (List<HashMap<String, String>>) bundle.getSerializable("contactWayList");
			contactWayMap = contactWayList.get(0);
		}
	}
	@Override
	protected void initAllView() {
		
		contactsnameValue = (EditText) findViewById(R.id.contactsname_value);
		
		contactsphoneValue = (EditText) findViewById(R.id.contactsphone_value);
		
		userphoneValue = (EditText) findViewById(R.id.userphone_value);
		
		emailValue = (EditText) findViewById(R.id.email_value);
		
		postalcodeValue = (EditText) findViewById(R.id.postalcode_value);
		
		qqmsnValue = (EditText) findViewById(R.id.qqmsn_value);
		
		
		if(contactWayMap!=null&&contactWayMap.size()!=0){
			if(contactWayMap.containsKey("联系人")){
				contactsnameStr = contactWayMap.get("联系人");
				contactsnameValue.setText(contactsnameStr);
			}
			if(contactWayMap.containsKey("联系人电话")){
				contactsphoneStr = contactWayMap.get("联系人电话");
				contactsphoneValue.setText(contactsphoneStr);
			}
			if(contactWayMap.containsKey("邮箱")){
				emailStr = contactWayMap.get("邮箱");
				emailValue.setText(emailStr);
			}
			if(contactWayMap.containsKey("邮政编码")){
				postalcodeStr = contactWayMap.get("邮政编码");
				postalcodeValue.setText(postalcodeStr);
			}
			if(contactWayMap.containsKey("QQ")){
				qqmsnStr = contactWayMap.get("QQ");
				qqmsnValue.setText(qqmsnStr);
				
			}
			if(contactWayMap.containsKey("本人手机号")){
				userphoneStr = contactWayMap.get("本人手机号");
				userphoneValue.setText(userphoneStr);				
			}
			
		}
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 确定按钮
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

	           if(contactsnameStr.equals(contactsnameValue.getText().toString())&&contactsphoneStr.equals(contactsphoneValue.getText().toString())&&emailStr.equals(emailValue.getText().toString())
	        		   &&postalcodeStr.equals(postalcodeValue.getText().toString())&&qqmsnStr.equals(qqmsnValue.getText().toString())&&userphoneStr.equals(userphoneValue.getText().toString())
	        		   ){
	        	   
	        	   Toast.makeText(getApplicationContext(), "请编辑之后再保存吧~~", Toast.LENGTH_SHORT).show();
	        	   
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

		String url = "appPersonInfo!modify.app";

		Message msg = new Message();
     
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("Userid", userId+""));
		//params.add(new BasicNameValuePair("Userid",100020+""));
		params.add(new BasicNameValuePair("ContactsName", contactsnameValue.getText().toString()));
		params.add(new BasicNameValuePair("ContactsPhone",contactsphoneValue.getText().toString()));
		params.add(new BasicNameValuePair("email",emailValue.getText().toString()));
		params.add(new BasicNameValuePair("PostalCode", postalcodeValue.getText().toString()));
		params.add(new BasicNameValuePair("QQMsn", qqmsnValue.getText().toString()));
		params.add(new BasicNameValuePair("UsePhone", userphoneValue.getText().toString()));
		
		if(contactWayMap==null||!contactWayMap.containsKey("推荐自己")){
			params.add(new BasicNameValuePair("RecContent", " "));
		}
		
		params.add(new BasicNameValuePair("modifytype", "0"));//保存到简历表中
		
		result = getPostHttpContent(url, params);
		
		newContactWayMap.put("联系人", contactsnameValue.getText().toString());	
		newContactWayMap.put("联系人电话", contactsphoneValue.getText().toString());
		newContactWayMap.put("邮箱", emailValue.getText().toString());
		newContactWayMap.put("邮政编码", postalcodeValue.getText().toString());
		newContactWayMap.put("QQ", qqmsnValue.getText().toString());
		newContactWayMap.put("本人手机号", userphoneValue.getText().toString());
		list.add(newContactWayMap);
		
		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			sendExceptionMsg(result);
			return;
		}
		
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultCode").toString().equals("0")) {
			msg.what = 00;
			handler.sendMessage(msg);
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			sendExceptionMsg(err);
		}
		
	}
	// 处理线程发送的消息
		private Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 00:
					Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("list", list);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
					break;
				}
			}
		};

}
