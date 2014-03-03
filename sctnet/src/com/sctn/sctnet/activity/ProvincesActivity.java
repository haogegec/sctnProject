package com.sctn.sctnet.activity;

import android.os.Bundle;
import android.view.View;

import com.sctn.sctnet.R;

/**
 * 全国省份
 * @author xueweiwei
 *
 */
public class ProvincesActivity extends BaicActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_area_listview);
		setTitleBar(getString(R.string.ProvincesActivityTitle), View.VISIBLE, View.GONE);	
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
