package com.sctn.sctnet.activity;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
/**
 * 关于界面
 * @author 姜勇男
 *
 */
public class AboutActivity extends Activity{
	
	private TextView versionText;
	String versionName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_activity);
		initAllView();
		reigesterAllEvent();
	}
	protected void initAllView() {
		versionText = (TextView) findViewById(R.id.version_text);
		try {
			versionName = getApplicationContext().getPackageManager().getPackageInfo("com.sctn.sctnet", 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String versionNameText = "V"+versionName;
		versionText.setText(versionNameText);
	}

	protected void reigesterAllEvent() {
		
	}
}
