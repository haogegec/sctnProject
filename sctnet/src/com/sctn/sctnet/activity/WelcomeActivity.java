package com.sctn.sctnet.activity;

import com.sctn.sctnet.R;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 欢迎页
 * @author wanghaoc
 *
 */
public class WelcomeActivity extends Activity {

	private boolean isFirstIn;
	private  SharedPreferences preferences ;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome_activity);
		isFirstLode();
		init();
	}

	/**
	 * 根据参数来决定跳转到那个页面
	 * @author wanghaoc
	 */
	public void init() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2500);
					if(isFirstIn){
						changeLodePref();
						goGuide();
					}else{
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
	 * @author wanghaoc
	 */
	private void changeLodePref() {
		Editor edit = preferences.edit();//获得编辑器  
		edit.putBoolean("isFirstRun", false);//这个地方一定要分开写，否则失效。
		edit.commit();
	}
	/**
	 *引导页
	 * @author wanghaoc
	 */
	private void goGuide() {
		startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
	}

	/**
	 *  跳转到主页
	 * @author wanghaoc
	 */
	private void goHome() {
		startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
	}

	/**
	 *判断是否是第一次登陆系统并设置参数
	 * @author wanghaoc
	 */
	private void isFirstLode(){
		preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("isFirstRun", true);
	}
}
