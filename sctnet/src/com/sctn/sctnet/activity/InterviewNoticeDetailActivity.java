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
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.ItemView;

/**
 * 面试通知详情页面
 * 
 * @author 姜勇男 
 * 
 */
public class InterviewNoticeDetailActivity extends BaicActivity {
	
	private TextView tv_username;
	private TextView tv_jobsname;
	private TextView tv_interviewTime;
	private TextView tv_address;
	private TextView tv_contact;// 联系人
	private TextView tv_phone;
	
	private String username;
	private String jobsname;
	private String interviewTime;
	private String address;
	private String contact;
	private String phone;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interview_notice_detail);
		setTitleBar(getString(R.string.interviewNotice), View.VISIBLE, View.GONE);
		
		username = SharePreferencesUtils.getSharedStringData("userName");

		initAllView();
		initIntent();
//		reigesterAllEvent();
	}

	protected void initIntent() {
		Bundle bundle = getIntent().getExtras();
		jobsname = bundle.getString("jobsname");
		interviewTime = bundle.getString("interviewTime");
		address = bundle.getString("address");
		contact = bundle.getString("contact");
		phone = bundle.getString("phone");

		tv_username.setText(username);
		tv_jobsname.setText(jobsname);
		tv_interviewTime.setText(interviewTime);
		tv_address.setText(address);
		tv_contact.setText(contact);
		tv_phone.setText(phone);
	}

	@Override
	protected void initAllView() {
		tv_username = (TextView)findViewById(R.id.tv_username);
		tv_jobsname = (TextView)findViewById(R.id.tv_postName);
		tv_interviewTime = (TextView)findViewById(R.id.tv_interviewTime);
		tv_address = (TextView)findViewById(R.id.tv_address);
		tv_contact = (TextView)findViewById(R.id.tv_contact);
		tv_phone = (TextView)findViewById(R.id.tv_phone);
		
	}

	@Override
	protected void reigesterAllEvent() {
		
	}
}
