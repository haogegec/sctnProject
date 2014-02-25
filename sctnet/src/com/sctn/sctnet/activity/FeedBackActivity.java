package com.sctn.sctnet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sctn.sctnet.R;
/**
 * 用户反馈界面
 * @author 姜勇男
 *
 */
public class FeedBackActivity extends BaicActivity{
	
	Button btn_submit ;// 提交按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_activity);
		setTitleBar(getString(R.string.feedback), View.VISIBLE, View.GONE);
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		btn_submit = (Button)findViewById(R.id.btn_submit);
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 提交按钮
		btn_submit.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "提交", Toast.LENGTH_SHORT).show();
			}	
		});
		
		
	}
}
