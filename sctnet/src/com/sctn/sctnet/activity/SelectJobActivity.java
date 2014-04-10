package com.sctn.sctnet.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

public class SelectJobActivity extends BaicActivity {

	private ListView lv_job;
	private MyAdapter jobListAdapter;
//	private MyAdapter subJobListAdapter;
	
//	private static String[] jobs;
//	private static String[] jobIds;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
//	private List<Map<String, String>> jobItems = new ArrayList<Map<String, String>>();
	
	// 服务端返回结果
	private String result;
	private String key;// 职务的id

//	private Builder builder;// 学历选择
//	private Dialog dialog;

	private String flag;// WorkExperienceEditActivity、SelectJobExpActivity两个页面跳过来之后，根据flag，请求的服务端方法不同

	private int itemCount; // 当前窗口可见项总数
	private int visibleLastIndex = 0;// 最后的可视项索引
	private int page = 1;
//	private int page2 = 1;
	private int total = 0;// 总条数
	private int pageSize = Constant.PageSize;
	private View footViewBar;// 下滑加载条
	private int count;// 一次可以显示的条数（=pageSize或者小于）
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_area_listview);
		super.setTitleBar("选择职位类别", View.VISIBLE, View.GONE);

		initIntent();
		initAllView();
		reigesterAllEvent();
		requestDataThread(0);

	}

	private void initIntent() {
		flag = getIntent().getStringExtra("flag");
	}

	// /**
	// * 请求数据线程
	// *
	// */
	// private void requestJobThread() {
	// showProcessDialog(false);
	// Thread mThread = new Thread(new Runnable() {// 启动新的线程，
	// @Override
	// public void run() {
	// requestJob();
	// }
	// });
	// mThread.start();
	// }
	//
	// private void requestJob() {
	//
	// String url = "appCmbShow.app";
	// List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("type", "4"));
	// params.add(new BasicNameValuePair("key", "1"));
	// result = getPostHttpContent(url, params);
	//
	// if (StringUtil.isExcetionInfo(result)) {
	// SelectJobActivity.this.sendExceptionMsg(result);
	// return;
	// }
	//
	// if (StringUtil.isBlank(result)) {
	// result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
	// SelectJobActivity.this.sendExceptionMsg(result);
	// }
	// Message m = new Message();
	// responseJsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
	// if (responseJsonObject.get("resultcode").toString().equals("0")) {
	//
	// com.alibaba.fastjson.JSONObject json =
	// responseJsonObject.getJSONObject("result");
	// Set<Entry<String, Object>> set = json.entrySet();
	// Iterator<Entry<String, Object>> iter = set.iterator();
	// jobs = new String[set.size()];
	// jobIds = new String[set.size()];
	// int i = 0;
	// while (iter.hasNext()) {
	// Map<String, String> map = new HashMap<String, String>();
	// Entry obj = iter.next();
	// map.put("id", (String) obj.getKey());
	// map.put("value", (String) obj.getValue());
	// jobs[i] = (String) obj.getValue();
	// jobIds[i] = (String) obj.getKey();
	// listItems.add(map);
	// i++;
	// }
	// m.what = Constant.JOB;
	// } else {
	// String errorResult = (String) responseJsonObject.get("result");
	// String err = StringUtil.getAppException4MOS(errorResult);
	// SelectJobActivity.this.sendExceptionMsg(err);
	// }
	//
	// handler.sendMessage(m);
	// }

	/**
	 * 请求数据线程
	 * 
	 */
	private void requestDataThread(final int i) {
		if (i == 0) {
			showProcessDialog(false);
		}
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestData(i);
					}
				});
		mThread.start();
	}

	private void requestData(int j) {

		String url = "appCmbShow.app";
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		if ("WorkExperienceEditActivity".equals(flag)) {
			params.add(new BasicNameValuePair("type", "4"));
		} else {
			params.add(new BasicNameValuePair("type", "8"));
		}
		params.add(new BasicNameValuePair("key", "1"));
		params.add(new BasicNameValuePair("page", page + ""));
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			SelectJobActivity.this.sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			SelectJobActivity.this.sendExceptionMsg(result);
		}
		Message m = new Message();
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultcode").toString().equals("0")) {
			total = responseJsonObject.getInteger("resultCount");
			JSONArray jArray = responseJsonObject.getJSONArray("result");
			if (jArray.size() > 15) {
				count = 15;
			} else {
				count = jArray.size();
			}
			for (int i = 0; i < jArray.size(); i++) {
				JSONObject jObject = jArray.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", jObject.getString("key"));
				map.put("value", jObject.getString("value"));
				listItems.add(map);
			}

			// Set<Entry<String, Object>> set = json.entrySet();
			// Iterator<Entry<String, Object>> iter = set.iterator();
//			 jobs = new String[set.size()];
			// jobIds = new String[set.size()];
			// int i = 0;
			// while (iter.hasNext()) {
			// Map<String, String> map = new HashMap<String, String>();
			// Entry obj = iter.next();
			// map.put("id", (String) obj.getKey());
			// map.put("value", (String) obj.getValue());
			// jobs[i] = (String) obj.getValue();
			// jobIds[i] = (String) obj.getKey();
			// listItems.add(map);
			// i++;
			// }
			
			if (j == 0) {
				m.what = Constant.JOB;
			} else {
				m.what = Constant.JOB_UPDATE;
			}
			
			handler.sendMessage(m);
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			SelectJobActivity.this.sendExceptionMsg(err);
		}

		
	}

//	/**
//	 * 
//	 * @param position
//	 * @param index  index=0时第一次加载，index=1时分页请求
//	 */
//	private void initJobDataThread(int position,int index) {
//
//		String url = "appCmbShow.app";
//
//		Message msg = new Message();
//		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//		if ("WorkExperienceEditActivity".equals(flag)) {
//			params.add(new BasicNameValuePair("type", "5"));
//		} else {
//			params.add(new BasicNameValuePair("type", "13"));
//		}
//		params.add(new BasicNameValuePair("key", listItems.get(position).get("id")));
//		result = getPostHttpContent(url, params);
//
//		if (StringUtil.isExcetionInfo(result)) {
//			SelectJobActivity.this.sendExceptionMsg(result);
//			return;
//		}
//
//		if (StringUtil.isBlank(result)) {
//			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
//			SelectJobActivity.this.sendExceptionMsg(result);
//		}
//		JSONObject responseJsonObject = JSONObject.parseObject(result);
//		if (responseJsonObject.get("resultcode").toString().equals("0")) {
//			total = responseJsonObject.getInteger("resultCount");
//			JSONArray jArray = responseJsonObject.getJSONArray("result");
//			if (jArray.size() > 15) {
//				count = 15;
//			} else {
//				count = jArray.size();
//			}
//			for (int i = 0; i < jArray.size(); i++) {
//				JSONObject jObject = jArray.getJSONObject(i);
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("id", jObject.getString("key"));
//				map.put("value", jObject.getString("value"));
//				jobItems.add(map);
//			}
//			
//			msg.what = index == 0 ? Constant.DETAIL_JOB : Constant.DETAIL_JOB_UPDATE;
//			handler.sendMessage(msg);
//		} else {
//			String errorResult = (String) responseJsonObject.get("result");
//			String err = StringUtil.getAppException4MOS(errorResult);
//			SelectJobActivity.this.sendExceptionMsg(err);
//		}
//		
//		
//	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.JOB:
				initUI();
				break;
				
			case Constant.JOB_UPDATE:
				updateUI();
				break;
			case Constant.DETAIL_JOB:
//				initJob();
				break;
			case Constant.DETAIL_JOB_UPDATE:
//				updateJob();
				break;
			}
			closeProcessDialog();
		}
	};

	@Override
	protected void initAllView() {
		lv_job = (ListView) findViewById(R.id.lv_area);
//		builder = new AlertDialog.Builder(SelectJobActivity.this);
		footViewBar = View.inflate(SelectJobActivity.this, R.layout.foot_view_loading, null);
		jobListAdapter = new MyAdapter(this, listItems, R.layout.select_area_item);
//		subJobListAdapter = new MyAdapter(this, jobItems, R.layout.select_area_item);
		lv_job.setAdapter(jobListAdapter);
		lv_job.setOnScrollListener(listener);
	}

	@Override
	protected void reigesterAllEvent() {
		lv_job.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

//				showProcessDialog(false);
//				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
//							@Override
//							public void run() {
//								initJobDataThread(position,0);
//							}
//						});
//				mThread.start();
				
				Intent intent = new Intent(SelectJobActivity.this,SelectItemActivity.class);
				intent.putExtra("list", (Serializable)listItems);
				intent.putExtra("which", "SelectJob");
				intent.putExtra("id", listItems.get(position).get("id"));
				intent.putExtra("value", listItems.get(position).get("value"));
				startActivityForResult(intent,Constant.DETAIL_JOB);
				
				
			}

		});
	}

	protected void initUI() {
		if (total > pageSize * page) {
			lv_job.addFooterView(footViewBar);// 添加list底部更多按钮
		}
		lv_job.setAdapter(jobListAdapter);
	}
	
	protected void updateUI(){
		if (total <= pageSize * page) {
			lv_job.removeFooterView(footViewBar);// 添加list底部更多按钮
		}
		jobListAdapter.notifyDataSetChanged();
	}

//	protected void initJob() {
////		builder.setTitle("请选择您的职务");
////		builder.setSingleChoiceItems(jobs, 0, new DialogInterface.OnClickListener() {
////
////			@Override
////			public void onClick(DialogInterface dialog, int which) {
////
////				Intent intent = getIntent();
////				intent.putExtra("jobId", jobIds[which]);
////				intent.putExtra("job", jobs[which]);
////				setResult(RESULT_OK, intent);
////				finish();
////
////				dialog.dismiss();
////			}
////
////		});
////		dialog = builder.create();
////		dialog.show();
//		
//		if (total > pageSize * page) {
//			lv_job.addFooterView(footViewBar);// 添加list底部更多按钮
//		}
//		lv_job.setAdapter(subJobListAdapter);
//		
//
//	}
//	
//	protected void updateJob(){
//		if (total <= pageSize * page) {
//			lv_job.removeFooterView(footViewBar);// 添加list底部更多按钮
//		}
//		subJobListAdapter.notifyDataSetChanged();
//	}
	
	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			itemCount = visibleItemCount;
			visibleLastIndex = firstVisibleItem + visibleItemCount - 1;

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				if (view.getLastVisiblePosition() == jobListAdapter.getCount()) {
					page++;
					requestDataThread(1);// 滑动list请求数据
				}
			}

		}
	};
	
//	private AbsListView.OnScrollListener listener2 = new AbsListView.OnScrollListener() {
//
//		@Override
//		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//			itemCount = visibleItemCount;
//			visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
//
//		}
//
//		@Override
//		public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//				if (view.getLastVisiblePosition() == subJobListAdapter.getCount()) {
//					page2++;
//					initJobDataThread(position,1);// 滑动list请求数据
//				}
//			}
//
//		}
//	};

	// 自定义适配器
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
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView job = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				job = (TextView) convertView.findViewById(R.id.area);
				ViewCache viewCache = new ViewCache();
				viewCache.job = job;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				job = viewCache.job;
			}

			job.setText(list.get(position).get("value"));

			return convertView;
		}

	}

	private final class ViewCache {
		public TextView job;// 
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.DETAIL_JOB:
				String currentJob = data.getStringExtra("currentJob");
				String currentJobId = data.getStringExtra("currentJobId");
				
				Intent intent = getIntent();
				intent.putExtra("job", data.getStringExtra("currentJob"));
				intent.putExtra("jobId", data.getStringExtra("currentJobId"));
				setResult(RESULT_OK, intent);
				finish();
				break;

			

			}
		}
	}
	
	
}