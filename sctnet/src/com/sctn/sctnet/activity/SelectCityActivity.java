package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;

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
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SortUtil;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

public class SelectCityActivity extends BaicActivity {

	private ListView lv_city;

	private String provinceId;
	private String province;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();

	private String result;// 服务端返回的结果

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_area_listview);
		super.setTitleBar("选择城市", View.VISIBLE, View.GONE);

		initIntent();
		initAllView();
		reigesterAllEvent();
		requestCityThread();

	}

	protected void initIntent(){
		provinceId = getIntent().getStringExtra("provinceId");
		province = getIntent().getStringExtra("province");
	}
	
	@Override
	protected void initAllView() {
		lv_city = (ListView) findViewById(R.id.lv_area);
	}

	@Override
	protected void reigesterAllEvent() {
		lv_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = getIntent();
				intent.putExtra("city", listItems.get(position).get("value"));
				intent.putExtra("cityId", listItems.get(position).get("id"));
				intent.putExtra("province", province);
				intent.putExtra("provinceId", provinceId);
				setResult(RESULT_OK, intent);
				finish();
			}

		});
	}

	private void requestCityThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestCity();
					}
				});
		mThread.start();
	}

	private void requestCity() {
		String url = "appCmbShow.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", Constant.CITY_TYPE + ""));
		params.add(new BasicNameValuePair("key", provinceId));
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			SelectCityActivity.this.sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			SelectCityActivity.this.sendExceptionMsg(result);
		}
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultcode").toString().equals("0")) {

			JSONObject json = responseJsonObject.getJSONObject("result");
			Set<Entry<String, Object>> set = json.entrySet();
			Iterator<Entry<String, Object>> iter = set.iterator();
			int i = 0;
			while (iter.hasNext()) {
				Map<String, String> map = new HashMap<String, String>();
				Entry obj = iter.next();
				map.put("id", (String) obj.getKey());
				map.put("value", (String) obj.getValue());
				listItems.add(map);
				i++;
			}
			
			// 把list的元素（map）按key="id"升序排序（排序的关键是：SortUtil类里的compare（）方法）
			Collections.sort(listItems, new SortUtil());
			
			msg.what = Constant.CITY_TYPE;
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			SelectCityActivity.this.sendExceptionMsg(err);
		}

		handler.sendMessage(msg);
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.CITY_TYPE:
				initUI();
				break;
			}
			closeProcessDialog();
		}
	};

	// 初始化城市列表
	protected void initUI() {
		lv_city.setAdapter(new CityAdapter(this, listItems, R.layout.select_area_item));
	}

	// 自定义适配器
	class CityAdapter extends BaseAdapter {
		private Context mContext;// 上下文对象
		List<Map<String, String>> list;// 这是要绑定的数据
		private int resource;// 这是要绑定的 item 布局文件
		private LayoutInflater inflater;// 布局填充器，Android系统内置的

		public CityAdapter(Context context, List<Map<String, String>> list, int resource) {
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
			TextView city = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				city = (TextView) convertView.findViewById(R.id.area);
				ViewCache viewCache = new ViewCache();
				viewCache.city = city;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				city = viewCache.city;
			}

			city.setText(list.get(position).get("value"));

			return convertView;
		}

	}

	private final class ViewCache {
		public TextView city;// 市
	}

}
