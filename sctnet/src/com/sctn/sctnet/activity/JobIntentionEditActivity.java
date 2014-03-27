package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.sctn.sctnet.Utils.SharePreferencesUtils;
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
	private String cityId = "";
	private String workAreaStr = "";

	private RelativeLayout workState;
	private TextView workStateValue;
	private String workStateStr = "";// 工作性质
	private String workStateId = "";
	private String[] workStates;
	private String[] workStateIds;

	private RelativeLayout workmanner;
	private TextView workmannerValue;
	private String industry = "";// 行业
	private String industryId = "";

	private RelativeLayout companyType;
	private TextView companyTypeValue;
	private String companyTypeStr = "";// 企业类型
	private String companyTypeId = "";
	private String[] companyTypes;// 企业类型
	private String[] companyTypeIds;// 企业类型ID

	private RelativeLayout wage;
	private TextView wageValue;
	private String wageStr = "";// 薪资范围
	private String wageId = "";
	private String[] wages;
	private String[] wageIds;

	private EditText housewhereValue;
	private String housewhereStr = "";// 住房要求
	

	private Builder builder;
	private Dialog dialog;// 弹出框

	private String result;// 服务端返回的结果

	private long userId;

	private HashMap<String, String> jobIntentionMap;

	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> newPersonalExperienceMap = new HashMap<String, String>();//基本信息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_intention_edit_activity);
		setTitleBar(getString(R.string.JobIntentionEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);

		initBundle();
		initAllView();
		reigesterAllEvent();
	}

	private void initBundle() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		userId = SharePreferencesUtils.getSharedlongData("userId");
		if (bundle != null && bundle.getSerializable("jobIntentionList") != null) {
			List<HashMap<String, String>> jobIntentionList = (List<HashMap<String, String>>) bundle.getSerializable("jobIntentionList");
			jobIntentionMap = jobIntentionList.get(0);
		}
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

		if (jobIntentionMap != null && jobIntentionMap.size() != 0) {

			if (jobIntentionMap.containsKey("工作地区")) {
				workAreaStr = jobIntentionMap.get("工作地区");
				workAreaValue.setText(workAreaStr);
			}
			if (jobIntentionMap.containsKey("工作性质")) {
				workStateStr = jobIntentionMap.get("工作性质");
				workStateValue.setText(workStateStr);
			}
			if (jobIntentionMap.containsKey("行业")) {
				industry = jobIntentionMap.get("行业");
				workmannerValue.setText(industry);
			}
			if (jobIntentionMap.containsKey("企业类型")) {
				companyTypeStr = jobIntentionMap.get("企业类型");
				companyTypeValue.setText(companyTypeStr);
			}
			if (jobIntentionMap.containsKey("薪水范围")) {
				wageStr = jobIntentionMap.get("薪水范围");
				wageValue.setText(wageStr);
			}
			if (jobIntentionMap.containsKey("住房要求")) {
				housewhereStr = jobIntentionMap.get("住房要求");
				housewhereValue.setText(housewhereStr);
			}
		}

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

				showProcessDialog(false);
				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
							@Override
							public void run() {
								initWorkStateThread();
							}
						});
				mThread.start();

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

				showProcessDialog(false);
				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
							@Override
							public void run() {
								initWageThread();
							}
						});
				mThread.start();

			}

		});

		// 确定按钮
		titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (workAreaStr.equals(workAreaValue.getText().toString()) && workStateStr.equals(workStateValue.getText().toString()) && industry.equals(workmannerValue.getText().toString()) && companyTypeStr.equals(companyTypeValue.getText().toString()) && wageStr.equals(wageValue.getText().toString()) && housewhereStr.equals(housewhereValue.getText().toString())) {

					Toast.makeText(getApplicationContext(), "请编辑之后再保存吧~~", Toast.LENGTH_SHORT).show();
				} else {
					requestDataThread();
				}
			}

		});

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
		//		params.add(new BasicNameValuePair("Userid",100020+""));
		if (!cityId.equals("")) {
			params.add(new BasicNameValuePair("WorkRegion", cityId));
		}
		if (!workStateId.equals("")) {
			params.add(new BasicNameValuePair("JobsState", workStateId));
		}
		if (!industryId.equals("")) {
			params.add(new BasicNameValuePair("WorkManner", industryId));
		}
		if (!companyTypeId.equals("")) {
			params.add(new BasicNameValuePair("CompanyType", companyTypeId));
		}
		if (!wageId.equals("")) {
			params.add(new BasicNameValuePair("Wage", wageId));
		}

		params.add(new BasicNameValuePair("HouseSubsidy", housewhereStr));

		params.add(new BasicNameValuePair("modifytype", "1"));// 保存到求职意向表中

		result = getPostHttpContent(url, params);

		newPersonalExperienceMap.put("工作地区", workAreaValue.getText().toString());
		newPersonalExperienceMap.put("工作性质", workStateValue.getText().toString());
		newPersonalExperienceMap.put("行业", workmannerValue.getText().toString());
		newPersonalExperienceMap.put("企业类型", companyTypeValue.getText().toString());
		newPersonalExperienceMap.put("薪水范围", wageValue.getText().toString());
		newPersonalExperienceMap.put("住房要求", housewhereValue.getText().toString());
		list.add(newPersonalExperienceMap);
		
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
			JobIntentionEditActivity.this.sendExceptionMsg(err);
		}

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

	private void initWorkStateThread() {
		String url = "appCmbShow.app";
		Message msg = new Message();

		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("type", "19"));
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
				workStates = new String[resultJsonObject.length()];
				workStateIds = new String[resultJsonObject.length()];
				int i = 0;
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = resultJsonObject.getString(key);
					workStateIds[i] = key;
					workStates[i] = value;
					i++;
				}
				msg.what = Constant.WORK_STATE;
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

	private void initWageThread() {
		String url = "appCmbShow.app";
		Message msg = new Message();

		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("type", "20"));
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
				wages = new String[resultJsonObject.length()];
				wageIds = new String[resultJsonObject.length()];
				int i = 0;
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = resultJsonObject.getString(key);
					wageIds[i] = key;
					wages[i] = value;
					i++;
				}
				msg.what = Constant.WAGE_RANGE;
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
				closeProcessDialog();
				break;

			case Constant.WORK_STATE:
				initWorkState();
				closeProcessDialog();
				break;

			case Constant.WAGE_RANGE:
				initWage();
				closeProcessDialog();
				break;
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

		}
	};

	private void initProperty() {
		builder.setTitle("请选择企业类型");
		builder.setSingleChoiceItems(companyTypes, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				companyTypeValue.setText(companyTypes[which]);
				companyTypeId = companyTypeIds[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}

	private void initWage() {
		builder.setTitle("请选择薪资范围");
		builder.setSingleChoiceItems(wages, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				wageValue.setText(wages[which]);
				wageId = wageIds[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}

	private void initWorkState(){
		builder.setTitle("请选择工作性质");
		builder.setSingleChoiceItems(workStates, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				workStateValue.setText(workStates[which]);
				workStateId = workStateIds[which];
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
