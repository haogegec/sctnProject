package com.sctn.sctnet.activity;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

public class EducationExperienceEditActivity extends BaicActivity {

	private EditText graduatedschoolValue;
	private String graduatedschoolStr = "";// 毕业学校

	private RelativeLayout graduateddate;
	private TextView graduateddateValue;
	private String graduateddateStr = "";// 毕业日期
	private String graduateddateDate;

	private EditText graduatedcodeValue;
	private String graduatedcodeStr = "";// 毕业证号

	private RelativeLayout education;
	private TextView educationValue;
	private String degree = "";// 学历
	private String degreeId = "";// 学历ID

	private EditText degreeValue;
	private String degreeStr = "";// 学位

	private EditText degreecertValue;
	private String degreecertStr = "";// 学位证号

	private EditText professionValue;
	private String professionStr = "";// 专业

	private EditText technologyValue;
	private String technologyStr = "";// 专业职称

	private EditText aidprofessionValue;
	private String aidprofessionStr = "";// 辅助专业

	private RelativeLayout computerlevel;
	private TextView computerlevelValue;
	private String computerlevelStr = "";// 微机水平
	private String[] computerlevels;
	private String[] computerlevelIds;

	private RelativeLayout oneenglish;
	private TextView oneenglishValue;// 第一 外语

	private RelativeLayout oneenglishlevel;
	private TextView oneenglishlevelValue;// 第一外语水平

	private RelativeLayout twoenglish;
	private TextView twoenglishValue;// 第二外语

	private RelativeLayout twoenglishlevel;
	private TextView twoenglishlevelValue;// 第二外语水平

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

	private String[] degrees;// 学历
	private String[] degreeIds;// 学历ID

	private String[] foreignLanguageIds;
	private String[] foreignLanguage;
	private String[] languageLevelIds;
	private String[] languageLevels;

	private String firstLanguage;
	private String firstLanguageLevel;
	private String firstLanguageId;
	private String firstLanguageLevelId;

	private String secondLanguage;
	private String secondLanguageLevel;
	private String secondLanguageId;
	private String secondLanguageLevelId;

	private Builder builder;
	private Dialog dialog;// 弹出框

	private String result;// 服务端返回的结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.education_experience_edit_activity);
		setTitleBar(getString(R.string.EducationExperienceEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);

		initAllView();
		reigesterAllEvent();
		initForeignLanguageThread();
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

		oneenglish = (RelativeLayout) findViewById(R.id.oneenglish);
		oneenglishValue = (TextView) findViewById(R.id.oneenglish_value);

		oneenglishlevel = (RelativeLayout) findViewById(R.id.oneenglishlevel);
		oneenglishlevelValue = (TextView) findViewById(R.id.oneenglishlevel_value);

		twoenglish = (RelativeLayout) findViewById(R.id.twoenglish);
		twoenglishValue = (TextView) findViewById(R.id.twoenglish_value);

		twoenglishlevel = (RelativeLayout) findViewById(R.id.twoenglishlevel);
		twoenglishlevelValue = (TextView) findViewById(R.id.twoenglishlevel_value);

		builder = new AlertDialog.Builder(EducationExperienceEditActivity.this);

	}

	@Override
	protected void reigesterAllEvent() {

		graduateddate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 弹出年月日时间选择框
				datePickerDialog = new DatePickerDialog(EducationExperienceEditActivity.this, myDateSetListener, mYear, mMonth, mDay);
				/* 显示出日期设置对话框 */
				datePickerDialog.show();

			}

		});

		// 学历
		education.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showProcessDialog(false);
				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
							@Override
							public void run() {
								initDegreeThread();
							}
						});
				mThread.start();

			}

		});

		// 微机水平
		computerlevel.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showProcessDialog(false);
				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
							@Override
							public void run() {
								initComputerLevelThread();
							}
						});
				mThread.start();

			}

		});

		oneenglish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initForeignLanguage();
			}

		});

		oneenglishlevel.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showProcessDialog(false);
				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
							@Override
							public void run() {
								initLanguageLevelThread();
							}
						});
				mThread.start();

			}

		});

		twoenglish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				builder.setTitle("请选择您的第二外语");
				builder.setSingleChoiceItems(foreignLanguage, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						twoenglishValue.setText(foreignLanguage[which]);
						secondLanguageId = foreignLanguageIds[which];
						secondLanguage = foreignLanguage[which];
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();
			}

		});

		twoenglishlevel.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择您的外语能力");
				builder.setSingleChoiceItems(languageLevels, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						twoenglishlevelValue.setText(languageLevels[which]);
						secondLanguageLevelId = languageLevelIds[which];
						secondLanguageLevel = languageLevels[which];
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});
	}

	private void initForeignLanguageThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						initLanguageThread();
					}
				});
		mThread.start();
	}

	private void initLanguageThread() {
		String url = "appCmbShow.app";
		Message msg = new Message();

		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("type", "6"));
			params.add(new BasicNameValuePair("key", "1"));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);

			if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果

				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
				Iterator it = resultJsonObject.keys();
				foreignLanguageIds = new String[resultJsonObject.length()];
				foreignLanguage = new String[resultJsonObject.length()];
				int i = 0;
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = resultJsonObject.getString(key);
					foreignLanguageIds[i] = key;
					foreignLanguage[i] = value;
					i++;
				}
				msg.what = Constant.FIRST_FOREIGN_LANGUAGE;
				handler.sendMessage(msg);
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				sendExceptionMsg(err);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initForeignLanguage() {
		builder.setTitle("请选择您的第一外语");
		builder.setSingleChoiceItems(foreignLanguage, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				oneenglishValue.setText(foreignLanguage[which]);
				firstLanguageId = foreignLanguageIds[which];
				firstLanguage = foreignLanguage[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 请求数据，获取学历
	 */
	private void initDegreeThread() {

		String url = "appCmbShow.app";
		Message msg = new Message();

		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("type", "11"));
			params.add(new BasicNameValuePair("key", "1"));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);

			if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果

				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
				Iterator it = resultJsonObject.keys();
				degreeIds = new String[resultJsonObject.length()];
				degrees = new String[resultJsonObject.length()];
				int i = 0;
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = resultJsonObject.getString(key);
					degreeIds[i] = key;
					degrees[i] = value;
					i++;
				}

				msg.what = Constant.DEGREE;
				handler.sendMessage(msg);
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			sendExceptionMsg(err);
		}
	}

	private void initComputerLevelThread() {
		String url = "appCmbShow.app";
		Message msg = new Message();

		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("type", "18"));
			params.add(new BasicNameValuePair("key", "1"));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);

			if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果

				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
				Iterator it = resultJsonObject.keys();
				computerlevelIds = new String[resultJsonObject.length()];
				computerlevels = new String[resultJsonObject.length()];
				int i = 0;
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = resultJsonObject.getString(key);
					computerlevelIds[i] = key;
					computerlevels[i] = value;
					i++;
				}

				msg.what = Constant.COMPUTER_LEVEL;
				handler.sendMessage(msg);
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			sendExceptionMsg(err);
		}
	}
	
	private void initLanguageLevelThread() {

		String url = "appCmbShow.app";
		Message msg = new Message();

		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("type", "7"));
			params.add(new BasicNameValuePair("key", "1"));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);

			if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果

				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
				Iterator it = resultJsonObject.keys();
				languageLevelIds = new String[resultJsonObject.length()];
				languageLevels = new String[resultJsonObject.length()];
				int i = 0;
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = resultJsonObject.getString(key);
					languageLevelIds[i] = key;
					languageLevels[i] = value;
					i++;
				}
				msg.what = Constant.LANGUAGE_LEVEL;
				handler.sendMessage(msg);
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				EducationExperienceEditActivity.this.sendExceptionMsg(err);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.DEGREE:
				initDegree();
				break;
			case Constant.FIRST_FOREIGN_LANGUAGE:
				// initForeignLanguage();
				break;

			case Constant.LANGUAGE_LEVEL:
				initLanguageLevel();
				break;
				
			case Constant.COMPUTER_LEVEL:
				initComputerLevel();
			}
			closeProcessDialog();
		}
	};

	/**
	 * 请求完数据，更新界面的数据
	 */
	private void initDegree() {

		builder.setTitle("请选择您的学历");
		builder.setSingleChoiceItems(degrees, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				educationValue.setText(degrees[which]);
				degree = degrees[which];
				degreeId = degreeIds[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}
	
	private void initComputerLevel() {

		builder.setTitle("请选择微机水平");
		builder.setSingleChoiceItems(computerlevels, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				computerlevelValue.setText(computerlevels[which]);
				computerlevelStr = computerlevelIds[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}
	
	private void initLanguageLevel() {
		builder.setTitle("请选择您的外语能力");
		builder.setSingleChoiceItems(languageLevels, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				oneenglishlevelValue.setText(languageLevels[which]);
				firstLanguageLevelId = languageLevelIds[which];
				firstLanguageLevel = languageLevels[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}

	private OnDateSetListener myDateSetListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
		dateStringBuilder.append(mYear).append("-").append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append((mDay < 10) ? "0" + mDay : mDay);
		graduateddateDate = dateStringBuilder.toString();
		graduateddateValue.setText(graduateddateDate);
		dateStringBuilder = new StringBuffer();
	}

}
