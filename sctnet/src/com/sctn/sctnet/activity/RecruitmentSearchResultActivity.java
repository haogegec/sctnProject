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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
/**
 * 招聘会搜索结果页面
 * @author xueweiwei
 *
 */
public class RecruitmentSearchResultActivity extends BaicActivity{
	
	private ListView recruitmentListView;
	private View footViewBar;// 下滑加载条
	private SimpleAdapter recruitmentListAdapter;
	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	
	private int count;// 一次可以显示的条数（=pageSize或者小于）
	// 请求数据
	private int pageNo = 1;
	private int pageSize = Constant.PageSize;
	private String searchStr;
	// 返回数据
	private int total;// 总条数
	private String result;// 服务端返回的json字符串
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_list_more_activity);
		setTitleBar("搜索结果", View.VISIBLE, View.GONE);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		searchStr = bundle.getString("searchStr");
		initAllView();
		reigesterAllEvent();
		requestDataThread(0);// 第一次请求数据
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

			String url = "appOfferInfo!queryInfo.app";

			Message msg = new Message();
			try {

				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

				params.add(new BasicNameValuePair("page", pageNo + ""));
				params.add(new BasicNameValuePair("SiteRecruitmentName", searchStr));

				result = getPostHttpContent(url, params);

				if (StringUtil.isExcetionInfo(result)) {
					RecruitmentSearchResultActivity.this.sendExceptionMsg(result);
					return;
				}

				if (StringUtil.isBlank(result)) {
					result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
					RecruitmentSearchResultActivity.this.sendExceptionMsg(result);
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
						RecruitmentSearchResultActivity.this.sendExceptionMsg(err);
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
						item.put("recruitment_type", resultJsonArray.getJSONObject(j).get("holdclassid"));
						item.put("recruitment_name", resultJsonArray.getJSONObject(j).get("siterecruitmentname"));
						item.put("recruitment_time",resultJsonArray.getJSONObject(j).get("holddate"));
						item.put("recruitment_id", resultJsonArray.getJSONObject(j).get("id"));

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
					RecruitmentSearchResultActivity.this.sendExceptionMsg(err);
				}

			} catch (JSONException e) {
				String err = StringUtil.getAppException4MOS("解析json出错！");
				RecruitmentSearchResultActivity.this.sendExceptionMsg(err);
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
			
			recruitmentListView.setAdapter(recruitmentListAdapter);

			if (total > pageSize * pageNo) {
				recruitmentListView.addFooterView(footViewBar);// 添加list底部更多按钮
			}
			
		}

		/**
		 * 滑动list请求数据更新页面
		 */
		private void updateUI() {

			if (total <= pageSize * pageNo) {
				recruitmentListView.removeFooterView(footViewBar);// 添加list底部更多按钮
			}
			recruitmentListAdapter.notifyDataSetChanged();
//			recruitmentListView.setAdapter(recruitmentListAdapter);
//			recruitmentListView.setSelection((pageNo - 1) * 10-5);
		}


	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				pageNo++;
				requestDataThread(1);// 滑动list请求数据
			}
			

		}
	};

	@Override
	protected void initAllView() {
		
		recruitmentListView = (ListView) findViewById(R.id.information_list);

		footViewBar = View.inflate(RecruitmentSearchResultActivity.this, R.layout.foot_view_loading, null);
		recruitmentListAdapter = new SimpleAdapter(
				RecruitmentSearchResultActivity.this, items,
				R.layout.recruitment_item,
				new String[] { "recruitment_type","recruitment_name","recruitment_time" },
				new int[] { R.id.recruitment_type,R.id.recruitment_name,R.id.recruitment_time });
		recruitmentListView.setAdapter(recruitmentListAdapter);
		recruitmentListView.setOnScrollListener(listener);
		
	}

	@Override
	protected void reigesterAllEvent() {
		recruitmentListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(RecruitmentSearchResultActivity.this,RecruitmentCompanyListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("recruitmentId", items.get(position).get("recruitment_id").toString());
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
			
		});
		
	}

}
