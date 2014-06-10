package com.sctn.sctnet.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.DateUtil;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.SortUtil;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.cache.CacheProcess;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.entity.LoginInfo;
import com.sctn.sctnet.sqlite.DBHelper;
import com.sctn.sctnet.view.CustomDialog;

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
	private List<Map<String, String>> items = new ArrayList<Map<String, String>>();

	Map<Integer, Object> jobIdAndCompanyIdMaps = new HashMap<Integer, Object>();// 记录选中的checkbox的jobId和companyId，申请职位时用到
	Map<Integer, Object> jobNameAndCompanyNameMaps = new HashMap<Integer, Object>();// 记录选中的checkbox的jobName和companyName，申请职位时用到

	//	Map<Integer, Object> jobIdMaps = new HashMap<Integer, Object>();// 记录选中的checkbox的jobId,收藏职位时用到
	
	Map<Integer, Object> jobIdAndCompanyIdMaps2 = new HashMap<Integer, Object>();// 记录选中的checkbox的jobId和companyId，收藏职位时用到
	Map<Integer, Object> jobNameAndCompanyNameMaps2 = new HashMap<Integer, Object>();// 记录选中的checkbox的jobName和companyName，收藏职位时用到
	
	Map<Integer, Object> jobShareMaps = new HashMap<Integer, Object>();// 记录选中的checkbox的jobId,分享职位时用到
	Map<Integer, Boolean> checkBoxState = new HashMap<Integer, Boolean>();// 记录checkbox的状态

	private Button btn_apply;// 申请
	private Button btn_collect;// 收藏
	private Button btn_share;// 分享
	private long userId;// 用户唯一标识
	private CacheProcess cacheProcess;// 缓存数据
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
	private String jobNameAndCompanyName;
	
	private String jobIdAndCompanyId2;// 职位搜索结果中，可以同时选择多个职位进行收藏（格式：jobId1-companyId1|jobId2-companyId2|jobId3-companyId3|......）
	private String jobNameAndCompanyName2;
	
	private String jobId;// 职位搜索结果中，可以同时选择多个职位进行收藏（格式：jobId1|jobId2|jobId3|......）
	private String jobShare;// 职位分享，可以同时选择多个职位进行分享（格式：company开始招聘job|company开始招聘job|company开始招聘job|......）
	private String whichUrl = "";
	private String type;
	private String key;

	private int itemCount; // 当前窗口可见项总数
	private int visibleLastIndex = 0;// 最后的可视项索引
	private SQLiteDatabase database;
	
	private String appliedJobsName = "";// 已申请过的职位名（多个用逗号隔开）
	private String collectedJobsName = "";// 已收藏过的职位名（多个用逗号隔开）

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_list_activity);
		setTitleBar("共" + total + "个职位", View.VISIBLE, View.GONE);

		// 初始化Share SDK，一定要 重写 ondestroy（），停止SDK
		ShareSDK.initSDK(this);
		
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

		// 初始化本地数据库
		DBHelper dbHelper = new DBHelper(this, "jobSearchLog");
		database = dbHelper.getWritableDatabase();

		initImagePath();
		initAllView();
		reigesterAllEvent();
		requestDataThread(0);// 第一次请求数据
	}

	public Long getCount(String tableName) {

		Cursor cursor = database.rawQuery("select count(*) from " + tableName, null);
		cursor.moveToFirst();
		Long count = cursor.getLong(0);
		cursor.close();
		return count;
	}
	
	@Override
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}
	
//	@Override
//	protected void onResume() {
//		ShareSDK.stopSDK(this);
//		ShareSDK.initSDK(this);
//		super.onResume();
//	}

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
		// jobList.setAdapter(jobListAdapter);
		// jobList.setSelection(visibleLastIndex - itemCount + 1);
	}

	@Override
	protected void initAllView() {
		cacheProcess = new CacheProcess();
		userId = cacheProcess.getLongCacheValueInSharedPreferences(this, "userId");
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
					String userId = SharePreferencesUtils.getSharedlongData("userId") + "";

					if (LoginInfo.hasResume(userId)) {// 如果当前用户已经有简历
						if (jobIdAndCompanyIdMaps.size() == 0) {
							Toast.makeText(getApplicationContext(), "请选择职位", Toast.LENGTH_LONG).show();
						} else {
							jobIdAndCompanyId = null;
							jobNameAndCompanyName = null;
							
							for (Map.Entry<Integer, Object> entry : jobIdAndCompanyIdMaps.entrySet()) {
								if (StringUtil.isBlank(jobIdAndCompanyId)) {
									jobIdAndCompanyId = entry.getValue().toString();
								} else {
									jobIdAndCompanyId += "|" + entry.getValue().toString();
								}
							}
							
							for (Map.Entry<Integer, Object> entry : jobNameAndCompanyNameMaps.entrySet()) {
								if (StringUtil.isBlank(jobNameAndCompanyName)) {
									jobNameAndCompanyName = entry.getValue().toString();
								} else {
									jobNameAndCompanyName += "|" + entry.getValue().toString();
								}
							}
							
							
							applyThread();
						}
					} else {// 如果当前用户还没有创建简历，就跳到创建简历页面

						
						
						final CustomDialog dialog = new CustomDialog(JobListActivity.this, R.style.CustomDialog);
//						dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
					//	dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
						dialog.setTitle("友情提示");
						dialog.setMessage("您的简历还不完善暂不能申请职位，是否要去完善您的简历呢？");
						dialog.setOnPositiveListener("是",new OnClickListener(){

							@Override
							public void onClick(View v) {
								
								Intent intent = new Intent(JobListActivity.this, ResumeManageActivity.class);
								startActivity(intent);
							}
							
						});
						dialog.setOnNegativeListener("否", new OnClickListener(){

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
							
						});
						dialog.show();
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
					if (jobIdAndCompanyIdMaps.size() == 0) {
						Toast.makeText(getApplicationContext(), "请选择职位", Toast.LENGTH_LONG).show();
					} else {
						
						jobIdAndCompanyId2 = null;
						jobNameAndCompanyName2 = null;
						
						for (Map.Entry<Integer, Object> entry : jobIdAndCompanyIdMaps2.entrySet()) {
							if (StringUtil.isBlank(jobIdAndCompanyId2)) {
								jobIdAndCompanyId2 = entry.getValue().toString();
							} else {
								jobIdAndCompanyId2 += "|" + entry.getValue().toString();
							}
						}
						
						for (Map.Entry<Integer, Object> entry : jobNameAndCompanyNameMaps2.entrySet()) {
							if (StringUtil.isBlank(jobNameAndCompanyName2)) {
								jobNameAndCompanyName2 = entry.getValue().toString();
							} else {
								jobNameAndCompanyName2 += "|" + entry.getValue().toString();
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

				if (LoginInfo.isLogin()) {
					if (jobShareMaps.size() == 0) {
						Toast.makeText(getApplicationContext(), "请选择职位分享", Toast.LENGTH_LONG).show();
					} else {
						
						for (Map.Entry<Integer, Object> entry : jobShareMaps.entrySet()) {

							if (StringUtil.isBlank(jobShare)) {
								jobShare = entry.getValue().toString();
							} else {
								jobShare += "\n\n\n" + entry.getValue().toString();
							}
						}
						
						
						OnekeyShare oks = new OnekeyShare();
						oks.setNotification(R.drawable.logo, getString(R.string.app_name));
						oks.setTitle("我看到一个很不错的招聘信息，想告诉大家，有兴趣的可以看看哦~");
						oks.setTitleUrl("http://www.scrc168.com/");
						oks.setText(jobShare);
//						oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
						oks.setSite(getApplicationContext().getString(R.string.app_name));
				        oks.setSiteUrl("http://www.scrc168.com/"); // siteUrl是分享此内容的网站地址，这是分享到 QQ空间时候的参数
						oks.setCallback(new PlatformActionListener() {

							@Override
							public void onError(Platform arg0, int arg1, Throwable arg2) {
								handler.sendEmptyMessage(Constant.SHARE_ERROR);
							}

							@Override
							public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
								handler.sendEmptyMessage(Constant.SHARE_COMPLETE);
							}

							@Override
							public void onCancel(Platform arg0, int arg1) {
								handler.sendEmptyMessage(Constant.SHARE_CANCEL);
							}
						});
						oks.show(getApplicationContext());
					}
				} else {
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(JobListActivity.this, LoginActivity.class);
					startActivityForResult(intent, Constant.LOGIN_SHARE_JOB_ACTIVITY);
				}
			}

		});
		super.titleLeftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (StringUtil.isBlank(whichUrl)) {
					Intent intent = new Intent(JobListActivity.this, JobSearchActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(JobListActivity.this, WorkSearchActivity.class);
					startActivity(intent);
				}
				finish();

			}

		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_HOME)) && event.getRepeatCount() == 0) {
			if (StringUtil.isBlank(whichUrl)) {
				Intent intent = new Intent(JobListActivity.this, JobSearchActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(JobListActivity.this, WorkSearchActivity.class);
				startActivity(intent);
			}
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
				String[] temp2 = jobNameAndCompanyName.split("-");
				url = "appPersonCenter!userSendRusume.app";
				params.add(new BasicNameValuePair("Companyid", temp[1]));
				params.add(new BasicNameValuePair("jobsid", temp[0]));
				params.add(new BasicNameValuePair("CompanyName", temp2[1]));
				params.add(new BasicNameValuePair("jobsName", temp2[0]));
				params.add(new BasicNameValuePair("SendTime", DateUtil.getFormatCurrentTime("yyyy:MM:dd")));
				
			} else {
				String[] temp = jobIdAndCompanyId.split("\\|");
				String[] temp2 = jobNameAndCompanyName.split("\\|");
				
				String args = "";
				
				for(int i=0; i<temp.length; i++){
					if(StringUtil.isBlank(args)){
						args += temp[i] + "-" + temp2[i] + "-" + DateUtil.getFormatCurrentTime("yyyy:MM:dd");
					} else {
						args += "|" + temp[i] + "-" + temp2[i] + "-" + DateUtil.getFormatCurrentTime("yyyy:MM:dd");
					}
				}
				
				url = "appPersonCenter!batSendRusume.app";
				params.add(new BasicNameValuePair("batRusumes", args));
			}
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				JobListActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONArray responseJsonArray = new JSONArray(result);
			JSONObject responseJsonObject = responseJsonArray.getJSONObject(responseJsonArray.length()-1);
			
			if(0 == responseJsonObject.getInt("resultcode")){// 表示申请成功
					if ("0".equals(responseJsonObject.getString("resultcode"))) {// 表示职位申请成功
						msg.what = Constant.APPLY_SUCCESS;
						handler.sendMessage(msg);
					} 
			} else if(1 == responseJsonObject.getInt("resultcode")){// 表示申请失败
				if(1 == responseJsonObject.getInt("flag")){ //flag==1 代表已经申请过了
					
					int count = responseJsonObject.getInt("count");//申请过的职位个数
					appliedJobsName = "";
					for(int i=0; i<count; i++){
						JSONArray jarray =  responseJsonArray.getJSONArray(i);
						JSONObject jobject = jarray.getJSONObject(0);
						
						appliedJobsName += StringUtil.isBlank(appliedJobsName)? jobject.getString("jobsname"):","+jobject.getString("jobsname");
					}
					
					msg.what = Constant.APPLY_FAILED;
					handler.sendMessage(msg);
				} else {// 系统出错
					String errorResult = responseJsonObject.getString("result");
					String err = StringUtil.getAppException4MOS(errorResult);
					sendExceptionMsg(err);
				}
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
			if (-1 == jobIdAndCompanyId2.indexOf("|")) {
				
				String[] temp = jobIdAndCompanyId2.split("-");
				String[] temp2 = jobNameAndCompanyName2.split("-");
				
				url = "appPersonCenter!insertUserJobInfo.app";
				params.add(new BasicNameValuePair("Companyid", temp[1]));
				params.add(new BasicNameValuePair("jobsid", temp[0]));
				params.add(new BasicNameValuePair("CompanyName", temp2[1]));
				params.add(new BasicNameValuePair("jobsName", temp2[0]));
				params.add(new BasicNameValuePair("SendTime", DateUtil.getFormatCurrentTime("yyyy:MM:dd")));
				
//				params.add(new BasicNameValuePair("jobsid", jobId));
			} else {
				
				String[] temp = jobIdAndCompanyId2.split("\\|");
				String[] temp2 = jobNameAndCompanyName2.split("\\|");
				
				String args = "";
				
				for(int i=0; i<temp.length; i++){
					if(StringUtil.isBlank(args)){
						args += temp[i] + "-" + temp2[i] + "-" + DateUtil.getFormatCurrentTime("yyyy:MM:dd");
					} else {
						args += "|" + temp[i] + "-" + temp2[i] + "-" + DateUtil.getFormatCurrentTime("yyyy:MM:dd");
					}
				}
				
				url = "appPersonCenter!batUserJobInfo.app";
				params.add(new BasicNameValuePair("batJobs", args));
				
//				params.add(new BasicNameValuePair("batJobs", jobId));
			}

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				JobListActivity.this.sendExceptionMsg(result);
				return;
			}
			
			JSONArray responseJsonArray = new JSONArray(result);
			JSONObject responseJsonObject = responseJsonArray.getJSONObject(responseJsonArray.length()-1);
			
			if(0 == responseJsonObject.getInt("resultcode")){// 表示申请成功
					if ("0".equals(responseJsonObject.getString("resultcode"))) {// 表示职位申请成功
						msg.what = Constant.COLLECT_SUCCESS;
						handler.sendMessage(msg);
					} 
			} else if(1 == responseJsonObject.getInt("resultcode")){// 表示申请失败
				if(1 == responseJsonObject.getInt("flag")){ //flag==1 代表已经申请过了
					
					int count = responseJsonObject.getInt("count");//申请过的职位个数
					collectedJobsName = "";
					for(int i=0; i<count; i++){
						JSONArray jarray =  responseJsonArray.getJSONArray(i);
						JSONObject jobject = jarray.getJSONObject(0);
						
						collectedJobsName += StringUtil.isBlank(collectedJobsName)? jobject.getString("jobsname"):","+jobject.getString("jobsname");
					}
					
					msg.what = Constant.COLLECT_FAILED;
					handler.sendMessage(msg);
				} else {// 系统出错
					String errorResult = responseJsonObject.getString("result");
					String err = StringUtil.getAppException4MOS(errorResult);
					sendExceptionMsg(err);
				}
			}

//			JSONObject responseJsonObject = new JSONObject(result);
//			if(1 == responseJsonObject.getInt("flag")){
//				msg.what = Constant.COLLECT_FAILED;
//				handler.sendMessage(msg);
//			} else {
//				if ("0".equals(responseJsonObject.getString("resultcode"))) {// 表示职位收藏成功
//					msg.what = Constant.COLLECT_SUCCESS;
//					handler.sendMessage(msg);
//				} else {
//					String errorResult = responseJsonObject.getString("result");
//					String err = StringUtil.getAppException4MOS(errorResult);
//					sendExceptionMsg(err);
//				}
//			}
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
				params.add(new BasicNameValuePair("Userid", userId + ""));
			} else {
				url = "appPostSearch.app";
				params.add(new BasicNameValuePair("page", pageNo + ""));
				params.add(new BasicNameValuePair("pageSize", pageSize + ""));
				params.add(new BasicNameValuePair("WorkRegion", workRegion));
				params.add(new BasicNameValuePair("JobsClass", jobsClass));
				params.add(new BasicNameValuePair("NeedProfession", needProfession));
				params.add(new BasicNameValuePair("key", ""));
				params.add(new BasicNameValuePair("Userid", userId + ""));
			}

			result = getPostHttpContent(url, params);
			System.out.println("-----"+result);
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
				// 将搜索记录保存到本地数据库中
				String[] id = { String.valueOf(0) };
				if (StringUtil.isBlank(whichUrl)) {
					if (getCount("jobSearchLog") > 5) {
						database.delete("jobSearchLog", "_id=?", id);
					}
					String[] arg = { workRegion, jobsClass, needProfession };
					database.delete("jobSearchLog", "workAreaId=? and jobClassId=? and needProfessionId=?", arg);
					ContentValues values = new ContentValues();
					values.put("workAreaName", workRegionName);
					values.put("jobClassName", jobsClassName);
					values.put("needProfessionName", needProfessionName);
					values.put("workAreaId", workRegion);
					values.put("jobClassId", jobsClass);
					values.put("needProfessionId", needProfession);
					values.put("total", "约" + total + "个");

					database.insert("jobSearchLog", null, values);
				} else {
					if (getCount("searchLog") > 5) {
						database.delete("searchLog", "_id=?", id);
					}
					String[] arg = { key };
					database.delete("searchLog", "key=?", arg);
					ContentValues values = new ContentValues();
					values.put("key", key);

					database.insert("searchLog", null, values);
				}

				if (resultJsonArray.length() > 15) {
					count = 15;
				} else {
					count = resultJsonArray.length();
				}
				for (int j = 0; j < count; j++) {

					Map<String, String> item = new HashMap<String, String>();
					item.put("companyid", resultJsonArray.getJSONObject(j).getString("companyid"));// 公司id
					item.put("companyname", resultJsonArray.getJSONObject(j).getString("companyname"));// 公司
					item.put("jobsid", resultJsonArray.getJSONObject(j).getString("jobsid"));// 职位ID
					item.put("jobsName", resultJsonArray.getJSONObject(j).getString("jobsname"));// 职位
					item.put("clickNum", resultJsonArray.getJSONObject(j).getString("clicknum"));// 点击次数
					item.put("computerLevel", resultJsonArray.getJSONObject(j).getString("computerlevel"));// 计算机能力
					item.put("description", resultJsonArray.getJSONObject(j).getString("description"));// 工作描述
					item.put("english", resultJsonArray.getJSONObject(j).getString("english"));// 语种
					item.put("houseWhere", resultJsonArray.getJSONObject(j).getString("housewhere"));// 是否提供住宿
					item.put("jobsClass", resultJsonArray.getJSONObject(j).getString("jobsclass"));// 职位类别
					item.put("jobsNumber", resultJsonArray.getJSONObject(j).getString("jobsnumber"));// 招聘人数
					item.put("jobsstate", resultJsonArray.getJSONObject(j).getString("jobsstate"));// 职位状态
					item.put("monthlySalary", resultJsonArray.getJSONObject(j).getString("monthlysalary"));// 月薪
					item.put("needAge", resultJsonArray.getJSONObject(j).getString("needages"));// 最小年纪
					item.put("needEducation", resultJsonArray.getJSONObject(j).getString("neededucationname"));// 学历
					item.put("needHeight", resultJsonArray.getJSONObject(j).getString("needheight"));// 身高
					item.put("needProfession", resultJsonArray.getJSONObject(j).getString("needprofession"));// 专业
					item.put("needWorkExperience", resultJsonArray.getJSONObject(j).getString("needworkexperience"));// 职位状态
					item.put("political", resultJsonArray.getJSONObject(j).getString("political"));// 政治面貌
					if(StringUtil.isBlank(resultJsonArray.getJSONObject(j).getString("posttime"))){
						item.put("postTime", resultJsonArray.getJSONObject(j).getString("posttime"));
					} else {
						item.put("postTime", resultJsonArray.getJSONObject(j).getString("posttime").substring(0,10));// 发布时间
					}
					
					item.put("rid", resultJsonArray.getJSONObject(j).getString("rid"));
					item.put("sex", resultJsonArray.getJSONObject(j).getString("sex"));// 性别
					item.put("titles", resultJsonArray.getJSONObject(j).getString("titles"));// 技术
					if (resultJsonArray.getJSONObject(j).getString("validitytime") != null && !"".equals(resultJsonArray.getJSONObject(j).getString("validitytime"))) {
						if (!StringUtil.isBlank(resultJsonArray.getJSONObject(j).getString("validitytime"))) {
							if (resultJsonArray.getJSONObject(j).getString("validitytime").length() > 11) {
								item.put("validityTime", resultJsonArray.getJSONObject(j).getString("validitytime").substring(0, 10));// 有效时间
							}
						} else {
							item.put("validityTime", resultJsonArray.getJSONObject(j).getString("validitytime"));// 有效时间
						}
					} else {
						item.put("validityTime", resultJsonArray.getJSONObject(j).getString("validitytime"));
					}
					item.put("workManner", resultJsonArray.getJSONObject(j).getString("workmanner"));// 工作方式
					item.put("workRegion", resultJsonArray.getJSONObject(j).getString("workregionname"));// 工作地区,workregion是编号
					item.put("contract", resultJsonArray.getJSONObject(j).getString("phone"));
					items.add(item);
				}
				
//				Collections.sort(items, new Sort());
				
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
				
			case Constant.APPLY_FAILED:
				Toast.makeText(getApplicationContext(), "您已申请过"+appliedJobsName+"职位", Toast.LENGTH_SHORT).show();
				break;
				
			case Constant.COLLECT_SUCCESS:
				Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
				break;
				
			case Constant.COLLECT_FAILED:
				Toast.makeText(getApplicationContext(), "您已收藏过"+collectedJobsName+"职位", Toast.LENGTH_SHORT).show();
				break;
				
			case Constant.SHARE_COMPLETE:
				Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
				jobShare = "";
				closeProcessDialog();
				break;

			case Constant.SHARE_CANCEL:
				Toast.makeText(getApplicationContext(), "分享取消", Toast.LENGTH_SHORT).show();
				jobShare = "";
				closeProcessDialog();
				break;

			case Constant.SHARE_ERROR:
				Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_SHORT).show();
				jobShare = "";
				closeProcessDialog();
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
		List<Map<String, String>> list;// 这是要绑定的数据
		private int resource;// 这是要绑定的 item 布局文件
		private LayoutInflater inflater;// 布局填充器，Android系统内置的

		public MyAdapter(Context context, List<Map<String, String>> list, int resource) {
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
			final String jobsName = list.get(position).get("jobsName").toString();
			
			if(StringUtil.isBlank(key)){
				jobName.setText(list.get(position).get("jobsName").toString());
				company.setText(list.get(position).get("companyname").toString());
				workplace.setText(list.get(position).get("workRegion").toString());
			} else {
				// 多个关键字用空格隔开，需要拆分
				String[] keys = key.split(" ");
				
				// 职位名称：搜索关键字高亮显示
				String str_jobName = list.get(position).get("jobsName").toString();
				SpannableString s = new SpannableString(str_jobName);// 高亮显示之后的字符串
				for(int i=0; i<keys.length; i++){
					
//			        s = new SpannableString(str_jobName);
			        Pattern p = Pattern.compile(keys[i]);
			        Matcher m = p.matcher(s);

			        while (m.find()) {
			            int start = m.start();
			            int end = m.end();
			            s.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			        }
				}
				jobName.setText(s);
				
				
				// 公司名称：搜索关键字高亮显示
		        String str_companyName = list.get(position).get("companyname").toString();
		        SpannableString s2 = new SpannableString(str_companyName);
		        for(int i=0; i<keys.length; i++){
		        	Pattern p2 = Pattern.compile(keys[i]);
			        Matcher m2 = p2.matcher(s2);

			        while (m2.find()) {
			            int start2 = m2.start();
			            int end2 = m2.end();
			            s2.setSpan(new ForegroundColorSpan(Color.RED), start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			        }
		        }
		        company.setText(s2);
		        
		        // 工作地区：搜索关键字高亮显示
		        String str_workplace = list.get(position).get("workRegion").toString();
		        SpannableString s3 = new SpannableString(str_workplace);
		        for(int i=0; i<keys.length; i++){
		        	Pattern p3 = Pattern.compile(keys[i]);
			        Matcher m3 = p3.matcher(s3);

			        while (m3.find()) {
			            int start3 = m3.start();
			            int end3 = m3.end();
			            s3.setSpan(new ForegroundColorSpan(Color.RED), start3, end3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			        }
		        }
		        workplace.setText(s3);
			}
			
			
			//jobName.setText(list.get(position).get("jobsName").toString());
//			company.setText(list.get(position).get("companyname").toString());
			degree.setText(list.get(position).get("needEducation").toString());
			// workingYears.setText(list.get(position).get("workingYears").toString());
//			workplace.setText(list.get(position).get("workRegion").toString());
			releaseTime.setText(list.get(position).get("postTime"));

			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						checkBoxState.put(position, isChecked);
						jobIdAndCompanyIdMaps.put(position, jobsid + "-" + list.get(position).get("companyid"));
						jobNameAndCompanyNameMaps.put(position, jobsName + "-" + list.get(position).get("companyname"));
						jobIdAndCompanyIdMaps2.put(position, jobsid + "-" + list.get(position).get("companyid"));
						jobNameAndCompanyNameMaps2.put(position, jobsName + "-" + list.get(position).get("companyname"));
						
//						jobIdMaps.put(position, jobsid);
//						jobShareMaps.put(position, list.get(position).get("companyname") + "正在招聘" + list.get(position).get("jobsName"));
					
						jobShareMaps.put(position, "公司名称：" + list.get(position).get("companyname") + "\n职位名称：" + list.get(position).get("jobsName") + "\n联系电话：" + list.get(position).get("contract") );
					} else {
						checkBoxState.remove(position);
						jobIdAndCompanyIdMaps.remove(position);
						jobNameAndCompanyNameMaps.remove(position);
						jobIdAndCompanyIdMaps2.remove(position);
						jobNameAndCompanyNameMaps2.remove(position);
						
//						jobIdMaps.remove(position);
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
			case Constant.LOGIN_APPLY_JOB_ACTIVITY:
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				break;

			case Constant.LOGIN_COLLECT_JOB_ACTIVITY:
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				break;

			case Constant.LOGIN_SHARE_JOB_ACTIVITY:
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				break;

			}
		}
	}
	
}
