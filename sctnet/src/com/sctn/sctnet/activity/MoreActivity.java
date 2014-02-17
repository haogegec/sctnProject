package com.sctn.sctnet.activity;

import android.os.Bundle;
import android.view.View;

import com.sctn.sctnet.R;

/**
 * 更多界面
 * @author xueweiwei
 *
 */
public class MoreActivity extends BaicActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);
		setTitleBar(getString(R.string.moreActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);		
		initAllView();
		reigesterAllEvent();
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
