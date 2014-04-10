package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;

public class SalaryQueryListActivity extends BaicActivity{

	private String result;// 服务端返回的结果
	private String userId;
	private String password;
	
	private ListView salaryQueryListView;
	private SimpleAdapter salaryQueryListAdapter;
	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	private String[] key = {"id","salary","city","average_salary"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_list_more_activity);
		setTitleBar(getString(R.string.salarySurveyQueryList), View.VISIBLE, View.GONE);
		initAllView();
		Intent intent = this.getIntent();
		userId = intent.getStringExtra("userId");
		password = intent.getStringExtra("password");
		
		reigesterAllEvent();
		queryThread();
	}
	
	@Override
	protected void initAllView() {
		
		salaryQueryListView = (ListView) findViewById(R.id.information_list);
		salaryQueryListAdapter = new SimpleAdapter(
				SalaryQueryListActivity.this, items,
				R.layout.salary_query_list_item,
				key,new int[] { R.id.salary_id_text,R.id.salary_text,R.id.current_city_text,R.id.average_salary_text});
		salaryQueryListView.setAdapter(salaryQueryListAdapter);
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		
	}

	private void queryThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						query();
					}
				});
		mThread.start();
	}
	
	private void query() {
		String url = "appSalary!search.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("salaryUserid", userId));
		params.add(new BasicNameValuePair("salaryPwd", password));
		
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			SalaryQueryListActivity.this.sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			SalaryQueryListActivity.this.sendExceptionMsg(result);
		}
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultCode").toString().equals("0")) {

			JSONArray resultJsonArray = responseJsonObject.getJSONArray("result");
			if(resultJsonArray==null||resultJsonArray.size()==0){
				String err = StringUtil.getAppException4MOS("没有您要查询的结果");
				SalaryQueryListActivity.this.sendExceptionMsg(err);
				return;
			}
			for (int j = 0; j < resultJsonArray.size(); j++) {

				Map<String, Object> item = new HashMap<String, Object>();
				item.put(key[0], resultJsonArray.getJSONObject(j).get("salaryUserid"));// 小栏目的id
				item.put(key[1], resultJsonArray
						.getJSONObject(j).get("monthsalary"));
				item.put(key[2], resultJsonArray
						.getJSONObject(j).get("cityname"));
				item.put(key[3],
						resultJsonArray.getJSONObject(j).get("salary"));

				items.add(item);
			}
			msg.what = 0;
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			SalaryQueryListActivity.this.sendExceptionMsg(err);
		}

		handler.sendMessage(msg);
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				initUI();
				break;
			}
			closeProcessDialog();
		}
	};
	
   private void initUI() {
		
		
	   salaryQueryListView.setAdapter(salaryQueryListAdapter);
		
	}
}
