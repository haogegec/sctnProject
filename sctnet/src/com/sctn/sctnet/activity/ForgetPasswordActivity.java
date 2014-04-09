package com.sctn.sctnet.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.entity.LoginInfo;

public class ForgetPasswordActivity extends BaicActivity{
	
	private EditText et_username;// 用户名
	private EditText et_email;// 邮箱
	private Button btn;// 确定按钮
	
	private String userName;
	private String email;
	
	private String response;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password_activity);
		setTitleBar(getString(R.string.forgetPasswordTitle), View.VISIBLE, View.GONE);
		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		et_username = (EditText) findViewById(R.id.et_username);
		et_email = (EditText) findViewById(R.id.et_email);
		btn = (Button) findViewById(R.id.btn_ok);
	}

	@Override
	protected void reigesterAllEvent() {
		
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				if(StringUtil.isBlank(et_username.getText().toString())){
					Toast.makeText(ForgetPasswordActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
                if(StringUtil.isBlank(et_email.getText().toString())){
					Toast.makeText(ForgetPasswordActivity.this, "邮箱不能为空",  Toast.LENGTH_SHORT).show();
					return;
				}
                userName =  et_username.getText().toString();
                email = et_email.getText().toString();
                if(!StringUtil.isEmailFormat(email)){
					Toast.makeText(getApplicationContext(), "邮箱格式不正确，请重新输入", Toast.LENGTH_LONG).show();
					return;
				}
                //请求数据
                forgetPasswordThread();
			}
			
		});
		
	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void forgetPasswordThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						forgetPassword();
					}
				});
		mThread.start();
	}
	
	/**
	 * 请求数据，并将返回结果显示在界面上
	 */
	private void forgetPassword() {

		String url = "eMailSendImpl.app";

		Message msg = new Message();
		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			
			params.add(new BasicNameValuePair("username", userName));
			params.add(new BasicNameValuePair("email", email));// 没填写邮箱的时候传空值
			response = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(response)) {
				ForgetPasswordActivity.this.sendExceptionMsg(response);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(response);
			if (responseJsonObject.getString("resultCode").equals("0")) {// 获得响应结果

				
					setResult(RESULT_OK);
					finish();
				
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				ForgetPasswordActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			String err = StringUtil.getAppException4MOS("解析json出错！");
			ForgetPasswordActivity.this.sendExceptionMsg(err);
		}
	}


}
