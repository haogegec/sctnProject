package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

/**
 * 信息咨询更多
 * 
 * @author xueweiwei
 * 
 */
public class InformationListMoreActivity extends BaicActivity {

	private String title;
	private SimpleAdapter informationListAdapter;
	private ListView informationList;
	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	private String search;

	private View footViewBar;// 下滑加载条
	private int count;// 一次可以显示的条数（=pageSize或者小于）
	// 请求数据
	private int pageNo = 1;
	private int pageSize = Constant.PageSize;
	private String cid;// 大栏目id
	private String url;
	// 返回数据
	private int total;// 总条数
	private String result;// 服务端返回的json字符串
	private int selected;//滑动list时定位滑动条的位置

	private int itemCount; // 当前窗口可见项总数   
	private int visibleLastIndex = 0;//最后的可视项索引 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_list_more_activity);
		initBundle();
		initAllView();
		reigesterAllEvent();
		requestDataThread(0);// 第一次请求数据
	}

	private void initBundle() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("title");
		if(bundle.getString("cid")!=null){
			cid = bundle.getString("cid");
		}
		if(bundle.getString("search")!=null){
			search = bundle.getString("search");
		}
		url = bundle.getString("url");
	}

	/**
	 * 请求数据线程
	 * 
	 * @param i判断是哪次请求数据
	 *            ，如果第一次请求则为0，如果是滑动list请求数据则为1
	 */
	private void requestDataThread(final int i) {

		if (i == 0) {
			showProcessDialog(true);
		}

		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestData(i);
					}
				});
		mThread.start();
	}

	private void requestData(int i) {

	//	String url = "appInfo!findByCid.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

			params.add(new BasicNameValuePair("page", pageNo + ""));
			params.add(new BasicNameValuePair("pageSize", pageSize + ""));
			if(!StringUtil.isBlank(cid)){
				params.add(new BasicNameValuePair("cid", cid));
			}
			if(!StringUtil.isBlank(search)){
				params.add(new BasicNameValuePair("title", search));
			}

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				InformationListMoreActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {
				result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
				InformationListMoreActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONArray resultJsonArray = responseJsonObject
						.getJSONArray("result");
				if(resultJsonArray==null||resultJsonArray.length()==0){
					String err = StringUtil.getAppException4MOS("没有您要搜索的结果");
					InformationListMoreActivity.this.sendExceptionMsg(err);
					return;
				}
				total = responseJsonObject.getInt("resultCount");// 总数
				if (resultJsonArray.length() > 15) {
					count = 15;
				} else {
					count = resultJsonArray.length();
				}
				for (int j = 0; j < count; j++) {

					Map<String, Object> item = new HashMap<String, Object>();
					item.put("id", resultJsonArray.getJSONObject(j).get("id"));// 小栏目的id
					item.put("little_column_title", resultJsonArray
							.getJSONObject(j).get("title"));// 小栏目的名称
					item.put("content",
							resultJsonArray.getJSONObject(j).get("content"));// 职位

					items.add(item);
				}
				if (i == 0) {
					msg.what = 0;

				} else {
					msg.what = 1;
				}
				handler.sendMessage(msg);

			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				InformationListMoreActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			InformationListMoreActivity.this.sendExceptionMsg(err);
		}
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

	/**
	 * 第一次请求数据初始化页面
	 */
	private void initUI() {
		
		if (total > pageSize * pageNo) {
			informationList.addFooterView(footViewBar);// 添加list底部更多按钮
		}
		
		informationList.setAdapter(informationListAdapter);
		
	}

	/**
	 * 滑动list请求数据更新页面
	 */
	private void updateUI() {

		if (total <= pageSize * pageNo) {
			informationList.removeFooterView(footViewBar);// 添加list底部更多按钮
		}		
	//	informationListAdapter.getDataList.addAll(items);    
		informationListAdapter.notifyDataSetChanged();
		informationList.setAdapter(informationListAdapter);
		informationList.setSelection(visibleLastIndex - itemCount + 1);
	}

	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			itemCount = visibleItemCount;  
	        visibleLastIndex = firstVisibleItem + visibleItemCount - 1; 
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			selected =  informationList.getLastVisiblePosition();  
			if ((view.getLastVisiblePosition() == view.getCount() - 1)&&(total > pageSize * pageNo)) {
				
						pageNo++;
						requestDataThread(1);// 滑动list请求数据
				
			}
			
//			if ((view.getLastVisiblePosition() == informationListAdapter.getCount())&&(total > pageSize * pageNo)) {
//				pageNo++;
//				requestDataThread(1);// 滑动list请求数据
//			}		

		}
	};

	@Override
	protected void initAllView() {
		setTitleBar(title, View.VISIBLE, View.GONE);
		
		informationList = (ListView) findViewById(R.id.information_list);
		footViewBar = View.inflate(InformationListMoreActivity.this, R.layout.foot_view_loading, null);
		informationListAdapter = new SimpleAdapter(
				InformationListMoreActivity.this, items,
				R.layout.information_list_item,
				new String[] { "little_column_title" },
				new int[] { R.id.little_column_content });
		informationList.setAdapter(informationListAdapter);
		informationList.setOnScrollListener(listener);
	}

	@Override
	protected void reigesterAllEvent() {

		informationList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("title", title);
				bundle.putString("id", items.get(position).get("id")+"");
				intent.setClass(InformationListMoreActivity.this, InformationDetailActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});

	}

}
