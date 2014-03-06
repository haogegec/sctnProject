package com.sctn.sctnet.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
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
	private CheckBox rememberPassword;
	private CheckBox autoLogin;
	private boolean isRememberPassword;
	private boolean isAutoLogin;
	private boolean isLogin;
	private String userName;
	private String password;
	
	
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
		String response="";
		sctnApp.setReLogin(false);
		JSONObject responseJsonObject = null;
		userName = etUserName.getText().toString();
		password = etPassword.getText().toString();
		
		if(StringUtil.isBlank(userName)&&StringUtil.isBlank(password)) {
			Toast.makeText(getApplicationContext(), "用户名和密码不能为空！",
					Toast.LENGTH_SHORT).show();
			return;
		}else if(StringUtil.isBlank(userName)) {
			
			Toast.makeText(getApplicationContext(), "用户名不能为空！",
					Toast.LENGTH_SHORT).show();
			return;
		}else if(StringUtil.isBlank(password)) {
			Toast.makeText(getApplicationContext(), "密码不能为空！",
					Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			JSONObject requestJsonObject = new JSONObject();
			requestJsonObject.put("userName", userName);
			requestJsonObject.put("password", password);
			//获取响应的结果信息
//			response = getPostHttpContent("appLogin.app?method=execute", requestJsonObject.toString());
			
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

				long userId = responseJsonObject.getJSONObject("result").getLong("userid");
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
				Editor editor = sharedPreferences.edit();
				editor.putString("userName", userName);
				editor.putString("password", password);
				editor.putLong("userId", userId);
				editor.putBoolean("isLogin", true);
				if (isAutoLogin) {
					editor.putBoolean("isRememberPassword", isRememberPassword);
					editor.putBoolean("isAutoLogin", isAutoLogin);
				} else if(isRememberPassword){
					editor.putBoolean("isRememberPassword", isRememberPassword);
				}
				Intent intent = new Intent();
				setResult(RESULT_OK,intent);
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
		isRememberPassword = sharedPreferences.getBoolean("isRememberPassword",	false);
		isAutoLogin = sharedPreferences.getBoolean("isAutoLogin", false);
		isLogin = sharedPreferences.getBoolean("isLogin", false);
		if(isRememberPassword) {
			etUserName.setText(userName);
			if(!StringUtil.isBlank(password)) {
				etPassword.setText("*******");
			}
		}else {
			etUserName.setText(userName);			
			etPassword.setText("");
			
		}
		
		rememberPassword.setChecked(isRememberPassword);
		autoLogin.setChecked(isAutoLogin);
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
		rememberPassword = (CheckBox) findViewById(R.id.login_remember_password);
		autoLogin = (CheckBox) findViewById(R.id.login_auto_login);
	}

	@Override
	protected void reigesterAllEvent() {
		loginBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				loginInTread();
			}

		});

		rememberPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
		});
		
		etPassword.setOnClickListener(new EditText.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				etPassword.selectAll();
				
			}
		});
		
		etPassword.setOnTouchListener(new EditText.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etPassword.requestFocus();
				return false;
			}
		});
		
		etUserName.setOnClickListener(new EditText.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				etUserName.selectAll();
				
			}
		});
		
		etUserName.setOnTouchListener(new EditText.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etUserName.requestFocus();
				return false;
			}
		});
	}

}
