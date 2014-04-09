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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.activity.JobListActivity.MyAdapter;
import com.sctn.sctnet.contants.Constant;
/**
 * 招聘会列表页面
 * @author xueweiwei
 *
 */
public class RecruitmentActivity extends BaicActivity{
	
	private ListView recruitmentListView;
	private View footViewBar;// 下滑加载条
	private MyAdapter recruitmentListAdapter;
	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	private EditText searchEdit;
	private ImageView searchImg;
	private int count;// 一次可以显示的条数（=pageSize或者小于）
	// 请求数据
	private int pageNo = 1;
	private int pageSize = Constant.PageSize;
	// 返回数据
	private int total;// 总条数
	private String result;// 服务端返回的json字符串
	
	private int itemCount; // 当前窗口可见项总数   
	private int visibleLastIndex = 0;//最后的可视项索引 
	
	private TextView explainText;
	private String explain = "<font color='#0b98e0'><b>求职者参加本人才市场的各类现场招聘会均</b></font>"+"<font color='#f78514'><b>免费入场、免费建档！</b></font>"+"<font color='#0b98e0'><b>招聘会咨询电话：028-86136436</b></font>";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recruitment_list_activity);
		setTitleBar(getString(R.string.RecruitmentActivityTitle), View.VISIBLE, View.GONE);
		initAllView();
		reigesterAllEvent();
		requestDataThread(0);// 第一次请求数据
	}

	@Override
	protected void initAllView() {
		
		explainText = (TextView) findViewById(R.id.explain_text);
		recruitmentListView = (ListView) findViewById(R.id.recrumitment_list);
		searchEdit = (EditText) findViewById(R.id.search_edit_bg);
		searchImg = (ImageView) findViewById(R.id.search_bar);
		
		explainText.setText(Html.fromHtml(explain));
		
		footViewBar = View.inflate(RecruitmentActivity.this, R.layout.foot_view_loading, null);
		recruitmentListAdapter = new MyAdapter(this, items, R.layout.recruitment_item);
//		recruitmentListAdapter = new SimpleAdapter(
//				RecruitmentActivity.this, items,
//				R.layout.recruitment_item,
//				new String[] { "recruitment_type","recruitment_name","recruitment_time" },
//				new int[] { R.id.recruitment_type,R.id.recruitment_name,R.id.recruitment_time });
	//	recruitmentListView.addFooterView(footViewBar);// 添加list底部更多按钮
		recruitmentListView.setAdapter(recruitmentListAdapter);
		recruitmentListView.setOnScrollListener(listener);
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		recruitmentListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(RecruitmentActivity.this,RecruitmentCompanyListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("recruitmentId", items.get(position).get("recruitment_id").toString());
				bundle.putString("partitionlist", items.get(position).get("recruitment_partition_list").toString());
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
			
		});
		searchImg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecruitmentActivity.this,RecruitmentSearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("searchStr", searchEdit.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
			
		});
//		searchEdit.setOnEditorActionListener(new OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_ENTER || actionId == 0||actionId == 5) {
//            	   Intent intent = new Intent(RecruitmentActivity.this,RecruitmentSearchResultActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putString("searchStr", searchEdit.getText().toString());
//					intent.putExtras(bundle);
//					startActivity(intent);
//               } 
//               return false; 
//           } 
//       });
	}
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
		public View getView(final int position, View convertView, ViewGroup parent) {

			TextView name;// 招聘会名称
			TextView time;// 时间
			TextView week;// 周几
			TextView address;// 招聘会地点
			LinearLayout addressLayout;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				name = (TextView) convertView.findViewById(R.id.recruitment_name);
				time = (TextView) convertView.findViewById(R.id.recruitment_time);
				week = (TextView) convertView.findViewById(R.id.recruitment_week);				
				address = (TextView) convertView.findViewById(R.id.recruitment_address);
				addressLayout = (LinearLayout) convertView.findViewById(R.id.recruitment_address_layout);
				
				ViewCache viewCache = new ViewCache();
				viewCache.name = name;
				viewCache.time = time;
				viewCache.week = week;
				viewCache.address = address;
				viewCache.addressLayout = addressLayout;
				
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				name = viewCache.name;
				time = viewCache.time;
				week = viewCache.week;
				address = viewCache.address;
				addressLayout = viewCache.addressLayout;
			}

			String id = list.get(position).get("recruitment_id").toString();
			name.setText(list.get(position).get("recruitment_name").toString());
			time.setText(list.get(position).get("recruitment_time").toString());
			week.setText(list.get(position).get("recruitment_week").toString());
			address.setText(list.get(position).get("recruitment_address").toString());

			addressLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(RecruitmentActivity.this, CompanyLocationActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("detailAddress",list.get(position).get("recruitment_address").toString());
					bundle.putString("city", "成都");
					intent.putExtras(bundle);
					startActivity(intent);
					
				}
				
			});
//			convertView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					Intent intent = new Intent(RecruitmentActivity.this,RecruitmentCompanyListActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putString("recruitmentId", list.get(position).get("recruitment_id").toString());
//					intent.putExtras(bundle);
//					startActivity(intent);
//				}
//			});

			return convertView;
		}

	}

	private final class ViewCache {
		public TextView name;// 招聘会名称
		public TextView time;// 时间
		public TextView week;// 周几
		public TextView address;// 招聘会地点
		public LinearLayout addressLayout;
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

			String url = "appOfferInfo.app";

			Message msg = new Message();
			try {

				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

				params.add(new BasicNameValuePair("page", pageNo + ""));
				

				result = getPostHttpContent(url, params);

				if (StringUtil.isExcetionInfo(result)) {
					RecruitmentActivity.this.sendExceptionMsg(result);
					return;
				}

				if (StringUtil.isBlank(result)) {
					result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
					RecruitmentActivity.this.sendExceptionMsg(result);
					return;
				}

				JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

				// JSON的解析过程
				responseJsonObject = new JSONObject(result);
				if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

					JSONArray resultJsonArray = responseJsonObject
							.getJSONArray("result");

					total = responseJsonObject.getInt("resultCount");// 总数
					if (resultJsonArray.length() > 15) {
						count = 15;
					} else {
						count = resultJsonArray.length();
					}
					for (int j = 0; j < count; j++) {

						Map<String, Object> item = new HashMap<String, Object>();
						item.put("recruitment_week", resultJsonArray.getJSONObject(j).get("weekTime"));
						item.put("recruitment_name", resultJsonArray.getJSONObject(j).get("siterecruitmentname"));
						item.put("recruitment_time",resultJsonArray.getJSONObject(j).get("holddate").toString().substring(0,10));
						item.put("recruitment_id", resultJsonArray.getJSONObject(j).get("recruitmentid"));
						item.put("recruitment_address", "四川省成都市小南街99号(人民公园斜对面·长城园旁)");
						item.put("recruitment_partition_list",resultJsonArray.getJSONObject(j).get("partitionlist"));
						
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
					RecruitmentActivity.this.sendExceptionMsg(err);
				}

			} catch (JSONException e) {
				String err = StringUtil.getAppException4MOS("解析json出错！");
				RecruitmentActivity.this.sendExceptionMsg(err);
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
			
			// 必须在setAdapter之前把head和Footer设置好
			if (total > pageSize * pageNo) {
				recruitmentListView.addFooterView(footViewBar);// 添加list底部更多按钮
			}
			recruitmentListView.setAdapter(recruitmentListAdapter);
			
		}

		/**
		 * 滑动list请求数据更新页面
		 */
		private void updateUI() {

			if (total <= pageSize * pageNo) {
				recruitmentListView.removeFooterView(footViewBar);// 添加list底部更多按钮
			}
			recruitmentListAdapter.notifyDataSetChanged();
			//想太多。。。
//			recruitmentListView.setAdapter(recruitmentListAdapter);
//			recruitmentListView.setSelection(visibleLastIndex - itemCount + 1);
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

			if ((view.getLastVisiblePosition() == recruitmentListAdapter.getCount())&&(total > pageSize * pageNo)) {
				pageNo++;
				requestDataThread(1);// 滑动list请求数据
			}
			

		}
	};
}
