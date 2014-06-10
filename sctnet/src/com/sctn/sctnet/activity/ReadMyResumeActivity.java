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
import com.sctn.sctnet.cache.CacheProcess;

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
	private CacheProcess cacheProcess;// 缓存数据
	
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
		cacheProcess = new CacheProcess();
		userId = cacheProcess.getLongCacheValueInSharedPreferences(this, "userId");
		
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
					bundle.putString("jobId", listItems.get(position).get("jobsid").toString());
					bundle.putString("companyId", listItems.get(position).get("companyid").toString());
					intent.putExtras(bundle);
					startActivity(intent);
				} else if("interviewInvitation".equals(flag)){
					Intent intent = new Intent(ReadMyResumeActivity.this,InterviewNoticeDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("jobsname", listItems.get(position).get("jobsname").toString());
					bundle.putString("interviewTime", listItems.get(position).get("facetimeplay").toString() + " ~ " +listItems.get(position).get("facetimeend").toString());
					bundle.putString("address", listItems.get(position).get("faceaddress").toString());
					bundle.putString("contact", listItems.get(position).get("contactsname").toString());
					bundle.putString("phone", listItems.get(position).get("phone").toString());
					intent.putExtras(bundle);
					startActivity(intent);
				} else if("jobIntentions".equals(flag)){
					Intent intent = new Intent(ReadMyResumeActivity.this,CompanyInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag", "ReadMyResumeActivity");
					bundle.putString("jobId", listItems.get(position).get("jobsid").toString());
					bundle.putString("companyId", listItems.get(position).get("companyId").toString());
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
			params.add(new BasicNameValuePair("Userid", userId+""));
//			params.add(new BasicNameValuePair("Userid", "217294"));
			result = getPostHttpContent(url, params);
			System.out.println(result);
			if (StringUtil.isExcetionInfo(result)) {
				ReadMyResumeActivity.this.sendExceptionMsg(result);
				return;
			}
			
			JSONObject responseObject = new JSONObject(result);
			if(responseObject.getInt("resultcode")==0){
				JSONArray responseJsonArray = responseObject.getJSONArray("result");
				for(int i=0; i<responseJsonArray.length(); i++){
					JSONObject object = responseJsonArray.optJSONObject(i);
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("companyName", object.getString("companyname"));// 公司名称
					item.put("companyType", object.getString("companytype"));// 公司类型
					item.put("companyIndustry", object.getString("companyindustry"));// 公司行业
					item.put("readTime", object.getString("viewtime").substring(0,10));// 查看简历时间
					item.put("jobsid", object.getString("jobsid"));// 公司行业
					item.put("companyid", object.getString("companyid"));// 公司行业
					listItems.add(item);
				}
				msg.what = 0;
				handler.sendMessage(msg);
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
    	
    	String url = "appPersonCenter!faceInviteDetailInfo.app";
		Message msg = new Message();
		
		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid", userId+""));
//			params.add(new BasicNameValuePair("Userid", "217294"));
			result = getPostHttpContent(url, params);
			if (StringUtil.isExcetionInfo(result)) {
				ReadMyResumeActivity.this.sendExceptionMsg(result);
				return;
			}
			
			JSONObject responseJsonObject = new JSONObject(result);
			if(responseJsonObject.getInt("resultcode")==0){
				JSONArray responseJsonArray = responseJsonObject.getJSONArray("result");
				for(int i=0; i<responseJsonArray.length(); i++){
					JSONObject object = responseJsonArray.optJSONObject(i);
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("companyName", object.getString("companyname"));
					item.put("companyType", object.getString("companytype"));// 公司类型
					item.put("companyIndustry", object.getString("companyindustry"));// 公司行业
					item.put("readTime", object.getString("addtime").substring(0,10));// 查看简历时间
					item.put("faceaddress", object.getString("faceaddress"));// 面试地址
					item.put("facetimeplay", object.getString("facetimeplay"));// 面试起始时间
					item.put("facetimeend", object.getString("facetimeend"));// 面试结束时间
					item.put("contactsname", object.getString("contactsname"));// 联系人
					item.put("phone", object.getString("phone"));// 联系电话
					item.put("jobsname", object.getString("jobsname"));// 面试岗位
					listItems.add(item);
				}
				msg.what = 1;
				handler.sendMessage(msg);
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
  			 
  			companyName.setText(((Map)list.get(position)).get("companyName").toString());
  			companyType.setText(((Map)list.get(position)).get("companyType").toString());
  			companyIndustry.setText(((Map)list.get(position)).get("companyIndustry").toString());
  			readTime.setText(((Map)list.get(position)).get("readTime").toString());
  			
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
   			TextView postName = null;
   			TextView companyName = null;
   			TextView companyType = null;
   			TextView readTime = null;
   			
   			if(convertView == null){// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
   				convertView = inflater.inflate(resource, null);
   				
   				// 初始化控件
   				postName = (TextView) convertView.findViewById(R.id.post_name);
   				companyName = (TextView) convertView.findViewById(R.id.company_name); 				
   				companyType = (TextView) convertView.findViewById(R.id.company_type);
   				readTime = (TextView) convertView.findViewById(R.id.read_time);
   				
   				ViewCache viewCache = new ViewCache();
   				viewCache.postName = postName;
   				viewCache.companyName = companyName;
   				viewCache.companyType = companyType;
   				viewCache.readTime = readTime;
   				convertView.setTag(viewCache);
   			} else {
   				// 初始化控件
   				ViewCache viewCache = (ViewCache)convertView.getTag();
   				postName = viewCache.postName;
   				companyName = viewCache.companyName;
   				companyType = viewCache.companyType;
   				readTime = viewCache.readTime;
   			}
   			 
   			postName.setText(((Map)list.get(position)).get("jobsname").toString());
   			companyName.setText(((Map)list.get(position)).get("companyName").toString());
   			companyType.setText(((Map)list.get(position)).get("companyType").toString());
   			readTime.setText(((Map)list.get(position)).get("readTime").toString());
   			
   			return convertView;
   		}
   		
   	}
  	
  	private final class ViewCache {
  		public TextView postName;//面试岗位名称
  		public TextView companyName;// 公司名称
  		public TextView companyType;// 公司类型
  		public TextView companyIndustry;// 公司行业
  		public TextView readTime;// 查看简历的时间
  	}
    
}
