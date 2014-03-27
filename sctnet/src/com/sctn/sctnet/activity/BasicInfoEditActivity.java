package com.sctn.sctnet.activity;

import java.util.ArrayList;
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
	private String origin_cityId = "";
	private String orginStr = "";
	private String orginId = "";

	private RelativeLayout birthday;
	private TextView birthdayValue;
	private String birthdayStr = "";// 出生日期
	private String birthdayDate;

	private RelativeLayout people;
	private TextView peopleValue;// 民族
	private String peopleStr = "";
	private String peopleId = "";
	private String[] peoples;
	private String[] peopleIds;

	private RelativeLayout political;
	private TextView politicalValue;
	private String politicalStr = "";// 政治面貌
	private String politicalId = "";
	private String[] politicals;
	private String[] politicalIds;

	private EditText heighValue;
	private String heighStr = "";// 身高

	private RelativeLayout maritalStatus;
	private TextView maritalStatusValue;
	private String maritalStatusStr = "";// 婚姻状况
	private String maritalStatusId = "";
	private String[] maritalStatuses;
	private String[] maritalStatusIds;

	private RelativeLayout healthStatus;
	private TextView healthStatusValue;
	private String healthStatusStr = "";// 健康状况
	private String healthStatusId = "";
	private String[] healthStatuses;
	private String[] healthStatusIds;

	private RelativeLayout accountCity;
	private TextView accountCityValue;// 户口所在地
	private String account_province;
	private String account_provinceId;
	private String account_city;
	private String account_cityId;
	private String accountCityStr = "";

	private RelativeLayout currentCity;
	private TextView currentCityValue;// 居住地
	private String current_province;
	private String current_provinceId;
	private String current_city;
	private String current_cityId;
	private String currentCityStr = "";

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

	private String result;// 服务端返回的结果
	private long userId;
	
	private HashMap<String, String> basicInfoMap;//基本信息

	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> newBasicInfoMap = new HashMap<String, String>();//基本信息
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
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		userId = SharePreferencesUtils.getSharedlongData("userId");
		if(bundle!=null&&bundle.getSerializable("basicInfoList")!=null){
			List<HashMap<String, String>> basicInfoList = (List<HashMap<String, String>>) bundle.getSerializable("basicInfoList");
			basicInfoMap = basicInfoList.get(0);
		}
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
		
		

		if(basicInfoMap!=null&&basicInfoMap.size()!=0){
			
			if(basicInfoMap.containsKey("姓名")){
				nameStr = basicInfoMap.get("姓名");
				nameValue.setText(nameStr);
			}
			if(basicInfoMap.containsKey("籍贯")){
				orginStr = basicInfoMap.get("籍贯");
				orginValue.setText(orginStr);
			}
			if(basicInfoMap.containsKey("户口所在地")){
				accountCityStr = basicInfoMap.get("户口所在地");
				accountCityValue.setText(accountCityStr);
			}
			if(basicInfoMap.containsKey("地址")){
	            addressStr = basicInfoMap.get("地址");
				addressValue.setText(addressStr);
			}
			if(basicInfoMap.containsKey("出生日期")){
				birthdayStr = basicInfoMap.get("出生日期");
				birthdayValue.setText(birthdayStr);
			}
			if(basicInfoMap.containsKey("身份证号")){
				cardIdStr = basicInfoMap.get("身份证号");
				cardIdValue.setText(cardIdStr);
			}
           if(basicInfoMap.containsKey("当前城市")){
        	   currentCityStr = basicInfoMap.get("当前城市");
        	   currentCityValue.setText(currentCityStr);
			}
           if(basicInfoMap.containsKey("驾驶证号")){
        	   driveCodeStr = basicInfoMap.get("驾驶证号");
        	   driveCodeValue.setText(driveCodeStr);
			}
           if(basicInfoMap.containsKey("健康状况")){
        	   healthStatusStr = basicInfoMap.get("健康状况");
        	   healthStatusValue.setText(healthStatusStr);
			}
           if(basicInfoMap.containsKey("婚姻状况")){
        	   maritalStatusStr = basicInfoMap.get("婚姻状况");
        	   maritalStatusValue.setText(maritalStatusStr);
			}
           if(basicInfoMap.containsKey("民族")){
        	   peopleStr = basicInfoMap.get("民族");
        	   peopleValue.setText(peopleStr);
			}
           if(basicInfoMap.containsKey("政治面貌")){
        	   politicalStr = basicInfoMap.get("政治面貌");
        	   politicalValue.setText(politicalStr);
			}
           if(basicInfoMap.containsKey("性别")){
			   if(basicInfoMap.get("性别").equals("0")){
				   female.setSelected(true);
				   sex = "0";
			   }else{
				   mail.setSelected(true);
				   sex = "1";
			   }
        	   
			}
           if(basicInfoMap.containsKey("身高")){
			   heighStr = basicInfoMap.get("身高");
        	   heighValue.setText(heighStr);
			}
           
		}
	}

	@Override
	protected void reigesterAllEvent() {

		// 确定按钮
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

	           if(nameStr.equals(nameValue.getText().toString())&&orginStr.equals(orginValue.getText().toString())&&accountCityStr.equals(accountCityValue.getText().toString())
	        		   &&addressStr.equals(addressValue.getText().toString())&&birthdayStr.equals(birthdayValue.getText().toString())&&cardIdStr.equals(cardIdValue.getText().toString())
	        		   &&currentCityStr.equals(currentCityValue.getText().toString())&&driveCodeStr.equals(driveCodeValue.getText().toString())&&healthStatusStr.equals(healthStatusValue.getText().toString())
	        		   &&maritalStatusStr.equals(maritalStatusValue.getText().toString())&&peopleStr.equals(peopleValue.getText().toString())&&politicalStr.equals(politicalValue.getText().toString())
	        		   &&heighStr.equals(heighValue.getText().toString())){
	        	   
	        	   Toast.makeText(getApplicationContext(), "请编辑之后再保存吧~~", Toast.LENGTH_SHORT).show();
	        	   
	           }else{
	        	   requestDataThread();
	           }
				
			}
		});

		// 籍贯
		orgin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BasicInfoEditActivity.this, SelectAreaActivity.class);
				intent.putExtra("flag", "BasicInfoEditActivity");
				startActivityForResult(intent, Constant.SELECT_NATIVE_PLACE_REQUEST_CODE);
			}

		});

		// 出生日期
		birthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 弹出年月日时间选择框
				datePickerDialog = new DatePickerDialog(BasicInfoEditActivity.this, myDateSetListener, mYear, mMonth, mDay);
				/* 显示出日期设置对话框 */
				datePickerDialog.show();

			}

		});

		// 民族
		people.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				requestPeopleThread();

			}

		});

		// 政治面貌
		political.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				requestPoliticalThread();
				
			}

		});

		// 婚姻状况
		maritalStatus.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				requestMaritalThread();
				
			}

		});

		// 健康状况
		healthStatus.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				requestHealthThread();

			}

		});

		// 户口所在地
		accountCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BasicInfoEditActivity.this, SelectAreaActivity.class);
				intent.putExtra("flag", "BasicInfoEditActivity");
				startActivityForResult(intent, Constant.SELECT_RESIDENCE_REQUEST_CODE);
			}

		});

		// 居住地
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
	
	private void requestPeopleThread(){
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestPeople();
					}
				});
		mThread.start();
	}
	
	private void requestPoliticalThread(){
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable(){

			@Override
			public void run() {
				requestPolitical();
			}
			
		});
		mThread.start();
	}
	
	private void requestMaritalThread(){
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable(){

			@Override
			public void run() {
				requestMarital();
			}
			
		});
		mThread.start();
	}
	
	private void requestHealthThread(){
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable(){

			@Override
			public void run() {
				requestHealth();
			}
			
		});
		mThread.start();
	}
	
	private void requestData() {

		String url = "appPersonInfo!modify.app";

		Message msg = new Message();
     
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		//params.add(new BasicNameValuePair("Userid", userId+""));
		params.add(new BasicNameValuePair("Userid",100020+""));
		params.add(new BasicNameValuePair("TrueName", nameValue.getText().toString()));
		params.add(new BasicNameValuePair("Address", addressValue.getText().toString()));
		params.add(new BasicNameValuePair("BirthDay", birthdayValue.getText().toString()));
		params.add(new BasicNameValuePair("CardId", cardIdValue.getText().toString()));
		params.add(new BasicNameValuePair("CurrentCity", currentCityValue.getText().toString()));
		params.add(new BasicNameValuePair("DriveCode", driveCodeValue.getText().toString()));
		if(!healthStatusId.equals("")){
			params.add(new BasicNameValuePair("HealthState",healthStatusId));
		}
		if(!maritalStatusId.equals("")){
			params.add(new BasicNameValuePair("MarriageState", maritalStatusId));
		}
		if(!peopleId.equals("")){
			params.add(new BasicNameValuePair("People", peopleId));
		}
		if(!politicalId.equals("")){
			params.add(new BasicNameValuePair("Political", politicalId));
		}
		
		if(female.isSelected()){
			params.add(new BasicNameValuePair("Sex", "0"));
		}else{
			params.add(new BasicNameValuePair("Sex", "1"));
		}
		if(!StringUtil.isBlank(origin_cityId)){
			params.add(new BasicNameValuePair("Birthplace",origin_cityId));
		}
		if(!StringUtil.isBlank(account_cityId)){
			params.add(new BasicNameValuePair("AccountCity",account_cityId));
		}
		params.add(new BasicNameValuePair("UseHeight", heighValue.getText().toString()));
		params.add(new BasicNameValuePair("modifytype", "0"));//保存到简历表中
		
		result = getPostHttpContent(url, params);

      
		newBasicInfoMap.put("姓名", nameValue.getText().toString());
		newBasicInfoMap.put("籍贯", orginValue.getText().toString());
		newBasicInfoMap.put("户口所在地", accountCityValue.getText().toString());
		newBasicInfoMap.put("地址", addressValue.getText().toString());
		newBasicInfoMap.put("出生日期", birthdayValue.getText().toString());
		newBasicInfoMap.put("身份证号", cardIdValue.getText().toString());
		newBasicInfoMap.put("当前城市", currentCityValue.getText().toString());
		newBasicInfoMap.put("驾驶证号", driveCodeValue.getText().toString());
		newBasicInfoMap.put("健康状况", healthStatusValue.getText().toString());
		newBasicInfoMap.put("婚姻状况", maritalStatusValue.getText().toString());
		newBasicInfoMap.put("民族", peopleValue.getText().toString());
		newBasicInfoMap.put("政治面貌", politicalValue.getText().toString());
		if(female.isSelected()){
			newBasicInfoMap.put("性别", "0");
		}else{
			newBasicInfoMap.put("性别", "1");
		}
		newBasicInfoMap.put("身高", heighValue.getText().toString());
		
		list.add(newBasicInfoMap);
		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			sendExceptionMsg(result);
			return;
		}
		
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultCode").toString().equals("0")) {
			msg.what = 00;
			handler.sendMessage(msg);
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			sendExceptionMsg(err);
		}
		
	}
	
	private void requestPeople() {

		String url = "appCmbShow.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "14"));
		params.add(new BasicNameValuePair("key", "1"));
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			sendExceptionMsg(result);
		}
		
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultcode").toString().equals("0")) {

			JSONObject json = responseJsonObject.getJSONObject("result");
			Set<Entry<String, Object>> set = json.entrySet();
			Iterator<Entry<String, Object>> iter = set.iterator();
			peoples = new String[set.size()];
			peopleIds = new String[set.size()];
			int i = 0;
			while (iter.hasNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Entry obj = iter.next();
				map.put("id", (String) obj.getKey());
				map.put("value", (String) obj.getValue());
				peoples[i] = (String) obj.getValue();
				peopleIds[i] = (String) obj.getKey();
				i++;
			}
			msg.what = Constant.PEOPLE;
			handler.sendMessage(msg);
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			sendExceptionMsg(err);
		}
		
	}
	
	private void requestPolitical() {

		String url = "appCmbShow.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "15"));
		params.add(new BasicNameValuePair("key", "1"));
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			sendExceptionMsg(result);
		}
		
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultcode").toString().equals("0")) {

			JSONObject json = responseJsonObject.getJSONObject("result");
			Set<Entry<String, Object>> set = json.entrySet();
			Iterator<Entry<String, Object>> iter = set.iterator();
			politicals = new String[set.size()];
			politicalIds = new String[set.size()];
			int i = 0;
			while (iter.hasNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Entry obj = iter.next();
				map.put("id", (String) obj.getKey());
				map.put("value", (String) obj.getValue());
				politicals[i] = (String) obj.getValue();
				politicalIds[i] = (String) obj.getKey();
				i++;
			}
			msg.what = Constant.POLITICAL;
			handler.sendMessage(msg);
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			sendExceptionMsg(err);
		}
		
	}
	
	private void requestMarital() {

		String url = "appCmbShow.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "16"));
		params.add(new BasicNameValuePair("key", "1"));
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			sendExceptionMsg(result);
		}
		
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultcode").toString().equals("0")) {

			JSONObject json = responseJsonObject.getJSONObject("result");
			Set<Entry<String, Object>> set = json.entrySet();
			Iterator<Entry<String, Object>> iter = set.iterator();
			maritalStatuses = new String[set.size()];
			maritalStatusIds = new String[set.size()];
			int i = 0;
			while (iter.hasNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Entry obj = iter.next();
				map.put("id", (String) obj.getKey());
				map.put("value", (String) obj.getValue());
				maritalStatuses[i] = (String) obj.getValue();
				maritalStatusIds[i] = (String) obj.getKey();
				i++;
			}
			msg.what = Constant.MARITAL;
			handler.sendMessage(msg);
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			sendExceptionMsg(err);
		}
		
	}
	
	
	private void requestHealth() {

		String url = "appCmbShow.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "17"));
		params.add(new BasicNameValuePair("key", "1"));
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			sendExceptionMsg(result);
		}
		
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultcode").toString().equals("0")) {

			JSONObject json = responseJsonObject.getJSONObject("result");
			Set<Entry<String, Object>> set = json.entrySet();
			Iterator<Entry<String, Object>> iter = set.iterator();
			healthStatuses = new String[set.size()];
			healthStatusIds = new String[set.size()];
			int i = 0;
			while (iter.hasNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Entry obj = iter.next();
				map.put("id", (String) obj.getKey());
				map.put("value", (String) obj.getValue());
				healthStatuses[i] = (String) obj.getValue();
				healthStatusIds[i] = (String) obj.getKey();
				i++;
			}
			msg.what = Constant.HEALTH;
			handler.sendMessage(msg);
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			sendExceptionMsg(err);
		}
		
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 00:
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("list", list);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
				break;
				
			case Constant.PEOPLE:
				initPeople();
				break;
				
			case Constant.POLITICAL:
				initPolitical();
				break;
				
			case Constant.MARITAL:
				initMarital();
				break;
				
			case Constant.HEALTH:
				initHealth();
				break;
				
				
			}
			
			
			
			closeProcessDialog();
		}
	};
	
	private void initPeople(){
		builder.setTitle("请选择您的民族");
		builder.setSingleChoiceItems(peoples, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				peopleValue.setText(peoples[which]);
				peopleId = peopleIds[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}
	
	private void initPolitical(){
		builder.setTitle("请选择您的政治面貌");
		builder.setSingleChoiceItems(politicals, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				politicalValue.setText(politicals[which]);
				politicalId = politicalIds[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}
	
	private void initMarital(){
		builder.setTitle("请选择您的婚姻状况");
		builder.setSingleChoiceItems(maritalStatuses, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				maritalStatusValue.setText(maritalStatuses[which]);
				maritalStatusId = maritalStatusIds[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}
	
	private void initHealth(){
		builder.setTitle("请选择您的健康状况");
		builder.setSingleChoiceItems(healthStatuses, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				healthStatusValue.setText(healthStatuses[which]);
				healthStatusId = healthStatusIds[which];
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();
	}

}
