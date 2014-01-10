package com.sctn.sctnet.activity;


import com.sctn.sctnet.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class HomeActivity extends BaicActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置标题栏为用户自定义标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		setTitleBar(getString(R.string.homeActivityTitle), View.VISIBLE, View.VISIBLE);
		
		
		
	}

}
