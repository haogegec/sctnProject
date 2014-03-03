package com.sctn.sctnet.activity;

import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sctn.sctnet.R;

public class EducationExperienceEditActivity extends BaicActivity{
	
	private EditText graduatedschoolValue;
	private String graduatedschoolStr = "";//毕业学校
	
	private RelativeLayout graduateddate;
	private TextView graduateddateValue;
	private String graduateddateStr = "";//毕业日期
	private String graduateddateDate;
	
	private EditText graduatedcodeValue;
	private String graduatedcodeStr = "";//毕业证号
	
	private RelativeLayout education;
	private TextView educationValue;
	private String educationStr = "";//学历
	
	private EditText degreeValue;
	private String degreeStr = "";//学位
	
	private EditText degreecertValue;
	private String degreecertStr = "";//学位证号
	
	private EditText professionValue;
	private String professionStr = "";//专业
	
	private EditText technologyValue;
	private String technologyStr = "";//专业职称
	
	private EditText aidprofessionValue;
	private String aidprofessionStr = "";//辅助专业
	
	private RelativeLayout computerlevel;
	private TextView computerlevelValue;
	private String computerlevelStr = "";//微机水平
	
	private EditText oneenglishValue;
	private String oneenglishStr = "";//第一 外语
	
	private RelativeLayout oneenglishlevel;
	private TextView oneenglishlevelValue;
	private String oneenglishlevelStr = "";//第二外语水平
	
	private EditText twoenglishValue;
	private String twoenglishStr = "";//第一 外语
	
	private RelativeLayout twoenglishlevel;
	private TextView twoenglishlevelValue;
	private String twoenglishlevelStr = "";//第二外语水平
	
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
	
	private String[] educationDialogText = {"初中","高中","中专","大专","本科","硕士","MBA","博士"};// 学历
	private String[] levelDialogText = {"二级","三级","四级","六级","八级"};// 外语水平
	
	private Builder builder;
	private Dialog dialog;//弹出框
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.education_experience_edit_activity);
		setTitleBar(getString(R.string.EducationExperienceEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		graduatedschoolValue = (EditText) findViewById(R.id.graduatedschool_value);
		
		graduateddate = (RelativeLayout) findViewById(R.id.graduateddate);
		graduateddateValue = (TextView) findViewById(R.id.graduateddate_value);
		
		graduatedcodeValue = (EditText) findViewById(R.id.graduatedcode_value);
		
		education = (RelativeLayout) findViewById(R.id.education);
		educationValue = (TextView) findViewById(R.id.education_value);
		
		degreeValue = (EditText) findViewById(R.id.degree_value);
		
		degreecertValue = (EditText) findViewById(R.id.degreecert_value);
		
		professionValue = (EditText) findViewById(R.id.profession_value);
		
		technologyValue = (EditText) findViewById(R.id.technology_value);
		
		aidprofessionValue = (EditText) findViewById(R.id.aidprofession_value);
				
		computerlevel = (RelativeLayout) findViewById(R.id.computerlevel);
		computerlevelValue = (TextView) findViewById(R.id.computerlevel_value);
		
		oneenglishValue = (EditText) findViewById(R.id.oneenglish_value);
		
		oneenglishlevel = (RelativeLayout) findViewById(R.id.oneenglishlevel);
		oneenglishlevelValue = (TextView) findViewById(R.id.oneenglish_value);
		
        twoenglishValue = (EditText) findViewById(R.id.twoenglish_value);
		
		twoenglishlevel = (RelativeLayout) findViewById(R.id.twoenglishlevel);
		twoenglishlevelValue = (TextView) findViewById(R.id.twoenglish_value);
		
		builder = new AlertDialog.Builder(EducationExperienceEditActivity.this);
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		graduateddate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {				
				// 弹出年月日时间选择框
				datePickerDialog = new DatePickerDialog(
						EducationExperienceEditActivity.this, myDateSetListener,
						mYear, mMonth, mDay);
				/* 显示出日期设置对话框 */
				datePickerDialog.show();
				
			}
			
		});
		
		education.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择学历");
				builder.setSingleChoiceItems(educationDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						educationValue.setText(educationDialogText[which]);
						dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
				
			}
			
		});
		
		computerlevel.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择微机水平");
				builder.setSingleChoiceItems(levelDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						computerlevelValue.setText(levelDialogText[which]);
						dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
				
			}
			
		});
		
		oneenglishlevel.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择学历");
				builder.setSingleChoiceItems(levelDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						oneenglishlevelValue.setText(levelDialogText[which]);
						dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
				
			}
			
		});
		
		twoenglishlevel.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择学历");
				builder.setSingleChoiceItems(levelDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						twoenglishlevelValue.setText(levelDialogText[which]);
						dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
				
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
		graduateddateDate = dateStringBuilder.toString();
		graduateddateValue.setText(graduateddateDate);
		dateStringBuilder = new StringBuffer();
	}

}
