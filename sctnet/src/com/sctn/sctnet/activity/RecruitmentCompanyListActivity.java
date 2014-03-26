package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
/**
 * 现场招聘会的公司列表
 * @author xueweiwei
 *
 */
public class RecruitmentCompanyListActivity extends BaicActivity{
	
	private ListView recruitmentCompanyListView;
	private SimpleAdapter recruitmentCompanyListAdapter;
	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	private String[] key = { "company_name_text","contact_name_text","contact_way_text","email_text","company_address_text"
			,"register_time_text","company_website_text","postal_code_text","company_desc"};
	//请求数据
	private String recruitmentId;
	
	//返回数据
	private String result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_list_more_activity);
		setTitleBar(getString(R.string.RecruitmentCompanyListActivityTitle), View.VISIBLE, View.GONE);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		recruitmentId = bundle.getString("recruitmentId");
		initAllView();
		reigesterAllEvent();
		requestDataThread();
	}
	
	/**
	 * 请求数据线程
	 * 
	 */
	private void requestDataThread() {

		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestData();
					}
				});
		mThread.start();
	}
	
	private void requestData() {

		String url = "appOfferDetailInfo.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

			params.add(new BasicNameValuePair("RecruitmentID", recruitmentId));
//			params.add(new BasicNameValuePair("RecruitmentID", 801+""));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				RecruitmentCompanyListActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {
				result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
				RecruitmentCompanyListActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONArray resultJsonArray = responseJsonObject
						.getJSONArray("result");

				for (int j = 0; j < resultJsonArray.length(); j++) {

					Map<String, Object> item = new HashMap<String, Object>();
					item.put(key[0], resultJsonArray.getJSONObject(j).get("companyname"));
					item.put(key[1], resultJsonArray.getJSONObject(j).get("contactsname"));
					item.put(key[2],resultJsonArray.getJSONObject(j).get("phone"));
					item.put(key[3], resultJsonArray.getJSONObject(j).get("e_mail"));
					item.put(key[4], resultJsonArray.getJSONObject(j).get("address"));
					item.put(key[5], resultJsonArray.getJSONObject(j).get("registertime").toString().substring(0,10));
					item.put(key[6], resultJsonArray.getJSONObject(j).get("websites"));
					item.put(key[7], resultJsonArray.getJSONObject(j).get("postalcode"));
					item.put(key[8], resultJsonArray.getJSONObject(j).get("companyinfo"));

					items.add(item);
				}
				
				msg.what = 0;

				handler.sendMessage(msg);

			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				RecruitmentCompanyListActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			RecruitmentCompanyListActivity.this.sendExceptionMsg(err);
		}
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				recruitmentCompanyListView.setAdapter(recruitmentCompanyListAdapter);
				closeProcessDialog();

				break;
			}

		}
	};
	

	@Override
	protected void initAllView() {
		
		recruitmentCompanyListView = (ListView) findViewById(R.id.information_list);

		recruitmentCompanyListAdapter = new SimpleAdapter(
				RecruitmentCompanyListActivity.this, items,
				R.layout.recruitment_company_list_item,
				key,new int[] { R.id.company_name_text,R.id.contact_name_text,R.id.contact_way_text,
						R.id.email_text,R.id.company_address_text,R.id.register_time_text,R.id.company_website_text
						,R.id.postal_code_text});
		recruitmentCompanyListView.setAdapter(recruitmentCompanyListAdapter);
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		recruitmentCompanyListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				
				Intent intent = new Intent(RecruitmentCompanyListActivity.this,RecruitmentCompanyDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("companyName", items.get(position).get(key[0]).toString());
				bundle.putString("companyInfo", items.get(position).get(key[8]).toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});
		
	}

}
