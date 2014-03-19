package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.contants.Constant;
/**
 * 编辑职业生涯
 * @author xueweiwei
 *
 */
public class WorkExperienceEditActivity extends BaicActivity{

	private EditText companynameValue;
	private String companynameStr = "";//当前公司
	
	private RelativeLayout currentprofessional;
	private TextView currentprofessionalValue;//当前从事行业
	
	private RelativeLayout adminpost;
	private TextView adminpostValue;//当前从事职业
	
	private EditText workexperienceValue;
	private String workexperienceValueStr = "";//工作年限
	
	private RelativeLayout workperformance;
	private TextView workperformanceValue;
	private String workperformanceStr = "";//工作经验
	
	private String currentIndustryId;// 目前就职的行业的ID
	private String currentIndustry;// 目前就职的行业
	private String jobId;// 目前的职位类别ID
	private String job;// 目前的职位类别
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_experience_edit_activity);
		setTitleBar(getString(R.string.WorkExperienceEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		companynameValue = (EditText) findViewById(R.id.companyname_value);
		
		currentprofessional = (RelativeLayout) findViewById(R.id.currentprofessional);
		currentprofessionalValue = (TextView) findViewById(R.id.currentprofessional_value);
		
		adminpost = (RelativeLayout) findViewById(R.id.adminpost);
		adminpostValue = (TextView) findViewById(R.id.adminpost_value);
		
		workexperienceValue = (EditText) findViewById(R.id.workexperience_value);
		
		workperformance = (RelativeLayout) findViewById(R.id.workperformance);
		workperformanceValue = (TextView) findViewById(R.id.workperformance_value);
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 当前从事的行业
		currentprofessional.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(WorkExperienceEditActivity.this, SelectCurrentIndustryActivity.class);
				intent.putExtra("flag", "WorkExperienceEditActivity");
				startActivityForResult(intent, Constant.CURRENT_INDUSTRY_REQUEST_CODE);
				
			}
			
		});
		
		// 当前从事的职业
		adminpost.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(WorkExperienceEditActivity.this, SelectJobActivity.class);
				intent.putExtra("flag", "WorkExperienceEditActivity");
				startActivityForResult(intent, Constant.JOB_REQUEST_CODE);
				
			}
			
		});

		workperformance.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorkExperienceEditActivity.this,WorkPerformanceEditActivity.class);
				startActivity(intent);
			}
			
		});
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

				case Constant.CURRENT_INDUSTRY_REQUEST_CODE: 
					currentIndustry = data.getStringExtra("currentIndustry");
					currentIndustryId = data.getStringExtra("currentIndustryId");
					currentprofessionalValue.setText(currentIndustry);
				break;
				case Constant.JOB_REQUEST_CODE: 
					jobId = data.getStringExtra("jobId");
					job = data.getStringExtra("job");
					adminpostValue.setText(job);
					break;
			

			}
		}
	}

}
