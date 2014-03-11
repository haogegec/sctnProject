package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

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

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;

/**
 * 《谁看了我的简历》界面
 * @author 姜勇男
 *
 */
public class ReadMyResumeActivity extends BaicActivity {

	private ListView lv_company;
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	
	private long userId;
	private String flag;
	private String result;// 服务器返回的结果
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_my_resume_listview);
		
		initIntent();
		if("readMyResume".equals(flag)){
			setTitleBar(getString(R.string.readMyResume), View.VISIBLE, View.GONE);
		} else if("interviewInvitation".equals(flag)){
			setTitleBar(getString(R.string.invitationsForInterviews), View.VISIBLE, View.GONE);
		} else if("jobIntentions".equals(flag)){
			setTitleBar(getString(R.string.jobIntentions), View.VISIBLE, View.GONE);
		}
		initAllView();
		reigesterAllEvent();
		initIntent();
		if("readMyResume".equals(flag)) initReadMyResumeThread();
		else if("interviewInvitation".equals(flag)) initInterviewInvitationThread();
	}

	// 接收上一页面传过来的数据
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
				if("readMyResume".equals(flag)){
					Intent intent = new Intent(ReadMyResumeActivity.this,CompanyInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag", "ReadMyResumeActivity");
					intent.putExtras(bundle);
					startActivity(intent);
				} else if("interviewInvitation".equals(flag)){
					Intent intent = new Intent(ReadMyResumeActivity.this,InterviewNoticeDetailActivity.class);
					Bundle bundle = new Bundle();
					intent.putExtras(bundle);
					startActivity(intent);
				} else if("jobIntentions".equals(flag)){
					Intent intent = new Intent(ReadMyResumeActivity.this,CompanyInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag", "ReadMyResumeActivity");
					intent.putExtras(bundle);
					startActivity(intent);
				}
				
				
			}

		});
		
	}
	
	protected void initReadMyResumeThread(){
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						initReadMyResume();
					}
				});
		mThread.start();
	}
	
	//初始化数据
    protected void initReadMyResume(){
    	
    	String url = "appPersonCenter!resumeDetailInfo.app";
		Message msg = new Message();
		
		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//			userId = SharePreferencesUtils.getSharedlongData("userId");
//			params.add(new BasicNameValuePair("Userid", userId));
			params.add(new BasicNameValuePair("Userid", "217294"));
			result = getPostHttpContent(url, params);
			System.out.println(result);
			if (StringUtil.isExcetionInfo(result)) {
				ReadMyResumeActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONArray responseJsonArray = new JSONArray(result);
			if(responseJsonArray != null && responseJsonArray.length() > 0){
				JSONObject jObject = responseJsonArray.optJSONObject(0);
				if("0".equals(jObject.getString("resultcode"))){
					JSONArray resultList = jObject.getJSONArray("result");
					for(int i=0; i<resultList.length(); i++){
						JSONObject object = resultList.optJSONObject(0);
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("companyName", object.getString("companyname"));// 公司名称
						item.put("companyType", object.getString("jobsclass"));// 公司类型
//						item.put("", object.getString(""));// 公司行业
						item.put("readTime", object.getString("viewtime").substring(0,10));// 查看简历时间
						listItems.add(item);
					}
					msg.what = 0;
					handler.sendMessage(msg);
					
				} else {
					String errorResult = (String) jObject.get("result");
					String err = StringUtil.getAppException4MOS(errorResult);
					ReadMyResumeActivity.this.sendExceptionMsg(err);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    protected void initInterviewInvitationThread(){
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						initInterviewInvitation();
					}
				});
		mThread.start();
	}
    
    //初始化数据
    protected void initInterviewInvitation(){
    	
    	String url = "appPersonCenter!resumeDetailInfo.app";
		Message msg = new Message();
		
		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//			userId = SharePreferencesUtils.getSharedlongData("userId");
//			params.add(new BasicNameValuePair("Userid", userId));
			params.add(new BasicNameValuePair("Userid", "217294"));
			result = getPostHttpContent(url, params);
			if (StringUtil.isExcetionInfo(result)) {
				ReadMyResumeActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONArray responseJsonArray = new JSONArray(result);
			if(responseJsonArray != null && responseJsonArray.length() > 0){
				JSONObject jObject = responseJsonArray.optJSONObject(0);
				if("0".equals(jObject.getString("resultcode"))){
					JSONArray resultList = jObject.getJSONArray("result");
					for(int i=0; i<resultList.length(); i++){
						JSONObject object = resultList.optJSONObject(0);
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("companyName", object.getString("companyname"));
						item.put("content", object.getString(""));
						item.put("readTime", object.getString(""));
						listItems.add(item);
					}
					msg.what = 1;
					handler.sendMessage(msg);
					
				} else {
					String errorResult = (String) jObject.get("result");
					String err = StringUtil.getAppException4MOS(errorResult);
					ReadMyResumeActivity.this.sendExceptionMsg(err);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    // 处理线程发送的消息
 	private Handler handler = new Handler() {

 		public void handleMessage(Message msg) {
 			switch (msg.what) {
 				case 0:
 					setMyAdapter();
 					break;
 				case 1:
 					setMyAdapter2();
 					break;
 			}
 			closeProcessDialog();
 		}
 	};
 	
 	/**
	 * 请求完数据，适配器里添加数据
	 */
	private void setMyAdapter() {
		lv_company.setAdapter(new MyAdapter(this,listItems,R.layout.read_my_resume_item));
	}
	
	/**
	 * 请求完数据，适配器里添加数据
	 */
	private void setMyAdapter2() {
		lv_company.setAdapter(new MyAdapter2(this,listItems,R.layout.interview_invitation_item));
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
  			 
  			companyName.setText(((Map)listItems.get(position)).get("companyName").toString());
  			
  			return convertView;
  		}
  		
  	}
  	
 // 自定义适配器
   	class MyAdapter2 extends BaseAdapter{
   		private Context mContext;// 上下文对象
   		List<Map<String, Object>> list;// 这是要绑定的数据
   		private int resource;// 这是要绑定的 item 布局文件
   		private LayoutInflater inflater;// 布局填充器，Android系统内置的
   		
   		public MyAdapter2(Context context, List<Map<String, Object>> list, int resource){
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
   			TextView content = null;
   			TextView readTime = null;
   			
   			if(convertView == null){// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
   				convertView = inflater.inflate(resource, null);
   				
   				// 初始化控件
   				companyName = (TextView) convertView.findViewById(R.id.company_name); 				
   				content = (TextView) convertView.findViewById(R.id.content);
   				readTime = (TextView) convertView.findViewById(R.id.read_time);
   				
   				ViewCache viewCache = new ViewCache();
   				viewCache.companyName = companyName;
   				viewCache.content = content;
   				viewCache.readTime = readTime;
   				convertView.setTag(viewCache);
   			} else {
   				// 初始化控件
   				ViewCache viewCache = (ViewCache)convertView.getTag();
   				companyName = viewCache.companyName;
   				content = viewCache.content;
   				readTime = viewCache.readTime;
   			}
   			 
   			companyName.setText(((Map)listItems.get(position)).get("companyName").toString());
   			
   			return convertView;
   		}
   		
   	}
  	
  	private final class ViewCache {
  		public TextView companyName;// 公司名称
  		public TextView companyType;// 公司类型
  		public TextView companyIndustry;// 公司行业
  		public TextView readTime;// 查看简历的时间
  		public TextView content;// 面试通知内容
  	}
    
}
