package com.sctn.sctnet.activity;

import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sctn.sctnet.R;

/**
 * 档案查询页面
 * @author xueweiwei
 *
 */
public class ArchivesQueryActivity extends BaicActivity{
	
	private EditText nameEdit;
	private RelativeLayout birthdayLayout;
	private TextView birthdayValue;
	private String nameStr;
	private String birthdayStr;
	private Button searchBtn;
	private String birthdayDate;
	/* 定义程序用到的UI元素对象:日历设置器 */
	private DatePickerDialog datePickerDialog;
	/* 定义日历对象，初始化时，用来获取当前时间 */
	private Calendar mCalendar = Calendar.getInstance(Locale.CHINA);/*
																	 * 从Calendar抽象基类获得实例对象
																	 * ，并设置成中国时区
																	 */
	/* 从日历对象中获取当前的：年、月、日 */
	private int currYear = mCalendar.get(Calendar.YEAR);
	private int currMonth = mCalendar.get(Calendar.MONTH);
	private int currDay = mCalendar.get(Calendar.DAY_OF_MONTH);

	private int mYear = currYear;
	private int mMonth = currMonth;
	private int mDay = currDay;
	private StringBuffer dateStringBuilder = new StringBuffer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.archives_query_activity);
		setTitleBar(getString(R.string.ArchivesQueryActivityTitle), View.VISIBLE, View.GONE);
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		nameEdit = (EditText) findViewById(R.id.true_name_edit);
		birthdayLayout = (RelativeLayout) findViewById(R.id.birthday);
		birthdayValue = (TextView) findViewById(R.id.birthday_value);
		searchBtn = (Button) findViewById(R.id.search_btn);
	}

	@Override
	protected void reigesterAllEvent() {
		
		birthdayLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// 弹出年月日时间选择框
				datePickerDialog = new DatePickerDialog(
						ArchivesQueryActivity.this, myDateSetListener,
						mYear, mMonth, mDay);
				/* 显示出日期设置对话框 */
				datePickerDialog.show();
				
			}
			
		});
		searchBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ArchivesQueryActivity.this,ArchivesListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("trueName", nameEdit.getText().toString());
				bundle.putString("birthday", birthdayDate);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});
	}
	
	private OnDateSetListener myDateSetListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			/* 把设置修改后的日期赋值给我的年、月、日变量 */
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			/* 显示设置后的日期 */
			loadDate();
		}
	};
	
	/* 设置年月日日期时间显示 */
	private void loadDate() {
		dateStringBuilder.append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay);
		birthdayDate = dateStringBuilder.toString();
		birthdayValue.setText(birthdayDate);
		dateStringBuilder = new StringBuffer();
	}

}
