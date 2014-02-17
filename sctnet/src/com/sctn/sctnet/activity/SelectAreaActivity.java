package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;
import com.sctn.sctnet.R;

public class SelectAreaActivity extends BasicActivity {
	
	private ListView lv_area;
	private static String[] cities = {"北京","成都","广州","杭州","南京","上海","深圳","天津","武汉","西安"};
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

//	private String requestParameters;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_area_listview);
    	super.setTitleBar("选择地区", View.VISIBLE, View.GONE);
    	
    	initView();
		registerListener();
		initData();
    	
        
    }
    
    @Override
	protected void initView() {
		// TODO Auto-generated method stub
		lv_area = (ListView) findViewById(R.id.lv_area);
	}

	@Override
	protected void registerListener() {
		lv_area.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(SelectAreaActivity.this,SalarySurveyActivity.class);
				intent.putExtra("area", cities[position]);
				setResult(RESULT_OK,intent);
				finish();
				
			}

		});
	} 
    
    //初始化城市列表
    protected void initData(){
    	
    	for (int i = 0; i < cities.length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("city", cities[i]);
			listItems.add(item);
		}
    	lv_area.setAdapter(new MyAdapter(this,listItems,R.layout.select_area_item));
    	
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
 			TextView area = null;
 			
 			
 			if(convertView == null){// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
 				convertView = inflater.inflate(resource, null);
 				
 				// 初始化控件
 				area = (TextView) convertView.findViewById(R.id.area); 				
 				ViewCache viewCache = new ViewCache();
 				viewCache.area = area;
 				convertView.setTag(viewCache);
 			} else {
 				// 初始化控件
 				ViewCache viewCache = (ViewCache)convertView.getTag();
 				area = viewCache.area;
 			}
 			 
 			area.setText(cities[position]);
 			
 			return convertView;
 		}
 		
 	}
 	
 	private final class ViewCache {
 		public TextView area;// 地区
 	}
}