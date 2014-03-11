package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sctn.sctnet.R;

public class WorkDirectionDetailActivity extends BaicActivity{

	private String title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_detail);
		initBundle();
		initAllView();
		reigesterAllEvent();
	}
	private void initBundle(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("title");
	}
	@Override
	protected void initAllView() {
		
		setTitleBar(title, View.VISIBLE, View.GONE);
		
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}

}
