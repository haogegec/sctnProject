package com.sctn.sctnet.activity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
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
	private String[] foreignLanguageIds;
	private String[] foreignLanguage;
	private String[] languageLevelIds;
	private String[] languageLevels;

	private String language;// SalarySurveyActivity页面传过来的值
	private String languageLevel;// // SalarySurveyActivity页面传过来的值

	private String languageId;
	private String languageLevelId;

	private String result;// 服务端返回的结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_foreign_language_activity);
		setTitleBar(getString(R.string.selectForeignLanguage), View.VISIBLE, View.VISIBLE);
		initForeignLanguageThread();
		initIntent();
		initAllView();
		reigesterAllEvent();
	}

	protected void initIntent() {
		Intent intent = getIntent();
		language = intent.getStringExtra("foreignLanguage");
		languageLevel = intent.getStringExtra("languageLevel");

	};

	@Override
	protected void initAllView() {
		super.titleRightButton.setImageResource(R.drawable.queding);

		first_language = (RelativeLayout) findViewById(R.id.first_language);
		tv_first_language2 = (TextView) findViewById(R.id.tv_first_language2);
		tv_first_language2.setText(language);

		language_level = (RelativeLayout) findViewById(R.id.language_level);
		tv_language_level2 = (TextView) findViewById(R.id.tv_language_level2);
		tv_language_level2.setText(languageLevel);

		builder = new AlertDialog.Builder(SelectForeignLanguageActivity.this);
	}

	@Override
	protected void reigesterAllEvent() {

		// 选择第一外语
		first_language.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				initForeignLanguage();
			}

		});

		// 选择外语能力
		language_level.setOnClickListener(new ImageView.OnClickListener() {

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

		// 确定按钮
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putExtra("foreignLanguage", tv_first_language2.getText());
				intent.putExtra("languageLevel", tv_language_level2.getText());
				intent.putExtra("foreignLanguageId", languageId);
				intent.putExtra("languageLevelId", languageLevelId);

				setResult(RESULT_OK, intent);
				finish();

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
				SelectForeignLanguageActivity.this.sendExceptionMsg(result);
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
				SelectForeignLanguageActivity.this.sendExceptionMsg(err);
			}

		} catch (Exception e) {
			e.printStackTrace();
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
				SelectForeignLanguageActivity.this.sendExceptionMsg(result);
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
				SelectForeignLanguageActivity.this.sendExceptionMsg(err);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.FIRST_FOREIGN_LANGUAGE:
				// initForeignLanguage();
				break;
			case Constant.LANGUAGE_LEVEL:
				initLanguageLevel();
				break;

			}
			closeProcessDialog();
		}
	};

	private void initForeignLanguage() {
		builder.setTitle("请选择您的第一外语");
		builder.setSingleChoiceItems(foreignLanguage, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tv_first_language2.setText(foreignLanguage[which]);
				languageId = foreignLanguageIds[which];
				language = foreignLanguage[which];
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
				tv_language_level2.setText(languageLevels[which]);
				languageLevelId = languageLevelIds[which];
				languageLevel = languageLevels[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
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
