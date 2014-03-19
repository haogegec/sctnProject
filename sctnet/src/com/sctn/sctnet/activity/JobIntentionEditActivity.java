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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

/**
 * 编辑求职意向
 * 
 * @author xueweiwei
 * 
 */
public class JobIntentionEditActivity extends BaicActivity {

	private RelativeLayout workArea;
	private TextView workAreaValue;
	private String province;
	private String provinceId;
	private String city;
	private String cityId;

	private RelativeLayout workState;
	private TextView workStateValue;
	private String workStateStr = "";// 工作性质

	private RelativeLayout workmanner;
	private TextView workmannerValue;
	private String industry;// 行业
	private String industryId;

	private RelativeLayout companyType;
	private TextView companyTypeValue;
	private String companyTypeStr;// 企业类型
	private String companyTypeId;

	private RelativeLayout wage;
	private TextView wageValue;
	private String wageStr = "";// 薪资范围

	private EditText housewhereValue;
	private String housewhereStr = "";// 住房要求

	private String[] workStateDialogText = { "全职", "兼职" };// 工作性质
	private String[] companyTypes;// 企业类型
	private String[] companyTypeIds;// 企业类型ID
	private String[] wageDialogText = { "面议", "1500以下", "1500-1999", "2000-2999", "3000-3999", "4000-4999", "5000以上" };// 薪资范围

	private Builder builder;
	private Dialog dialog;// 弹出框

	private String result;// 服务端返回的结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_intention_edit_activity);
		setTitleBar(getString(R.string.JobIntentionEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);

		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {

		workArea = (RelativeLayout) findViewById(R.id.workarea);
		workAreaValue = (TextView) findViewById(R.id.workarea_value);

		workState = (RelativeLayout) findViewById(R.id.workstate);
		workStateValue = (TextView) findViewById(R.id.workstate_value);

		workmanner = (RelativeLayout) findViewById(R.id.workmanner);
		workmannerValue = (TextView) findViewById(R.id.workmanner_value);

		companyType = (RelativeLayout) findViewById(R.id.company_type);
		companyTypeValue = (TextView) findViewById(R.id.company_type_value);

		wage = (RelativeLayout) findViewById(R.id.wage);
		wageValue = (TextView) findViewById(R.id.wage_value);

		housewhereValue = (EditText) findViewById(R.id.housewhere_value);

		builder = new AlertDialog.Builder(JobIntentionEditActivity.this);

	}

	@Override
	protected void reigesterAllEvent() {

		// 工作地区
		workArea.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(JobIntentionEditActivity.this, SelectAreaActivity.class);
				intent.putExtra("flag", "JobIntentionEditActivity");
				startActivityForResult(intent, Constant.SELECT_NATIVE_PLACE_REQUEST_CODE);

			}

		});

		// 工作性质
		workState.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择工作性质");
				builder.setSingleChoiceItems(workStateDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						workStateValue.setText(workStateDialogText[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});

		// 行业
		workmanner.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JobIntentionEditActivity.this, SelectCurrentIndustryActivity.class);
				startActivityForResult(intent, Constant.CURRENT_INDUSTRY_REQUEST_CODE);
			}

		});

		// 企业类型
		companyType.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showProcessDialog(false);
				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
							@Override
							public void run() {
								initCompanyTypeThread();
							}
						});
				mThread.start();

			}

		});

		// 薪水范围
		wage.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择薪资范围");
				builder.setSingleChoiceItems(wageDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						wageValue.setText(wageDialogText[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});

		// 确定按钮
		titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
				finish();
			}

		});

	}

	private void initCompanyTypeThread() {
		String url = "appCmbShow.app";
		Message msg = new Message();

		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("type", "12"));
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
				companyTypes = new String[resultJsonObject.length()];
				companyTypeIds = new String[resultJsonObject.length()];
				int i = 0;
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = resultJsonObject.getString(key);
					companyTypeIds[i] = key;
					companyTypes[i] = value;
					i++;
				}
				msg.what = Constant.PROPERTY;
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

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {

			case Constant.PROPERTY:
				initProperty();
				break;
			}
			closeProcessDialog();
		}
	};

	private void initProperty() {
		builder.setTitle("请选择企业类型");
		builder.setSingleChoiceItems(companyTypes, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				companyTypeValue.setText(companyTypes[which]);
				companyTypeStr = companyTypes[which];
				companyTypeId = companyTypeIds[which];
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
			case Constant.SELECT_NATIVE_PLACE_REQUEST_CODE:
				province = data.getStringExtra("province");
				provinceId = data.getStringExtra("provinceId");
				city = data.getStringExtra("city");
				cityId = data.getStringExtra("cityId");
				workAreaValue.setText(province + "  " + city);
				break;

			case Constant.CURRENT_INDUSTRY_REQUEST_CODE:
				industry = data.getStringExtra("currentIndustry");
				industryId = data.getStringExtra("currentIndustryId");
				workmannerValue.setText(industry);
				break;
			}
		}
	}

}
