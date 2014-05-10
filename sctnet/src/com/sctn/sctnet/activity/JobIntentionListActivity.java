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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
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
	
	private List<Map<String,Boolean>> flagIdList = new ArrayList<Map<String,Boolean>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_intention_listview);
		setTitleBar(getString(R.string.JobIntentionListActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.add);

		cacheProcess = new CacheProcess();
		userId = cacheProcess.getLongCacheValueInSharedPreferences(this, "userId");

		initIntent();
		initAllView();
		reigesterAllEvent();
		initDataTread();
	}

	private void initIntent() {
		//		Intent intent = this.getIntent();
		//		Bundle bundle = intent.getExtras();
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
				intent.putExtras(bundle);

				startActivityForResult(intent,JOB_INTENTION_REQUEST_CODE);

			}

		});
		
		// 添加新的求职意向
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(jobIntentionList.size() < 5){
					String flagId = "";
					
					for(Map<String, Boolean> map:flagIdList){
						int temp = -1;
						for(Map.Entry<String, Boolean> entry: map.entrySet()) {
							 if(!entry.getValue()){
								 flagId = entry.getKey();
								 temp = 1;
								 break;
							 }
						}
						if(temp > 0) break;
					}
					

					Intent intent = new Intent(JobIntentionListActivity.this, JobIntentionEditActivity.class);
//					HashMap<String, String> map = (HashMap<String, String>) jobIntentionList.get(position);
					Bundle bundle = new Bundle();
//					bundle.putSerializable("map", map);
					bundle.putString("flagId", flagId);
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

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void initDataTread() {
		showProcessDialog(false);
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

		JSONObject responseJsonObject;
		try {
			responseJsonObject = new JSONObject(result);
			JSONArray responseJsonArray = responseJsonObject.getJSONArray("result");

			for (int i = 0; i < responseJsonArray.length(); i++) {
				JSONObject jObject = (JSONObject) responseJsonArray.get(i);
				String flagId = jObject.getString("flagid");// 求职意向ID
				if (StringUtil.isBlank(jObject.getString("reccontent"))) {
					boolean isResumeNull = SharePreferencesUtils.getSharedBooleanData(flagId);// false表示当前的求职意向是空。
					Map<String,Boolean> flagIdmap = new HashMap<String, Boolean>();
					flagIdmap.put(flagId, isResumeNull);
					flagIdList.add(flagIdmap);
					if (isResumeNull) {
						String workregionname = jObject.getString("workregionname");// 工作地区
						String jobsstate = jObject.getString("jobsstate");// 工作性质
						String workmannername = jObject.getString("workmannername");// 欲从事行业
						String post = jObject.getString("postcodename");// 欲从事岗位
						String wagename = jObject.getString("wagename");// 月薪要求
						String companytype = jObject.getString("companytype");// 企业性质
						String housesubsidy = jObject.getString("housesubsidy");// 住房要求
						Map<String, String> map = new HashMap<String, String>();
						map.put("工作地区", workregionname);
						map.put("工作性质", jobsstate);
						map.put("欲从事行业", workmannername);
						map.put("欲从事岗位", post);
						map.put("月薪要求", wagename);
						map.put("flagId", flagId);
						map.put("企业性质",companytype);
						map.put("住房要求", housesubsidy);
						jobIntentionList.add(map);
					}

				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		msg.what = 0;
		handler.sendMessage(msg);
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI();
				closeProcessDialog();
				break;
			}
		}
	};
	
	private void updateUI(){
		adapter = new MyAdapter(getApplicationContext(), jobIntentionList, R.layout.job_intention_item);
		jobIntentionlistview.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
			case JOB_INTENTION_REQUEST_CODE:
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

			TextView workArea = null;
			TextView workState = null;
			TextView wage = null;
			TextView industry = null;
			TextView post = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				workArea = (TextView) convertView.findViewById(R.id.tv_workArea);
				workState = (TextView) convertView.findViewById(R.id.tv_workState);
				wage = (TextView) convertView.findViewById(R.id.tv_wage);
				industry = (TextView) convertView.findViewById(R.id.tv_industry);
				post = (TextView) convertView.findViewById(R.id.tv_post);

				ViewCache viewCache = new ViewCache();
				viewCache.workArea = workArea;
				viewCache.workState = workState;
				viewCache.wage = wage;
				viewCache.industry = industry;
				viewCache.post = post;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				workArea = viewCache.workArea;
				workState = viewCache.workState;
				wage = viewCache.wage;
				industry = viewCache.industry;
				post = viewCache.post;
			}

			workArea.setText(list.get(position).get("工作地区"));
			workState.setText(list.get(position).get("工作性质"));
			wage.setText(list.get(position).get("月薪要求"));
			industry.setText(list.get(position).get("欲从事行业"));
			post.setText(list.get(position).get("欲从事岗位"));

			return convertView;
		}

	}

	private final class ViewCache {
		public TextView workArea;// 工作地区
		public TextView workState;// 工作性质
		public TextView wage;// 月薪
		public TextView industry;// 欲从事行业
		public TextView post;// 欲从事岗位
	}

}
