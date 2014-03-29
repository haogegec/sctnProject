package com.sctn.sctnet.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.entity.LoginInfo;
import com.sctn.sctnet.sqlite.DBHelper;

/**
 * 职位搜索结果界面
 * 
 * @author 姜勇男
 * 
 */
public class JobListActivity extends BaicActivity {

	// 定义图片存放的地址
	public static String TEST_IMAGE;
	private MyAdapter jobListAdapter;
	private ListView jobList;
	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

	Map<Integer, Object> jobIdAndCompanyIdMaps = new HashMap<Integer, Object>();// 记录选中的checkbox的jobId和companyId，申请职位时用到
	Map<Integer, Object> jobIdMaps = new HashMap<Integer, Object>();// 记录选中的checkbox的jobId,收藏职位时用到
	Map<Integer, Object> jobShareMaps = new HashMap<Integer, Object>();// 记录选中的checkbox的jobId,分享职位时用到
	Map<Integer, Boolean> checkBoxState = new HashMap<Integer, Boolean>();// 记录checkbox的状态

	private Button btn_apply;// 申请
	private Button btn_collect;// 收藏
	private Button btn_share;// 分享

	private View footViewBar;// 下滑加载条
	private int count;// 一次可以显示的条数（=pageSize或者小于）
	// 请求数据
	private int pageNo = 1;
	private int pageSize = Constant.PageSize;
	private String workRegion;
	private String jobsClass;
	private String needProfession;

	private String workRegionName;
	private String jobsClassName;
	private String needProfessionName;
	// 返回数据
	private int total = 0;// 总条数
	private String result;// 服务端返回的json字符串

	private String jobIdAndCompanyId;// 职位搜索结果中，可以同时选择多个职位进行申请（格式：jobId1-companyId1|jobId2-companyId2|jobId3-companyId3|......）
	private String jobId;// 职位搜索结果中，可以同时选择多个职位进行收藏（格式：jobId1|jobId2|jobId3|......）
	private String jobShare;// 职位分享，可以同时选择多个职位进行分享（格式：company开始招聘job|company开始招聘job|company开始招聘job|......）
	private String whichUrl = "";
	private String type;
	private String key;

	private int itemCount; // 当前窗口可见项总数
	private int visibleLastIndex = 0;// 最后的可视项索引
	private SQLiteDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_list_activity);
		setTitleBar("共" + total + "个职位", View.VISIBLE, View.GONE);
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle.getString("whichActivity") != null) {
			whichUrl = "workSearchActivity";
			type = bundle.getString("type");
			key = bundle.getString("key");
		} else {
			workRegion = bundle.getString("areaId");
			jobsClass = bundle.getString("industryTypeId");
			needProfession = bundle.getString("positionTypeId");

			workRegionName = bundle.getString("areaName");
			jobsClassName = bundle.getString("industryTypeName");
			needProfessionName = bundle.getString("positionTypeName");
		}

		//初始化本地数据库
		DBHelper dbHelper = new DBHelper(this,"jobSearchLog");
		database = dbHelper.getWritableDatabase();
		
		// 初始化ShareSDK
		// AbstractWeibo.initSDK(this);
		initImagePath();
		initAllView();
		reigesterAllEvent();
		requestDataThread(0);// 第一次请求数据
	}

	public Long getCount() {
		  
		  Cursor cursor = database.rawQuery("select count(*) from jobSearchLog",null);
		  cursor.moveToFirst();
		  Long count = cursor.getLong(0);
		  cursor.close();
		  return count;
	}
	/**
	 * 初始化分享的图片
	 */
	private void initImagePath() {
		try {// 判断SD卡中是否存在此文件夹
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pic.png";
			} else {
				TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath() + "/pic.png";
			}
			File file = new File(TEST_IMAGE);
			// 判断图片是否存此文件夹中
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}

	/**
	 * 第一次请求数据初始化页面
	 */
	private void initUI() {
		// setTitleBar("共" + total + "个职位", View.VISIBLE, View.GONE);
		super.getTitleTextView().setText("共" + total + "个职位");
		if (total > pageSize * pageNo) {
			jobList.addFooterView(footViewBar);// 添加list底部更多按钮
		}
		jobList.setAdapter(jobListAdapter);
	}

	/**
	 * 滑动list请求数据更新页面
	 */
	private void updateUI() {

		if (total <= pageSize * pageNo) {
			jobList.removeFooterView(footViewBar);// 添加list底部更多按钮
		}
		jobListAdapter.notifyDataSetChanged();
		jobList.setAdapter(jobListAdapter);
		jobList.setSelection(visibleLastIndex - itemCount + 1);
	}

	@Override
	protected void initAllView() {
		jobList = (ListView) findViewById(R.id.lv_jobList);
		btn_apply = (Button) findViewById(R.id.btn_apply);
		btn_collect = (Button) findViewById(R.id.btn_collect);
		btn_share = (Button) findViewById(R.id.btn_share);
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

				if (LoginInfo.isLogin()) {
					if (jobIdAndCompanyIdMaps.size() == 0) {
						Toast.makeText(getApplicationContext(), "请选择职位", Toast.LENGTH_LONG).show();
					} else {
						for (Map.Entry<Integer, Object> entry : jobIdAndCompanyIdMaps.entrySet()) {
							// Toast.makeText(getApplicationContext(),
							// "申请的JOB_ID-COMPANY_ID：" + entry.getValue(),
							// Toast.LENGTH_LONG).show();
							if (StringUtil.isBlank(jobIdAndCompanyId)) {
								jobIdAndCompanyId = entry.getValue().toString();
							} else {
								jobIdAndCompanyId += "|" + entry.getValue().toString();
							}
						}
						applyThread();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(JobListActivity.this, LoginActivity.class);
					startActivityForResult(intent, Constant.LOGIN_APPLY_JOB_ACTIVITY);
				}
			}
		});

		// 收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (LoginInfo.isLogin()) {
					if (jobIdMaps.size() == 0) {
						Toast.makeText(getApplicationContext(), "请选择职位", Toast.LENGTH_LONG).show();
					} else {
						for (Map.Entry<Integer, Object> entry : jobIdMaps.entrySet()) {
							// Toast.makeText(getApplicationContext(),
							// "申请的 JOB_ID：" + entry.getValue(),
							// Toast.LENGTH_LONG).show();
							if (StringUtil.isBlank(jobId)) {
								jobId = entry.getValue().toString();
							} else {
								jobId += "|" + entry.getValue().toString();
							}
						}
						collectThread();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(JobListActivity.this, LoginActivity.class);
					startActivityForResult(intent, Constant.LOGIN_COLLECT_JOB_ACTIVITY);
				}

			}
		});

		// 分享
		btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// if (jobShareMaps.size() == 0) {
				// Toast.makeText(getApplicationContext(), "请选择职位分享",
				// Toast.LENGTH_LONG).show();
				// } else {
				for (Map.Entry<Integer, Object> entry : jobShareMaps.entrySet()) {

					if (jobShare.length() == 0) {
						jobShare = entry.getValue().toString();
					} else {
						jobShare += "|" + entry.getValue().toString();
					}
				}
				// showGrid(false);
			}

			// }
		});
		super.titleLeftButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JobListActivity.this,JobSearchActivity.class);
				startActivity(intent);
				finish();				
			}
			
		});

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_HOME))
				&& event.getRepeatCount() == 0) {
			Intent intent = new Intent(JobListActivity.this,JobSearchActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}
	// /**
	// * 使用快捷分享完成图文分享
	// */
	// private void showGrid(boolean silent) {
	// Intent i = new Intent(this, ShareAllGird.class);
	// // 分享时Notification的图标
	// i.putExtra("notif_icon", R.drawable.ic_launcher);
	// // 分享时Notification的标题
	// i.putExtra("notif_title", this.getString(R.string.app_name));
	// // text是分享文本，所有平台都需要这个字段
	// i.putExtra("text","欢迎是用shareSDK");
	//
	//
	// // 是否直接分享
	// i.putExtra("silent", silent);
	// this.startActivity(i);
	// }

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void applyThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						apply();
					}
				});
		mThread.start();
	}

	// 职位申请
	protected void apply() {

		String url = "";

		Message msg = new Message();

		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			long userId = SharePreferencesUtils.getSharedlongData("userId");
			params.add(new BasicNameValuePair("Userid", userId + ""));

			if (-1 == jobIdAndCompanyId.indexOf("|")) {

				String[] temp = jobIdAndCompanyId.split("-");

				url = "appPersonCenter!userSendRusume.app";
				params.add(new BasicNameValuePair("Companyid", temp[1]));
				params.add(new BasicNameValuePair("jobsid", temp[0]));
			} else {
				url = "appPersonCenter!batSendRusume.app";
				params.add(new BasicNameValuePair("batRusumes", jobIdAndCompanyId));
			}
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				JobListActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);
			if ("0".equals(responseJsonObject.getString("resultcode"))) {// 表示职位申请成功

				msg.what = Constant.APPLY_SUCCESS;
				handler.sendMessage(msg);
			} else {
				String errorResult = responseJsonObject.getString("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				sendExceptionMsg(err);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void collectThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						collect();
					}
				});
		mThread.start();
	}

	// 职位收藏
	protected void collect() {

		String url = "";

		Message msg = new Message();

		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			long userId = SharePreferencesUtils.getSharedlongData("userId");
			params.add(new BasicNameValuePair("Userid", userId + ""));
			if (-1 == jobId.indexOf("|")) {
				url = "appPersonCenter!insertUserJobInfo.app";
				params.add(new BasicNameValuePair("jobsid", jobId));
			} else {
				url = "appPersonCenter!batUserJobInfo.app";
				params.add(new BasicNameValuePair("batJobs", jobId));
			}

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				JobListActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);
			if ("0".equals(responseJsonObject.getString("resultcode"))) {// 表示职位收藏成功
				msg.what = Constant.COLLECT_SUCCESS;
				handler.sendMessage(msg);
			} else {
				String errorResult = responseJsonObject.getString("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				sendExceptionMsg(err);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 请求数据线程
	 * 
	 * @param i判断是哪次请求数据
	 *            ，如果第一次请求则为0，如果是滑动list请求数据则为1
	 */
	private void requestDataThread(final int i) {

		if (i == 0) {
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

	private void requestData(int i) {

		String url;

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			if (whichUrl.equals("workSearchActivity")) {
				url = "appPostSearch!queryKey.app";
				params.add(new BasicNameValuePair("page", pageNo + ""));
				params.add(new BasicNameValuePair("pageSize", pageSize + ""));
				params.add(new BasicNameValuePair("type", type));
				params.add(new BasicNameValuePair("key", key));
			} else {
				url = "appPostSearch.app";
				params.add(new BasicNameValuePair("page", pageNo + ""));
				params.add(new BasicNameValuePair("pageSize", pageSize + ""));
				params.add(new BasicNameValuePair("WorkRegion", workRegion));
				params.add(new BasicNameValuePair("JobsClass", jobsClass));
				params.add(new BasicNameValuePair("NeedProfession", needProfession));
				params.add(new BasicNameValuePair("key", ""));
			}

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				JobListActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {
				result = StringUtil.getAppException4MOS("很遗憾，您要的信息未找到！");
				JobListActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONArray resultJsonArray = responseJsonObject.getJSONArray("result");

				if (resultJsonArray == null || resultJsonArray.length() == 0) {
					String err = StringUtil.getAppException4MOS("很遗憾，您要的信息未找到！");
					JobListActivity.this.sendExceptionMsg(err);
					total = 0;
					return;
				}
                
				total = responseJsonObject.getInt("resultCount");// 总数
				//将搜索记录保存到本地数据库中
				String[] id = {String.valueOf(0)};
				if(getCount()>5){
					database.delete("jobSearchLog", "_id=?", id);
				}
				String[] arg = {workRegion,jobsClass,needProfession};
				database.delete("jobSearchLog", "workAreaId=? and jobClassId=? and needProfessionId=?", arg);
				ContentValues values = new ContentValues();
				values.put("workAreaName", workRegionName);
				values.put("jobClassName", jobsClassName);
				values.put("needProfessionName", needProfessionName);
				values.put("workAreaId", workRegion);
				values.put("jobClassId", jobsClass);
				values.put("needProfessionId", needProfession);
				values.put("total", "约"+total+"个");
								
				database.insert("jobSearchLog", null, values);
				
				
				if (resultJsonArray.length() > 15) {
					count = 15;
				} else {
					count = resultJsonArray.length();
				}
				for (int j = 0; j < count; j++) {

					Map<String, Object> item = new HashMap<String, Object>();
					item.put("companyid", resultJsonArray.getJSONObject(j).get("companyid"));// 公司id
					item.put("companyname", resultJsonArray.getJSONObject(j).get("companyname"));// 公司
					item.put("jobsid", resultJsonArray.getJSONObject(j).get("jobsid"));// 职位ID
					item.put("jobsName", resultJsonArray.getJSONObject(j).get("jobsname"));// 职位
					item.put("clickNum", resultJsonArray.getJSONObject(j).get("clicknum"));// 点击次数
					item.put("computerLevel", resultJsonArray.getJSONObject(j).get("computerlevel"));// 计算机能力
					item.put("description", resultJsonArray.getJSONObject(j).get("description"));// 工作描述
					item.put("english", resultJsonArray.getJSONObject(j).get("english"));// 语种
					item.put("houseWhere", resultJsonArray.getJSONObject(j).get("housewhere"));// 是否提供住宿
					item.put("jobsClass", resultJsonArray.getJSONObject(j).get("jobsclass"));// 职位类别
					item.put("jobsNumber", resultJsonArray.getJSONObject(j).get("jobsnumber"));// 招聘人数
					item.put("jobsstate", resultJsonArray.getJSONObject(j).get("jobsstate"));// 职位状态
					item.put("monthlySalary", resultJsonArray.getJSONObject(j).get("monthlysalary"));// 月薪
					item.put("needAge", resultJsonArray.getJSONObject(j).get("needages"));// 最小年纪
					item.put("needEducation", resultJsonArray.getJSONObject(j).get("neededucation"));// 学历
					item.put("needHeight", resultJsonArray.getJSONObject(j).get("needheight"));// 身高
					item.put("needProfession", resultJsonArray.getJSONObject(j).get("needprofession"));// 专业
					item.put("needWorkExperience", resultJsonArray.getJSONObject(j).get("needworkexperience"));// 职位状态
					item.put("political", resultJsonArray.getJSONObject(j).get("political"));// 政治面貌
					item.put("postTime", resultJsonArray.getJSONObject(j).get("posttime"));// 发布时间
					item.put("rid", resultJsonArray.getJSONObject(j).get("rid"));
					item.put("sex", resultJsonArray.getJSONObject(j).get("sex"));// 性别
					item.put("titles", resultJsonArray.getJSONObject(j).get("titles"));// 技术
					item.put("validityTime", resultJsonArray.getJSONObject(j).get("validitytime"));// 有效时间
					item.put("workManner", resultJsonArray.getJSONObject(j).get("workmanner"));// 工作方式
					item.put("workRegion", resultJsonArray.getJSONObject(j).get("workregionname"));// 工作地区,workregion是编号
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
			case Constant.APPLY_SUCCESS:
				Toast.makeText(getApplicationContext(), "申请成功", Toast.LENGTH_SHORT).show();
				break;
			case Constant.COLLECT_SUCCESS:
				Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
				break;
			}
			closeProcessDialog();
		}
	};

	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			itemCount = visibleItemCount;
			visibleLastIndex = firstVisibleItem + visibleItemCount - 1;

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				if (view.getLastVisiblePosition() == jobListAdapter.getCount()) {
					pageNo++;
					requestDataThread(1);// 滑动list请求数据
				}
			}

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
			// TextView workingYears = null;
			TextView workplace = null;
			TextView releaseTime = null;
			CheckBox checkbox = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				jobName = (TextView) convertView.findViewById(R.id.jobName);
				company = (TextView) convertView.findViewById(R.id.company);
				degree = (TextView) convertView.findViewById(R.id.degree);
				// workingYears = (TextView)
				// convertView.findViewById(R.id.workingYears);
				workplace = (TextView) convertView.findViewById(R.id.workplace);
				releaseTime = (TextView) convertView.findViewById(R.id.release_time);
				checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

				ViewCache viewCache = new ViewCache();
				viewCache.jobName = jobName;
				viewCache.company = company;
				viewCache.degree = degree;
				// viewCache.workingYears = workingYears;
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
				// workingYears = viewCache.workingYears;
				workplace = viewCache.workplace;
				releaseTime = viewCache.releaseTime;
				checkbox = viewCache.checkbox;
			}

			// final int sequenceId =
			// (Integer)list.get(position).get("sequenceId");
			final String jobsid = list.get(position).get("jobsid").toString();
			jobName.setText(list.get(position).get("jobsName").toString());
			company.setText(list.get(position).get("companyname").toString());
			degree.setText(list.get(position).get("needEducation").toString());
			// workingYears.setText(list.get(position).get("workingYears").toString());
			workplace.setText(list.get(position).get("workRegion").toString());
			releaseTime.setText(list.get(position).get("validityTime").toString());

			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						checkBoxState.put(position, isChecked);
						jobIdAndCompanyIdMaps.put(position, jobsid + "-" + list.get(position).get("companyid"));
						jobIdMaps.put(position, jobsid);
						jobShareMaps.put(position, list.get(position).get("companyname") + "正在招聘" + list.get(position).get("jobsName"));
					} else {
						checkBoxState.remove(position);
						jobIdAndCompanyIdMaps.remove(position);
						jobIdMaps.remove(position);
						jobShareMaps.remove(position);
					}
				}
			});
			checkbox.setChecked(checkBoxState.get(position) == null ? false : true);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(JobListActivity.this, CompanyInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag", "jobListActivity");
					bundle.putString("jobId", list.get(position).get("jobsid").toString());
					bundle.putString("companyId", list.get(position).get("companyid").toString());
					bundle.putString("workRegionName", workRegionName);
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
		// public TextView workingYears;// 工作年限
		public TextView workplace;// 工作地点
		public TextView releaseTime;// 工作地点
		public CheckBox checkbox;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.LOGIN_APPLY_JOB_ACTIVITY: {
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				break;
			}
			case Constant.LOGIN_COLLECT_JOB_ACTIVITY: {
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				break;
			}
			}
		}
	}

	protected void onDestroy() {
		// 结束ShareSDK的统计功能并释放资源
		// AbstractWeibo.stopSDK(this);
		super.onDestroy();
	}
}
