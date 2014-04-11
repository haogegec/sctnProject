package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
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
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.DatePickerView;

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

	private RelativeLayout profession;
	private TextView professionValue;
	private String professionStr = "";// 专业
	private String professionId = "";

	private EditText technologyValue;
	private String technologyStr = "";// 专业职称

	private RelativeLayout aidprofession;
	private TextView aidprofessionValue;
	private String aidprofessionStr = "";// 辅助专业
	private String aidprofessionId = "";

	private RelativeLayout computerlevel;
	private TextView computerlevelValue;
	private String computerlevelStr = "";// 微机水平
	private String computerlevelId = "";
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

	private String firstLanguage = "";
	private String firstLanguageLevel = "";
	private String firstLanguageId = "";
	private String firstLanguageLevelId = "";

	private String secondLanguage = "";
	private String secondLanguageLevel = "";
	private String secondLanguageId = "";
	private String secondLanguageLevelId = "";

	private Builder builder;
	private Dialog dialog;// 弹出框

	private String result;// 服务端返回的结果

	private long userId;

	private HashMap<String, String> educationExperienceMap;// 基本信息

	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> newEducationExperienceMap = new HashMap<String, String>();// 基本信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.education_experience_edit_activity);
		setTitleBar(getString(R.string.EducationExperienceEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);

		initIntent();
		initAllView();
		reigesterAllEvent();
		// initForeignLanguageThread();
	}

	protected void initIntent() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		userId = SharePreferencesUtils.getSharedlongData("userId");
		if (bundle != null && bundle.getSerializable("educationExperienceList") != null) {
			List<HashMap<String, String>> basicInfoList = (List<HashMap<String, String>>) bundle.getSerializable("educationExperienceList");
			educationExperienceMap = basicInfoList.get(0);
		}
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

		profession = (RelativeLayout) findViewById(R.id.profession);
		professionValue = (TextView) findViewById(R.id.profession_value);

		technologyValue = (EditText) findViewById(R.id.technology_value);

		aidprofession = (RelativeLayout) findViewById(R.id.aidprofession);
		aidprofessionValue = (TextView) findViewById(R.id.aidprofession_value);

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

		if (educationExperienceMap != null && educationExperienceMap.size() != 0) {

			if (educationExperienceMap.containsKey("辅助专业")) {
				aidprofessionStr = educationExperienceMap.get("辅助专业");
				aidprofessionValue.setText(aidprofessionStr);
			}
			if (educationExperienceMap.containsKey("微机水平")) {
				computerlevelStr = educationExperienceMap.get("微机水平");
				computerlevelValue.setText(computerlevelStr);
			}
			if (educationExperienceMap.containsKey("学位")) {
				degreeStr = educationExperienceMap.get("学位");
				degreeValue.setText(degreeStr);
			}
			if (educationExperienceMap.containsKey("学位证号")) {
				degreecertStr = educationExperienceMap.get("学位证号");
				degreecertValue.setText(degreecertStr);
			}
			if (educationExperienceMap.containsKey("学历")) {
				degree = educationExperienceMap.get("学历");
				educationValue.setText(degree);
			}
			if (educationExperienceMap.containsKey("毕业证号")) {
				graduatedcodeStr = educationExperienceMap.get("毕业证号");
				graduatedcodeValue.setText(graduatedcodeStr);
			}
			if (educationExperienceMap.containsKey("毕业日期")) {
				graduateddateStr = educationExperienceMap.get("毕业日期");
				graduateddateValue.setText(graduateddateStr);
			}
			if (educationExperienceMap.containsKey("毕业学校")) {
				graduatedschoolStr = educationExperienceMap.get("毕业学校");
				graduatedschoolValue.setText(graduatedschoolStr);
			}
			if (educationExperienceMap.containsKey("第一外语")) {
				firstLanguage = educationExperienceMap.get("第一外语");
				oneenglishValue.setText(firstLanguage);
			}
			if (educationExperienceMap.containsKey("第一外语水平")) {
				firstLanguageLevel = educationExperienceMap.get("第一外语水平");
				oneenglishlevelValue.setText(firstLanguageLevel);
			}
			if (educationExperienceMap.containsKey("第二外语")) {
				secondLanguage = educationExperienceMap.get("第二外语");
				twoenglishValue.setText(secondLanguage);
			}
			if (educationExperienceMap.containsKey("第二外语水平")) {
				secondLanguageLevel = educationExperienceMap.get("第二外语水平");
				twoenglishlevelValue.setText(secondLanguageLevel);
			}
			if (educationExperienceMap.containsKey("专业")) {
				professionStr = educationExperienceMap.get("专业");
				professionValue.setText(professionStr);
			}
			if (educationExperienceMap.containsKey("专业职称")) {
				technologyStr = educationExperienceMap.get("专业职称");
				technologyValue.setText(technologyStr);
			}

		}

	}

	@Override
	protected void reigesterAllEvent() {

		// 确定按钮
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (aidprofessionStr.equals(aidprofessionValue.getText().toString()) && computerlevelStr.equals(computerlevelValue.getText().toString()) && degreeStr.equals(degreeValue.getText().toString()) && degreecertStr.equals(degreecertValue.getText().toString())
						&& degree.equals(educationValue.getText().toString()) && graduatedcodeStr.equals(graduatedcodeValue.getText().toString()) && graduateddateStr.equals(graduateddateValue.getText().toString()) && graduatedschoolStr.equals(graduatedschoolValue.getText().toString())
						&& firstLanguage.equals(oneenglishValue.getText().toString()) && firstLanguageLevel.equals(oneenglishlevelValue.getText().toString()) && secondLanguage.equals(twoenglishValue.getText().toString()) && secondLanguageLevel.equals(twoenglishlevelValue.getText().toString())
						&& professionStr.equals(professionValue.getText().toString()) && technologyStr.equals(technologyValue.getText().toString())) {

					Toast.makeText(getApplicationContext(), "请编辑之后再保存吧~~", Toast.LENGTH_SHORT).show();

				} else {
					requestDataThread();
				}

			}
		});

		graduateddate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DatePickerView datePickerView = new DatePickerView(EducationExperienceEditActivity.this, new DatePickerView.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						/* 把设置修改后的日期赋值给我的年、月、日变量 */
						mYear = year;
						mMonth = monthOfYear;
						mDay = dayOfMonth;
						/* 显示设置后的日期 */
						loadDate();
					}
				}, currYear, currMonth, currDay);
				datePickerView.myShow();

			}

		});

		// 学历
		education.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// showProcessDialog(false);
				// Thread mThread = new Thread(new Runnable() {// 启动新的线程，
				// @Override
				// public void run() {
				// initDegreeThread();
				// }
				// });
				// mThread.start();

				Intent intent = new Intent(EducationExperienceEditActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "Degree");
				startActivityForResult(intent, Constant.DEGREE);

			}

		});

		// 专业
		profession.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(EducationExperienceEditActivity.this, SelectCurrentIndustryActivity.class);
				intent.putExtra("flag", "education");
				startActivityForResult(intent, 1);

			}

		});

		// 辅助专业
		aidprofession.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(EducationExperienceEditActivity.this, SelectCurrentIndustryActivity.class);
				intent.putExtra("flag", "education");
				startActivityForResult(intent, 2);

			}

		});

		// 微机水平
		computerlevel.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// showProcessDialog(false);
				// Thread mThread = new Thread(new Runnable() {// 启动新的线程，
				// @Override
				// public void run() {
				// initComputerLevelThread();
				// }
				// });
				// mThread.start();

				Intent intent = new Intent(EducationExperienceEditActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "ComputerLevel");
				startActivityForResult(intent, Constant.COMPUTER_LEVEL);

			}

		});

		// 第一外语
		oneenglish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// initForeignLanguage();

				Intent intent = new Intent(EducationExperienceEditActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "SelectFirstLanguage");
				startActivityForResult(intent, Constant.FIRST_LANGUAGE);
			}

		});

		// 第一外语水平
		oneenglishlevel.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// showProcessDialog(false);
				// Thread mThread = new Thread(new Runnable() {// 启动新的线程，
				// @Override
				// public void run() {
				// initLanguageLevelThread();
				// }
				// });
				// mThread.start();

				Intent intent = new Intent(EducationExperienceEditActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "SelectLanguageLevel");
				startActivityForResult(intent, Constant.FIRST_LANGUAGE_LEVEL);

			}

		});

		// 第二外语
		twoenglish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EducationExperienceEditActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "SelectFirstLanguage");
				startActivityForResult(intent, Constant.SECOND_LANGUAGE);
			}

		});

		// 第二外语水平
		twoenglishlevel.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(EducationExperienceEditActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "SelectLanguageLevel");
				startActivityForResult(intent, Constant.SECOND_LANGUAGE_LEVEL);

			}

		});
	}

	// private void initForeignLanguageThread() {
	// showProcessDialog(false);
	// Thread mThread = new Thread(new Runnable() {// 启动新的线程，
	// @Override
	// public void run() {
	// initLanguageThread();
	// }
	// });
	// mThread.start();
	// }

	// private void initLanguageThread() {
	// String url = "appCmbShow.app";
	// Message msg = new Message();
	//
	// try {
	// List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("type", "6"));
	// params.add(new BasicNameValuePair("key", "1"));
	// result = getPostHttpContent(url, params);
	//
	// if (StringUtil.isExcetionInfo(result)) {
	// sendExceptionMsg(result);
	// return;
	// }
	//
	// JSONObject responseJsonObject = new JSONObject(result);
	//
	// if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果
	//
	// JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
	// Iterator it = resultJsonObject.keys();
	// foreignLanguageIds = new String[resultJsonObject.length()];
	// foreignLanguage = new String[resultJsonObject.length()];
	// int i = 0;
	// while (it.hasNext()) {
	// String key = (String) it.next();
	// String value = resultJsonObject.getString(key);
	// foreignLanguageIds[i] = key;
	// foreignLanguage[i] = value;
	// i++;
	// }
	// msg.what = Constant.FIRST_FOREIGN_LANGUAGE;
	// handler.sendMessage(msg);
	// } else {
	// String errorResult = (String) responseJsonObject.get("result");
	// String err = StringUtil.getAppException4MOS(errorResult);
	// sendExceptionMsg(err);
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// private void initForeignLanguage() {
	// builder.setTitle("请选择您的第一外语");
	// builder.setSingleChoiceItems(foreignLanguage, 0, new
	// DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// oneenglishValue.setText(foreignLanguage[which]);
	// firstLanguageId = foreignLanguageIds[which];
	// firstLanguage = foreignLanguage[which];
	// dialog.dismiss();
	// }
	//
	// });
	// dialog = builder.create();
	// dialog.show();
	// }

	// /**
	// * 请求数据，获取学历
	// */
	// private void initDegreeThread() {
	//
	// String url = "appCmbShow.app";
	// Message msg = new Message();
	//
	// try {
	//
	// List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("type", "11"));
	// params.add(new BasicNameValuePair("key", "1"));
	// result = getPostHttpContent(url, params);
	//
	// if (StringUtil.isExcetionInfo(result)) {
	// sendExceptionMsg(result);
	// return;
	// }
	//
	// JSONObject responseJsonObject = new JSONObject(result);
	//
	// if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果
	//
	// JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
	// Iterator it = resultJsonObject.keys();
	// degreeIds = new String[resultJsonObject.length()];
	// degrees = new String[resultJsonObject.length()];
	// int i = 0;
	// while (it.hasNext()) {
	// String key = (String) it.next();
	// String value = resultJsonObject.getString(key);
	// degreeIds[i] = key;
	// degrees[i] = value;
	// i++;
	// }
	//
	// msg.what = Constant.DEGREE;
	// handler.sendMessage(msg);
	// } else {
	// String errorResult = (String) responseJsonObject.get("result");
	// String err = StringUtil.getAppException4MOS(errorResult);
	// sendExceptionMsg(err);
	// }
	//
	// } catch (JSONException e) {
	// String err = StringUtil.getAppException4MOS("解析json出错！");
	// sendExceptionMsg(err);
	// }
	// }

	// private void initComputerLevelThread() {
	// String url = "appCmbShow.app";
	// Message msg = new Message();
	//
	// try {
	//
	// List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("type", "18"));
	// params.add(new BasicNameValuePair("key", "1"));
	// result = getPostHttpContent(url, params);
	//
	// if (StringUtil.isExcetionInfo(result)) {
	// sendExceptionMsg(result);
	// return;
	// }
	//
	// JSONObject responseJsonObject = new JSONObject(result);
	//
	// if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果
	//
	// JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
	// Iterator it = resultJsonObject.keys();
	// computerlevelIds = new String[resultJsonObject.length()];
	// computerlevels = new String[resultJsonObject.length()];
	// int i = 0;
	// while (it.hasNext()) {
	// String key = (String) it.next();
	// String value = resultJsonObject.getString(key);
	// computerlevelIds[i] = key;
	// computerlevels[i] = value;
	// i++;
	// }
	//
	// msg.what = Constant.COMPUTER_LEVEL;
	// handler.sendMessage(msg);
	// } else {
	// String errorResult = (String) responseJsonObject.get("result");
	// String err = StringUtil.getAppException4MOS(errorResult);
	// sendExceptionMsg(err);
	// }
	//
	// } catch (JSONException e) {
	// String err = StringUtil.getAppException4MOS("解析json出错！");
	// sendExceptionMsg(err);
	// }
	// }

	// private void initLanguageLevelThread() {
	//
	// String url = "appCmbShow.app";
	// Message msg = new Message();
	//
	// try {
	// List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("type", "7"));
	// params.add(new BasicNameValuePair("key", "1"));
	// result = getPostHttpContent(url, params);
	//
	// if (StringUtil.isExcetionInfo(result)) {
	// sendExceptionMsg(result);
	// return;
	// }
	//
	// JSONObject responseJsonObject = new JSONObject(result);
	//
	// if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果
	//
	// JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
	// Iterator it = resultJsonObject.keys();
	// languageLevelIds = new String[resultJsonObject.length()];
	// languageLevels = new String[resultJsonObject.length()];
	// int i = 0;
	// while (it.hasNext()) {
	// String key = (String) it.next();
	// String value = resultJsonObject.getString(key);
	// languageLevelIds[i] = key;
	// languageLevels[i] = value;
	// i++;
	// }
	// msg.what = Constant.LANGUAGE_LEVEL;
	// handler.sendMessage(msg);
	// } else {
	// String errorResult = (String) responseJsonObject.get("result");
	// String err = StringUtil.getAppException4MOS(errorResult);
	// EducationExperienceEditActivity.this.sendExceptionMsg(err);
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case Constant.DEGREE:
			// initDegree();
			// break;
			// case Constant.FIRST_FOREIGN_LANGUAGE:
			// // initForeignLanguage();
			// break;
			//
			// case Constant.LANGUAGE_LEVEL:
			// initLanguageLevel();
			// break;
			//
			// case Constant.COMPUTER_LEVEL:
			// initComputerLevel();
			// break;
			case 00:
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("list", list);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
				break;
			}
			closeProcessDialog();
		}
	};

	// /**
	// * 请求完数据，更新界面的数据
	// */
	// private void initDegree() {
	//
	// builder.setTitle("请选择您的学历");
	// builder.setSingleChoiceItems(degrees, 0, new
	// DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// educationValue.setText(degrees[which]);
	// degree = degrees[which];
	// degreeId = degreeIds[which];
	// dialog.dismiss();
	// }
	//
	// });
	// dialog = builder.create();
	// dialog.show();
	// }
	//
	// private void initComputerLevel() {
	//
	// builder.setTitle("请选择微机水平");
	// builder.setSingleChoiceItems(computerlevels, 0, new
	// DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// computerlevelValue.setText(computerlevels[which]);
	// computerlevelId = computerlevelIds[which];
	// dialog.dismiss();
	// }
	//
	// });
	// dialog = builder.create();
	// dialog.show();
	// }
	//
	// private void initLanguageLevel() {
	// builder.setTitle("请选择您的外语能力");
	// builder.setSingleChoiceItems(languageLevels, 0, new
	// DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// oneenglishlevelValue.setText(languageLevels[which]);
	// firstLanguageLevelId = languageLevelIds[which];
	// firstLanguageLevel = languageLevels[which];
	// dialog.dismiss();
	// }
	//
	// });
	// dialog = builder.create();
	// dialog.show();
	// }

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

	/**
	 * 请求数据线程
	 * 
	 */
	private void requestDataThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestData();
					}
				});
		mThread.start();
	}

	private void requestData() {

		String url = "appPersonInfo!modify.app";

		Message msg = new Message();

		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("Userid", userId + ""));
		// params.add(new BasicNameValuePair("Userid",100020+""));
		params.add(new BasicNameValuePair("GraduatedSchool", graduatedschoolValue.getText().toString()));
		params.add(new BasicNameValuePair("GraduatedCode", graduatedcodeValue.getText().toString()));
		params.add(new BasicNameValuePair("Technology", technologyValue.getText().toString()));
		params.add(new BasicNameValuePair("GraduatedDate", graduateddateValue.getText().toString()));
		params.add(new BasicNameValuePair("DegreeCert", degreecertValue.getText().toString()));
		params.add(new BasicNameValuePair("Degree", degreeValue.getText().toString()));

		if (educationExperienceMap == null || !educationExperienceMap.containsKey(" ")) {
			params.add(new BasicNameValuePair("RecContent", " "));
		}
		if (!firstLanguageId.equals("")) {
			params.add(new BasicNameValuePair("Oneenglish", firstLanguageId));
		}
		if (!secondLanguageId.equals("")) {

			params.add(new BasicNameValuePair("Twoenglish", secondLanguageId));
		}
		if (!firstLanguageLevelId.equals("")) {
			params.add(new BasicNameValuePair("OneLevel", firstLanguageLevelId));
		}
		if (!secondLanguageLevelId.equals("")) {
			params.add(new BasicNameValuePair("TwoLevel", secondLanguageLevelId));
		}
		if (!computerlevelId.equals("")) {
			params.add(new BasicNameValuePair("ComputerLevel", computerlevelId));
		}
		if (!degreeId.equals("")) {
			params.add(new BasicNameValuePair("Education", degreeId));
		}
		if (!professionId.equals("")) {
			params.add(new BasicNameValuePair("profession", professionId));
		}
		if (!aidprofessionId.equals("")) {
			params.add(new BasicNameValuePair("Aidprofession", aidprofessionId));
		}
		params.add(new BasicNameValuePair("modifytype", "0"));// 保存到简历表中

		result = getPostHttpContent(url, params);
		newEducationExperienceMap.put("辅助专业", aidprofessionValue.getText().toString());
		newEducationExperienceMap.put("微机水平", computerlevelValue.getText().toString());
		newEducationExperienceMap.put("学位", degreeValue.getText().toString());
		newEducationExperienceMap.put("学位证号", degreecertValue.getText().toString());
		newEducationExperienceMap.put("学历", educationValue.getText().toString());
		newEducationExperienceMap.put("毕业证号", graduatedcodeValue.getText().toString());
		newEducationExperienceMap.put("毕业日期", graduateddateValue.getText().toString());
		newEducationExperienceMap.put("毕业学校", graduatedschoolValue.getText().toString());
		newEducationExperienceMap.put("第一外语", oneenglishValue.getText().toString());
		newEducationExperienceMap.put("第一外语水平", oneenglishlevelValue.getText().toString());
		newEducationExperienceMap.put("第一外语水平", oneenglishlevelValue.getText().toString());
		newEducationExperienceMap.put("第二外语", twoenglishValue.getText().toString());
		newEducationExperienceMap.put("专业", professionValue.getText().toString());
		newEducationExperienceMap.put("专业职称", technologyValue.getText().toString());
		list.add(newEducationExperienceMap);
		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			sendExceptionMsg(result);
			return;
		}

		JSONObject responseJsonObject;
		try {
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.get("resultCode").toString().equals("0")) {
				msg.what = 00;
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				// professionStr = data.getStringExtra("currentIndustry");
				professionId = data.getStringExtra("currentIndustryId");
				professionValue.setText(data.getStringExtra("currentIndustry"));
				break;

			case 2:
				// aidprofessionStr = data.getStringExtra("currentIndustry");
				aidprofessionId = data.getStringExtra("currentIndustryId");
				aidprofessionValue.setText(data.getStringExtra("currentIndustry"));

				break;

			case Constant.DEGREE:
				educationValue.setText(data.getStringExtra("degree"));
				degree = data.getStringExtra("degree");
				degreeId = data.getStringExtra("degreeId");

				break;

			case Constant.COMPUTER_LEVEL:
				computerlevelValue.setText(data.getStringExtra("computerLevel"));
				computerlevelId = data.getStringExtra("computerLevelId");
				computerlevelStr = data.getStringExtra("computerLevel");
				break;

			case Constant.FIRST_LANGUAGE:
				oneenglishValue.setText(data.getStringExtra("language"));
				firstLanguageId = data.getStringExtra("languageId");
				firstLanguage = data.getStringExtra("language");
				break;

			case Constant.FIRST_LANGUAGE_LEVEL:
				oneenglishlevelValue.setText(data.getStringExtra("languageLevel"));
				firstLanguageLevelId = data.getStringExtra("languageLevelId");
				firstLanguageLevel = data.getStringExtra("languageLevel");
				break;

			case Constant.SECOND_LANGUAGE:
				twoenglishValue.setText(data.getStringExtra("language"));
				secondLanguageId = data.getStringExtra("languageId");
				secondLanguage = data.getStringExtra("language");
				break;

			case Constant.SECOND_LANGUAGE_LEVEL:
				twoenglishlevelValue.setText(data.getStringExtra("languageLevel"));
				secondLanguageLevelId = data.getStringExtra("languageLevelId");
				secondLanguageLevel = data.getStringExtra("languageLevel");
				break;
			}
		}
	}

}
