package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.ItemView;

/**
 * 编辑个人简介
 * @author xueweiwei
 *
 */
public class PersonalProfileEditActivity extends BaicActivity{

	private EditText reccontentEdit;
	private EditText specialtyContentEdit;
	private String reccontentStr;
	private String specialtyContentStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_profile_edit_activity);
		setTitleBar(getString(R.string.ProvincesActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		
		initAllView();
		reigesterAllEvent();
	}
	@Override
	protected void initAllView() {

		reccontentEdit = (EditText) findViewById(R.id.reccontent_edit);
		
		specialtyContentEdit = (EditText) findViewById(R.id.specialtyContent_edit);
		
		reccontentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
		specialtyContentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		super.getTitleRightButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//保存thread
			}
			
		});
	}

}
