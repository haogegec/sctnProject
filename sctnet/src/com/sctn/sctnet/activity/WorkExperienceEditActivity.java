package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sctn.sctnet.R;
/**
 * 编辑职业生涯
 * @author xueweiwei
 *
 */
public class WorkExperienceEditActivity extends BaicActivity{

	private EditText companynameValue;
	private String companynameStr = "";//当前公司
	
	private RelativeLayout currentprofessional;
	private TextView currentprofessionalValue;
	private String currentprofessionalStr = "";//当前从事行业
	
	private RelativeLayout adminpost;
	private TextView adminpostValue;
	private String adminpostStr = "";//当前从事职业
	
	private EditText workexperienceValue;
	private String workexperienceValueStr = "";//工作年限
	
	private RelativeLayout workperformance;
	private TextView workperformanceValue;
	private String workperformanceStr = "";//工作经验
	
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

		workperformance.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorkExperienceEditActivity.this,WorkPerformanceEditActivity.class);
				startActivity(intent);
			}
			
		});
		
	}

}
