package com.sctn.sctnet.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.Md5Builder;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;

public class RegisterActivity extends BaicActivity {

	private EditText et_username;// 用户名
	private EditText et_password;// 密码
	private EditText et_confirm_password;// 确认密码
	private EditText et_email;// 邮箱
	private Button btn_register;// 注册按钮
	
	private String username;
	private String password;
	private String confirmPassword;
	private String userTxtPwd;// 加密密码
	private String email;
	
	private String response;// 服务端返回结果
	private String userId;// 注册成功后服务端返回的userId
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		setTitleBar(getString(R.string.registerTitle), View.VISIBLE, View.GONE);
		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
		et_email = (EditText) findViewById(R.id.et_email);
		btn_register = (Button) findViewById(R.id.btn_register);
	}

	@Override
	protected void reigesterAllEvent() {
		
		btn_register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				username = et_username.getText().toString();
				password = et_password.getText().toString();
				confirmPassword = et_confirm_password.getText().toString();
				email = et_email.getText().toString();
				
				if(StringUtil.isBlank(username)){
					Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
				} else if (username.length() < 4 || username.length() > 16){
					Toast.makeText(getApplicationContext(), "请输入4到16位字母或数字", Toast.LENGTH_SHORT).show();
				} else if(StringUtil.isBlank(password)){
					Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
				} else if(password.length() < 6 || password.length() > 16){
					Toast.makeText(getApplicationContext(), "请输入6到16位字母或数字", Toast.LENGTH_SHORT).show();
				} else if(StringUtil.isBlank(confirmPassword)){
					Toast.makeText(getApplicationContext(), "请输入确认密码", Toast.LENGTH_SHORT).show();
				} else if(confirmPassword.length() < 6 || confirmPassword.length() > 16){
					Toast.makeText(getApplicationContext(), "请输入6到16位字母或数字", Toast.LENGTH_SHORT).show();
				} else if(!password.equals(confirmPassword)){
					Toast.makeText(getApplicationContext(), "两次输入的密码不一致，请重新输入", Toast.LENGTH_LONG).show();
				} else if(!StringUtil.isBlank(email)){
					if(!StringUtil.isEmailFormat(email)){
						Toast.makeText(getApplicationContext(), "邮箱格式不正确，请重新输入", Toast.LENGTH_LONG).show();
					} else {// 都校验没问题之后，提交注册
						userTxtPwd = Md5Builder.getMd5(password);
						registerThread();
					}
				} else {// 校验完用户名、密码、确认密码后，不输入邮箱，也可以提交注册
					userTxtPwd = Md5Builder.getMd5(password);
					registerThread();
				}
				
				
			}
		});
	}
	
	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void registerThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						register();
					}
				});
		mThread.start();
	}

	/**
	 * 请求数据，并将返回结果显示在界面上
	 */
	private void register() {

		String url = "appRegister.app";

		Message msg = new Message();
		try {
			// ClassID=1&UserName=wzp&UserPwd=wzp&UserTxtPwd=加密密码&E_Mail=wzpym1983@126.com
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("ClassID", "1"));
			params.add(new BasicNameValuePair("UserName", username));
			params.add(new BasicNameValuePair("UserPwd", password));
			params.add(new BasicNameValuePair("UserTxtPwd", userTxtPwd));
			params.add(new BasicNameValuePair("E_Mail", email));// 没填写邮箱的时候传空值
			response = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(response)) {
				RegisterActivity.this.sendExceptionMsg(response);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(response);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");

				String resultCode = resultJsonObject.getString("resultCode");// resultCode = "0" 代表注册成功
				
				if("0".equals(resultCode)){// resultCode = "0" 代表注册成功
					userId = resultJsonObject.getString("userid");// 注册完之后的userid
					SharePreferencesUtils.setSharedStringData("userName",username);
					SharePreferencesUtils.setSharedStringData("password",password);
					SharePreferencesUtils.setSharedStringData("userTxtPwd",userTxtPwd);
					SharePreferencesUtils.setSharedStringData("userId",userId);
					SharePreferencesUtils.setSharedBooleanData("isLogin",true);
					if(!StringUtil.isBlank(email))SharePreferencesUtils.setSharedStringData("email",email);
				}
				
				Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
//				setResult(RESULT_OK);
//				finish();
				
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				RegisterActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			RegisterActivity.this.sendExceptionMsg(err);
		}
	}


}
