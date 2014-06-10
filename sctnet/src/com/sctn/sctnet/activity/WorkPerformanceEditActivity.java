package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;

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
		Intent intent = this.getIntent();
		workPerformanceEditStr = intent.getStringExtra("workperformanceStr");
		initAllView();
		reigesterAllEvent();
	}
	
	@Override
	protected void initAllView() {

		workPerformanceEdit = (EditText) findViewById(R.id.workperformance_edit);
		workPerformanceEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5000)});
		workPerformanceEdit.setText(workPerformanceEditStr);
	}

	@Override
	protected void reigesterAllEvent() {
		
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(StringUtil.isBlank(workPerformanceEdit.getText().toString())||workPerformanceEditStr.equals(workPerformanceEdit.getText().toString())){
					
					Toast.makeText(getApplicationContext(), "请编辑之后再保存吧~~", Toast.LENGTH_SHORT).show();
				} else if(StringUtil.hasSpecialCharacters(workPerformanceEdit.getText().toString())){// 校验有无恶意字符
					Toast.makeText(getApplicationContext(), "请不要输入特殊字符", Toast.LENGTH_SHORT).show();
				} else{
					Intent intent = new Intent();
					intent.putExtra("workperformanceStr", workPerformanceEdit.getText().toString());
					setResult(RESULT_OK,intent);
					finish();
				}
				
			}
		});
		
	}

}
