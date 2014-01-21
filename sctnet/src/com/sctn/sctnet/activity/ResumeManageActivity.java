package com.sctn.sctnet.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sctn.sctnet.R;
/**
 * 简历管理界面
 * @author xueweiwei
 *
 */
public class ResumeManageActivity extends BaicActivity{
	
	private ImageView modifyImg;
	private TextView modifyTextView;
	private int darkBlueColor = Color.parseColor("#00008b");
	private int blueColor = Color.parseColor("#0b98e0");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_manager_activity);
		setTitleBar(getString(R.string.resumeManageActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		// TODO Auto-generated method stub
		modifyImg = (ImageView) findViewById(R.id.resumeModifyImg);
		modifyTextView = (TextView) findViewById(R.id.resumeModifyText);
		
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		modifyImg.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				toResumeEditActivity();
			}
			
		});
		modifyTextView.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(modifyTextView.isPressed()){
					modifyTextView.setTextColor(darkBlueColor);
				}else{
					modifyTextView.setTextColor(blueColor);
				}
				toResumeEditActivity();
			}
			
		});
		
	}

	//跳转到简历修改界面
	private void toResumeEditActivity(){
		Intent intent = new Intent(ResumeManageActivity.this,
				ResumeEditActivity.class);
		startActivity(intent);
		modifyTextView.setTextColor(blueColor);
	}
}
