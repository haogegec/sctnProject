package com.sctn.sctnet.activity;

import android.os.Bundle;
import android.view.View;

import com.sctn.sctnet.R;
/**
 * 简历管理界面
 * @author xueweiwei
 *
 */
public class ResumeManageActivity extends BaicActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_manager_activity);
		setTitleBar(getString(R.string.resumeManageActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		
		
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
