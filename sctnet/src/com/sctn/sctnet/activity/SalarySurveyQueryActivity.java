package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.sctn.sctnet.R;

/**
 * 薪酬查询页面
 * @author xueweiwei
 *
 */
public class SalarySurveyQueryActivity extends BaicActivity{
	
	private EditText salarySurvey_username;
	private EditText salarySurvey_password;
	private Button queryBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.salary_surver_query_activity);
		setTitleBar(getString(R.string.salarySurveyQuery), View.VISIBLE, View.GONE);
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		salarySurvey_username = (EditText) findViewById(R.id.salarySurvey_username);
		salarySurvey_password = (EditText) findViewById(R.id.salarySurvey_password);
		queryBtn = (Button) findViewById(R.id.btn_submit);
	}

	@Override
	protected void reigesterAllEvent() {
		
		queryBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(SalarySurveyQueryActivity.this,SalaryQueryListActivity.class);
				intent.putExtra("userId", salarySurvey_username.getText().toString());
				intent.putExtra("password", salarySurvey_password.getText().toString());
				startActivity(intent);
			}
			
		});
	}

	

}
