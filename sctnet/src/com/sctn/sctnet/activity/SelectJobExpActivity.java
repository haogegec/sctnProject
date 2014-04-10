package com.sctn.sctnet.activity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.CustomDialog;

/**
 * 职场经历界面
 * 
 * @author 姜勇男
 * 
 */
public class SelectJobExpActivity extends BaicActivity {

	private RelativeLayout rl_workingArea;
	private TextView tv_workingArea2;// 工作地区

	private RelativeLayout rl_workingYears;
	private TextView tv_workingYears2;// 工龄

	private RelativeLayout rl_industryCategory;
	private TextView tv_industryCategory2;// 行业类别

	private RelativeLayout rl_currentIndustry;
	private TextView tv_currentIndustry2;// 目前就职的行业

	private RelativeLayout rl_totalWorkingTime;
	private TextView tv_totalWorkingTime2;// 该行业累计工作时间

	private LinearLayout rl_job;
	private EditText et_job2;// 职务

	private RelativeLayout rl_jobExp;
	private TextView tv_jobExp2;// 担任现职务时间

	// private LinearLayout ll_monthlySalary;
	private EditText et_monthlySalary2;// 月薪

	// private LinearLayout ll_dividend;
	private EditText et_dividend2;// 分红

	// private LinearLayout ll_annualSalary;
	private EditText et_annualSalary2;// 年薪

	private RelativeLayout rl_scale;
	private TextView tv_scale2;// 现单位规模

	private RelativeLayout rl_property;
	private TextView tv_property2;// 现单位性质

	private EditText et_opinion;// 对目前薪资的看法

//	private Builder builder;
//	private Dialog dialog;
//
//	private String[] workingYears = { "6个月以下", "6~12个月", "1年", "2年", "3年", "4年", "5年", "6~9年", "10~15年", "16年以上" };// 工龄
//	private String[] totalWorkingTime = { "6个月以下", "6~12个月", "1年", "2年", "3年", "4年", "5年", "6~9年", "10~15年", "16年以上" };// 该行业累计工作时间
//	private String[] workExp = { "6个月以下", "6~12个月", "1年", "2年", "3年", "4年", "5年", "6~9年", "10~15年", "16年以上" };// 担任现职务之间

	// private String[] currentIndustries;// 目前就职的行业
	// private String[] currentIndustryIds;

//	private String[] scales;// 规模
//	private String[] scaleIds;// 规模ID
//
//	private String[] properties;// 单位性质
//	private String[] propertyIds;// 单位性质ID

	private String workingAreaCityId;// 工作地区城市ID
	private String workingAreaProvinceId; // 工作地区省份ID
	private String workingAreaCity;// 工作地区-城市
	private String workingAreaProvince;// 工作地区-省份
	private String workingYear;// 工龄
	private String currentIndustryId;// 目前就职的行业的ID
	private String currentIndustry;// 目前就职的行业
	private String jobId;// 目前的职位类别ID
	private String job;// 目前的职位类别
	private String totalworkingtime;// 该行业累计工作时间
	private String position;// 职务
	private String jobExp;// 担任现职务的时间
	private String monthlySalary;// 月薪
	private String dividend;// 分红
	private String annualSalary;// 年薪
	private String scaleId;// 规模ID
	private String scale;// 规模
	private String propertyId;// 性质ID
	private String property;// 性质
	private String opinion;// 对薪资的看法

	private String result;// 服务端返回的结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_jobexp_activity);
		setTitleBar(getString(R.string.jobExperience), View.VISIBLE, View.VISIBLE);
		initAllView();
		reigesterAllEvent();
		initIntent();
	}

	protected void initIntent() {
		Intent intent = getIntent();
		workingAreaProvinceId = intent.getStringExtra("workingAreaProvinceId");
		workingAreaProvince = intent.getStringExtra("workingAreaProvince");
		workingAreaCityId = intent.getStringExtra("workingAreaCityId");
		workingAreaCity = intent.getStringExtra("workingAreaCity");
		workingYear = intent.getStringExtra("workingYear");
		currentIndustryId = intent.getStringExtra("currentIndustryId");
		currentIndustry = intent.getStringExtra("currentIndustry");
		jobId = intent.getStringExtra("jobId");
		job = intent.getStringExtra("job");
		totalworkingtime = intent.getStringExtra("totalworkingtime");
		position = intent.getStringExtra("position");
		jobExp = intent.getStringExtra("jobExp");
		monthlySalary = intent.getStringExtra("monthlySalary");
		dividend = intent.getStringExtra("dividend");
		annualSalary = intent.getStringExtra("annualSalary");
		scaleId = intent.getStringExtra("scaleId");
		scale = intent.getStringExtra("scale");
		propertyId = intent.getStringExtra("propertyId");
		property = intent.getStringExtra("property");
		opinion = intent.getStringExtra("opinion");

		if(!StringUtil.isBlank(workingAreaProvince)){
			tv_workingArea2.setText(workingAreaProvince+" "+workingAreaCity);
		}
		tv_workingYears2.setText(workingYear);
		tv_industryCategory2.setText(currentIndustry);
		tv_currentIndustry2.setText(job);
		tv_totalWorkingTime2.setText(totalworkingtime);
		et_job2.setText(position);
		et_monthlySalary2.setText(monthlySalary);
		et_dividend2.setText(dividend);
		et_annualSalary2.setText(annualSalary);
		tv_scale2.setText(scale);
		tv_property2.setText(property);
		et_opinion.setText(opinion);

	};

	@Override
	protected void initAllView() {
		super.titleRightButton.setImageResource(R.drawable.queding);

		rl_workingArea = (RelativeLayout) findViewById(R.id.workingArea);
		tv_workingArea2 = (TextView) findViewById(R.id.tv_workingArea2);
		// tv_first_language2.setText(language);
		//
		rl_workingYears = (RelativeLayout) findViewById(R.id.workingYears);
		tv_workingYears2 = (TextView) findViewById(R.id.tv_workingYears2);
		// tv_language_level2.setText(languagelevel);

		rl_industryCategory = (RelativeLayout) findViewById(R.id.industryCategory);
		tv_industryCategory2 = (TextView) findViewById(R.id.tv_industryCategory2);

		rl_currentIndustry = (RelativeLayout) findViewById(R.id.currentIndustry);
		tv_currentIndustry2 = (TextView) findViewById(R.id.tv_currentIndustry2);

		rl_totalWorkingTime = (RelativeLayout) findViewById(R.id.totalWorkingTime);
		tv_totalWorkingTime2 = (TextView) findViewById(R.id.tv_totalWorkingTime2);

		rl_job = (LinearLayout) findViewById(R.id.job);
		et_job2 = (EditText) findViewById(R.id.et_job2);

		rl_jobExp = (RelativeLayout) findViewById(R.id.jobExp);
		tv_jobExp2 = (TextView) findViewById(R.id.tv_jobExp2);

		// ll_monthlySalary = (LinearLayout) findViewById(R.id.monthlySalary);
		et_monthlySalary2 = (EditText) findViewById(R.id.et_monthlySalary2);

		// ll_dividend = (LinearLayout) findViewById(R.id.dividend);
		et_dividend2 = (EditText) findViewById(R.id.et_dividend2);

		// ll_annualSalary = (LinearLayout) findViewById(R.id.annualSalary);
		et_annualSalary2 = (EditText) findViewById(R.id.et_annualSalary2);

		rl_scale = (RelativeLayout) findViewById(R.id.scale);
		tv_scale2 = (TextView) findViewById(R.id.tv_scale2);

		rl_property = (RelativeLayout) findViewById(R.id.property);
		tv_property2 = (TextView) findViewById(R.id.tv_property2);

		// rl_opinion = (RelativeLayout) findViewById(R.id.opinion);
		et_opinion = (EditText) findViewById(R.id.et_opinion);

//		builder = new AlertDialog.Builder(SelectJobExpActivity.this);
	}

	@Override
	protected void reigesterAllEvent() {

		// 工作地区
		rl_workingArea.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SelectJobExpActivity.this, SelectAreaActivity.class);
				startActivityForResult(intent, Constant.WORKINGAREA_REQUEST_CODE);
			}

		});

		// 工龄
		rl_workingYears.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
//				builder.setTitle("请选择您的工作年限");
//				builder.setSingleChoiceItems(workingYears, 0, new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						tv_workingYears2.setText(workingYears[which]);
//						workingYear = (which == 0) ? "1" : (which == 1) ? "6" : (which == 2) ? "12" : (which == 3) ? "24" : (which == 4) ? "36" : (which == 5) ? "48" : (which == 6) ? "60" : (which == 7) ? "72" : (which == 8) ? "120" : "192";
//						dialog.dismiss();
//					}
//
//				});
//				dialog = builder.create();
//				dialog.show();
				
				Intent intent = new Intent(SelectJobExpActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "WorkingYears");
				startActivityForResult(intent, Constant.WORKING_YEARS);
			}

		});

		// 目前就职的行业
		rl_industryCategory.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SelectJobExpActivity.this, SelectCurrentIndustryActivity.class);
				startActivityForResult(intent, Constant.CURRENT_INDUSTRY_REQUEST_CODE);
			}

		});

		// 担任现职务的时间
		rl_jobExp.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				builder.setTitle("请选择担任现职务的时间");
//				builder.setSingleChoiceItems(workExp, 0, new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						tv_jobExp2.setText(workExp[which]);
//						jobExp = (which == 0) ? "1" : (which == 1) ? "6" : (which == 2) ? "12" : (which == 3) ? "24" : (which == 4) ? "36" : (which == 5) ? "48" : (which == 6) ? "60" : (which == 7) ? "72" : (which == 8) ? "120" : "192";
//						dialog.dismiss();
//					}
//
//				});
//				dialog = builder.create();
//				dialog.show();
				
				Intent intent = new Intent(SelectJobExpActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "WorkExp");
				startActivityForResult(intent, Constant.WORK_EXP);
				
			}

		});

		// 目前就职的职位类别
		rl_currentIndustry.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(SelectJobExpActivity.this, SelectJobActivity.class);
				startActivityForResult(intent, Constant.JOB_REQUEST_CODE);

			}

		});

		// 该行业累计工作时间
		rl_totalWorkingTime.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				builder.setTitle("请选择累计工作时间");
//				builder.setSingleChoiceItems(totalWorkingTime, 0, new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						tv_totalWorkingTime2.setText(totalWorkingTime[which]);
//						totalworkingtime = (which == 0) ? "1" : (which == 1) ? "6" : (which == 2) ? "12" : (which == 3) ? "24" : (which == 4) ? "36" : (which == 5) ? "48" : (which == 6) ? "60" : (which == 7) ? "72" : (which == 8) ? "120" : "192";
//						dialog.dismiss();
//					}
//
//				});
//				dialog = builder.create();
//				dialog.show();
				
				
				Intent intent = new Intent(SelectJobExpActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "TotalWorkingTime");
				startActivityForResult(intent, Constant.TOTAL_WORKING_TIME);
				
			}

		});

		// 单位规模
		rl_scale.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

//				showProcessDialog(false);
//				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
//							@Override
//							public void run() {
//								initScaleThread();
//							}
//						});
//				mThread.start();

				Intent intent = new Intent(SelectJobExpActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "CompanyScale");
				startActivityForResult(intent, Constant.COMPANY_SCALE);
				
			}

		});

		// 单位性质
		rl_property.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

//				showProcessDialog(false);
//				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
//							@Override
//							public void run() {
//								initPropertyThread();
//							}
//						});
//				mThread.start();
				
				Intent intent = new Intent(SelectJobExpActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "CompanyProperty");
				startActivityForResult(intent, Constant.COMPANY_PROPERTY);
				
			}

		});

		// 确定按钮
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				position = et_job2.getText().toString();
				monthlySalary = et_monthlySalary2.getText().toString();
				dividend = et_dividend2.getText().toString();
				annualSalary = et_annualSalary2.getText().toString();
				opinion = et_opinion.getText().toString();

				if (StringUtil.isBlank(tv_workingArea2.getText().toString())) {
					Toast.makeText(getApplicationContext(), "请选择工作地区", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(workingYear)) {
					Toast.makeText(getApplicationContext(), "请选择工龄", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(currentIndustry)) {
					Toast.makeText(getApplicationContext(), "请选择目前就职的行业", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(job)) {
					Toast.makeText(getApplicationContext(), "请选择目前就职的职位类别", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(totalworkingtime)) {
					Toast.makeText(getApplicationContext(), "请选择该行业累计工作时间", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(position)) {
					Toast.makeText(getApplicationContext(), "请填写职务", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(jobExp)) {
					Toast.makeText(getApplicationContext(), "请选择担任现职务的时间", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(monthlySalary)) {
					Toast.makeText(getApplicationContext(), "请填写月薪", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(annualSalary)) {
					Toast.makeText(getApplicationContext(), "请填写年薪", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(scale)) {
					Toast.makeText(getApplicationContext(), "请选择单位规模", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(property)) {
					Toast.makeText(getApplicationContext(), "请选择单位性质", Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = getIntent();
					intent.putExtra("workingAreaProvinceId", workingAreaProvinceId);
					intent.putExtra("workingAreaProvince", workingAreaProvince);
					intent.putExtra("workingAreaCityId", workingAreaCityId);
					intent.putExtra("workingAreaCity", workingAreaCity);
					intent.putExtra("workingYear", workingYear);
					intent.putExtra("currentIndustry", currentIndustry);
					intent.putExtra("currentIndustryId", currentIndustryId);
					intent.putExtra("job", job);
					intent.putExtra("jobId", jobId);
					intent.putExtra("totalworkingtime", totalworkingtime);
					intent.putExtra("position", position);
					intent.putExtra("jobExp", jobExp);
					intent.putExtra("monthlySalary", monthlySalary);
					intent.putExtra("dividend", dividend);
					intent.putExtra("annualSalary", annualSalary);
					intent.putExtra("scale", scale);
					intent.putExtra("scaleId", scaleId);
					intent.putExtra("property", property);
					intent.putExtra("propertyId", propertyId);
					intent.putExtra("opinion", opinion);
					setResult(RESULT_OK, intent);
					finish();
				}

			}
		});

	}

//	private void initScaleThread() {
//		String url = "appCmbShow.app";
//		Message msg = new Message();
//
//		try {
//
//			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//			params.add(new BasicNameValuePair("type", "10"));
//			params.add(new BasicNameValuePair("key", "1"));
//			params.add(new BasicNameValuePair("page", "1"));
//			result = getPostHttpContent(url, params);
//
//			if (StringUtil.isExcetionInfo(result)) {
//				SelectJobExpActivity.this.sendExceptionMsg(result);
//				return;
//			}
//
//			JSONObject responseJsonObject = new JSONObject(result);
//
//			if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果
//
//				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
//				Iterator it = resultJsonObject.keys();
//				scaleIds = new String[resultJsonObject.length()];
//				scales = new String[resultJsonObject.length()];
//				int i = 0;
//				while (it.hasNext()) {
//					String key = (String) it.next();
//					String value = resultJsonObject.getString(key);
//					scaleIds[i] = key;
//					scales[i] = value;
//					i++;
//				}
//				msg.what = Constant.SCALE;
//				handler.sendMessage(msg);
//			} else {
//				String errorResult = (String) responseJsonObject.get("result");
//				String err = StringUtil.getAppException4MOS(errorResult);
//				SelectJobExpActivity.this.sendExceptionMsg(err);
//			}
//
//		} catch (JSONException e) {
//			String err = StringUtil.getAppException4MOS("解析json出错！");
//			SelectJobExpActivity.this.sendExceptionMsg(err);
//		}
//	}
//
//	private void initPropertyThread() {
//		String url = "appCmbShow.app";
//		Message msg = new Message();
//
//		try {
//
//			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//			params.add(new BasicNameValuePair("type", "12"));
//			params.add(new BasicNameValuePair("key", "1"));
//			result = getPostHttpContent(url, params);
//
//			if (StringUtil.isExcetionInfo(result)) {
//				SelectJobExpActivity.this.sendExceptionMsg(result);
//				return;
//			}
//
//			JSONObject responseJsonObject = new JSONObject(result);
//
//			if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果
//
//				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
//				Iterator it = resultJsonObject.keys();
//				propertyIds = new String[resultJsonObject.length()];
//				properties = new String[resultJsonObject.length()];
//				int i = 0;
//				while (it.hasNext()) {
//					String key = (String) it.next();
//					String value = resultJsonObject.getString(key);
//					propertyIds[i] = key;
//					properties[i] = value;
//					i++;
//				}
//				msg.what = Constant.PROPERTY;
//				handler.sendMessage(msg);
//			} else {
//				String errorResult = (String) responseJsonObject.get("result");
//				String err = StringUtil.getAppException4MOS(errorResult);
//				SelectJobExpActivity.this.sendExceptionMsg(err);
//			}
//
//		} catch (JSONException e) {
//			String err = StringUtil.getAppException4MOS("解析json出错！");
//			SelectJobExpActivity.this.sendExceptionMsg(err);
//		}
//	}

//	// 处理线程发送的消息
//	private Handler handler = new Handler() {
//
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//
//			case Constant.SCALE:
//				initScale();
//				break;
//
//			case Constant.PROPERTY:
//				initProperty();
//				break;
//			}
//			closeProcessDialog();
//		}
//	};

//	/**
//	 * 请求完数据，更新界面的数据
//	 */
//	private void initScale() {
//
//		builder.setTitle("请选择您目前就职的行业");
//		builder.setSingleChoiceItems(scales, 0, new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				tv_scale2.setText(scales[which]);
//				scaleId = scaleIds[which];
//				scale = scales[which];
//				dialog.dismiss();
//			}
//
//		});
//		dialog = builder.create();
//		dialog.show();
//	}

//	/**
//	 * 请求完数据，更新界面的数据
//	 */
//	private void initProperty() {
//
//		builder.setTitle("请选择您目前就职的行业");
//		builder.setSingleChoiceItems(properties, 0, new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				tv_property2.setText(properties[which]);
//				propertyId = propertyIds[which];
//				property = properties[which];
//				dialog.dismiss();
//			}
//
//		});
//		dialog = builder.create();
//		dialog.show();
//	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.WORKINGAREA_REQUEST_CODE: 
//				workingArea = data.getStringExtra("area");s
//				workingAreaId = data.getStringExtra("areaId");
				
				workingAreaCity = data.getStringExtra("city");
				workingAreaCityId = data.getStringExtra("cityId");
				workingAreaProvince = data.getStringExtra("province");
				workingAreaProvinceId = data.getStringExtra("provinceId");
				tv_workingArea2.setText(workingAreaProvince+" "+workingAreaCity);
				break;
				
			case Constant.CURRENT_INDUSTRY_REQUEST_CODE: 
				currentIndustry = data.getStringExtra("currentIndustry");
				currentIndustryId = data.getStringExtra("currentIndustryId");
				tv_industryCategory2.setText(currentIndustry);
				break;

			case Constant.JOB_REQUEST_CODE: 
				jobId = data.getStringExtra("jobId");
				job = data.getStringExtra("job");
				tv_currentIndustry2.setText(job);
				break;
			
			case Constant.WORKING_YEARS:
				
				tv_workingYears2.setText(data.getStringExtra("workingYear"));
				workingYear = data.getStringExtra("workingYearId");
				break;
				
			case Constant.WORK_EXP:
				
				tv_jobExp2.setText(data.getStringExtra("workExp"));
				jobExp = data.getStringExtra("workExpId");
				break;

			case Constant.TOTAL_WORKING_TIME:
				
				tv_totalWorkingTime2.setText(data.getStringExtra("totalWorkingTime"));
				totalworkingtime = data.getStringExtra("totalWorkingTimeId");
				break;
				
			case Constant.COMPANY_SCALE:
				
				tv_scale2.setText(data.getStringExtra("companyScale"));
				scaleId = data.getStringExtra("companyScaleId");
				scale = data.getStringExtra("companyScale");
				break;
				
			case Constant.COMPANY_PROPERTY:
				
				tv_property2.setText(data.getStringExtra("companyProperty"));
				propertyId = data.getStringExtra("companyPropertyId");
				property = data.getStringExtra("companyProperty");
				break;
			}
		}
	}
}
