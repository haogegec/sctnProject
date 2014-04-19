package com.sctn.sctnet.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.Md5Builder;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
/**
 * 登陆界面
 * @author xueweiwei
 *
 */
public class LoginActivity extends BaicActivity {

	private SharedPreferences sharedPreferences;
	private Button loginBtn;
	private EditText etUserName;
	private EditText etPassword;
//	private CheckBox rememberPassword;
//	private CheckBox autoLogin;
//	private boolean isRememberPassword;
//	private boolean isAutoLogin;
	private boolean isLogin;
	private String userName;
	private String password;
	private Button registerBtn;
	private Button forgetPasswordBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		setTitleBar(getString(R.string.loginActivityTitle), View.VISIBLE, View.GONE);
		initAllView();
		loadLoginInfo();
		reigesterAllEvent();
	}
	/**
	 * 登录验证并处理缓存
	 */
	public void login() {
		
		String url = "appLogin.app";

		Message msg = new Message();
		
		String response="";
		sctnApp.setReLogin(false);
		JSONObject responseJsonObject = null;
		userName = etUserName.getText().toString();
		password = etPassword.getText().toString();
		
		if(StringUtil.isBlank(userName)&&StringUtil.isBlank(password)) {
			
			response = StringUtil.getAppException4MOS("用户名和密码不能为空！");
			LoginActivity.this.sendExceptionMsg(response);
			return;
		}else if(StringUtil.isBlank(userName)) {
			
			response = StringUtil.getAppException4MOS("用户名不能为空！");
			LoginActivity.this.sendExceptionMsg(response);
			return;
			
		}else if(StringUtil.isBlank(password)) {
			response = StringUtil.getAppException4MOS("密码不能为空！");
			LoginActivity.this.sendExceptionMsg(response);
			return;
		}
		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("UserName", userName));
			params.add(new BasicNameValuePair("UserPwd", Md5Builder.getMd5(password)));
			response = getPostHttpContent(url, params);
			
			if (StringUtil.isExcetionInfo(response)) {
				LoginActivity.this.sendExceptionMsg(response);
				return;
			}
			
			if (StringUtil.isBlank(response)) {
				response = StringUtil.getAppException4MOS("未获得服务器响应结果！");
				LoginActivity.this.sendExceptionMsg(response);
				return;
			}
			// JSON的解析过程
			responseJsonObject = new JSONObject(response);
			if (responseJsonObject.getInt("resultCode")==0) {//获得响应结果，登录成功                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             

				long userId = responseJsonObject.getLong("userid");
				
				if(responseJsonObject.get("tag").equals("0")){
					SharePreferencesUtils.setSharedBooleanData(userId+"", true);
				}else{
					SharePreferencesUtils.setSharedBooleanData(userId+"", false);
				}
				
				Editor editor = sharedPreferences.edit();
				editor.putString("userName", userName);
				editor.putString("password", password);
				editor.putLong("userId", userId);
				editor.putBoolean("isLogin", true);
//				if (isAutoLogin) {
//					editor.putBoolean("isRememberPassword", isRememberPassword);
//					editor.putBoolean("isAutoLogin", isAutoLogin);
//				} else if(isRememberPassword){
//					editor.putBoolean("isRememberPassword", isRememberPassword);
//				}
				editor.commit();
				setResult(RESULT_OK);
				finish();
			}else{
				String result = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(result);
				LoginActivity.this.sendExceptionMsg(err);
			}
		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析JSON出错！");
			LoginActivity.this.sendExceptionMsg(err);
			return;
		}
	}

	/**
	 * 加载用户名密码等登录信息
	 */
	public void loadLoginInfo() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		userName = sharedPreferences.getString("userName", "");
		password = sharedPreferences.getString("password", "");
//		isRememberPassword = sharedPreferences.getBoolean("isRememberPassword",	false);
//		isAutoLogin = sharedPreferences.getBoolean("isAutoLogin", false);
		isLogin = sharedPreferences.getBoolean("isLogin", false);
		
		// 把用户名写到用户名输入框里
		etUserName.setText(userName);
		
//		if(isRememberPassword) {
//			etUserName.setText(userName);
//			if(!StringUtil.isBlank(password)) {
//				etPassword.setText("*******");
//			}
//		}else {
//			etUserName.setText(userName);			
//			etPassword.setText("");
//			
//		}
		
//		rememberPassword.setChecked(isRememberPassword);
//		autoLogin.setChecked(isAutoLogin);
	}
	
	/**
	 * 在子线程与远端服务器交互，验证登录
	 */
	private void loginInTread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						login();
					}
				});
		mThread.start();
	}
	
	
	@Override
	protected void initAllView() {
		loginBtn = (Button) findViewById(R.id.login_btn_login);
		etUserName = (EditText) findViewById(R.id.login_edit_username);
		etPassword = (EditText) findViewById(R.id.login_edit_password);
//		rememberPassword = (CheckBox) findViewById(R.id.login_remember_password);
//		autoLogin = (CheckBox) findViewById(R.id.login_auto_login);
		registerBtn = (Button) findViewById(R.id.register);
		forgetPasswordBtn = (Button) findViewById(R.id.forget_password);
	}

	@Override
	protected void reigesterAllEvent() {
		loginBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				loginInTread();
			}

		});

		/*rememberPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							isRememberPassword = true;
						} else {
							isRememberPassword = false;
							autoLogin.setChecked(false);
						}
					}
				});

		autoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					isAutoLogin = true;
					rememberPassword.setChecked(true);
				} else {
					isAutoLogin = false;
				}
			}
		});*/
		
		etPassword.setOnClickListener(new EditText.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				etPassword.selectAll();
				
			}
		});
		
//		etPassword.setOnTouchListener(new EditText.OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				etPassword.requestFocus();
//				return false;
//			}
//		});
		etUserName.setOnClickListener(new EditText.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etUserName.setFocusable(true);
//				etUserName.selectAll();
				
			}
		});
		
		etUserName.setOnTouchListener(new EditText.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) { 
					etUserName.setFocusableInTouchMode(true);
	                return false; 
	                // onTouch事件里包括：click,long click,down,up,(还有个move) ; true 表示当前事件被消耗，不再传递给其他事件了；false 表示当前事件没被消耗，还会传递给其他事件；
	                // 比如，在这里return false，说明当前down事件还没被消耗，执行完down事件后会触发click事件。
	                // 如果 return true，表示当前down事件被消耗，则不会触发click事件。
	                // touch事件触发顺序：down,click,up,long click
	            }
				
				return false;
				
			}
		});
		
		registerBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivityForResult(intent, Constant.REGISTER_REQUEST_CODE);
			}
			
		});
		forgetPasswordBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
				startActivityForResult(intent, Constant.FORGET_PASSWORD_REQUEST_CODE);
			}
			
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case Constant.REGISTER_REQUEST_CODE : {
					setResult(RESULT_OK);
					finish();
					break;
				}
				case Constant.FORGET_PASSWORD_REQUEST_CODE:{
					Toast.makeText(LoginActivity.this, "您的密码已发送到您注册的邮箱里，请查收后登陆！", Toast.LENGTH_LONG).show();
					break;
				}
			}
		}
	}

}
