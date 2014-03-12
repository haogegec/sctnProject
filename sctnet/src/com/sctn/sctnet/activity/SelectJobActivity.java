package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.SideBar;

public class SelectJobActivity extends BaicActivity {

	private ListView lv_job;
	private static String[] jobs;
	private static String[] jobIds;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();

	// 服务端返回结果
	private String result;
	private com.alibaba.fastjson.JSONObject responseJsonObject = null;// 返回结果存放在该json对象中
	private String key;// 职务的id

	private Builder builder;// 学历选择
	private Dialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_area_listview);
		super.setTitleBar("选择职位类别", View.VISIBLE, View.GONE);

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

		String url = "appCmbShow.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "8"));
		params.add(new BasicNameValuePair("key", "1"));
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
		responseJsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
		if (responseJsonObject.get("resultcode").toString().equals("0")) {

			com.alibaba.fastjson.JSONObject json = responseJsonObject.getJSONObject("result");
			Set<Entry<String, Object>> set = json.entrySet();
			Iterator<Entry<String, Object>> iter = set.iterator();
			jobs = new String[set.size()];
			jobIds = new String[set.size()];
			int i = 0;
			while (iter.hasNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Entry obj = iter.next();
				map.put("id", (String) obj.getKey());
				map.put("value", (String) obj.getValue());
				jobs[i] = (String) obj.getValue();
				jobIds[i] = (String) obj.getKey();
				listItems.add(map);
				i++;
			}
			m.what = Constant.JOB;
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			SelectJobActivity.this.sendExceptionMsg(err);
		}

		handler.sendMessage(m);
	}

	private void initJobDataThread(int position) {

		String url = "appCmbShow.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "13"));
		params.add(new BasicNameValuePair("key", jobIds[position]));
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
		responseJsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
		if (responseJsonObject.get("resultcode").toString().equals("0")) {

			com.alibaba.fastjson.JSONObject json = responseJsonObject.getJSONObject("result");
			Set<Entry<String, Object>> set = json.entrySet();
			Iterator<Entry<String, Object>> iter = set.iterator();
			jobs = new String[set.size()];
			jobIds = new String[set.size()];
			int i = 0;
			while (iter.hasNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Entry obj = iter.next();
				map.put("id", (String) obj.getKey());
				map.put("value", (String) obj.getValue());
				jobs[i] = (String) obj.getValue();
				jobIds[i] = (String) obj.getKey();
				listItems.add(map);
				i++;
			}
			m.what = Constant.DETAIL_JOB;
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			SelectJobActivity.this.sendExceptionMsg(err);
		}

		handler.sendMessage(m);
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.JOB:
				initUI();
				break;
			case Constant.DETAIL_JOB:
				initJobData();
				break;
			}
			closeProcessDialog();
		}
	};

	@Override
	protected void initAllView() {
		lv_job = (ListView) findViewById(R.id.lv_area);
		builder = new AlertDialog.Builder(SelectJobActivity.this);
	}

	@Override
	protected void reigesterAllEvent() {
		lv_job.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

				showProcessDialog(false);
				Thread mThread = new Thread(new Runnable() {// 启动新的线程，
							@Override
							public void run() {
								initJobDataThread(position);
							}
						});
				mThread.start();
			}

		});
	}

	protected void initUI() {
		lv_job.setAdapter(new MyAdapter(this, listItems, R.layout.select_area_item));
	}

	protected void initJobData() {
		builder.setTitle("请选择您的职务");
		builder.setSingleChoiceItems(jobs, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Intent intent = getIntent();
				intent.putExtra("jobId", jobIds[which]);
				intent.putExtra("job", jobs[which]);
				setResult(RESULT_OK,intent);
				finish();
				
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.show();

	}

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
				viewCache.area = job;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				job = viewCache.area;
			}

			job.setText(list.get(position).get("value"));

			return convertView;
		}

	}

	private final class ViewCache {
		public TextView area;// 地区
	}
}