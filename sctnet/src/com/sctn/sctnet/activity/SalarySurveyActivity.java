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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

/**
 * 薪酬调查界面
 * 
 * @author 姜勇男
 * 
 */
public class SalarySurveyActivity extends BaicActivity {

	private EditText salarySurvey_username;
	private EditText salarySurvey_password;

	private RadioGroup rg_sex;// 性别

	private EditText et_age2;// 年龄

	private RelativeLayout rl_degree;
	private TextView tv_degree2;// 学历

	private RadioGroup rg_overseasEduExp;// 海外工作经历

	// private RelativeLayout rl_workExp;
	// private TextView tv_workExp2;// 海外教育经历

	private RelativeLayout rl_foreignLanguage;
	private TextView tv_foreignLanguage2;// 外语能力

	// private LinearLayout rl_professionalCertificate;
	private EditText et_professionalCertificate2;// 职业资格证书

	// private LinearLayout rl_skill;
	private EditText et_skill2;// 技能技巧

	private RelativeLayout rl_jobExp;
	private TextView tv_jobExp2;// 职场经历

	private Builder builder;// 学历选择
	private Dialog dialog;

	private Button btn_submit;

	private String[] degreeIds;// 存储学历对应的ID
	private String[] degrees;// 学历

	private String salarySurveyUsername = "";
	private String salarySurveyPassword = "";
	private String degreeId = "";// 学历ID
	private String degree = "";
	private String sex = "男";// 性别
	private String age = "";// 年龄
	private String overseasEduExp = "1";// 海外工作经历,默认为1，1代表无
	private String foreignLanguage = "";// 外语能力，在SelectForeignLanguage页面选择之后，传到该页面
	private String foreignLanguageId = "";// 外语能力，在SelectForeignLanguage页面选择之后，传到该页面
	private String languageLevel = "";// 外语等级，在SelectForeignLanguage页面选择之后，传到该页面
	private String languageLevelId = "0";// 外语等级，在SelectForeignLanguage页面选择之后，传到该页面
	private String professionalCertificate = "";// 职业资格证书
	private String skill = "";// 技能技巧

	// ===================== 职场经历页面的参数 ==============================
	private String workingAreaProvinceId;// 工作地区省份ID
	private String workingAreaProvince;// 工作地区省份
	private String workingAreaCityId;// 工作地区城市ID
	private String workingAreaCity;// 工作地区城市
	private String workingYear;// 工龄
	private String currentIndustryId;// 目前就职的行业的ID
	private String currentIndustry;// 目前就职的行业的
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
	// ===================== 职场经历页面的参数 ==============================

	private String result;// 服务端返回结果数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.salary_survey_activity);
		setTitleBar(getString(R.string.salarySurvey), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.search_bar);
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		salarySurvey_username = (EditText) findViewById(R.id.salarySurvey_username);
		salarySurvey_password = (EditText) findViewById(R.id.salarySurvey_password);
		et_age2 = (EditText) findViewById(R.id.et_age2);
		rl_degree = (RelativeLayout) findViewById(R.id.degree);
		tv_degree2 = (TextView) findViewById(R.id.tv_degree2);
		rg_sex = (RadioGroup) findViewById(R.id.sex);
		rg_overseasEduExp = (RadioGroup) findViewById(R.id.overseasEduExp);
		// tv_overseasEduExp2 = (TextView)
		// findViewById(R.id.tv_overseasEduExp2);
		// rl_workExp = (RelativeLayout) findViewById(R.id.workExp);
		// tv_workExp2 = (TextView) findViewById(R.id.tv_workExp2);
		rl_foreignLanguage = (RelativeLayout) findViewById(R.id.foreignLanguage);
		tv_foreignLanguage2 = (TextView) findViewById(R.id.tv_foreignLanguage2);
		// rl_professionalCertificate = (LinearLayout)
		// findViewById(R.id.professionalCertificate);
		et_professionalCertificate2 = (EditText) findViewById(R.id.et_professional_certificate2);
		// rl_skill = (LinearLayout) findViewById(R.id.skill);
		et_skill2 = (EditText) findViewById(R.id.et_skill2);
		rl_jobExp = (RelativeLayout) findViewById(R.id.jobExp);
		tv_jobExp2 = (TextView) findViewById(R.id.tv_jobExp2);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		builder = new AlertDialog.Builder(SalarySurveyActivity.this);
	}

	@Override
	protected void reigesterAllEvent() {

		// 学历
		rl_degree.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				showProcessDialog(false);
//				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
//							@Override
//							public void run() {
//								initDegreeThread();
//							}
//						});
//				mThread.start();
				
				Intent intent = new Intent(SalarySurveyActivity.this, SelectItemActivity.class);
				intent.putExtra("which", "Degree");
				startActivityForResult(intent, Constant.DEGREE);
				
				
			}

		});

		rg_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) SalarySurveyActivity.this.findViewById(arg1);
				// 更新文本内容，以符合选中项
				sex = rb.getText().toString();
			}

		});

		// 是否有海外教育经历
		rg_overseasEduExp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) SalarySurveyActivity.this.findViewById(arg1);
				// 更新文本内容，以符合选中项
				if (rb.getText().toString().equals("无")) {
					overseasEduExp = "1";
				} else if (rb.getText().toString().equals("有")) {
					overseasEduExp = "0";
				}
			}

		});

		// // 有无工作经验
		// rl_workExp.setOnClickListener(new ImageView.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// builder.setTitle("请选择您的工作经验");
		// builder.setSingleChoiceItems(workExp, 0, new
		// DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// tv_workExp2.setText(workExp[which]);
		// dialog.dismiss();
		// }
		//
		// });
		// dialog = builder.create();
		// dialog.show();
		//
		// }
		//
		// });

		// 外语能力
		rl_foreignLanguage.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SalarySurveyActivity.this, SelectForeignLanguageActivity.class);
				if (!"".equals(tv_foreignLanguage2.getText()) && tv_foreignLanguage2.getText() != null) {
					intent.putExtra("foreignLanguage", foreignLanguage);
					intent.putExtra("languageLevel", languageLevel);
				}
				startActivityForResult(intent, Constant.FOREIGNLANGUAGE_REQUEST_CODE);

			}

		});

		// 职场经历
		rl_jobExp.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SalarySurveyActivity.this, SelectJobExpActivity.class);

				if (!StringUtil.isBlank(tv_jobExp2.getText().toString())) {
					intent.putExtra("workingAreaProvince", workingAreaProvince);
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
				}

				startActivityForResult(intent, Constant.JOBEXP_REQUEST_CODE);

			}

		});

		// // 职业资格证书
		// rl_professionalCertificate.setOnClickListener(new
		// ImageView.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// builder.setTitle("请选择您的海外教育经历");
		// builder.setSingleChoiceItems(professionalCertificates, 0, new
		// DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// tv_professionalCertificate2.setText(professionalCertificates[which]);
		// dialog.dismiss();
		// }
		//
		// });
		// dialog = builder.create();
		// dialog.show();
		//
		// }
		//
		// });
		//
		// // 技能技巧
		// rl_skill.setOnClickListener(new ImageView.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// builder.setTitle("请选择您的海外教育经历");
		// builder.setSingleChoiceItems(skills, 0, new
		// DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// tv_skill2.setText(skills[which]);
		// dialog.dismiss();
		// }
		//
		// });
		// dialog = builder.create();
		// dialog.show();
		//
		// }
		//
		// });

		btn_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				salarySurveyUsername = salarySurvey_username.getText().toString();
				salarySurveyPassword = salarySurvey_password.getText().toString();
				age = et_age2.getText().toString();
				professionalCertificate = et_professionalCertificate2.getText().toString();
				skill = et_skill2.getText().toString();

				if (StringUtil.isBlank(salarySurveyUsername)) {
					Toast.makeText(getApplicationContext(), "请输入参与调查的ID", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(salarySurveyPassword)) {
					Toast.makeText(getApplicationContext(), "请输入参与调查的密码", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(age)) {
					Toast.makeText(getApplicationContext(), "请填写年龄", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(degree)) {
					Toast.makeText(getApplicationContext(), "请选择学历", Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isBlank(tv_jobExp2.getText().toString())) {
					Toast.makeText(getApplicationContext(), "请选择职场经历", Toast.LENGTH_SHORT).show();
				} else {
					showProcessDialog(false);
					Thread mThread = new Thread(new Runnable() {// 启动新的线程，
								@Override
								public void run() {
									submitThread();
								}
							});
					mThread.start();

				}
			}
		});
		
		super.titleRightButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(SalarySurveyActivity.this, SalarySurveyQueryActivity.class);
				startActivity(intent);
			}
			
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.FOREIGNLANGUAGE_REQUEST_CODE: 

				foreignLanguage = data.getStringExtra("foreignLanguage");
				languageLevel = data.getStringExtra("languageLevel");
				foreignLanguageId = data.getStringExtra("foreignLanguageId");
				languageLevelId = data.getStringExtra("languageLevelId");
				if (!StringUtil.isBlank(foreignLanguage) && !StringUtil.isBlank(languageLevel)) {
					tv_foreignLanguage2.setText("已选择");
					tv_foreignLanguage2.setTextColor(getResources().getColor(R.color.green));
				} else {
					tv_foreignLanguage2.setText("");
				}
				Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
				break;

			

			case Constant.JOBEXP_REQUEST_CODE: 

				workingAreaProvinceId = data.getStringExtra("workingAreaProvinceId");
				workingAreaProvince = data.getStringExtra("workingAreaProvince");
				workingAreaCityId = data.getStringExtra("workingAreaCityId");
				workingAreaCity = data.getStringExtra("workingAreaCity");
				workingYear = data.getStringExtra("workingYear");
				currentIndustryId = data.getStringExtra("currentIndustryId");
				currentIndustry = data.getStringExtra("currentIndustry");
				jobId = data.getStringExtra("jobId");
				job = data.getStringExtra("job");
				totalworkingtime = data.getStringExtra("totalworkingtime");
				position = data.getStringExtra("position");
				jobExp = data.getStringExtra("jobExp");
				monthlySalary = data.getStringExtra("monthlySalary");
				dividend = data.getStringExtra("dividend");
				annualSalary = data.getStringExtra("annualSalary");
				scaleId = data.getStringExtra("scaleId");
				scale = data.getStringExtra("scale");
				propertyId = data.getStringExtra("propertyId");
				property = data.getStringExtra("property");
				opinion = data.getStringExtra("opinion");

				tv_jobExp2.setText("已选择");
				tv_jobExp2.setTextColor(getResources().getColor(R.color.green));

				Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
				break;

			
			
			case Constant.DEGREE:
				
				degree = data.getStringExtra("degree");
				degreeId = data.getStringExtra("degreeId");
				tv_degree2.setText(degree);
				
				break;
			

			}
		}
	}

	private void submitThread() {
		String url = "appSalary!insert.app";
		Message msg = new Message();

		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("age", age));
			params.add(new BasicNameValuePair("city", workingAreaCityId));
			params.add(new BasicNameValuePair("companytype", propertyId));
			params.add(new BasicNameValuePair("degree", degreeId));
			params.add(new BasicNameValuePair("dwgm", scaleId));
			params.add(new BasicNameValuePair("experience", workingYear));
			params.add(new BasicNameValuePair("foreignLanguage", foreignLanguageId));
			params.add(new BasicNameValuePair("grade", languageLevelId));
			params.add(new BasicNameValuePair("hyyears", totalworkingtime));
			params.add(new BasicNameValuePair("monthsalary", monthlySalary));
			params.add(new BasicNameValuePair("opinion", opinion));
			params.add(new BasicNameValuePair("oversea", overseasEduExp));
			params.add(new BasicNameValuePair("rzhy", currentIndustryId));
			params.add(new BasicNameValuePair("salary", dividend));
			params.add(new BasicNameValuePair("salaryUserid", salarySurveyUsername));
			params.add(new BasicNameValuePair("salaryPwd", salarySurveyPassword));
			params.add(new BasicNameValuePair("sex", sex));
			params.add(new BasicNameValuePair("skill", skill));
			params.add(new BasicNameValuePair("yearsalary", annualSalary));
			params.add(new BasicNameValuePair("zw", position));
			params.add(new BasicNameValuePair("zwtype", jobId));
			params.add(new BasicNameValuePair("zwyears", jobExp));
			params.add(new BasicNameValuePair("zyzgzs", professionalCertificate));

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				SalarySurveyActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);

			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				msg.what = Constant.SALARY_SURVEY_SUBMIT;
				handler.sendMessage(msg);
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				SalarySurveyActivity.this.sendExceptionMsg(err);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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
			params.add(new BasicNameValuePair("page", "1"));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				SalarySurveyActivity.this.sendExceptionMsg(result);
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
				SalarySurveyActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			SalarySurveyActivity.this.sendExceptionMsg(err);
		}
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.DEGREE:
				initDegree();
				break;
			case Constant.SALARY_SURVEY_SUBMIT:
				Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
				startHomeActivity();
				break;
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
				tv_degree2.setText(degrees[which]);
				degree = degrees[which];
				degreeId = degreeIds[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}
	
	private void startHomeActivity(){
		Intent intent = new Intent(SalarySurveyActivity.this,HomeActivity.class);
		startActivity(intent);
	}
}
