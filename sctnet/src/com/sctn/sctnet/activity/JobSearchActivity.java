package com.sctn.sctnet.activity;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.ItemView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author wanghaoc
 * 职位搜索功能点
 *
 */
public class JobSearchActivity extends BaicActivity {

	private ItemView searchitemView1;
	private ItemView searchitemView2;
	private ItemView searchitemView3;
	private ItemView searchitemView4;
	
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
		
		searchitemView1 = (ItemView) findViewById(R.id.itemview1);
		searchitemView1.setBackground(R.drawable.setting_na_bg_mid_pressed);
		searchitemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView1.setLabel("地区");
		searchitemView1.setValue("全国");
		searchitemView1.setDetailImageViewResource(R.drawable.detail);
		searchitemView1.setIconImageVisibility(View.GONE);
		
		searchitemView2 = (ItemView) findViewById(R.id.itemview2);
		searchitemView2.setBackground(R.drawable.setting_na_bg_mid_pressed);
		searchitemView2.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView2.setLabel("行业");
		searchitemView2.setDetailImageViewResource(R.drawable.detail);
		searchitemView2.setValue("所有行业");
		searchitemView2.setIconImageVisibility(View.GONE);
		
		searchitemView3 = (ItemView) findViewById(R.id.itemview3);
		searchitemView3.setBackground(R.drawable.setting_na_bg_mid_pressed);
		searchitemView3.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView3.setLabel("职能");
		searchitemView3.setValue("所有职能");
		searchitemView3.setDetailImageViewResource(R.drawable.detail);
		searchitemView3.setIconImageVisibility(View.GONE);
		
		searchitemView4 = (ItemView) findViewById(R.id.itemview4);
		searchitemView4.setBackground(R.drawable.setting_na_bg_mid_pressed);
		searchitemView4.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView4.setLabel("发布日期");
		searchitemView4.setValue("所有日期");
		searchitemView4.setDetailImageViewResource(R.drawable.detail);
		searchitemView4.setIconImageVisibility(View.GONE);
	}

	@Override
	protected void reigesterAllEvent() {
		
		searchitemView1.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
			}
		  });
				
			searchitemView2.getRelativeLayout().setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
			   //Intent intent = new Intent(JobSearchActivity.this,GuideActivity.class);
			  //startActivity(intent);				
				}
				
			});
			
			searchitemView3.getRelativeLayout().setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(JobSearchActivity.this,GuideActivity.class);
					startActivity(intent);				
				}
				
			});

			searchitemView4.getRelativeLayout().setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(JobSearchActivity.this,GuideActivity.class);
					startActivity(intent);				
				}
				
			});

	}
}
