package com.sctn.sctnet.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

/**
 * 编辑基本信息页面
 * 
 * @author xueweiwei
 * 
 */
public class BasicInfoEditActivity extends BaicActivity {

	private EditText nameValue;
	private String nameStr = "";// 姓名

	private RadioButton mail;
	private RadioButton female;
	private String sex = "";// 性别

	private RelativeLayout orgin;
	private TextView orginValue;// 籍贯
	private String origin_province;
	private String origin_provinceId;
	private String origin_city;
	private String origin_cityId;

	private RelativeLayout birthday;
	private TextView birthdayValue;
	private String birthdayStr = "";// 出生日期
	private String birthdayDate;

	private RelativeLayout people;
	private TextView peopleValue;
	private String peopleStr = "";// 民族

	private RelativeLayout political;
	private TextView politicalValue;
	private String politicalStr = "";// 政治面貌

	private EditText heighValue;
	private String heighStr = "";// 身高

	private RelativeLayout maritalStatus;
	private TextView maritalStatusValue;
	private String maritalStatusStr = "";// 婚姻状况

	private RelativeLayout healthStatus;
	private TextView healthStatusValue;
	private String healthStatusStr = "";// 健康状况

	private RelativeLayout accountCity;
	private TextView accountCityValue;// 户口所在地
	private String account_province;
	private String account_provinceId;
	private String account_city;
	private String account_cityId;

	private RelativeLayout currentCity;
	private TextView currentCityValue;// 居住地
	private String current_province;
	private String current_provinceId;
	private String current_city;
	private String current_cityId;

	private EditText cardIdValue;
	private String cardIdStr = "";// 身份证号

	private EditText driveCodeValue;
	private String driveCodeStr = "";// 驾驶证号

	private EditText addressValue;
	private String addressStr = "";// 地址

	private Builder builder;
	private Dialog dialog;// 弹出框

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

	private String[] peopleDialogText = { "汉族", "少数民族" };// 民族
	private String[] politicalDialogText = { "群众", "团员", "党员" };// 政治面貌
	private String[] maritalStatusDialogText = { "未婚", "已婚", "离婚" };// 婚姻状况
	private String[] healthStatusDialogText = { "健康", "亚健康", "残疾" };// 健康状况
	
	private String result;// 服务端返回的结果
	private long userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_basic_info_activity);
		setTitleBar(getString(R.string.BasicInfoEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		
		initIntent();
		initAllView();
		reigesterAllEvent();
	}

	protected void initIntent(){
		userId = SharePreferencesUtils.getSharedlongData("userId");
	}
	
	@Override
	protected void initAllView() {

		nameValue = (EditText) findViewById(R.id.name_value);

		mail = (RadioButton) findViewById(R.id.mail);
		female = (RadioButton) findViewById(R.id.female);

		orgin = (RelativeLayout) findViewById(R.id.orgin);
		orginValue = (TextView) findViewById(R.id.origin_value);

		birthday = (RelativeLayout) findViewById(R.id.birthday);
		birthdayValue = (TextView) findViewById(R.id.birthday_value);

		people = (RelativeLayout) findViewById(R.id.people);
		peopleValue = (TextView) findViewById(R.id.people_value);

		political = (RelativeLayout) findViewById(R.id.political);
		politicalValue = (TextView) findViewById(R.id.political_value);

		heighValue = (EditText) findViewById(R.id.heigh_value);

		maritalStatus = (RelativeLayout) findViewById(R.id.marital_status);
		maritalStatusValue = (TextView) findViewById(R.id.marital_status_value);

		healthStatus = (RelativeLayout) findViewById(R.id.health_status);
		healthStatusValue = (TextView) findViewById(R.id.health_status_value);

		accountCity = (RelativeLayout) findViewById(R.id.account_city);
		accountCityValue = (TextView) findViewById(R.id.account_city_value);

		currentCity = (RelativeLayout) findViewById(R.id.current_city);
		currentCityValue = (TextView) findViewById(R.id.current_city_value);

		cardIdValue = (EditText) findViewById(R.id.cardid_value);

		driveCodeValue = (EditText) findViewById(R.id.drivecode_value);

		addressValue = (EditText) findViewById(R.id.address_value);
		builder = new AlertDialog.Builder(BasicInfoEditActivity.this);

	}

	@Override
	protected void reigesterAllEvent() {

		// 确定按钮
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requestDataThread();
			}
		});

		orgin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BasicInfoEditActivity.this, SelectAreaActivity.class);
				intent.putExtra("flag", "BasicInfoEditActivity");
				startActivityForResult(intent, Constant.SELECT_NATIVE_PLACE_REQUEST_CODE);
			}

		});

		birthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 弹出年月日时间选择框
				datePickerDialog = new DatePickerDialog(BasicInfoEditActivity.this, myDateSetListener, mYear, mMonth, mDay);
				/* 显示出日期设置对话框 */
				datePickerDialog.show();

			}

		});

		people.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择您的民族");
				builder.setSingleChoiceItems(peopleDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						peopleValue.setText(peopleDialogText[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});

		political.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择您的政治面貌");
				builder.setSingleChoiceItems(politicalDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						politicalValue.setText(politicalDialogText[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});

		maritalStatus.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择您的婚姻状况");
				builder.setSingleChoiceItems(maritalStatusDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						maritalStatusValue.setText(maritalStatusDialogText[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});

		healthStatus.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				builder.setTitle("请选择您的民族");
				builder.setSingleChoiceItems(healthStatusDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						healthStatusValue.setText(healthStatusDialogText[which]);
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();

			}

		});

		accountCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BasicInfoEditActivity.this, SelectAreaActivity.class);
				intent.putExtra("flag", "BasicInfoEditActivity");
				startActivityForResult(intent, Constant.SELECT_RESIDENCE_REQUEST_CODE);
			}

		});

		currentCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BasicInfoEditActivity.this, SelectAreaActivity.class);
				intent.putExtra("flag", "BasicInfoEditActivity");
				startActivityForResult(intent, Constant.SELECT_HABITAT_REQUEST_CODE);
			}

		});

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
		birthdayDate = dateStringBuilder.toString();
		birthdayValue.setText(birthdayDate);
		dateStringBuilder = new StringBuffer();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.SELECT_NATIVE_PLACE_REQUEST_CODE:

				origin_city = data.getStringExtra("city");
				origin_cityId = data.getStringExtra("cityId");
				origin_province = data.getStringExtra("province");
				origin_provinceId = data.getStringExtra("provinceId");
				orginValue.setText(origin_province + "  " + origin_city);

				break;

			case Constant.SELECT_RESIDENCE_REQUEST_CODE:
				account_city = data.getStringExtra("city");
				account_cityId = data.getStringExtra("cityId");
				account_province = data.getStringExtra("province");
				account_provinceId = data.getStringExtra("provinceId");
				accountCityValue.setText(account_province + "  " + account_city);
				break;

			case Constant.SELECT_HABITAT_REQUEST_CODE:
				current_city = data.getStringExtra("city");
				current_cityId = data.getStringExtra("cityId");
				current_province = data.getStringExtra("province");
				current_provinceId = data.getStringExtra("provinceId");
				currentCityValue.setText(current_province + "  " + current_city);
				break;
			}
		}
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

		String url = "appCmbShow.app";

		Message msg = new Message();
//		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//		params.add(new BasicNameValuePair("Userid", userId+""));
//		result = getPostHttpContent(url, params);
//
//		if (StringUtil.isExcetionInfo(result)) {
//			sendExceptionMsg(result);
//			return;
//		}
//
//		if (StringUtil.isBlank(result)) {
//			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
//			sendExceptionMsg(result);
//		}
//		
//		JSONObject responseJsonObject = JSONObject.parseObject(result);
//		if (responseJsonObject.get("resultcode").toString().equals("0")) {
//
//			JSONObject json = responseJsonObject.getJSONObject("result");
//			Set<Entry<String, Object>> set = json.entrySet();
//			Iterator<Entry<String, Object>> iter = set.iterator();
//			provinces = new String[set.size()];
//			provinceIds = new String[set.size()];
//			int i = 0;
//			while (iter.hasNext()) {
//				Map<String, String> map = new HashMap<String, String>();
//				Entry obj = iter.next();
//				map.put("id", (String) obj.getKey());
//				map.put("value", (String) obj.getValue());
//				provinces[i] = (String) obj.getValue();
//				provinceIds[i] = (String) obj.getKey();
//				listItems.add(map);
//				i++;
//			}
//			msg.what = 00;
//		} else {
//			String errorResult = (String) responseJsonObject.get("result");
//			String err = StringUtil.getAppException4MOS(errorResult);
//			sendExceptionMsg(err);
//		}
		msg.what = 00;
		handler.sendMessage(msg);
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 00:
				
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			closeProcessDialog();
		}
	};

}
