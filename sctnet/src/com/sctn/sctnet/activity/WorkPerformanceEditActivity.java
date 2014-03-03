package com.sctn.sctnet.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.sctn.sctnet.R;

/**
 * 编辑工作业绩
 * @author xueweiwei
 *
 */
public class WorkPerformanceEditActivity extends BaicActivity{

	private EditText workPerformanceEdit;
	private String workPerformanceEditStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_performance_edit_activity);
		setTitleBar(getString(R.string.WorkPerformanceEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		
		initAllView();
		reigesterAllEvent();
	}
	
	@Override
	protected void initAllView() {

		workPerformanceEdit = (EditText) findViewById(R.id.workperformance_edit);
		workPerformanceEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5000)});
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}

}
