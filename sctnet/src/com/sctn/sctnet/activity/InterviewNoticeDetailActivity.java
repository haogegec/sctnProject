package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.ItemView;

/**
 * @author wanghaoc 职位搜索功能点
 * 
 */
public class InterviewNoticeDetailActivity extends BaicActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interview_notice_detail);
		setTitleBar(getString(R.string.interviewNotice), View.VISIBLE, View.GONE);

		initAllView();
		initIntent();
		reigesterAllEvent();
	}

	protected void initIntent() {
		
		

	}

	@Override
	protected void initAllView() {

	}

	@Override
	protected void reigesterAllEvent() {

		
//		search_btn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				Intent intent = new Intent(InterviewNoticeDetailActivity.this,JobListActivity.class);
//				startActivity(intent);
//				
//			}
//		});

	}

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case Constant.WORKINGAREA_REQUEST_CODE: {
					
					break;
				}
			}
		}
	}

}
