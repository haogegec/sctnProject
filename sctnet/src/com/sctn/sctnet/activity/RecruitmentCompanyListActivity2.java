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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.Utils.ViewUtils;
import com.sctn.sctnet.activity.JobListActivity.MyAdapter;

/**
 * 现场招聘会的公司列表
 * 
 * @author xueweiwei
 * 
 */
public class RecruitmentCompanyListActivity2 extends BaicActivity {

	private ListView recruitmentCompanyListView;
	private CompanyAdapter recruitmentCompanyListAdapter;
//	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
//	private String[] key = { "company_name_text", "partitionlist_name_text", "job_name_text", "contact_name_text", "contact_way_text", "company_address_text" };

//	private ListView recruiment_list;// 提供的职位
	private List<Map<String,Object>> listlist = new ArrayList<Map<String,Object>>();
	
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
		//		partitionlist = bundle.getString("partitionlist");
		initAllView();
		reigesterAllEvent();
		requestDataThread();
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

		String url = "appOfferDetailInfo.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

			params.add(new BasicNameValuePair("RecruitmentID", recruitmentId));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {
				result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
				sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONArray resultJsonArray = responseJsonObject.getJSONArray("result");

				String companyName = resultJsonArray.getJSONObject(0).get("companyname").toString();
				String zhanweihao = resultJsonArray.getJSONObject(0).get("number").toString();
				List<Map<String, Object>> items = new LinkedList<Map<String, Object>>();
				for (int j = 0; j < resultJsonArray.length(); j++) {
					String companyname = resultJsonArray.getJSONObject(j).get("companyname").toString();
					String jobName = resultJsonArray.getJSONObject(j).get("jobsname").toString();// 单位名称
					String number = resultJsonArray.getJSONObject(j).get("number").toString();// 展位号
					
					if(zhanweihao.equals(number)){
						Map<String,Object> mapItem = new HashMap<String, Object>();
						mapItem.put("jobsname", jobName);
						items.add(mapItem);
						if(j == resultJsonArray.length()-1){
							Map<String,Object> map = new HashMap<String, Object>();
							map.put("companyName", companyName);
							map.put("number", zhanweihao);
							map.put("jobNameList", items);
							listlist.add(map);
						}
					} else {
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("companyName", companyName);
						map.put("number", zhanweihao);
						map.put("jobNameList", items);
						listlist.add(map);
						companyName = companyname;
						zhanweihao = number;
						items = new LinkedList<Map<String,Object>>();
						Map<String,Object> mapItem = new HashMap<String, Object>();
						mapItem.put("jobsname", jobName);
						items.add(mapItem);
					}
				}
				
				
//				for (int j = 0; j < resultJsonArray.length(); j++) {
//
//					Map<String, Object> item = new HashMap<String, Object>();
//					item.put(key[0], resultJsonArray.getJSONObject(j).get("companyname"));
//					item.put(key[1], resultJsonArray.getJSONObject(j).get("number"));
//					item.put(key[2], resultJsonArray.getJSONObject(j).get("jobsname"));
//					item.put(key[3], resultJsonArray.getJSONObject(j).get("contactsname"));
//					item.put(key[4], resultJsonArray.getJSONObject(j).get("phone"));
//					item.put(key[5], resultJsonArray.getJSONObject(j).get("address"));
//
//					items.add(item);
//				}

				msg.what = 0;

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
			case 0:
				recruitmentCompanyListAdapter = new CompanyAdapter(RecruitmentCompanyListActivity2.this, listlist, R.layout.recruitment_company_list2);
				recruitmentCompanyListView.setAdapter(recruitmentCompanyListAdapter);
				closeProcessDialog();

				break;
			}

		}
	};

	@Override
	protected void initAllView() {

//		recruiment_list = (ListView) findViewById(R.id.recruiment_list);
		
		recruitmentCompanyListView = (ListView) findViewById(R.id.information_list);

//		recruitmentCompanyListAdapter = new SimpleAdapter(RecruitmentCompanyListActivity.this, items, R.layout.recruitment_company_list_item, key, new int[] { R.id.company_name_text, R.id.partitionlist_name_text, R.id.job_name_text });
//		recruitmentCompanyListView.setAdapter(recruitmentCompanyListAdapter);

	}

	@Override
	protected void reigesterAllEvent() {

		//		recruitmentCompanyListView.setOnItemClickListener(new OnItemClickListener(){
		//
		//			@Override
		//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
		//					long arg3) {
		//				
		//				
		//				Intent intent = new Intent(RecruitmentCompanyListActivity.this,RecruitmentCompanyDetailActivity.class);
		//				Bundle bundle = new Bundle();
		//				bundle.putString("companyName", items.get(position).get(key[0]).toString());
		//				bundle.putString("companyInfo", items.get(position).get(key[8]).toString());
		//				intent.putExtras(bundle);
		//				startActivity(intent);
		//			}
		//			
		//		});

	}
	
	class CompanyAdapter extends BaseAdapter{
		
		private Context mContext;// 上下文对象
		List<Map<String, Object>> list;// 这是要绑定的数据
		private int resource;// 这是要绑定的 item 布局文件
		private LayoutInflater inflater;// 布局填充器，Android系统内置的
		
		public CompanyAdapter(Context context, List<Map<String, Object>> list, int resource) {
			this.mContext = context;
			this.list = list;
			this.resource = resource;
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 布局填充服务
		}

		@Override
		public int getCount() {// 数据的总数量
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			TextView companyName;
			TextView zhanweihao;
			TextView jobs;
			
			if(convertView == null){
				convertView = inflater.inflate(resource, null);
				companyName = (TextView)convertView.findViewById(R.id.company_name_text);
				zhanweihao = (TextView)convertView.findViewById(R.id.partitionlist_name_text);
				jobs = (TextView)convertView.findViewById(R.id.jobs);
				
				ViewCache viewCache = new ViewCache();
				viewCache.companyName = companyName;
				viewCache.zhanweihao = zhanweihao;
				viewCache.jobs = jobs;
				
				convertView.setTag(viewCache);
			} else {
				ViewCache viewCache = (ViewCache)convertView.getTag();
				companyName = viewCache.companyName;
				zhanweihao = viewCache.zhanweihao;
				jobs = viewCache.jobs;
			}
			
			companyName.setText(listlist.get(position).get("companyName").toString());
			zhanweihao.setText(listlist.get(position).get("number").toString());
			
			List<Map<String, Object>> jobsNameList = (List<Map<String, Object>>)listlist.get(position).get("jobNameList");
			String jobNames = "";
			for(Map<String,Object> map : jobsNameList){
				jobNames += map.get("jobsname").toString()+"\t，";
			}
			jobs.setText(jobNames.substring(0,jobNames.lastIndexOf('，')));
			
			
//			recruitmentCompanyListAdapter = new SimpleAdapter(RecruitmentCompanyListActivity.this, items, R.layout.recruitment_company_list_item, key, new int[] { R.id.company_name_text, R.id.partitionlist_name_text, R.id.job_name_text });
//			jobList.setAdapter(new SimpleAdapter(getApplicationContext(), jobsNameList, R.layout.select_area_item, new String[] {"jobsname"}, new int[] {R.id.area}));
//			ViewUtils.setListViewHeightBasedOnChildren(jobList);
			
			return convertView;
		}
		
	}
	
	private final class ViewCache {
		public TextView companyName;// 单位名称
		public TextView zhanweihao;// 展位号
//		public ListView jobList;
		public TextView jobs;// 职位
	}

	
}
