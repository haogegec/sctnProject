package com.sctn.sctnet.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.contants.Constant;

/**
 * 选择外语能力界面
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

	private RelativeLayout rl_job;
	private TextView tv_job2;// 职务

	private LinearLayout ll_monthlySalary;
	private TextView tv_monthlySalary2;// 月薪

	private LinearLayout ll_dividend;
	private TextView tv_dividend2;// 分红

	private LinearLayout ll_annualSalary;
	private TextView tv_annualSalary2;// 年薪

	private RelativeLayout rl_scale;
	private TextView tv_scale2;// 现单位规模

	private RelativeLayout rl_property;
	private TextView tv_property2;// 现单位性质

	// private RelativeLayout rl_opinion;
	// private TextView tv_opinion2;// 对目前薪资的看法

	private Builder builder;// 学历选择
	private Dialog dialog;
	private String[] workingYears = { "无", "1~2年", "3~4年", "5~10年", "10年及以上" };// 工作经验
	private String[] industryCategory = { "计算机软件", "互联网/电子商务", "金融/银行/证券", "房地产/建筑", "广告" };// 行业类别
	private String[] currentIndustry = { "计算机软件", "互联网/电子商务", "金融/银行/证券", "房地产/建筑", "广告" };// 目前就职的行业
	private String[] totalWorkingTime = { "无", "1~2年", "3~4年", "5~10年", "10年及以上" };// 该行业累计工作时间
	private String[] job = { "java软件开发工程师", "C语言软件开发工程师", "系统架构师", "需求分析师" };// 职务
	private String[] scale = { "小型（从业人员数：300名以下）", "中型（从业人员数：300~2000名）", "从业人员数：2000名及以上" };// 职务
	private String[] property = { "国企", "私企", "外企" };// 职务

	private String language;// SalarySurveyActivity页面传过来的值
	private String languagelevel;// // SalarySurveyActivity页面传过来的值

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_jobexp_activity);
		setTitleBar(getString(R.string.jobExperience), View.VISIBLE, View.VISIBLE);
		// initIntent();
		initAllView();
		reigesterAllEvent();
	}

	protected void initIntent() {
		Intent intent = getIntent();
		String foreignLanguage = intent.getStringExtra("foreignLanguage");
		if (!"".equals(foreignLanguage) && foreignLanguage != null) {
			language = foreignLanguage.substring(0, foreignLanguage.indexOf('、'));
			languagelevel = foreignLanguage.substring(foreignLanguage.indexOf('、') + 1);
		}
	};

	@Override
	protected void initAllView() {
		super.titleRightButton.setImageResource(R.drawable.titlebar_img_btn_left_bg);

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

		rl_job = (RelativeLayout) findViewById(R.id.job);
		tv_job2 = (TextView) findViewById(R.id.tv_job2);

		ll_monthlySalary = (LinearLayout) findViewById(R.id.monthlySalary);
		tv_monthlySalary2 = (TextView) findViewById(R.id.tv_monthlySalary2);

		ll_dividend = (LinearLayout) findViewById(R.id.dividend);
		tv_dividend2 = (TextView) findViewById(R.id.tv_dividend2);

		ll_annualSalary = (LinearLayout) findViewById(R.id.annualSalary);
		tv_annualSalary2 = (TextView) findViewById(R.id.tv_annualSalary2);

		rl_scale = (RelativeLayout) findViewById(R.id.scale);
		tv_scale2 = (TextView) findViewById(R.id.tv_scale2);

		rl_property = (RelativeLayout) findViewById(R.id.property);
		tv_property2 = (TextView) findViewById(R.id.tv_property2);

		// rl_opinion = (RelativeLayout) findViewById(R.id.opinion);
		// tv_opinion2 = (TextView) findViewById(R.id.tv_opinion2);

		builder = new AlertDialog.Builder(SelectJobExpActivity.this);
	}

	@Override
	protected void reigesterAllEvent() {

		// 工作地区
		rl_workingArea.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SelectJobExpActivity.this, SelectAreaActivity.class);
				// if(!"".equals(tv_foreignLanguage2.getText()) &&
				// tv_foreignLanguage2.getText()!=null){
				// intent.putExtra("foreignLanguage",
				// tv_foreignLanguage2.getText());
				// }
				startActivityForResult(intent, Constant.WORKINGAREA_REQUEST_CODE);
			}

		});

		// 工龄
		rl_workingYears.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				builder.setTitle("请选择您的工作年限");
				builder.setSingleChoiceItems(workingYears, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_workingYears2.setText(workingYears[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();
			}

		});

		// 行业类别
		rl_industryCategory.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				builder.setTitle("请选择行业类别");
				builder.setSingleChoiceItems(industryCategory, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_industryCategory2.setText(industryCategory[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();
			}

		});

		// 目前就职的行业
		rl_currentIndustry.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				builder.setTitle("请选择您正在工作的行业");
				builder.setSingleChoiceItems(currentIndustry, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_currentIndustry2.setText(currentIndustry[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();
			}

		});

		// 该行业累计工作时间
		rl_totalWorkingTime.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				builder.setTitle("请选择累计工作时间");
				builder.setSingleChoiceItems(totalWorkingTime, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_totalWorkingTime2.setText(totalWorkingTime[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();
			}

		});

		// 职务
		rl_job.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				builder.setTitle("请选择您的职务");
				builder.setSingleChoiceItems(job, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_job2.setText(job[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();
			}

		});

		// 单位规模
		rl_scale.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				builder.setTitle("请选择单位规模");
				builder.setSingleChoiceItems(scale, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_scale2.setText(scale[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();
			}

		});

		// 单位性质
		rl_property.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				builder.setTitle("请选择单位规模");
				builder.setSingleChoiceItems(property, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_property2.setText(property[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();
			}

		});

		// 确定按钮
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();

			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.WORKINGAREA_REQUEST_CODE: {

				String area = data.getStringExtra("area");
				tv_workingArea2.setText(area);

				break;

			}

			}
		}
	}
}
