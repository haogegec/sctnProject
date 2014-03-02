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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sctn.sctnet.R;

/**
 * 《我申请的职位》界面
 * 
 * @author 姜勇男
 * 
 */
public class MyAppliedJobActivity extends BaicActivity {

	private ListView lv_company;
	private static String[] jobs = { "Java高级程序员", "美工", "ERP实施工程师", "js开发工程师", "Ios高级工程师", "高级算法工程师" };
	private static String[] companies = { "苹果", "三星", "摩托罗拉", "诺基亚", "小米", "华为" };
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	
	private String flag;// 判断点击的是职位申请记录还是职位收藏记录
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_applied_job_listview);

		initIntent();
		if("postApplication".equals(flag)){
			setTitleBar(getString(R.string.myAppliedJob), View.VISIBLE, View.GONE);
		} else if("postCollect".equals(flag)){
			setTitleBar(getString(R.string.myCollectedJob), View.VISIBLE, View.GONE);
		}
		initAllView();
		reigesterAllEvent();
		initData();
	}
	
	protected void initIntent(){
		Bundle bundle = getIntent().getExtras();
		flag = bundle.getString("flag");
	}

	@Override
	protected void initAllView() {
		lv_company = (ListView) findViewById(R.id.lv_company);
	}

	@Override
	protected void reigesterAllEvent() {
		lv_company.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MyAppliedJobActivity.this, CompanyInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("flag","MyAppliedJobActivity");// 职位申请记录和职位收藏记录进入同一个页面，用flag判断是点击哪个进来的
				intent.putExtras(bundle);
				startActivity(intent);

			}

		});

	}

	// 初始化数据
	protected void initData() {

		for (int i = 0; i < companies.length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("companyName", companies[i]);
			item.put("job", jobs[i]);
			listItems.add(item);
		}
		lv_company.setAdapter(new MyAdapter(this, listItems, R.layout.my_applied_job_item));

	}

	// 自定义适配器
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
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView jobName = null;
			TextView companyName = null;
			TextView applicationDate = null;
			LinearLayout status = null;
			TextView sendStatus = null;
			TextView replyStatus = null;
			ImageView collect = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				jobName = (TextView) convertView.findViewById(R.id.job_name);
				companyName = (TextView) convertView.findViewById(R.id.company_name);
				applicationDate = (TextView) convertView.findViewById(R.id.application_date);
				status = (LinearLayout) convertView.findViewById(R.id.status);
				sendStatus = (TextView) convertView.findViewById(R.id.send_status);
				replyStatus = (TextView) convertView.findViewById(R.id.reply_status);
				collect = (ImageView) convertView.findViewById(R.id.collect);

				ViewCache viewCache = new ViewCache();
				viewCache.jobName = jobName;
				viewCache.companyName = companyName;
				viewCache.applicationDate = applicationDate;
				viewCache.status = status;
				viewCache.sendStatus = sendStatus;
				viewCache.replyStatus = replyStatus;
				viewCache.collect = collect;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				jobName = viewCache.jobName;
				companyName = viewCache.companyName;
				applicationDate = viewCache.applicationDate;
				status = viewCache.status;
				sendStatus = viewCache.sendStatus;
				replyStatus = viewCache.replyStatus;
				collect = viewCache.collect;
			}
			jobName.setText(jobs[position]);
			companyName.setText(companies[position]);
			applicationDate.setText("2014-02-30");
			if("postApplication".equals(flag)){
				status.setVisibility(View.VISIBLE);
				collect.setVisibility(View.GONE);
				sendStatus.setText("已发送");
				sendStatus.setTextColor(getResources().getColor(R.color.green));
				replyStatus.setText("未回复");
				replyStatus.setTextColor(getResources().getColor(R.color.red));
			} else if("postCollect".equals(flag)){
				collect.setVisibility(View.VISIBLE);
				status.setVisibility(View.GONE);
				
				collect.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						((ImageView)v).setImageResource(R.drawable.star_none);
					}
				});
				
			}
			
			
			return convertView;
		}

	}

	private final class ViewCache {
		
		public TextView jobName;// 职位名称
		public TextView companyName;// 公司名称
		public TextView applicationDate;// 申请日期
		public LinearLayout status;
		public TextView sendStatus;// 发送状态
		public TextView replyStatus;// 回复状态
		public ImageView collect;// 收藏图标
	}

}
