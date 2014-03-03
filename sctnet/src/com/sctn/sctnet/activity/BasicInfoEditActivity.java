package com.sctn.sctnet.activity;

import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sctn.sctnet.R;

/**
 *  编辑基本信息页面
 * @author xueweiwei
 *
 */
public class BasicInfoEditActivity extends BaicActivity{
	
	private EditText nameValue;
	private String nameStr = "";// 姓名
	
	private RadioButton mail;
	private RadioButton female;
	private String sex = "";//性别
	
	private RelativeLayout orgin;
	private TextView orginValue;// 籍贯
	
	private RelativeLayout birthday;
	private TextView birthdayValue;
	private String birthdayStr = "";//出生日期
	private String birthdayDate;
	
	private RelativeLayout people;
	private TextView peopleValue;
	private String peopleStr = "";//民族
	
	private RelativeLayout political;
	private TextView politicalValue;
	private String politicalStr = "";//政治面貌
	
	private EditText heighValue;
	private String heighStr = "";//身高
	
	private RelativeLayout maritalStatus;
	private TextView maritalStatusValue;
	private String maritalStatusStr = "";//婚姻状况
	
	private RelativeLayout healthStatus;
	private TextView healthStatusValue;
	private String healthStatusStr = "";//健康状况
	
	private RelativeLayout accountCity;
	private TextView accountCityValue;
	private String accountCityStr = "";//户口所在地
	
	private RelativeLayout currentCity;
	private TextView currentCityValue;
	private String currentCityStr = "";//居住地
	
	private EditText cardIdValue;
	private String cardIdStr = "";//身份证号
	
	private EditText driveCodeValue;
	private String driveCodeStr = "";//驾驶证号
	
	private EditText addressValue;
	private String addressStr = "";//地址
	
	private Builder builder;
	private Dialog dialog;//弹出框
	
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
	
	private String[] peopleDialogText = {"汉族","少数民族"};// 民族
	private String[] politicalDialogText = {"群众","团员","党员"};// 政治面貌
	private String[] maritalStatusDialogText = {"未婚","已婚","离婚"};// 婚姻状况
	private String[] healthStatusDialogText = {"健康","亚健康","残疾"};// 健康状况
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_basic_info_activity);
		setTitleBar(getString(R.string.BasicInfoEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);	
		initAllView();
		reigesterAllEvent();
	}
	@Override
	protected void initAllView() {
		
		nameValue = (EditText) findViewById(R.id.name_value);
		
		mail = (RadioButton) findViewById(R.id.mail);
		female = (RadioButton) findViewById(R.id.female);
		
		orgin = (RelativeLayout) findViewById(R.id.orgin);
		orginValue = (TextView) findViewById(R.id.origin_value);
		
		birthday = (RelativeLayout) findViewById(R.id.birthday);
		birthdayValue = (TextView) findViewById(R.id.birthday_value);
		
		people = (RelativeLayout) findViewById(R.id.people);
		peopleValue = (TextView) findViewById(R.id.people_value);
		
		political = (RelativeLayout) findViewById(R.id.political);
		politicalValue = (TextView) findViewById(R.id.political_value);
		
		heighValue = (EditText) findViewById(R.id.heigh_value);
		
		maritalStatus = (RelativeLayout) findViewById(R.id.marital_status);
		maritalStatusValue = (TextView) findViewById(R.id.marital_status_value);
		
		healthStatus = (RelativeLayout) findViewById(R.id.health_status);
		healthStatusValue = (TextView) findViewById(R.id.health_status_value);
		
		accountCity = (RelativeLayout) findViewById(R.id.account_city);
		accountCityValue = (TextView) findViewById(R.id.account_city_value);
		
		currentCity = (RelativeLayout) findViewById(R.id.current_city);
		currentCityValue = (TextView) findViewById(R.id.current_city_value);
		
		cardIdValue = (EditText) findViewById(R.id.cardid_value);
		
		driveCodeValue = (EditText) findViewById(R.id.drivecode_value);
		
		addressValue = (EditText) findViewById(R.id.address_value);
		builder = new AlertDialog.Builder(BasicInfoEditActivity.this);
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		birthday.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {				
				// 弹出年月日时间选择框
				datePickerDialog = new DatePickerDialog(
						BasicInfoEditActivity.this, myDateSetListener,
						mYear, mMonth, mDay);
				/* 显示出日期设置对话框 */
				datePickerDialog.show();
				
			}
			
		});
		
		people.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择您的民族");
				builder.setSingleChoiceItems(peopleDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						peopleValue.setText(peopleDialogText[which]);
						dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
				
			}
			
		});
		
		political.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择您的政治面貌");
				builder.setSingleChoiceItems(politicalDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						politicalValue.setText(politicalDialogText[which]);
						dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
				
			}
			
		});
		
		maritalStatus.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择您的婚姻状况");
				builder.setSingleChoiceItems(maritalStatusDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						maritalStatusValue.setText(maritalStatusDialogText[which]);
						dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
				
			}
			
		});
		
		healthStatus.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择您的民族");
				builder.setSingleChoiceItems(healthStatusDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						healthStatusValue.setText(healthStatusDialogText[which]);
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
		birthdayDate = dateStringBuilder.toString();
		birthdayValue.setText(birthdayDate);
		dateStringBuilder = new StringBuffer();
	}

}
