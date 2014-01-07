package com.sctn.sctnet.activity;

import com.sctn.sctnet.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

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
		loadGuideInfo();
		Start();
	}

	public void Start() {
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

	private void goHome() {
		isFirstIn = false;
		startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
	}

	private void goGuide() {
		startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
	}

	private void loadGuideInfo(){
		sharePreferences=PreferenceManager.getDefaultSharedPreferences(this);
		isFirstIn = sharePreferences.getBoolean("isFirstRun", true);
	}
}
