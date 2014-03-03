package com.sctn.sctnet.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sctn.sctnet.R;

public class MyBaseAdapter extends BaseAdapter{

	private Context mContext;// 上下文对象
		List<Map<String, String>> list;// 这是要绑定的数据
		private int resource;// 这是要绑定的 item 布局文件
		private LayoutInflater inflater;// 布局填充器，Android系统内置的
		
		public MyBaseAdapter(Context context, List<Map<String, String>> list, int resource){
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
			 
			area.setText(list.get(position).get("city"));
			
			return convertView;
		}
		
	private	final class ViewCache {
			public TextView area;// 地区
		}
		
	}

