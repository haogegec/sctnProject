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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.cache.CacheProcess;
import com.sctn.sctnet.entity.LoginInfo;
import com.sctn.sctnet.view.CustomDialog;

public class JobIntentionListActivity extends BaicActivity {

	private ListView jobIntentionlistview;

	private List<Map<String, String>> jobIntentionList = new ArrayList<Map<String, String>>();
	private MyAdapter adapter;

	private long userId;// 用户唯一标识
	private CacheProcess cacheProcess;// 缓存数据
	private String result;
	
	public static final int JOB_INTENTION_REQUEST_CODE = 0;
	
	private List<String> flagIdList = new ArrayList<String>();

	// 工作性质
	private String[] workMannerIds = { "10000100", "10000200", "10000300", "10000400", "10000500", "10000600", "10000700" };
	private String[] workManners = { "全职", "兼职", "实习", "临时", "小时工", "不限", "其他" };
	String flagId = "";
	private String whichActivity;
	
	private int isFirstCreate = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_intention_listview);
		setTitleBar(getString(R.string.JobIntentionListActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.add_bg);

		cacheProcess = new CacheProcess();
		userId = cacheProcess.getLongCacheValueInSharedPreferences(this, "userId");

		initIntent();
		initAllView();
		reigesterAllEvent();
		initDataTread(false);
	}

	@SuppressWarnings("unchecked")
	private void initIntent() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		whichActivity = bundle.getString("whichActivity");// 如果 whichActivity = “ResumeCreate”时，用户还没有求职意向，这时服务端会返回一个空的求职意向，在客户端不应该显示这个求职意向
		flagIdList = (List<String>)bundle.getSerializable("flagIdList");
		isFirstCreate = bundle.getInt("isFirstCreate");
//		if (bundle != null && bundle.getSerializable("jobIntentionList") != null) {
//			jobIntentionList = (List<Map<String, String>>) bundle.getSerializable("jobIntentionList");
//		}
	}

	@Override
	protected void initAllView() {
		jobIntentionlistview = (ListView) findViewById(R.id.lv_job_intention);
	}

	@Override
	protected void reigesterAllEvent() {
		jobIntentionlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String flagId = jobIntentionList.get(position).get("flagId");

				Intent intent = new Intent(JobIntentionListActivity.this, JobIntentionEditActivity.class);
				HashMap<String, String> map = (HashMap<String, String>) jobIntentionList.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable("map", map);
				bundle.putString("flagId", flagId);
				bundle.putString("whichActivity", "JobIntentionList");
				bundle.putString("isFirstCreate", isFirstCreate+"");
				intent.putExtras(bundle);
				startActivityForResult(intent,JOB_INTENTION_REQUEST_CODE);

			}

		});
		
		jobIntentionlistview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				
				final CustomDialog dialog = new CustomDialog(JobIntentionListActivity.this, R.style.CustomDialog);
//				dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
//				dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
				dialog.setTitle("友情提示");
				dialog.setMessage("删除该求职意向？");
				dialog.setOnPositiveListener("确定",new OnClickListener(){

					@Override
					public void onClick(View v) {
						// 删除
//						Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						
						deleteJobIntentionThread(jobIntentionList.get(position).get("flagId"));//传入要删除的求职意向的flagid
					}
					
				});
				dialog.setOnNegativeListener("取消", new OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
					
				});
				dialog.show();
				
				return false;
			}

			
		});
		
		// 添加新的求职意向
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(jobIntentionList.size() < 5){
//					String flagId = "";
//					
//					for(Map<String, Boolean> map:flagIdList){
//						int temp = -1;
//						for(Map.Entry<String, Boolean> entry: map.entrySet()) {
//							 if(!entry.getValue()){
//								 flagId = entry.getKey();
//								 temp = 1;
//								 break;
//							 }
//						}
//						if(temp > 0) break;
//					}
					

					Intent intent = new Intent(JobIntentionListActivity.this, JobIntentionEditActivity.class);
//					HashMap<String, String> map = (HashMap<String, String>) jobIntentionList.get(position);
					Bundle bundle = new Bundle();
//					bundle.putSerializable("map", map);
					if("ResumeCreate".equals(whichActivity)){
						bundle.putString("flagId", flagId);
					} else {
						bundle.putString("flagId", 1+"");
					}
					bundle.putString("whichActivity", "JobIntentionList");
					intent.putExtras(bundle);

					startActivityForResult(intent,JOB_INTENTION_REQUEST_CODE);
				} else {
					final CustomDialog dialog = new CustomDialog(JobIntentionListActivity.this, R.style.CustomDialog);
					dialog.setTitle("友情提示");
					dialog.setMessage("最多可以申请5个哦~");
					dialog.setOnPositiveListener("确定",new OnClickListener(){

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
						
					});
					dialog.show();
				}
				
				
			}
			
		});
	}

	private void deleteJobIntentionThread(final String flagId) {
		super.setProcessText("正在删除");
		showProcessDialog(true);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						deleteJobIntention(flagId);
					}
				});
		mThread.start();
	}
	
	private void deleteJobIntention(String flagId) {

		String url = "appPersonInfo!modify.app";

		Message msg = new Message();

		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("Userid", userId + ""));
		params.add(new BasicNameValuePair("flagid", flagId));
		params.add(new BasicNameValuePair("deleteFlag", "1"));
		params.add(new BasicNameValuePair("modifytype", "1"));// 保存到求职意向表中

		result = getPostHttpContent(url, params);

//		newPersonalExperienceMap.put("工作地区", workAreaValue.getText().toString());
//		newPersonalExperienceMap.put("工作性质", workStateValue.getText().toString());
//		newPersonalExperienceMap.put("欲从事行业", workmannerValue.getText().toString());
//		newPersonalExperienceMap.put("欲从事岗位", postValue.getText().toString());
//		newPersonalExperienceMap.put("企业性质", companyTypeValue.getText().toString());
//		newPersonalExperienceMap.put("月薪要求", wageValue.getText().toString());
//		newPersonalExperienceMap.put("住房要求", housewhereValue.getText().toString());
//		list.add(newPersonalExperienceMap);
		
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
				msg.what = 1;
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
	
	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void initDataTread(boolean bool) {
		showProcessDialog(bool);// false 表示有加载条
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						initData();
					}
				});
		mThread.start();
	}

	/**
	 * 请求数据，并将返回结果显示在界面上
	 */
	private void initData() {
		String url = "appPersonInfo.app";

		Message msg = new Message();

		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("Userid", userId + ""));
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}
		
		// 如果 whichActivity = “ResumeCreate”时，用户还没有求职意向，这时服务端会返回一个空的求职意向，在客户端不应该显示这个求职意向
		if("ResumeCreate".equals(whichActivity)){
			
			JSONObject responseJsonObject;
			try {
				responseJsonObject = new JSONObject(result);
				JSONArray responseJsonArray = responseJsonObject.getJSONArray("result");
				JSONObject jObject = (JSONObject) responseJsonArray.get(0);// 这时 jsonArray有一个空的求职意向，size = 1，只有一个元素
				flagId = jObject.getString("flagid");// 求职意向ID
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			msg.what = 0;
			handler.sendMessage(msg);
		} else {
			JSONObject responseJsonObject;
			try {
				responseJsonObject = new JSONObject(result);
				JSONArray responseJsonArray = responseJsonObject.getJSONArray("result");

				jobIntentionList.clear();
				for (int i = 0; i < responseJsonArray.length(); i++) {
					JSONObject jObject = (JSONObject) responseJsonArray.get(i);
					flagId = jObject.getString("flagid");// 求职意向ID
//					if (StringUtil.isBlank(jObject.getString("reccontent"))) {
//						boolean isResumeNull = SharePreferencesUtils.getSharedBooleanData(flagId);// false表示当前的求职意向是空。
//						Map<String,Boolean> flagIdmap = new HashMap<String, Boolean>();
//						flagIdmap.put(flagId, isResumeNull);
//						flagIdList.add(flagIdmap);
//						if (isResumeNull) {
							String workregionname = jObject.getString("workregionname");// 工作地区
							String workmannerid = jObject.getString("workmanner");// 工作性质
							String workmannername = "";
							for(int j=0; j<workMannerIds.length; j++){
								if(workMannerIds[j].equals(workmannerid)){
									workmannername = workManners[j];
									break;
								}
							}
							String businessrname = jObject.getString("businessname");// 欲从事行业
							String post = jObject.getString("postcodename");// 欲从事岗位
							String wagename = jObject.getString("wagename");// 月薪要求
							String companytype = jObject.getString("companytype");// 企业性质
							String housewhere = jObject.getString("housewhere");// 住房要求
							Map<String, String> map = new HashMap<String, String>();
							map.put("工作地区", workregionname);
							map.put("工作性质", workmannername);
							map.put("欲从事行业", businessrname);
							map.put("欲从事岗位", post);
							map.put("月薪要求", wagename);
							map.put("flagId", flagId);
							map.put("企业性质",companytype);
							map.put("住房要求", housewhere);
							jobIntentionList.add(map);
//						}

//					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			msg.what = 0;
			handler.sendMessage(msg);
		}

		
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI();
				closeProcessDialog();
				break;
				
			case 1:
				closeProcessDialog();
				initDataTread(true);
				break;
			}
		}
	};
	
	private void updateUI(){
		adapter = new MyAdapter(getApplicationContext(), jobIntentionList, R.layout.job_intention_item);
		jobIntentionlistview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
			case JOB_INTENTION_REQUEST_CODE:
				setResult(RESULT_OK);
				finish();
				break;
			}
		}
	}

	class MyAdapter extends BaseAdapter {

		private Context mContext;// 上下文对象
		List<Map<String, String>> list;// 这是要绑定的数据
		private int resource;// 这是要绑定的 item 布局文件
		private LayoutInflater inflater;// 布局填充器，Android系统内置的

		public MyAdapter(Context context, List<Map<String, String>> list, int resource) {
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
		public View getView(final int position, View convertView, ViewGroup parent) {

			TextView jobIntention = null;
			
//			TextView workArea = null;
//			TextView workState = null;
//			TextView wage = null;
//			TextView industry = null;
//			TextView post = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				jobIntention = (TextView)convertView.findViewById(R.id.job_intention);
				jobIntention.setText("求职意向"+(position+1)+"（"+list.get(position).get("欲从事岗位")+"）");
				// 初始化控件
//				workArea = (TextView) convertView.findViewById(R.id.tv_workArea);
//				workState = (TextView) convertView.findViewById(R.id.tv_workState);
//				wage = (TextView) convertView.findViewById(R.id.tv_wage);
//				industry = (TextView) convertView.findViewById(R.id.tv_industry);
//				post = (TextView) convertView.findViewById(R.id.tv_post);
//
//				ViewCache viewCache = new ViewCache();
//				viewCache.workArea = workArea;
//				viewCache.workState = workState;
//				viewCache.wage = wage;
//				viewCache.industry = industry;
//				viewCache.post = post;
//				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
//				workArea = viewCache.workArea;
//				workState = viewCache.workState;
//				wage = viewCache.wage;
//				industry = viewCache.industry;
//				post = viewCache.post;
			}

//			workArea.setText(list.get(position).get("工作地区"));
//			workState.setText(list.get(position).get("工作性质"));
//			wage.setText(list.get(position).get("月薪要求"));
//			industry.setText(list.get(position).get("欲从事行业"));
//			post.setText(list.get(position).get("欲从事岗位"));

			return convertView;
		}

	}

	private final class ViewCache {
		
		public TextView jobIntention;
		
//		public TextView workArea;// 工作地区
//		public TextView workState;// 工作性质
//		public TextView wage;// 月薪
//		public TextView industry;// 欲从事行业
//		public TextView post;// 欲从事岗位
	}

}
