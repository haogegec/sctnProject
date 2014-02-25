package com.sctn.sctnet.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
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

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;

public class LoginActivity extends BaicActivity {

	private SharedPreferences sharedPreferences;
	private Button loginBtn;
	private EditText etUserName;
	private EditText etPassword;
	private CheckBox rememberPassword;
	private CheckBox autoLogin;
	private boolean isRememberPassword;
	private boolean isAutoLogin;
	private boolean isFirstRun = true;
	private String userName;
	private String password;
	private boolean isCiphertext = false;
	private AlarmManager am;
	private TextView register;// 注册按钮

//	private CacheProcess cacheProcess;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		setTitleBar(getString(R.string.loginActivityTitle), View.GONE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);
		
		initAllView();
//		cacheProcess = new CacheProcess();
		loadLoginInfo();
		if (isAutoLogin && !sctnApp.isReLogin()) {
			loginInTread();
		}
		reigesterAllEvent();
	}


	@Override
	protected void onDestroy() {
		closeProcessDialog();
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_HOME))
				&& event.getRepeatCount() == 0) {
			dialogExit();
		}
		return false;
	}

	/**
	 * 登录验证并处理缓存
	 */
	public void login() {
		String response="";
		sctnApp.setReLogin(false);
		JSONObject responseJsonObject = null;
		if(!isCiphertext) {
			userName = etUserName.getText().toString();
			password = etPassword.getText().toString();
		}
		if(StringUtil.isBlank(userName)&&StringUtil.isBlank(password)) {
//			LoginActivity.this.sendExceptionMsg(StringUtil.getAppException4MOS("用户名和密码不能为空！"));
			//toust用户名和密码不能为空
			return;
		}else if(StringUtil.isBlank(userName)) {
//			LoginActivity.this.sendExceptionMsg(StringUtil.getAppException4MOS("用户名不能为空！"));
			//用户名不能为空
			return;
		}else if(StringUtil.isBlank(password)) {
//			LoginActivity.this.sendExceptionMsg(StringUtil.getAppException4MOS("密码不能为空！"));
			//密码不能为空
			return;
		}
		try {
			JSONObject requestJsonObject = new JSONObject();
			requestJsonObject.put("userName", userName);
			requestJsonObject.put("password", password);
			requestJsonObject.put("isCiphertext", isCiphertext);
			//获取响应的结果信息
			response = getPostHttpContent("loginAction.do?method=spslogin4MOS", requestJsonObject.toString());
			
			if (StringUtil.isExcetionInfo(response)) {
//				LoginActivity.this.sendExceptionMsg(response);
				//tost  网络异常
				return;
			}
			Log.d("【Json】", response);
			
			if (StringUtil.isBlank(response)) {
				response = StringUtil.getAppException4MOS("未获得服务器响应结果！");
//				LoginActivity.this.sendExceptionMsg(response);
				//tost  未获得服务器响应结果！
				return;
			}
			// JSON的解析过程
			responseJsonObject = new JSONObject(response);
			if (responseJsonObject.has("suveJsonObject")) {//获得响应结果，登录成功                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
//				cacheProcess.initCache(mosApp, responseJsonObject);
				//add by xyc 20130521 缓存在mosApp中的数据可能由于Android操作系统的某种机制被回收，经常造成系统获取缓存对象为空，因此在获得服务器端传来的数据后，放到本地文件中
//				cacheProcess.initCacheInSharedPreferences(this, responseJsonObject);
//			    // 如果是第一次登录，则改变状态为false
//				if (isFirstRun) {
//					//如果是第一次登陆
//					isFirstRun = false;
////					startActivity(new Intent(LoginActivity.this, MosGuideActivity.class));
//				} else {
//					
//					startActivity(new Intent(LoginActivity.this, FuncNavActivity.class));
//				}
				if (isRememberPassword) {
					saveLoginInfo();
				} else {
					cleanLoginInfo();
				}
				finish();
			}
		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析JSON出错！");
//			LoginActivity.this.sendExceptionMsg(err);
			//tost  "解析JSON出错！
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
		System.out.println("passwordpasswordpassword="+password);
		isRememberPassword = sharedPreferences.getBoolean("isRememberPassword",	false);
		isAutoLogin = sharedPreferences.getBoolean("isAutoLogin", false);
		isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		if(isRememberPassword) {
			etUserName.setText(userName);
			if(!StringUtil.isBlank(password)) {
				isCiphertext = true;
				etPassword.setText("*******");
			}
		}else {
			etUserName.setText("");
			
				etPassword.setText("");
			
		}
		
		rememberPassword.setChecked(isRememberPassword);
		autoLogin.setChecked(isAutoLogin);
	}
	/**
	 * 保存用户名密码等登录信息
	 */
	public void saveLoginInfo() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	   // userName = mosApp.getSysUserExtendedMVO().getSysUserSVO().getSysUserName();
	    //password = mosApp.getSysUserExtendedMVO().getSysUserSVO().getPassword();
		Editor editor = sharedPreferences.edit();
//		editor.putString("userName", userName);
//		editor.putString("password", password);
		editor.putBoolean("isRememberPassword", isRememberPassword);
		editor.putBoolean("isAutoLogin", isAutoLogin);
		editor.commit();
	}

	public void cleanLoginInfo() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
//		editor.putString("userName", "");
//		editor.putString("password", "");
		editor.putBoolean("isRememberPassword", false);
		editor.putBoolean("isAutoLogin", false);
		editor.putBoolean("isFirstRun", isFirstRun);
		editor.commit();
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
		register = (TextView)findViewById(R.id.register);
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
				if(isCiphertext) {
					isCiphertext = false;
					etPassword.setText("");
				}else {
					etPassword.selectAll();
				}
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
				if(isCiphertext) {
					isCiphertext = false;
					etUserName.setText("");
					etPassword.setText("");
				}else {
					etUserName.selectAll();
				}
			}
		});
		
		etUserName.setOnTouchListener(new EditText.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etUserName.requestFocus();
				return false;
			}
		});
		
		// 注册
		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
			
		});
		
	}

}
