package com.sctn.sctnet.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
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

	private RelativeLayout rl_degree;
	private TextView tv_degree2;// 学历

	private RelativeLayout rl_overseasEduExp;
	private TextView tv_overseasEduExp2;// 海外教育经历

	private RelativeLayout rl_workExp;
	private TextView tv_workExp2;// 海外教育经历

	private RelativeLayout rl_foreignLanguage;
	private TextView tv_foreignLanguage2;// 外语能力

	private RelativeLayout rl_professionalCertificate;
	private TextView tv_professionalCertificate2;// 职业资格证书

	private RelativeLayout rl_skill;
	private TextView tv_skill2;// 技能技巧

	private RelativeLayout rl_jobExp;
	private TextView tv_jobExp2;// 职场经历

	private Builder builder;// 学历选择
	private Dialog dialog;
	private String[] degrees = { "小学", "初中", "高中", "专科", "本科", "硕士", "博士" };// 学历
	private String[] overseasEduExp = { "无", "1~2年", "3~4年", "5~10年", "10年及以上" };// 海外教育经历
	private String[] workExp = { "无", "1~2年", "3~4年", "5~10年", "10年及以上" };// 工作经验
	private String[] professionalCertificates = { "造价师", "会计师", "营业员", "推销员" };// 职业资格证书
	private String[] skills = { "C++", "图形设计", "演讲" };// 技能技巧

	private String sex;// 性别
	private String age;// 年龄
	private String degree;// 学历
	
	
	private String result;// 服务端返回结果数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.salary_survey_activity);
		setTitleBar(getString(R.string.salarySurvey), View.VISIBLE, View.GONE);
		initAllView();
		reigesterAllEvent();
//		initDataThread();
	}

	@Override
	protected void initAllView() {

		rl_degree = (RelativeLayout) findViewById(R.id.degree);
		tv_degree2 = (TextView) findViewById(R.id.tv_degree2);
		rl_overseasEduExp = (RelativeLayout) findViewById(R.id.overseasEduExp);
		tv_overseasEduExp2 = (TextView) findViewById(R.id.tv_overseasEduExp2);
		rl_workExp = (RelativeLayout) findViewById(R.id.workExp);
		tv_workExp2 = (TextView) findViewById(R.id.tv_workExp2);
		rl_foreignLanguage = (RelativeLayout) findViewById(R.id.foreignLanguage);
		tv_foreignLanguage2 = (TextView) findViewById(R.id.tv_foreignLanguage2);
		rl_professionalCertificate = (RelativeLayout) findViewById(R.id.professionalCertificate);
		tv_professionalCertificate2 = (TextView) findViewById(R.id.tv_professional_certificate2);
		rl_skill = (RelativeLayout) findViewById(R.id.skill);
		tv_skill2 = (TextView) findViewById(R.id.tv_skill2);
		rl_jobExp = (RelativeLayout) findViewById(R.id.jobExp);
		tv_jobExp2 = (TextView) findViewById(R.id.tv_jobExp2);

		builder = new AlertDialog.Builder(SalarySurveyActivity.this);
	}

	@Override
	protected void reigesterAllEvent() {

		// 学历
		rl_degree.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择您的学历");
				builder.setSingleChoiceItems(degrees, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_degree2.setText(degrees[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});

		// 是否有海外教育经历
		rl_overseasEduExp.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择您的海外教育经历");
				builder.setSingleChoiceItems(overseasEduExp, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_overseasEduExp2.setText(overseasEduExp[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});

		// 有无工作经验
		rl_workExp.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择您的工作经验");
				builder.setSingleChoiceItems(workExp, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_workExp2.setText(workExp[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});

		// 外语能力
		rl_foreignLanguage.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SalarySurveyActivity.this, SelectForeignLanguageActivity.class);
				if (!"".equals(tv_foreignLanguage2.getText()) && tv_foreignLanguage2.getText() != null) {
					intent.putExtra("foreignLanguage", tv_foreignLanguage2.getText());
				}
				startActivityForResult(intent, Constant.FOREIGNLANGUAGE_REQUEST_CODE);

			}

		});

		// 职场经历
		rl_jobExp.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SalarySurveyActivity.this, SelectJobExpActivity.class);
				if (!"".equals(tv_foreignLanguage2.getText()) && tv_foreignLanguage2.getText() != null) {
					intent.putExtra("foreignLanguage", tv_foreignLanguage2.getText());
				}
				startActivityForResult(intent, Constant.JOBEXP_REQUEST_CODE);

			}

		});

		// 职业资格证书
		rl_professionalCertificate.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择您的海外教育经历");
				builder.setSingleChoiceItems(professionalCertificates, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_professionalCertificate2.setText(professionalCertificates[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});

		// 技能技巧
		rl_skill.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择您的海外教育经历");
				builder.setSingleChoiceItems(skills, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_skill2.setText(skills[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.FOREIGNLANGUAGE_REQUEST_CODE: {

				String foreignLanguage = data.getStringExtra("foreignLanguage");
				String languageLevel = data.getStringExtra("languageLevel");
				tv_foreignLanguage2.setText(foreignLanguage + "、" + languageLevel);

				break;

			}

			case Constant.JOBEXP_REQUEST_CODE: {

				Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
				break;

			}

			}
		}
	}

//	/**
//	 * 在子线程与远端服务器交互，请求数据
//	 */
//	private void initDataThread() {
//		showProcessDialog(false);
//		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
//					@Override
//					public void run() {
//						initData();
//					}
//				});
//		mThread.start();
//	}
//
//	/**
//	 * 请求数据，并将返回结果显示在界面上
//	 */
//	private void initData() {
//
//		String url = "appPersonCenter.app?method=execute";
//
//		Message msg = new Message();
//		try {
//
//			JSONObject jsonParmter = new JSONObject();
//			jsonParmter.put("age", age);
//			jsonParmter.put("", );
//			jsonParmter.put("", );
//			jsonParmter.put("", );
//			jsonParmter.put("", );
//			jsonParmter.put("", );
//			jsonParmter.put("", );
//			jsonParmter.put("", );
//			jsonParmter.put("", );
//			String parameter = jsonParmter.toString();
//
//			result = getPostHttpContent(url, parameter);
//
//			if (StringUtil.isExcetionInfo(result)) {
//				SalarySurveyActivity.this.sendExceptionMsg(result);
//				return;
//			}
//
//			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中
//
//			// JSON的解析过程
//			responseJsonObject = new JSONObject(result);
//			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果
//
//				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
//
//				post = resultJsonObject.getString("post");// 职位申请记录
//				resume = resultJsonObject.getString("resume");// 职位收藏记录
//				company = resultJsonObject.getString("company");// 几个公司看过我的简历
//
//				msg.what = 0;
//				handler.sendMessage(msg);
//			} else {
//				String errorResult = (String) responseJsonObject.get("result");
//				String err = StringUtil.getAppException4MOS(errorResult);
//				PersonalCenterActivity.this.sendExceptionMsg(err);
//			}
//
//		} catch (JSONException e) {
//			String err = StringUtil.getAppException4MOS("解析json出错！");
//			PersonalCenterActivity.this.sendExceptionMsg(err);
//		}
//	}
//
//	// 处理线程发送的消息
//	private Handler handler = new Handler() {
//
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
//
//				updateUI();
//				break;
//
//			}
//			closeProcessDialog();
//		}
//	};
//
//	/**
//	 * 请求完数据，更新界面的数据
//	 */
//	private void updateUI() {
//		username.setText(userId + "");
//		itemView1.setValue("共" + post + "条");
//		postAppCount.setText(resume);
//		postCollCount.setText(company);
//
//	}
}
