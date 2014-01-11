package com.sctn.sctnet.activity;


import com.sctn.sctnet.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class HomeActivity extends BaicActivity {

	private ImageView personalCenterImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置标题栏为用户自定义标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		setTitleBar(getString(R.string.homeActivityTitle), View.GONE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);
		
		personalCenterImg = (ImageView) findViewById(R.id.person_center_img);
		
		personalCenterImg.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this,PersonalCenterActivity.class);
				startActivity(intent);
			}
			
		});
		
	}

}
