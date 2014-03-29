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

import android.content.Context;
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

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.SideBar;

public class SelectCurrentIndustryActivity extends BaicActivity {

	private ListView lv_area;
	private SideBar indexBar;
	private static String[] currentIndustry;
	private static String[] currentIndustryIds;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
	private String flag;
	
	// 服务端返回结果
	private String result;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_current_industry_listview);
		initIntent();
		if(!StringUtil.isBlank(flag)&&flag.equals("education")){
			super.setTitleBar("选择专业", View.VISIBLE, View.GONE);
		}else{
			super.setTitleBar("选择目前就职的行业", View.VISIBLE, View.GONE);
		}
		

		
		initAllView();
		reigesterAllEvent();
		requestDataThread();

	}

	private void initIntent(){
		flag = getIntent().getStringExtra("flag");
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
						initCurrentIndustryThread();
					}
				});
		mThread.start();
	}

	private void initCurrentIndustryThread() {
		String url = "appCmbShow.app";
		Message msg = new Message();

		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			if("WorkExperienceEditActivity".equals(flag)){
				params.add(new BasicNameValuePair("type", "3"));
			}else if("education".equals(flag)){
				params.add(new BasicNameValuePair("type", "21"));
			}else {
				params.add(new BasicNameValuePair("type", "9"));
			}
			params.add(new BasicNameValuePair("key", "1"));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				SelectCurrentIndustryActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);

			if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果

				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");
				Iterator it = resultJsonObject.keys();
				currentIndustry = new String[resultJsonObject.length()];
				currentIndustryIds = new String[resultJsonObject.length()];
				int i=0;
				while (it.hasNext()) {
					Map<String, String> map = new HashMap<String, String>();
					String key = (String) it.next();
					String value = resultJsonObject.getString(key);
					currentIndustryIds[i] = key;
					currentIndustry[i] = value;
					map.put("id", key);
					map.put("value", value);
					listItems.add(map);
					i++;
				}

				msg.what = Constant.CURRENT_INDUSTRY;
				handler.sendMessage(msg);
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				SelectCurrentIndustryActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			SelectCurrentIndustryActivity.this.sendExceptionMsg(err);
		}
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.CURRENT_INDUSTRY:
				initUI();
				break;

			}
			closeProcessDialog();
		}
	};

	@Override
	protected void initAllView() {
		// TODO Auto-generated method stub
		lv_area = (ListView) findViewById(R.id.lv_currentIndustry);

		lv_area.setAdapter(new MyAdapter(this, listItems, R.layout.select_area_item));
		// indexBar = (SideBar) findViewById(R.id.sideBar);
		// indexBar.setListView(lv_area);
	}

	@Override
	protected void reigesterAllEvent() {
		lv_area.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               
				Intent intent = getIntent();
				intent.putExtra("currentIndustry", currentIndustry[position]);
				intent.putExtra("currentIndustryId", currentIndustryIds[position]);
				setResult(RESULT_OK, intent);
				finish();

			}

		});
	}

	// 初始化城市列表
	protected void initUI() {

		lv_area.setAdapter(new MyAdapter(this, listItems, R.layout.select_area_item));

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
			TextView area = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				area = (TextView) convertView.findViewById(R.id.area);
				ViewCache viewCache = new ViewCache();
				viewCache.area = area;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				area = viewCache.area;
			}

			area.setText(list.get(position).get("value"));

			return convertView;
		}

	}

	private final class ViewCache {
		public TextView area;// 地区
	}
}