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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;

/**
 * 职位搜索结果界面
 * @author 姜勇男
 *
 */
public class JobListActivity extends BaicActivity {

	private MyAdapter jobListAdapter;
	private ListView jobList;
	private List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();

	Map<Integer,Object> idMaps = new HashMap<Integer,Object>();
	Map<Integer, Boolean> checkBoxState = new HashMap<Integer, Boolean>();// 记录checkbox的状态
	
	private Button btn_apply;// 申请
	private Button btn_collect;// 收藏
	private Button btn_share;// 分享
	
	private String[] jobNames = {"1234","2345","3456","4567","5678","6789","7890","8901","9012","0123","asdf","jkll","yuiop","qwert"};
	private View footViewBar;// 下滑加载条
	private int count;//一次可以显示的条数（=pageSize或者小于）
	//请求数据
	private int pageNo = 1;
	private int pageSize = Constant.PageSize;
	private String workRegion;
	private String jobsClass;
	private String needProfession;
	//返回数据
	private int total;//总条数
	private String result;//服务端返回的json字符串

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_list_activity);
		setTitleBar("共"+0+"个职位", View.VISIBLE, View.GONE);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		workRegion = bundle.getString("areaId");
		jobsClass = bundle.getString("industryTypeId");
		needProfession = bundle.getString("positionTypeId");

		initAllView();
		reigesterAllEvent();
		requestDataThread(0);//第一次请求数据
	}
	
	
	/**
	 * 第一次请求数据初始化页面
	 */
	private void initUI() {
		setTitleBar("共"+total+"个职位", View.VISIBLE, View.GONE);
		if(total>pageSize*pageNo){
			jobList.addFooterView(footViewBar);//添加list底部更多按钮
		}
		jobList.setAdapter(jobListAdapter);
	}
	/**
	 * 滑动list请求数据更新页面
	 */
	private void updateUI(){
		
		if(total<=pageSize*pageNo){
			jobList.removeFooterView(footViewBar);//添加list底部更多按钮
		}
		jobListAdapter.notifyDataSetChanged();
		jobList.setAdapter(jobListAdapter);
		jobList.setSelection((pageNo - 1) * pageSize);
	}
	
	@Override
	protected void initAllView() {
		jobList = (ListView)findViewById(R.id.lv_jobList);
		btn_apply = (Button)findViewById(R.id.btn_apply);
		btn_collect = (Button)findViewById(R.id.btn_collect);
		btn_share = (Button)findViewById(R.id.btn_share);
		footViewBar = View.inflate(JobListActivity.this, R.layout.foot_view_loading, null);
		jobListAdapter = new MyAdapter(this, items, R.layout.job_list_item);
		jobList.setAdapter(jobListAdapter);
		jobList.setOnScrollListener(listener);
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 申请
		btn_apply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(idMaps.size() == 0){
					Toast.makeText(getApplicationContext(), "请选择职位", Toast.LENGTH_LONG).show();
				} else {
					for (Map.Entry<Integer, Object> entry : idMaps.entrySet()) {
						Toast.makeText(getApplicationContext(), "申请的ID："+entry.getValue(), Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
		// 收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(idMaps.size() == 0){
					Toast.makeText(getApplicationContext(), "请选择职位", Toast.LENGTH_LONG).show();
				} else {
					for (Map.Entry<Integer, Object> entry : idMaps.entrySet()) {
						Toast.makeText(getApplicationContext(), "申请："+entry.getValue(), Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
		// 分享
		btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			//	Toast.makeText(getApplicationContext(), "分享", Toast.LENGTH_LONG).show();


			}
		});
		
		
		
	}
	
	/**
	 * 请求数据线程
	 * @param i判断是哪次请求数据，如果第一次请求则为0，如果是滑动list请求数据则为1
	 */
	private void requestDataThread(final int i) {
		
		if(i==0){
			showProcessDialog(false);
		}		
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestData(i);
					}
				});
		mThread.start();
	}
	
	private void requestData(int i){
		
		String url = "appPostSearch.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			
			params.add(new BasicNameValuePair("pageNo", pageNo+""));
			params.add(new BasicNameValuePair("pageSize", pageSize+""));
			params.add(new BasicNameValuePair("WorkRegion", workRegion));
			params.add(new BasicNameValuePair("JobsClass", jobsClass));
			params.add(new BasicNameValuePair("NeedProfession", needProfession));

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				JobListActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {
				result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
				JobListActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONArray resultJsonArray = responseJsonObject.getJSONArray("result");
				
				if(resultJsonArray==null||resultJsonArray.length()==0){
					String err = StringUtil.getAppException4MOS("没有您要搜索的结果");
					JobListActivity.this.sendExceptionMsg(err);
					return;
				}
				
				total = responseJsonObject.getInt("total");//总数
				if (resultJsonArray.length() > 15) {
					count = 15;
				} else {
					count = resultJsonArray.length();
				}
                for(int j=0;j<count;j++){
                	
                	Map<String, Object> item = new HashMap<String, Object>();
        			item.put("companyid", resultJsonArray.getJSONObject(i).get("companyid"));//公司id
        			item.put("companyname", resultJsonArray.getJSONObject(i).get("companyname"));// 公司
        			item.put("jobsName",resultJsonArray.getJSONObject(i).get("jobsName"));// 职位
        			item.put("clickNum",resultJsonArray.getJSONObject(i).get("clickNum"));//点击次数
        			item.put("computerLevel",resultJsonArray.getJSONObject(i).get("computerLevel"));//计算机能力
        			item.put("description",resultJsonArray.getJSONObject(i).get("description"));//工作描述
        			item.put("english",resultJsonArray.getJSONObject(i).get("english"));//语种
        			item.put("houseWhere",resultJsonArray.getJSONObject(i).get("houseWhere"));//是否提供住宿
        			item.put("jobsClass",resultJsonArray.getJSONObject(i).get("jobsClass"));//职位类别
        			item.put("jobsNumber",resultJsonArray.getJSONObject(i).get("jobsNumber"));//招聘人数
        			item.put("jobsstate",resultJsonArray.getJSONObject(i).get("jobsstate"));//职位状态
        			item.put("monthlySalary",resultJsonArray.getJSONObject(i).get("monthlySalary"));//月薪
        			item.put("needAge",resultJsonArray.getJSONObject(i).get("needAge"));//最小年纪
        			item.put("needEducation",resultJsonArray.getJSONObject(i).get("needEducation"));//学历
        			item.put("needHeight",resultJsonArray.getJSONObject(i).get("needHeight"));//身高
        			item.put("needProfession",resultJsonArray.getJSONObject(i).get("needProfession"));//专业
        			item.put("needWorkExperience",resultJsonArray.getJSONObject(i).get("needWorkExperience"));//职位状态
        			item.put("political",resultJsonArray.getJSONObject(i).get("political"));//政治面貌
        			item.put("postTime",resultJsonArray.getJSONObject(i).get("postTime"));//发布时间
        			item.put("rid",resultJsonArray.getJSONObject(i).get("rid"));
        			item.put("sex",resultJsonArray.getJSONObject(i).get("sex"));//性别
        			item.put("titles",resultJsonArray.getJSONObject(i).get("titles"));//技术
        			item.put("validityTime",resultJsonArray.getJSONObject(i).get("validityTime"));//有效时间
        			item.put("workManner",resultJsonArray.getJSONObject(i).get("workManner"));//工作方式
        			item.put("workRegion",resultJsonArray.getJSONObject(i).get("workRegion"));//工作地区
        			items.add(item);
                }
                if(i==0){
                	msg.what = 0;
    				
                }else{
                	msg.what = 1;
                }
                handler.sendMessage(msg);

				
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				JobListActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			JobListActivity.this.sendExceptionMsg(err);
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
		
	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			
			pageNo++;
			requestDataThread(1);//滑动list请求数据


		}
	};

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
			
			TextView jobName = null;
			TextView company = null;
			TextView degree = null;
			TextView workingYears = null;
			TextView workplace = null;
			TextView releaseTime = null;
			CheckBox checkbox = null;
			
			
			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				jobName = (TextView) convertView.findViewById(R.id.jobName);
				company = (TextView) convertView.findViewById(R.id.company);
				degree = (TextView) convertView.findViewById(R.id.degree);
				workingYears = (TextView) convertView.findViewById(R.id.workingYears);
				workplace = (TextView) convertView.findViewById(R.id.workplace);
				releaseTime = (TextView) convertView.findViewById(R.id.release_time);
				checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
				
				ViewCache viewCache = new ViewCache();
				viewCache.jobName = jobName;
				viewCache.company = company;
				viewCache.degree = degree;
				viewCache.workingYears = workingYears;
				viewCache.workplace = workplace;
				viewCache.releaseTime = releaseTime;
				viewCache.checkbox = checkbox;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				jobName = viewCache.jobName;
				company = viewCache.company;
				degree = viewCache.degree;
				workingYears = viewCache.workingYears;
				workplace = viewCache.workplace;
				releaseTime = viewCache.releaseTime;
				checkbox = viewCache.checkbox;
			}

			final int sequenceId = (Integer)list.get(position).get("sequenceId");
			jobName.setText(list.get(position).get("jobName").toString());
			company.setText(list.get(position).get("company").toString());
			degree.setText(list.get(position).get("degree").toString());
			workingYears.setText(list.get(position).get("workingYears").toString());
			workplace.setText(list.get(position).get("workplace").toString());
			releaseTime.setText(list.get(position).get("releaseTime").toString());
			
			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						checkBoxState.put(position, isChecked);
						idMaps.put(position, sequenceId);
					} else {
						checkBoxState.remove(position);
						idMaps.remove(position);
					}
				}
			});
			checkbox.setChecked(checkBoxState.get(position) == null ? false : true);
			
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(JobListActivity.this,CompanyInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag", "jobListActivity");
					intent.putExtras(bundle);
					startActivity(intent);
					
				}
			});
			
			return convertView;
		}
		
	}
	
	private final class ViewCache {
		public TextView jobName;// 职位名称
		public TextView company;// 公司
		public TextView degree;// 学历要求
		public TextView workingYears;// 工作年限
		public TextView workplace;// 工作地点
		public TextView releaseTime;// 工作地点
		public CheckBox checkbox;
	}
	
}
