package com.sctn.sctnet.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.PushUtil;
import com.sctn.sctnet.contants.Constant;

/**
 * 欢迎页
 * 
 * @author wanghaoc
 * 
 */
public class WelcomeActivity extends InstrumentedActivity {

	private boolean isFirstIn;
	private SharedPreferences preferences;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	// 推送用的变量=========================================
	public static boolean isForeground = false;// =======
	private MessageReceiver mMessageReceiver;// =========

	// =================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome_activity);
		isFirstLode();
		init();
		
		Toast.makeText(getApplicationContext(), JPushInterface.getRegistrationID(this), Toast.LENGTH_LONG).show();
		System.out.println("ID = "+JPushInterface.getRegistrationID(this));
	}

	/**
	 * 根据参数来决定跳转到那个页面
	 * 
	 * @author wanghaoc
	 */
	public void init() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2500);
					if (isFirstIn) {
						changeLodePref();
						goGuide();
					} else {
						goHome();
					}
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	/**
	 * 修改登陸參數
	 * 
	 * @author wanghaoc
	 */
	private void changeLodePref() {
		Editor edit = preferences.edit();// 获得编辑器
		edit.putBoolean("isFirstRun", false);// 这个地方一定要分开写，否则失效。
		edit.commit();
	}

	/**
	 * 引导页
	 * 
	 * @author wanghaoc
	 */
	private void goGuide() {
		startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
	}

	/**
	 * 跳转到主页
	 * 
	 * @author wanghaoc
	 */
	private void goHome() {
		startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
	}

	/**
	 * 判断是否是第一次登陆系统并设置参数
	 * 
	 * @author wanghaoc
	 */
	private void isFirstLode() {
		preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("isFirstRun", true);
	}

	// for receive customer msg from jpush server
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constant.MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(Constant.KEY_MESSAGE);
				String extras = intent.getStringExtra(Constant.KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(Constant.KEY_MESSAGE + " : " + messge + "\n");
				if (!PushUtil.isEmpty(extras)) {
					showMsg.append(Constant.KEY_EXTRAS + " : " + extras + "\n");
				}
			}
		}
	}
	
	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}


	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}
}
