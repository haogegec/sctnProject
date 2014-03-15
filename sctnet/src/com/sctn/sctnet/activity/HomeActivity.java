package com.sctn.sctnet.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.entity.LoginInfo;

public class HomeActivity extends BaicActivity {
	private EditText search_edit;
	private ImageView resume_manage_click;
	private ImageView job_search_click;
	private ImageView work_direction_click;
	private ImageView salary_survey_click;
	private ImageView rem_meeting_click;
	private ImageView infomation_query_click;
	private ImageView document_query_click;
	private ImageView function_more_click;
	private ImageView person_centre_click;
	private ImageView personalCenterImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		setTitleBar(getString(R.string.homeActivityTitle), View.GONE, View.VISIBLE);

		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);
		search_edit = (EditText) findViewById(R.id.search_edit_bg);
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

		super.titleRightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});

		search_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, WorkSearchActivity.class);
				startActivity(intent);
			}
		});

		resume_manage_click.setOnClickListener(new OnClickListener() {
			// 简历管理
			@Override
			public void onClick(View v) {
				
				if (LoginInfo.isLogin()) {
					Intent intent = new Intent(HomeActivity.this, ResumeManageActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
					startActivityForResult(intent,Constant.LOGIN_RESUME_MANAGE_ACTIVITY);
				}
			}
		});

		job_search_click.setOnClickListener(new OnClickListener() {
			// 职位搜索
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, JobSearchActivity.class);
				startActivity(intent);
			}
		});

		work_direction_click.setOnClickListener(new OnClickListener() {
			// 办事指南
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, WorkDirectionActivity.class);
				startActivity(intent);
			}
		});

		salary_survey_click.setOnClickListener(new OnClickListener() {
			// 薪资调查
			@Override
			public void onClick(View v) {
				
				if (LoginInfo.isLogin()) {
					Intent intent = new Intent(HomeActivity.this, SalarySurveyActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
					startActivityForResult(intent,Constant.LOGIN_SALARY_SURVEY_ACTIVITY);
				}
				
			}
		});

		rem_meeting_click.setOnClickListener(new OnClickListener() {
			// 招聘会现场
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, RecruitmentActivity.class);
				startActivity(intent);
			}
		});

		infomation_query_click.setOnClickListener(new OnClickListener() {
			// 信息咨询
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, InformationQueryActivity.class);
				startActivity(intent);
			}
		});

		document_query_click.setOnClickListener(new OnClickListener() {
			// 档案查询
			@Override
			public void onClick(View v) {
			}
		});

		function_more_click.setOnClickListener(new OnClickListener() {
			// 更多
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, MoreActivity.class);
				startActivity(intent);
			}
		});

		person_centre_click.setOnClickListener(new OnClickListener() {
			// 个人中心
			@Override
			public void onClick(View v) {
				if (LoginInfo.isLogin()) {
					Intent intent = new Intent(HomeActivity.this, PersonalCenterActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
					startActivityForResult(intent,Constant.LOGIN_PERSONAL_CENTER_ACTIVITY);
				}
			}
		});

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case Constant.LOGIN_PERSONAL_CENTER_ACTIVITY : {
					startActivity(new Intent(HomeActivity.this,PersonalCenterActivity.class));
					break;
				}
				case Constant.LOGIN_RESUME_MANAGE_ACTIVITY : {
					startActivity(new Intent(HomeActivity.this,ResumeManageActivity.class));
					break;
				}
				case Constant.LOGIN_SALARY_SURVEY_ACTIVITY : {
					startActivity(new Intent(HomeActivity.this,SalarySurveyActivity.class));
					break;
				}
				
				
			}
		}
	}

}
