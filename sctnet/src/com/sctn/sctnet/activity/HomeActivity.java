package com.sctn.sctnet.activity;


import com.sctn.sctnet.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class HomeActivity extends BaicActivity {
	private ImageView    searchBar;
	private ImageView	 resume_manage_click;
	private ImageView	 job_search_click;
	private ImageView	 work_direction_click;
	private ImageView	 salary_survey_click;
	private ImageView	 rem_meeting_click;
	private ImageView	 infomation_query_click;
	private ImageView	 document_query_click;
	private ImageView	 function_more_click;
	private ImageView	 person_centre_click;
	private ImageView personalCenterImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		setTitleBar(getString(R.string.homeActivityTitle), View.GONE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);
		
		initAllView();
		reigesterAllEvent();
		
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
	@Override
	protected void initAllView() {
	    
		 searchBar = (ImageView) findViewById(R.id.search_bar);
				 resume_manage_click = (ImageView) findViewById(R.id.resume_manage_img);
		    	 job_search_click = (ImageView) findViewById(R.id.job_search_img);
				 work_direction_click = (ImageView) findViewById(R.id.work_direction_img);
				 salary_survey_click = (ImageView) findViewById(R.id.salary_survey_img);
				 rem_meeting_click = (ImageView) findViewById(R.id.rem_meeting_img);
				 infomation_query_click = (ImageView) findViewById(R.id.infomation_query_img);
				 document_query_click = (ImageView) findViewById(R.id.document_query_img);
				 function_more_click = (ImageView) findViewById(R.id.function_more_img);
				 person_centre_click = (ImageView) findViewById(R.id.person_center_img);		
	}
	@Override
	protected void reigesterAllEvent() {
		 searchBar.setOnClickListener(new OnClickListener() {
				//搜索页面
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(HomeActivity.this,
							WorkSearchActivity.class);
					startActivity(intent);
				}
			});
	 
	
	resume_manage_click.setOnClickListener(new OnClickListener() {
		//简历管理
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this,
					JobSearchActivity.class);
			startActivity(intent);
		}
	});
	 
	 
	job_search_click.setOnClickListener(new OnClickListener() {
			//职位搜索
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						JobSearchActivity.class);
				startActivity(intent);
			}
		});
		 
	work_direction_click.setOnClickListener(new OnClickListener() {
			//办事指南
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(HomeActivity.this,
//						JobSearchActivity.class);
//				startActivity(intent);
			}
		});
		 
	salary_survey_click.setOnClickListener(new OnClickListener() {
			//薪资调查
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(HomeActivity.this,
//						JobSearchActivity.class);
//				startActivity(intent);
			}
		});
	
	rem_meeting_click.setOnClickListener(new OnClickListener() {
		//招聘会现场
		@Override
		public void onClick(View v) {
//			Intent intent = new Intent(HomeActivity.this,
//					JobSearchActivity.class);
//			startActivity(intent);
		}
	});
		 
	infomation_query_click.setOnClickListener(new OnClickListener() {
			//信息咨询
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(HomeActivity.this,
//						JobSearchActivity.class);
//				startActivity(intent);
			}
		});
		 
	document_query_click.setOnClickListener(new OnClickListener() {
			//档案查询
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(HomeActivity.this,
//						JobSearchActivity.class);
//				startActivity(intent);
			}
		});
		 
	function_more_click.setOnClickListener(new OnClickListener() {
			//更多
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(HomeActivity.this,
//						JobSearchActivity.class);
//				startActivity(intent);
			}
		});
		 
	person_centre_click.setOnClickListener(new OnClickListener() {
			//个人中心
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(HomeActivity.this,
//						JobSearchActivity.class);
//				startActivity(intent);
			}
		});
		
	}

}
