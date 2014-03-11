package com.sctn.sctnet.activity;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

public class SelectPositionActivity extends BaicActivity{
	
	private ListView lv_area;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
	//服务端返回结果
	private String result;
	private com.alibaba.fastjson.JSONObject responseJsonObject = null;// 返回结果存放在该json对象中
	private List<Map> backIndustryType;
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_area_listview);
    	super.setTitleBar("选择职业", View.VISIBLE, View.GONE);
    	
    	initAllView();
    	reigesterAllEvent();
		requestDataThread();
        
    }
	
	 /**
		 * 请求数据线程
		 * 
		 */
		private void requestDataThread() {
			showProcessDialog(true);
			Thread mThread = new Thread(new Runnable() {// 启动新的线程，
						@Override
						public void run() {
							requestData();
						}
					});
			mThread.start();
		}
		 private void requestData(){
				
				String url = "appCmbShow.app";

				Message msg = new Message();
				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("type", Constant.INDUSTRY_TYPE+""));
				params.add(new BasicNameValuePair("key", "1"));
				result = getPostHttpContent(url, params);

				if (StringUtil.isExcetionInfo(result)) {
					SelectPositionActivity.this.sendExceptionMsg(result);
					return;
				}

				if (StringUtil.isBlank(result)) {
					result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
					SelectPositionActivity.this.sendExceptionMsg(result);
					return;
				}
				Message m=new Message();
				responseJsonObject = com.alibaba.fastjson.JSONObject
						.parseObject(result);
				if(responseJsonObject.get("resultcode").toString().equals("0")) {
					
					com.alibaba.fastjson.JSONObject json = responseJsonObject.getJSONObject("result");
					Set<Entry<String,Object>> set = json.entrySet();
					Iterator<Entry<String,Object>> iter = set.iterator();
					while(iter.hasNext()){
						Map<String,String> map = new HashMap<String,String>();
						Entry obj = iter.next();
						map.put("id",(String) obj.getKey());
						map.put("value", (String) obj.getValue());
						listItems.add(map);
					}
					m.what = 0;
				}else {
					String errorResult = (String) responseJsonObject.get("result");
					String err = StringUtil.getAppException4MOS(errorResult);
					SelectPositionActivity.this.sendExceptionMsg(err);
				}
						
				handler.sendMessage(m);
		 }
		   
		// 处理线程发送的消息
				private Handler handler = new Handler() {

					public void handleMessage(Message msg) {
						switch (msg.what) {
						case 0:
							initUI();
							break;

						}
						closeProcessDialog();
					}
				};

	@Override
	protected void initAllView() {
		
        lv_area = (ListView) findViewById(R.id.lv_area);
		
		lv_area.setAdapter(new MyAdapter(this,listItems,R.layout.select_area_item));
	}

	@Override
	protected void reigesterAllEvent() {
		
		lv_area.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(SelectPositionActivity.this,SelectPositionDetailActivity.class);
				intent.putExtra("content", listItems.get(position).get("value"));
				intent.putExtra("id", listItems.get(position).get("value"));
				startActivityForResult(intent,Constant.POSITION_TYPE);
			}

		});
		
	}
	  //初始化城市列表
    protected void initUI(){
    	
    	lv_area.setAdapter(new MyAdapter(this,listItems,R.layout.select_area_item));
    	
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

 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.POSITION_TYPE: {
				
				backIndustryType = (List<Map>) ((List) data.getSerializableExtra("list")).get(0);
				Intent intent = getIntent();
				ArrayList list = new ArrayList();
				list.add(backIndustryType);
				intent.putExtra("list", list);
				setResult(RESULT_OK,intent);
				finish();
				
				break;
			}
			
			
		}
		}
	}

}
