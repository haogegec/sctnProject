package com.sctn.sctnet.activity;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sctn.sctnet.R;
import com.sctn.sctnet.cache.CacheProcess;

/**
 * 当用户没有简历时，点击简历管理时弹出的界面
 * @author xueweiwei
 *
 */
public class ResumeCreateActivity extends BaicActivity{

	private ArrayList<HashMap<String, String>> jobIntentionList = new ArrayList<HashMap<String, String>>();// 求职意向
	private Button createBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_create_activity);
		setTitleBar(getString(R.string.ResumeCreateActivityTitle),
				View.VISIBLE, View.GONE);
		
		
		initIntent();
		initAllView();
		reigesterAllEvent();

	}
	
	private void initIntent(){
//		Bundle bundle = getIntent().getExtras();
//		jobIntentionList = (ArrayList<HashMap<String, String>>)bundle.getSerializable("jobIntentionList");
	}
	
	@Override
	protected void initAllView() {
		
		createBtn = (Button) findViewById(R.id.create_btn);
		
	}
	@Override
	protected void reigesterAllEvent() {
		
		createBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(ResumeCreateActivity.this,ResumeEditActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("whichActivity", "ResumeCreate");
//				bundle.putString("flagId", jobIntentionList.get(0).get("flagId"));
//				bundle.putSerializable("jobIntentionList", jobIntentionList);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
			
		});
		
	}

}
