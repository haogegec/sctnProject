package com.sctn.sctnet.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSONArray;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.activity.SelectPositionActivity.MyAdapter;
import com.sctn.sctnet.contants.Constant;

public class SelectCurrentPositionActivity extends BaicActivity{
	
	private ListView lv_area;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
	//服务端返回结果
	private String result;
	private com.alibaba.fastjson.JSONObject responseJsonObject = null;// 返回结果存放在该json对象中
	private List<Map> backIndustryType;
	
	private int page = 1;
	private int total;// 总条数
	private int pageSize = Constant.PageSize;
	private int pageCount;// 一次可以显示的条数（=pageSize或者小于）
	private View footViewBar;// 下滑加载条
	private MyAdapter myAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_area_listview);
        
		super.setTitleBar("选择欲从事岗位", View.VISIBLE, View.GONE);
    	
    	initAllView();
    	reigesterAllEvent();
		requestDataThread(0);
        
    }
	
	 /**
		 * 请求数据线程
		 * 
		 */
		private void requestDataThread(final int i) {
			if(i==0) showProcessDialog(false);
			Thread mThread = new Thread(new Runnable() {// 启动新的线程，
						@Override
						public void run() {
							requestData(i);
						}
					});
			mThread.start();
		}
		 private void requestData(int i){
				
				String url = "appCmbShow.app";

				Message msg = new Message();
				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("type", Constant.POSITION_TYPE+""));
				params.add(new BasicNameValuePair("key", "1"));
				params.add(new BasicNameValuePair("page", page+""));
				result = getPostHttpContent(url, params);

				if (StringUtil.isExcetionInfo(result)) {
					SelectCurrentPositionActivity.this.sendExceptionMsg(result);
					return;
				}

				if (StringUtil.isBlank(result)) {
					result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
					SelectCurrentPositionActivity.this.sendExceptionMsg(result);
					return;
				}
				Message m=new Message();
				responseJsonObject = com.alibaba.fastjson.JSONObject
						.parseObject(result);
				if(responseJsonObject.get("resultcode").toString().equals("0")) {
					
                   JSONArray json = responseJsonObject.getJSONArray("result");
					
					total = (Integer) responseJsonObject.get("resultCount");// 总数
					if (json.size() > 15) {
						pageCount = 15;
					} else {
						pageCount = json.size();
					}
					for(int j=0;j<pageCount;j++){
						
						String key = json.getJSONObject(j).getString("key");
						String value = json.getJSONObject(j).getString("value");
						Map<String,String> map = new HashMap<String,String>();
						
						map.put("id",key);
						map.put("value", value);
						listItems.add(map);
					}
					if (i == 0) {
						m.what = 0;

					} else {
						m.what = 1;
					}
				}else {
					String errorResult = (String) responseJsonObject.get("result");
					String err = StringUtil.getAppException4MOS(errorResult);
					SelectCurrentPositionActivity.this.sendExceptionMsg(err);
				}
						
				handler.sendMessage(m);
		 }
		   
		// 处理线程发送的消息
				private Handler handler = new Handler() {

					public void handleMessage(Message msg) {
						switch (msg.what) {
						case 0:
							initUI();
							closeProcessDialog();
							break;
						case 1:
							updateUI();
							break;

						}
						
						
					}
				};

	@Override
	protected void initAllView() {
		
        lv_area = (ListView) findViewById(R.id.lv_area);
        footViewBar = View.inflate(SelectCurrentPositionActivity.this, R.layout.foot_view_loading, null);
        myAdapter = new MyAdapter(this,listItems,R.layout.select_area_item);
		lv_area.setAdapter(myAdapter);
		lv_area.setOnScrollListener(listener);
	}

	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (view.getLastVisiblePosition() == myAdapter.getCount()) {
				page++;
				requestDataThread(1);// 滑动list请求数据
			}
			

		}
	};
	
	@Override
	protected void reigesterAllEvent() {
		
		lv_area.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
//				Intent intent = getIntent();
//				intent.putExtra("postStr", listItems.get(position).get("value"));
//				intent.putExtra("postId", listItems.get(position).get("id"));
//				setResult(RESULT_OK, intent);
//				finish();
				
				Intent intent = new Intent(SelectCurrentPositionActivity.this,SelectItemActivity.class);
				intent.putExtra("list", (Serializable)listItems);
				intent.putExtra("which", "SelectJob");
				intent.putExtra("id", listItems.get(position).get("id"));
				intent.putExtra("value", listItems.get(position).get("value"));
				startActivityForResult(intent,Constant.DETAIL_JOB);
			}

		});
		
	}
	  //初始化城市列表
    protected void initUI(){
    	
    	if (total > pageSize * page) {
    		lv_area.addFooterView(footViewBar);// 添加list底部更多按钮
		}
    	lv_area.setAdapter(myAdapter);
    	
    }
    /**
	 * 滑动list请求数据更新页面
	 */
	private void updateUI() {

		if (total <= pageSize * page) {
			lv_area.removeFooterView(footViewBar);// 添加list底部更多按钮
		}
		myAdapter.notifyDataSetChanged();

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.DETAIL_JOB:
				String currentJob = data.getStringExtra("currentJob");
				String currentJobId = data.getStringExtra("currentJobId");
				
				Intent intent = getIntent();
				intent.putExtra("postStr", currentJob);
				intent.putExtra("postId", currentJobId);
				setResult(RESULT_OK, intent);
				finish();
				break;

			

			}
		}
	}
	
    // 自定义适配器
 	class MyAdapter extends BaseAdapter{
 		private Context mContext;// 上下文对象
 		List<Map<String, String>> list;// 这是要绑定的数据
 		private int resource;// 这是要绑定的 item 布局文件
 		private LayoutInflater inflater;// 布局填充器，Android系统内置的
 		
 		public MyAdapter(Context context, List<Map<String, String>> list, int resource){
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
 			 
 			area.setText(list.get(position).get("value"));
 			
 			return convertView;
 		}
 		
 	}
 	
 	private final class ViewCache {
 		public TextView area;// 地区
 	}

}
