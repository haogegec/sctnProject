package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sctn.sctnet.R;

/**
 * 参加招聘会公司简介
 * @author xueweiwei
 *
 */
public class RecruitmentCompanyDetailActivity extends BaicActivity{

	private String companyInfo;
	private String companyName;
	
	private TextView companyDetailText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_detail);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		companyInfo = bundle.getString("companyInfo");
		companyName = bundle.getString("companyName");
		
		setTitleBar(companyName, View.VISIBLE, View.GONE);
		
		initAllView();
		reigesterAllEvent();
	}
	
	@Override
	protected void initAllView() {
		companyDetailText = (TextView) findViewById(R.id.information_detail_text);
		companyDetailText.setTextColor(R.color.black);
		companyDetailText.setText(companyInfo);
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}

}
