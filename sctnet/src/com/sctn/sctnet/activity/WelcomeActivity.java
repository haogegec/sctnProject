package com.sctn.sctnet.activity;

import com.sctn.sctnet.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * 欢迎页
 * @author wanghaoc
 *
 */
public class WelcomeActivity extends Activity {
	
	private boolean isFirstIn = true;
	private SharedPreferences sharePreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome_activity);
		isFirstLode();
		init();
	}
	
	/**
	 * 初始化页面
	 * @author wanghaoc
	 */
	public void init() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2500);
					if(isFirstIn){
						goHome();
					}else{
						goGuide();
					}
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 跳转到主页
	 * @author wanghaoc
	 */
	private void goHome() {
		isFirstIn = false;
		startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
	}

	/**
	 * 引导页
	 * @author wanghaoc
	 */
	private void goGuide() {
		startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
	}
	
	/**
	 *判断是否是第一次登陆系统
	 * @author wanghaoc
	 */
	private void isFirstLode(){
		sharePreferences=PreferenceManager.getDefaultSharedPreferences(this);
		isFirstIn = sharePreferences.getBoolean("isFirstRun", true);
	}
}
