package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;

public class JobListActivity extends BaicActivity {

	private MyAdapter jobListAdapter;
	private ListView jobList;
	private List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();

	Map<Integer,Object> idMaps = new HashMap<Integer,Object>();
	Map<Integer, Boolean> checkBoxState = new HashMap<Integer, Boolean>();// 记录checkbox的状态
	
	private Button btn_apply;// 申请
	private Button btn_collect;// 收藏
	private Button btn_share;// 分享
	
	private String[] jobNames = {"1234","2345","3456","4567","5678","6789","7890","8901","9012","0123","asdf","jkll","yuiop","qwert"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_list_activity);
		

		initAllView();
		initIntent();
		reigesterAllEvent();
	}
	
	protected void initIntent() {
		setTitleBar("共"+245+"个职位", View.VISIBLE, View.GONE);
		for (int i = 0; i < jobNames.length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("sequenceId", i+1);// 职位
			item.put("jobName", jobNames[i]);// 职位
			item.put("company", "家里蹲公司");// 公司
			item.put("degree", "本科");// 学历要求
			item.put("workingYears", "3年以上");// 工作年限要求
			item.put("workplace", "四川");// 工作地区
			item.put("releaseTime", "2014-02-20");// 发布时间
			items.add(item);
		}
		jobListAdapter = new MyAdapter(this, items, R.layout.job_list_item);
		jobList.setAdapter(jobListAdapter);
		jobListAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void initAllView() {
		jobList = (ListView)findViewById(R.id.lv_jobList);
//		jobListAdapter = new MyAdapter(this, items, R.layout.job_list_item);
//		jobList.setAdapter(jobListAdapter);
//		jobListAdapter.notifyDataSetChanged();
		
		btn_apply = (Button)findViewById(R.id.btn_apply);
		btn_collect = (Button)findViewById(R.id.btn_collect);
		btn_share = (Button)findViewById(R.id.btn_share);
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 申请
		btn_apply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(idMaps.size() == 0){
					Toast.makeText(getApplicationContext(), "请选择职位", Toast.LENGTH_LONG).show();
				} else {
					for (Map.Entry<Integer, Object> entry : idMaps.entrySet()) {
						Toast.makeText(getApplicationContext(), "申请的ID："+entry.getValue(), Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
		// 收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(idMaps.size() == 0){
					Toast.makeText(getApplicationContext(), "请选择职位", Toast.LENGTH_LONG).show();
				} else {
					for (Map.Entry<Integer, Object> entry : idMaps.entrySet()) {
						Toast.makeText(getApplicationContext(), "申请："+entry.getValue(), Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
		// 分享
		btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "分享", Toast.LENGTH_LONG).show();
			}
		});
		
		
		
	}

	class MyAdapter extends BaseAdapter {
		
		private Context mContext;// 上下文对象
		List<Map<String, Object>> list;// 这是要绑定的数据
		private int resource;// 这是要绑定的 item 布局文件
		private LayoutInflater inflater;// 布局填充器，Android系统内置的
		
		public MyAdapter(Context context, List<Map<String, Object>> list, int resource) {
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
			
			TextView jobName = null;
			TextView company = null;
			TextView degree = null;
			TextView workingYears = null;
			TextView workplace = null;
			TextView releaseTime = null;
			CheckBox checkbox = null;
			
			
			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				jobName = (TextView) convertView.findViewById(R.id.jobName);
				company = (TextView) convertView.findViewById(R.id.company);
				degree = (TextView) convertView.findViewById(R.id.degree);
				workingYears = (TextView) convertView.findViewById(R.id.workingYears);
				workplace = (TextView) convertView.findViewById(R.id.workplace);
				releaseTime = (TextView) convertView.findViewById(R.id.release_time);
				checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
				
				ViewCache viewCache = new ViewCache();
				viewCache.jobName = jobName;
				viewCache.company = company;
				viewCache.degree = degree;
				viewCache.workingYears = workingYears;
				viewCache.workplace = workplace;
				viewCache.releaseTime = releaseTime;
				viewCache.checkbox = checkbox;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				jobName = viewCache.jobName;
				company = viewCache.company;
				degree = viewCache.degree;
				workingYears = viewCache.workingYears;
				workplace = viewCache.workplace;
				releaseTime = viewCache.releaseTime;
				checkbox = viewCache.checkbox;
			}

			final int sequenceId = (Integer)list.get(position).get("sequenceId");
			jobName.setText(list.get(position).get("jobName").toString());
			company.setText(list.get(position).get("company").toString());
			degree.setText(list.get(position).get("degree").toString());
			workingYears.setText(list.get(position).get("workingYears").toString());
			workplace.setText(list.get(position).get("workplace").toString());
			releaseTime.setText(list.get(position).get("releaseTime").toString());
			
			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						checkBoxState.put(position, isChecked);
						idMaps.put(position, sequenceId);
					} else {
						checkBoxState.remove(position);
						idMaps.remove(position);
					}
				}
			});
			checkbox.setChecked(checkBoxState.get(position) == null ? false : true);
			
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(JobListActivity.this,CompanyInfoActivity.class);
					startActivity(intent);
					
				}
			});
			
			return convertView;
		}
		
	}
	
	private final class ViewCache {
		public TextView jobName;// 职位名称
		public TextView company;// 公司
		public TextView degree;// 学历要求
		public TextView workingYears;// 工作年限
		public TextView workplace;// 工作地点
		public TextView releaseTime;// 工作地点
		public CheckBox checkbox;
	}
	
}
