package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.ItemView;

/**
 * @author wanghaoc 职位搜索功能点
 * 
 */
public class JobSearchActivity extends BaicActivity {

	private ItemView searchitemView1;
	private ItemView searchitemView2;
	private ItemView searchitemView3;
	private ItemView searchitemView4;
	private EditText search_edit;
	private ScrollView sv_scroll;
	private LinearLayout search_history_layout;

//	private ListView lv_searchList;
	private static String[] positionList = { "软件开发工程师", "数据库管理员", "销售经理", "会计", "职员", "房地产经纪" };// 职位
	private static String[] industryList = { "计算机软件/互联网", "通信", "证券/银行", "房地产", "广告", "教育/培训" };
	private static String[] workplaceList = { "北京", "上海", "深圳", "广州", "武汉", "湖南" };
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_search_activity);
		setTitleBar(getString(R.string.jobsearchTitle), View.GONE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);

		initAllView();
		initIntent();
		reigesterAllEvent();
	}

	protected void initIntent() {
		
		for(int i = 0; i < 5; i++){
			RelativeLayout layout = (RelativeLayout)getLayoutInflater().inflate(R.layout.job_search_item, null);
			TextView tv_search_criteria = (TextView)layout.getChildAt(0);// 搜索条件
			TextView tv_job_number = (TextView)layout.getChildAt(1);// 搜索结果
			tv_search_criteria.setText("“"+workplaceList[i]+"”+“"+industryList[i]+"”+“"+positionList[i]+"+“近两天”");
			tv_job_number.setText("约123456个职位");
			search_history_layout.addView(layout,i);
			
		}
		

//		for (int i = 0; i < positionList.length; i++) {
//			Map<String, Object> item = new HashMap<String, Object>();
//			item.put("position", positionList[i]);
//			item.put("industry", industryList[i]);
//			item.put("workplace", workplaceList[i]);
//			listItems.add(item);
//		}
//		lv_searchList.setAdapter(new MyAdapter(this, listItems, R.layout.job_search_item));
//		lv_searchList.setFocusable(false);// 这样才能打开页面的时候会置顶显示
//		ViewUtils.setListViewHeightBasedOnChildren(lv_searchList, 0);
	}

	@Override
	protected void initAllView() {

		search_edit = (EditText) findViewById(R.id.search_edit_bg);

		searchitemView1 = (ItemView) findViewById(R.id.itemview1);
		searchitemView1.setBackground(R.drawable.setting_na_bg_mid_pressed);
		searchitemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView1.setLabel("地区");
		searchitemView1.setValue("全国");
		searchitemView1.setDetailImageViewResource(R.drawable.detail);
		searchitemView1.setIconImageVisibility(View.GONE);

		searchitemView2 = (ItemView) findViewById(R.id.itemview2);
		searchitemView2.setBackground(R.drawable.setting_na_bg_mid_pressed);
		searchitemView2.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView2.setLabel("行业");
		searchitemView2.setDetailImageViewResource(R.drawable.detail);
		searchitemView2.setValue("所有行业");
		searchitemView2.setIconImageVisibility(View.GONE);

		searchitemView3 = (ItemView) findViewById(R.id.itemview3);
		searchitemView3.setBackground(R.drawable.setting_na_bg_mid_pressed);
		searchitemView3.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView3.setLabel("职能");
		searchitemView3.setValue("所有职能");
		searchitemView3.setDetailImageViewResource(R.drawable.detail);
		searchitemView3.setIconImageVisibility(View.GONE);

		searchitemView4 = (ItemView) findViewById(R.id.itemview4);
		searchitemView4.setBackground(R.drawable.setting_na_bg_mid_pressed);
		searchitemView4.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView4.setLabel("发布日期");
		searchitemView4.setValue("所有日期");
		searchitemView4.setDetailImageViewResource(R.drawable.detail);
		searchitemView4.setIconImageVisibility(View.GONE);

		sv_scroll = (ScrollView) findViewById(R.id.sv_scroll);
		search_history_layout = (LinearLayout) findViewById(R.id.search_history_layout);
//		lv_searchList = (ListView) findViewById(R.id.search_history_listview1);
	}

	@Override
	protected void reigesterAllEvent() {

		search_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JobSearchActivity.this, WorkSearchActivity.class);
				startActivity(intent);
			}
		});

		// 选择工作地区
		searchitemView1.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JobSearchActivity.this,SelectAreaActivity.class);
				startActivityForResult(intent,Constant.WORKINGAREA_REQUEST_CODE);
			}
		});

		// 选择行业
		searchitemView2.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new
				// Intent(JobSearchActivity.this,GuideActivity.class);
				// startActivity(intent);
			}

		});

		// 选择职能
		searchitemView3.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JobSearchActivity.this, GuideActivity.class);
				startActivity(intent);
			}
		});

		// 选择发布时间
		searchitemView4.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JobSearchActivity.this, SelectReleaseTimeActivity.class);
				startActivityForResult(intent,Constant.RELEASETIME_REQUEST_CODE);
			}

		});

	}

//	// 自定义适配器
//	class MyAdapter extends BaseAdapter {
//		private Context mContext;// 上下文对象
//		List<Map<String, Object>> list;// 这是要绑定的数据
//		private int resource;// 这是要绑定的 item 布局文件
//		private LayoutInflater inflater;// 布局填充器，Android系统内置的
//
//		public MyAdapter(Context context, List<Map<String, Object>> list, int resource) {
//			this.mContext = context;
//			this.list = list;
//			this.resource = resource;
//			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 布局填充服务
//		}
//
//		@Override
//		public int getCount() {// 数据的总数量
//			return list.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			TextView searchCriteria = null;// 搜索条件
//			TextView jobNumber = null;// 职位个数
//
//			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
//				convertView = inflater.inflate(resource, null);
//
//				// 初始化控件
//				searchCriteria = (TextView) convertView.findViewById(R.id.search_criteria);
//				jobNumber = (TextView) convertView.findViewById(R.id.job_number);
//				
//				ViewCache viewCache = new ViewCache();
//				viewCache.searchCriteria = searchCriteria;
//				viewCache.jobNumber = jobNumber;
//				convertView.setTag(viewCache);
//			} else {
//				// 初始化控件
//				ViewCache viewCache = (ViewCache) convertView.getTag();
//				searchCriteria = viewCache.searchCriteria;
//				jobNumber = viewCache.jobNumber;
//			}
//
//			searchCriteria.setText("“"+list.get(position).get("workplace")+"”+“"+list.get(position).get("industry")+"”+“"+list.get(position).get("position")+"”+“近三天”");
//			jobNumber.setText("246999个职位");
//			
//			convertView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					
//					Intent intent = new Intent(JobSearchActivity.this,JobListActivity.class);
//					startActivity(intent);
//					
//				}
//			});
//			
//			return convertView;
//		}
//
//	}
//
//	private final class ViewCache {
//		public TextView searchCriteria;
//		public TextView jobNumber;// 工作地点
//	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.WORKINGAREA_REQUEST_CODE: {
				
				String area = data.getStringExtra("area");
				searchitemView1.setValue(area);
				break;
			}
			
			case Constant.RELEASETIME_REQUEST_CODE: {
				String releaseTime = data.getStringExtra("releaseTime");
				searchitemView4.setValue(releaseTime);
				break;
			}
			
			
			}
		}
	}

}
