package com.sctn.sctnet.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sctn.sctnet.R;
/**
 * 关于界面
 * @author 姜勇男
 *
 */
public class AboutActivity extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_activity);
		initAllView();
		reigesterAllEvent();
	}
	protected void initAllView() {
		
	}

	protected void reigesterAllEvent() {
		
	}
}
