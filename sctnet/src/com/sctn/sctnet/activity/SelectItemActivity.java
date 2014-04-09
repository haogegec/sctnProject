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
import org.json.JSONArray;

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
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

public class SelectItemActivity extends BaicActivity {

	private ListView listview;

	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();

	private String result;// 服务端返回的结果

	private String whichActivity;
	
	private String[] languageIds = {"50003600","50004600","50003100","50006900","50003800","50012700","50013200","50007100","50000600"};
	private String[] languages = {"英语","法语","德语","意大利语","西班牙语","葡萄牙语","俄语","日语","阿拉伯语"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_area_listview);
		

		initIntent();
		initAllView();
		reigesterAllEvent();

	}

	protected void initIntent(){
		whichActivity = getIntent().getStringExtra("whichActivity");
		
		if("SelectFirstLanguage".equals(whichActivity)){
			super.setTitleBar("选择第一外语", View.VISIBLE, View.GONE);
			for(int i=0; i<languageIds.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", languageIds[i]);
				map.put("value", languages[i]);
				listItems.add(map);
			}
		} else if("SelectLanguageLevel".equals(whichActivity)){
			super.setTitleBar("选择外语等级", View.VISIBLE, View.GONE);
			for(int i=0; i<languageIds.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", languageIds[i]);
				map.put("value", languages[i]);
				listItems.add(map);
			}
		}
		
	}
	
	@Override
	protected void initAllView() {
		listview = (ListView) findViewById(R.id.lv_area);
		listview.setAdapter(new CityAdapter(this, listItems, R.layout.select_area_item));
	}

	@Override
	protected void reigesterAllEvent() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if("SelectFirstLanguage".equals(whichActivity)){
					Intent intent = getIntent();
					intent.putExtra("language", languages[position]);
					intent.putExtra("languageId", languageIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if("SelectLanguageLevel".equals(whichActivity)){
					
				}
				
			}

		});
	}

	// 初始化城市列表
	protected void initUI() {
		listview.setAdapter(new CityAdapter(this, listItems, R.layout.select_area_item));
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
			TextView item = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				item = (TextView) convertView.findViewById(R.id.area);
				ViewCache viewCache = new ViewCache();
				viewCache.item = item;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				item = viewCache.item;
			}

			item.setText(list.get(position).get("value"));

			return convertView;
		}

	}

	private final class ViewCache {
		public TextView item;
	}

}
