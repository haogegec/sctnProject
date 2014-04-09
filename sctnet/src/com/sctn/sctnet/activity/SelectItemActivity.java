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

	private String which;
	
	// 外语能力
	private String[] languageIds = {"50003600","50004600","50003100","50006900","50003800","50012700","50013200","50007100","50000600"};
	private String[] languages = {"英语","法语","德语","意大利语","西班牙语","葡萄牙语","俄语","日语","阿拉伯语"};
	private String[] languageLevelIds = {"60001100","60000600","60000500","60001000","60000900","60000100","60000200","60000300","60000400","60001200"};
	private String[] languageLevels = {"国家专业八级","国家专业六级","国家专业四级","大学英语六级","大学英语四级","精通","熟练","良好","一般","差"};
	
	// 学历
	private String[] degreeIds = {"14000900","14000800","14000700","14000600","14000500","14000400","14000300","14000200","14001100"};
	private String[] degrees = {"博士","研究生","本科","大专","中专","技工","中学","小学","其他"};
	
	// 工龄、该行业累计工作时间、担任现职务时间
	private String[] workingYears = { "6个月以下", "6~12个月", "1年", "2年", "3年", "4年", "5年", "6~9年", "10~15年", "16年以上" };
	private String[] workingYearIds = {"1","6","12","24","36","48","60","72","120","192"};
	
	// 公司规模
	private String[] companyScaleIds = {"10000600","10000500","10000400","10000300","10000200","10000100","10000700"};
	private String[] companyScales = {"10人以下","10--99人","100--499人","500--999人","1000--2999人","3000人以上","其他"};
	
	// 公司性质
	private String[] companyPropertyIds = {"22000100","22000200","22000300","22000400","22000500","22000600","22000700","22000800","22000900","22001000","22001200","22001300","22001400","22001100"};
	private String[] companyProperties = {"国家机关","事业单位","国营企业","三资企业","集体企业","民营企业","私营企业","乡镇企业","有限责任","股份制企业","分支","外商企业","合资企业","其他"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_area_listview);
		
		initIntent();
		initAllView();
		reigesterAllEvent();

	}

	protected void initIntent(){
		which = getIntent().getStringExtra("which");
		
		if("SelectFirstLanguage".equals(which)){
			super.setTitleBar("选择第一外语", View.VISIBLE, View.GONE);
			for(int i=0; i<languageIds.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", languageIds[i]);
				map.put("value", languages[i]);
				listItems.add(map);
			}
		} else if("SelectLanguageLevel".equals(which)){
			super.setTitleBar("选择外语等级", View.VISIBLE, View.GONE);
			for(int i=0; i<languageLevelIds.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", languageLevelIds[i]);
				map.put("value", languageLevels[i]);
				listItems.add(map);
			}
		} else if("Degree".equals(which)){
			super.setTitleBar("选择学历", View.VISIBLE, View.GONE);
			for(int i=0; i<degreeIds.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", degreeIds[i]);
				map.put("value", degrees[i]);
				listItems.add(map);
			}
		} else if("WorkingYears".equals(which)){
			super.setTitleBar("选择您的工作年限", View.VISIBLE, View.GONE);
			for(int i=0; i<workingYearIds.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", workingYearIds[i]);
				map.put("value", workingYears[i]);
				listItems.add(map);
			}
		} else if("WorkExp".equals(which)){
			super.setTitleBar("担任现职务时间", View.VISIBLE, View.GONE);
			for(int i=0; i<workingYearIds.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", workingYearIds[i]);
				map.put("value", workingYears[i]);
				listItems.add(map);
			}
		} else if("TotalWorkingTime".equals(which)){
			super.setTitleBar("累计工作时间", View.VISIBLE, View.GONE);
			for(int i=0; i<workingYearIds.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", workingYearIds[i]);
				map.put("value", workingYears[i]);
				listItems.add(map);
			}
		} else if("CompanyScale".equals(which)){
			super.setTitleBar("选择公司规模", View.VISIBLE, View.GONE);
			for(int i=0; i<companyScaleIds.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", companyScaleIds[i]);
				map.put("value", companyScales[i]);
				listItems.add(map);
			}
		} else if("CompanyProperty".equals(which)){
			super.setTitleBar("选择公司性质", View.VISIBLE, View.GONE);
			for(int i=0; i<companyPropertyIds.length; i++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", companyPropertyIds[i]);
				map.put("value", companyProperties[i]);
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
				
				if("SelectFirstLanguage".equals(which)){
					Intent intent = getIntent();
					intent.putExtra("language", languages[position]);
					intent.putExtra("languageId", languageIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if("SelectLanguageLevel".equals(which)){
					Intent intent = getIntent();
					intent.putExtra("languageLevel", languageLevels[position]);
					intent.putExtra("languageLevelId", languageLevelIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if("Degree".equals(which)){
					Intent intent = getIntent();
					intent.putExtra("degree", degrees[position]);
					intent.putExtra("degreeId", degreeIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if("WorkingYears".equals(which)){
					Intent intent = getIntent();
					intent.putExtra("workingYear", workingYears[position]);
					intent.putExtra("workingYearId", workingYearIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if("WorkExp".equals(which)){
					Intent intent = getIntent();
					intent.putExtra("workExp", workingYears[position]);
					intent.putExtra("workExpId", workingYearIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if("TotalWorkingTime".equals(which)){
					Intent intent = getIntent();
					intent.putExtra("totalWorkingTime", workingYears[position]);
					intent.putExtra("totalWorkingTimeId", workingYearIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if("CompanyScale".equals(which)){
					Intent intent = getIntent();
					intent.putExtra("companyScale", companyScales[position]);
					intent.putExtra("companyScaleId", companyScaleIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if("CompanyProperty".equals(which)){
					Intent intent = getIntent();
					intent.putExtra("companyProperty", companyProperties[position]);
					intent.putExtra("companyPropertyId", companyPropertyIds[position]);
					setResult(RESULT_OK, intent);
					finish();
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
