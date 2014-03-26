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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.cache.CacheProcess;

/**
 * 《我申请的职位》界面
 * 
 * @author 姜勇男
 * 
 */
public class MyAppliedJobActivity extends BaicActivity {

	private ListView lv_company;
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

	private String flag;// 判断点击的是职位申请记录还是职位收藏记录
	private CacheProcess cacheProcess;// 缓存数据
	private long userId;// 用户唯一标识
	private String result;// 获取服务端返回结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_applied_job_listview);

		initIntent();
		if ("postApplication".equals(flag)) {
			setTitleBar(getString(R.string.myAppliedJob), View.VISIBLE, View.GONE);
		} else if ("postCollect".equals(flag)) {
			setTitleBar(getString(R.string.myCollectedJob), View.VISIBLE, View.GONE);
		}
		initAllView();
		reigesterAllEvent();
		initDataThread();
	}

	protected void initIntent() {
		Bundle bundle = getIntent().getExtras();
		flag = bundle.getString("flag");
	}

	@Override
	protected void initAllView() {
		cacheProcess = new CacheProcess();
		userId = cacheProcess.getLongCacheValueInSharedPreferences(this, "userId");
		lv_company = (ListView) findViewById(R.id.lv_company);
	}

	@Override
	protected void reigesterAllEvent() {
		lv_company.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MyAppliedJobActivity.this, CompanyInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("flag", "MyAppliedJobActivity");// 职位申请记录和职位收藏记录进入同一个页面，用flag判断是点击哪个进来的
				bundle.putString("jobId", (String) listItems.get(position).get("jobsid"));
				bundle.putString("companyId", (String) listItems.get(position).get("companyid"));
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});

	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void initDataThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						if ("postApplication".equals(flag)) {
							initData();// 职位申请记录
						} else if ("postCollect".equals(flag)) {
							initData2();// 职位收藏记录
						}
					}
				});
		mThread.start();
	}

	// 初始化职位申请记录
	protected void initData() {

		String url = "appPersonCenter!postInfo.app";

		Message msg = new Message();

		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//			params.add(new BasicNameValuePair("Userid", "217294"));
			params.add(new BasicNameValuePair("Userid",userId+""));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				MyAppliedJobActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONArray responseJsonArray = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonArray = new JSONArray(result);
			if (responseJsonArray != null && responseJsonArray.length() != 0) {
				JSONObject jObject = responseJsonArray.optJSONObject(0);
				if (0 == jObject.getInt("resultcode")) {// 获得响应结果

					JSONArray applyList = jObject.getJSONArray("result");
					for (int i = 0; i < applyList.length(); i++) {
						JSONObject applyInfo = applyList.optJSONObject(0);
						String jobsid = applyInfo.getString("jobsid");// 职位ID
						String jobsname = applyInfo.getString("jobsname");// 职位名称
						String companyid = applyInfo.getString("companyid");// 公司ID
						String companyname = applyInfo.getString("companyname");// 公司名称
						String sendtime = applyInfo.getString("sendtime");// 申请简历的时间
						String send_status = applyInfo.getString("send_status");// 发送状态
						String reply_status = applyInfo.getString("reply_status");// 回复状态

						Map<String, Object> item = new HashMap<String, Object>();
						item.put("jobsid", jobsid);
						item.put("jobsname", jobsname);
						item.put("companyid", companyid);
						item.put("companyname", companyname);
						item.put("sendtime", sendtime);
						item.put("send_status", send_status);
						item.put("reply_status", reply_status);
						listItems.add(item);

					}

					msg.what = 0;
					handler.sendMessage(msg);
				} else {
					String errorResult = (String) jObject.get("result");
					String err = StringUtil.getAppException4MOS(errorResult);
					MyAppliedJobActivity.this.sendExceptionMsg(err);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 初始化职位收藏记录
	protected void initData2() {

		String url = "appPersonCenter!postSaveInfo.app";

		Message msg = new Message();

		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//			params.add(new BasicNameValuePair("Userid", "197244"));
			params.add(new BasicNameValuePair("Userid",userId+""));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				MyAppliedJobActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONArray responseJsonArray = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonArray = new JSONArray(result);
			if (responseJsonArray != null && responseJsonArray.length() != 0) {
				JSONObject jObject = responseJsonArray.optJSONObject(0);
				if (0 == jObject.getInt("resultcode")) {// 获得响应结果

					JSONArray applyList = jObject.getJSONArray("result");
					for (int i = 0; i < applyList.length(); i++) {
						JSONObject applyInfo = applyList.optJSONObject(0);

						String jobsid = applyInfo.getString("jobsid");// 职位ID
						String companyid = applyInfo.getString("companyid");// 公司ID
						String companyname = applyInfo.getString("companyname");// 公司名称
						String collecttime = applyInfo.getString("adddate");// 收藏时间

						String jobsclass = applyInfo.getString("jobsclass");// 职位行业
						String jobsname = applyInfo.getString("jobsname");// 职位名称
						String workregion = applyInfo.getString("workregion");// 工作地区
						String needprofession = applyInfo.getString("needprofession");// 专业要求：计算机专业OR不限专业等等
						String monthlysalary = applyInfo.getString("monthlysalary");// 月薪
						String needworkexperience = applyInfo.getString("needworkexperience");// 工作经验
						String jobsstate = applyInfo.getString("jobsstate");// 职位状态：1代表？2代表？
						String neededucation = applyInfo.getString("neededucation");// 学历要求：本科OR专科等等
						String housewhere = applyInfo.getString("housewhere");// 住宿：包吃包住OR不提供宿舍等等

						Map<String, Object> item = new HashMap<String, Object>();
						item.put("jobsclass", jobsclass);
						item.put("jobsid", jobsid);
						item.put("jobsname", jobsname);
						item.put("companyid", companyid);
						item.put("companyname", companyname);
						item.put("collecttime", collecttime);
						item.put("workregion", workregion);
						item.put("needprofession", needprofession);
						item.put("monthlysalary", monthlysalary);
						item.put("needworkexperience", needworkexperience);
						item.put("jobsstate", jobsstate);
						item.put("neededucation", neededucation);
						item.put("housewhere", housewhere);
						listItems.add(item);

					}

					msg.what = 0;
					handler.sendMessage(msg);
				} else {
					String errorResult = (String) jObject.get("result");
					String err = StringUtil.getAppException4MOS(errorResult);
					MyAppliedJobActivity.this.sendExceptionMsg(err);
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

			}
			closeProcessDialog();
		}
	};

	/**
	 * 请求完数据，适配器里添加数据
	 */
	private void setMyAdapter() {
		lv_company.setAdapter(new MyAdapter(this, listItems, R.layout.my_applied_job_item));

	}

	// 自定义适配器
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
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView jobName = null;
			TextView companyName = null;
			TextView applicationDate = null;
			LinearLayout status = null;
			TextView sendStatus = null;
			TextView replyStatus = null;
			ImageView collect = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				jobName = (TextView) convertView.findViewById(R.id.job_name);
				companyName = (TextView) convertView.findViewById(R.id.company_name);
				applicationDate = (TextView) convertView.findViewById(R.id.application_date);
				status = (LinearLayout) convertView.findViewById(R.id.status);
				sendStatus = (TextView) convertView.findViewById(R.id.send_status);
				replyStatus = (TextView) convertView.findViewById(R.id.reply_status);
				collect = (ImageView) convertView.findViewById(R.id.collect);

				ViewCache viewCache = new ViewCache();
				viewCache.jobName = jobName;
				viewCache.companyName = companyName;
				viewCache.applicationDate = applicationDate;
				viewCache.status = status;
				viewCache.sendStatus = sendStatus;
				viewCache.replyStatus = replyStatus;
				viewCache.collect = collect;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				jobName = viewCache.jobName;
				companyName = viewCache.companyName;
				applicationDate = viewCache.applicationDate;
				status = viewCache.status;
				sendStatus = viewCache.sendStatus;
				replyStatus = viewCache.replyStatus;
				collect = viewCache.collect;
			}
			jobName.setText(((Map) getItem(position)).get("jobsname").toString());
			companyName.setText(((Map) getItem(position)).get("companyname").toString());
			// 服务端返回格式：yyyy-MM-dd HH:mm:ss ; 客户端只显示年月日即可
			if ("postApplication".equals(flag)) {
				status.setVisibility(View.VISIBLE);
				collect.setVisibility(View.GONE);
				applicationDate.setText(((Map) getItem(position)).get("sendtime").toString().substring(0, 10));
				if ("0".equals(((Map) getItem(position)).get("send_status").toString())) {
					sendStatus.setText("已发送");
					sendStatus.setTextColor(getResources().getColor(R.color.green));
				} else if ("1".equals(((Map) getItem(position)).get("send_status").toString())) {
					sendStatus.setText("未发送");
					sendStatus.setTextColor(getResources().getColor(R.color.red));
				}

				if ("1".equals(((Map) getItem(position)).get("reply_status").toString())) {
					replyStatus.setText("已回复");
					replyStatus.setTextColor(getResources().getColor(R.color.green));
				} else if ("0".equals(((Map) getItem(position)).get("reply_status").toString())) {
					replyStatus.setText("未回复");
					replyStatus.setTextColor(getResources().getColor(R.color.red));
				}
			} else if ("postCollect".equals(flag)) {
				collect.setVisibility(View.VISIBLE);
				status.setVisibility(View.GONE);
				applicationDate.setText(((Map) getItem(position)).get("collecttime").toString().substring(0, 10));

				collect.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						((ImageView) v).setImageResource(R.drawable.star_none);
					}
				});

			}

			return convertView;
		}

	}

	private final class ViewCache {

		public TextView jobName;// 职位名称
		public TextView companyName;// 公司名称
		public TextView applicationDate;// 申请日期
		public LinearLayout status;
		public TextView sendStatus;// 发送状态
		public TextView replyStatus;// 回复状态
		public ImageView collect;// 收藏图标

	}

}
