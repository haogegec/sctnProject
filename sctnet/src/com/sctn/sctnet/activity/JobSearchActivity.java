package com.sctn.sctnet.activity;

import com.sctn.sctnet.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class JobSearchActivity extends BaicActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_search_activity);
		setTitleBar(getString(R.string.jobsearchTitle), View.GONE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);
		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
	}

	@Override
	protected void reigesterAllEvent() {
		
	}
			
}
