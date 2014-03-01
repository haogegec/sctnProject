package com.sctn.sctnet.activity;

import android.os.Bundle;
import android.view.View;

import com.sctn.sctnet.R;
import com.sctn.sctnet.cache.CacheProcess;

/**
 * 当用户没有简历时，点击简历管理时弹出的界面
 * @author xueweiwei
 *
 */
public class ResumeCreateActivity extends BaicActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_manager_activity);
		setTitleBar(getString(R.string.resumeManageActivityTitle),
				View.VISIBLE, View.VISIBLE);
		

	}
	
	@Override
	protected void initAllView() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}

}
