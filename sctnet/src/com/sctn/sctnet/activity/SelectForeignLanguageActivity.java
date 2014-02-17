package com.sctn.sctnet.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;

/**
 * 选择外语能力界面
 * 
 * @author 姜勇男
 * 
 */
public class SelectForeignLanguageActivity extends BaicActivity {

	private RelativeLayout first_language;
	private TextView tv_first_language2;// 第一外语

	private RelativeLayout language_level;
	private TextView tv_language_level2;// 外语等级

	private Builder builder;// 学历选择
	private Dialog dialog;
	private String[] foreignLanguage = { "英语", "日语", "俄语", "西班牙语", "韩语", "法语" };
	private String[] languageLevel = { "N5", "N4", "N3", "N2", "N1" };// 日语等级，以后和后台交互之后，就不用分英语等级和日语等级了。

	private String language;// SalarySurveyActivity页面传过来的值
	private String languagelevel;// // SalarySurveyActivity页面传过来的值
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_foreign_language_activity);
		setTitleBar(getString(R.string.selectForeignLanguage), View.VISIBLE, View.VISIBLE);
		initIntent();
		initAllView();
		reigesterAllEvent();
	}

	protected void initIntent(){
		 Intent intent = getIntent();
		 String foreignLanguage = intent.getStringExtra("foreignLanguage");
		 if(!"".equals(foreignLanguage) && foreignLanguage != null){
			 language = foreignLanguage.substring(0,foreignLanguage.indexOf('、'));
			 languagelevel = foreignLanguage.substring(foreignLanguage.indexOf('、')+1);
		 }
	};
	
	@Override
	protected void initAllView() {
		super.titleRightButton.setImageResource(R.drawable.titlebar_img_btn_left_bg);
		
		first_language = (RelativeLayout) findViewById(R.id.first_language);
		tv_first_language2 = (TextView) findViewById(R.id.tv_first_language2);
		tv_first_language2.setText(language);
		
		language_level = (RelativeLayout) findViewById(R.id.language_level);
		tv_language_level2 = (TextView) findViewById(R.id.tv_language_level2);
		tv_language_level2.setText(languagelevel);
		
		builder = new AlertDialog.Builder(SelectForeignLanguageActivity.this);
	}

	@Override
	protected void reigesterAllEvent() {

		// 选择第一外语
		first_language.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				builder.setTitle("请选择您的第一外语");
				builder.setSingleChoiceItems(foreignLanguage, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_first_language2.setText(foreignLanguage[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();
			}

		});

		// 选择外语能力
		language_level.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				builder.setTitle("请选择您的外语能力");
				builder.setSingleChoiceItems(languageLevel, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_language_level2.setText(languageLevel[which]);
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
//				Toast.makeText(getApplicationContext(), "确定", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(SelectForeignLanguageActivity.this,SalarySurveyActivity.class);
				intent.putExtra("foreignLanguage", tv_first_language2.getText());
				intent.putExtra("languageLevel", tv_language_level2.getText());
				setResult(RESULT_OK,intent);
				finish();
			
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			// case 1: {
			//
			// String area = data.getStringExtra("area");
			//
			// tv_area2.setText(area);
			//
			// break;
			//
			// }

			}
		}
	}
}
