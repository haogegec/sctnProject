package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.ItemView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 《谁看了我的简历》界面
 * @author 姜勇男
 *
 */
public class ReadMyResumeActivity extends BaicActivity {

	private ListView lv_company;
	private static String[] companies = {"苹果","三星","摩托罗拉","诺基亚","小米","华为"};
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_my_resume_listview);
		setTitleBar(getString(R.string.readMyResume), View.VISIBLE, View.GONE);
		
		initAllView();
		reigesterAllEvent();
		initData();
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
				Intent intent = new Intent(ReadMyResumeActivity.this,CompanyInfoActivity.class);
				startActivity(intent);
				
			}

		});
		
	}
	
	//初始化数据
    protected void initData(){
    	
    	for (int i = 0; i < companies.length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("companyName", companies[i]);
			listItems.add(item);
		}
    	lv_company.setAdapter(new MyAdapter(this,listItems,R.layout.read_my_resume_item));
    	
    }
    
 // 自定义适配器
  	class MyAdapter extends BaseAdapter{
  		private Context mContext;// 上下文对象
  		List<Map<String, Object>> list;// 这是要绑定的数据
  		private int resource;// 这是要绑定的 item 布局文件
  		private LayoutInflater inflater;// 布局填充器，Android系统内置的
  		
  		public MyAdapter(Context context, List<Map<String, Object>> list, int resource){
  			this.mContext = context;
  			this.list = list;
  			this.resource = resource;
  			this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 布局填充服务
  		}
  		
  		@Override
  		public int getCount() {//数据的总数量
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
  			TextView companyName = null;
  			TextView companyType = null;
  			TextView companyIndustry = null;
  			TextView readTime = null;
  			
  			if(convertView == null){// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
  				convertView = inflater.inflate(resource, null);
  				
  				// 初始化控件
  				companyName = (TextView) convertView.findViewById(R.id.company_name); 				
  				companyType = (TextView) convertView.findViewById(R.id.company_type);
  				companyIndustry = (TextView) convertView.findViewById(R.id.company_industry);
  				readTime = (TextView) convertView.findViewById(R.id.read_time);
  				
  				ViewCache viewCache = new ViewCache();
  				viewCache.companyName = companyName;
  				viewCache.companyType = companyType;
  				viewCache.companyIndustry = companyIndustry;
  				viewCache.readTime = readTime;
  				convertView.setTag(viewCache);
  			} else {
  				// 初始化控件
  				ViewCache viewCache = (ViewCache)convertView.getTag();
  				companyName = viewCache.companyName;
  				companyType = viewCache.companyType;
  				companyIndustry = viewCache.companyIndustry;
  				readTime = viewCache.readTime;
  			}
  			 
  			companyName.setText(companies[position]);
  			
  			return convertView;
  		}
  		
  	}
  	
  	private final class ViewCache {
  		public TextView companyName;// 公司名称
  		public TextView companyType;// 公司类型
  		public TextView companyIndustry;// 公司行业
  		public TextView readTime;// 查看简历的时间
  	}
    
}
