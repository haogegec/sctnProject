package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.entity.LoginInfo;
import com.sctn.sctnet.view.CustomDialog;

/**
 * 职位描述/公司简介 Tab页面
 * 
 * @author 姜勇男
 * 
 */
public class CompanyInfoActivity extends BaicActivity {

	private ViewPager mPager;// 页卡控件
	private List<View> listViews; // Tab页面列表
	private TextView company_profile_title, job_description_title;
	private LinearLayout footbar;
	private int currentPage = 0;// 0 显示职位描述、1 显示公司简介

	private Button btn_apply;// 申请
	private Button btn_collect;// 收藏
	private Button btn_share;// 分享

	private String flag;// 标识：是从JobListActivity页面进来还是从ReadMyResumeActivity页面进来
	private String jobId;
	private String companyId;
	private String workRegionName;// 用来传送给地图，城市

	private String result;// 服务端返回的结果

	private RelativeLayout rl_layout;
	// 公司简介tab页的控件
	private TextView tv_companyName, tv_companyIndustry, tv_companyType, tv_companyScale, tv_companyAddress, tv_companyIntro, tv_companyContacts, tv_companyEmail, tv_companyWebsite;
	private LinearLayout ll_address;
	
	// 职位描述tab页的控件
	private TextView tv_companyName2, tv_companyIndustry2, tv_companyType2, tv_companyScale2, tv_companyAddress2, tv_workingArea2, tv_releaseTime2, tv_jobName2, tv_jobDetail2, tv_companyContacts2,
			tv_companyEmail2, tv_companyWebsite2;
	private RelativeLayout rl_address;
	
	private String companyName;// 公司名称
	private String companyIndustry;// 公司行业
	private String companyType;// 公司性质
	private String companyScale;// 公司规模
	private String companyAddress;// 公司地址
	private String companyIntro;// 公司介绍
	private String companyContacts;// 联系人
	private String companyPhone;// 联系电话
	private String companyEmail;// 电子邮箱
	private String companyWebsite;// 网址
	private String workingArea;// 工作地区
	private String releaseTime;// 职位发布时间
	private String jobName;// 职位名称
	private String jobDetail;// 职位描述

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_info_activity);
		setTitleBar(getString(R.string.companyProfile), View.VISIBLE, View.GONE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);

		// 初始化Share SDK，一定要 重写 ondestroy（），停止SDK
		ShareSDK.initSDK(this);

		initIntent();
		initTextView();
		initAllView();
		initViewPager();
		reigesterAllEvent();
		initDataThread();
	}

	protected void initIntent() {
		Bundle bundle = getIntent().getExtras();
		flag = bundle.getString("flag");
		jobId = bundle.getString("jobId");
		companyId = bundle.getString("companyId");
		workRegionName = bundle.getString("workRegionName");
	}

	/**
	 * 初始化头标
	 */
	private void initTextView() {
		company_profile_title = (TextView) findViewById(R.id.company_profile_title);
		job_description_title = (TextView) findViewById(R.id.job_description_title);

		job_description_title.setOnClickListener(new MyOnClickListener(0));
		company_profile_title.setOnClickListener(new MyOnClickListener(1));
	}

	/**
	 * 对tab页，和tab页的title初始化以及tab页之间的切换
	 */
	public void initViewPager() {
		// 初始化tab页
		if ("ReadMyResumeActivity".equals(flag)) {
			footbar.setVisibility(View.GONE);
			mPager = (ViewPager) findViewById(R.id.vp_company_info2);
			mPager.setVisibility(View.VISIBLE);

			ViewPager viewPager = (ViewPager) findViewById(R.id.vp_company_info);
			viewPager.setVisibility(View.GONE);
		} else {
			footbar.setVisibility(View.VISIBLE);
			mPager = (ViewPager) findViewById(R.id.vp_company_info);
			mPager.setVisibility(View.VISIBLE);

			ViewPager viewPager = (ViewPager) findViewById(R.id.vp_company_info2);
			viewPager.setVisibility(View.GONE);
		}
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.job_detail_layout, null));
		listViews.add(mInflater.inflate(R.layout.company_info_layout, null));

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(currentPage);// 设置当前显示的内容页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		setPageTitlesColor(currentPage);
	}

	@Override
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	@Override
	protected void initAllView() {
		rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
		footbar = (LinearLayout) findViewById(R.id.footbar_layout_ly);
		btn_apply = (Button) findViewById(R.id.btn_apply);
		btn_collect = (Button) findViewById(R.id.btn_collect);
		btn_share = (Button) findViewById(R.id.btn_share);
	}

	@Override
	protected void reigesterAllEvent() {
		// 申请
		btn_apply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 这里得判断是否登录、没登录则跳转到登录页面
				if (LoginInfo.isLogin()) {
					
					String userId = SharePreferencesUtils.getSharedlongData("userId")+"";
					
					if (LoginInfo.hasResume(userId)) {// 如果当前用户已经有简历
						applyThread();
					} else {// 如果当前用户还没有创建简历，就跳到创建简历页面

						final CustomDialog dialog = new CustomDialog(CompanyInfoActivity.this, R.style.CustomDialog);
//						dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
					//	dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
						dialog.setTitle("友情提示");
						dialog.setMessage("您的简历还不完善暂不能申请职位，是否要去完善您的简历呢？");
						dialog.setOnPositiveListener("是",new OnClickListener(){

							@Override
							public void onClick(View v) {
								
								Intent intent = new Intent(CompanyInfoActivity.this, ResumeManageActivity.class);
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
					Intent intent = new Intent(CompanyInfoActivity.this, LoginActivity.class);
					startActivityForResult(intent, Constant.LOGIN_APPLY_JOB_ACTIVITY);
				}
			}
		});

		// 收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 这里得判断是否登录、没登录则跳转到登录页面
				if (LoginInfo.isLogin()) {
					collectThread();
				} else {
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(CompanyInfoActivity.this, LoginActivity.class);
					startActivityForResult(intent, Constant.LOGIN_COLLECT_JOB_ACTIVITY);
				}
			}
		});

		// 分享
		btn_share.setOnClickListener(new View.OnClickListener() {

			
			@Override
			public void onClick(View v) {

				OnekeyShare oks = new OnekeyShare();
				oks.setNotification(R.drawable.logo, getString(R.string.app_name));
				oks.setTitle("我看到一个很不错的招聘信息，想告诉大家，有兴趣的可以看看哦~");
				oks.setTitleUrl("http://www.scrc168.com/");
				oks.setText("我看到一个很不错的招聘信息，想告诉大家，有兴趣的可以看看哦~ \n\n公司名称：" + companyName + "\n职位名称：" + jobName + "\n联系人及联系电话：" + companyContacts + "\n电子邮箱：" + companyEmail
						+ "\n单位网址：" + companyWebsite);
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
		});

	}

	/**
	 * 头标监听器
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 将两个个页卡界面装入其中，默认显示第一个页卡，还需要实现一个适配器。 ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {

			((ViewPager) arg0).addView(mListViews.get(arg1), 0);

			if (arg1 == 0) {
				initJobDetailPage(arg0);
			}
			if (arg1 == 1) {
				initCompanyInfoPage(arg0);
			}

			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	// 初始化公司简介
	private void initCompanyInfoPage(View companyInfoPage) {

		tv_companyName = (TextView) companyInfoPage.findViewById(R.id.company_name);// 公司名称
		tv_companyIndustry = (TextView) companyInfoPage.findViewById(R.id.company_industry);// 公司所属行业
		tv_companyType = (TextView) companyInfoPage.findViewById(R.id.company_type);// 公司性质
		tv_companyScale = (TextView) companyInfoPage.findViewById(R.id.company_scale);// 公司规模
		ll_address = (LinearLayout) companyInfoPage.findViewById(R.id.company_address_layout);// 包含公司地址和定位图标的layout
		tv_companyAddress = (TextView) companyInfoPage.findViewById(R.id.company_address);// 公司地址
		tv_companyIntro = (TextView) companyInfoPage.findViewById(R.id.company_intro);// 公司介绍
		tv_companyContacts = (TextView) companyInfoPage.findViewById(R.id.company_contacts);// 联系人及联系电话
		// tv_companyPhone = (TextView) companyInfoPage.findViewById(R.id.company_phone);// 联系电话
		tv_companyEmail = (TextView) companyInfoPage.findViewById(R.id.company_email);// 电子邮箱
		tv_companyWebsite = (TextView) companyInfoPage.findViewById(R.id.company_website);// 公司网址

		ll_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CompanyInfoActivity.this, CompanyLocationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("detailAddress", tv_companyAddress.getText().toString());
				bundle.putString("city", workRegionName);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
	}

	// 初始化职位描述
	private void initJobDetailPage(View companyInfoPage) {

		tv_companyName2 = (TextView) companyInfoPage.findViewById(R.id.company_name);// 公司名称
		tv_companyIndustry2 = (TextView) companyInfoPage.findViewById(R.id.company_industry);// 公司所属行业
		tv_companyType2 = (TextView) companyInfoPage.findViewById(R.id.company_type);// 公司性质
		tv_companyScale2 = (TextView) companyInfoPage.findViewById(R.id.company_scale);// 公司规模
		tv_workingArea2 = (TextView) companyInfoPage.findViewById(R.id.working_area);// 工作地区
		tv_releaseTime2 = (TextView) companyInfoPage.findViewById(R.id.release_time);// 发布时间
		tv_jobName2 = (TextView) companyInfoPage.findViewById(R.id.job_name);// 职位名称
		tv_jobDetail2 = (TextView) companyInfoPage.findViewById(R.id.job_detail);// 职位详情
		rl_address = (RelativeLayout) companyInfoPage.findViewById(R.id.company_address_layout);// 包含公司地址和定位图标的layout
		tv_companyAddress2 = (TextView) companyInfoPage.findViewById(R.id.company_address);// 公司地址
		tv_companyContacts2 = (TextView) companyInfoPage.findViewById(R.id.company_contacts);// 联系人及联系电话
		// tv_companyPhone2 = (TextView) companyInfoPage.findViewById(R.id.company_phone);// 联系电话
		tv_companyEmail2 = (TextView) companyInfoPage.findViewById(R.id.company_email);// 电子邮箱
		tv_companyWebsite2 = (TextView) companyInfoPage.findViewById(R.id.company_website);// 公司网址

		rl_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CompanyInfoActivity.this, CompanyLocationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("detailAddress", tv_companyAddress.getText().toString());
				bundle.putString("city", workRegionName);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
	}

	/**
	 * 页卡切换监听
	 * 
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			setPageTitlesColor(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * 设置tab的title文字颜色、背景
	 * 
	 * @param titleIndex
	 *            tab页序号
	 */
	private void setPageTitlesColor(int titleIndex) {
		if (titleIndex == 0) {
			company_profile_title.setBackgroundColor(getResources().getColor(R.color.background));
			company_profile_title.setTextColor(getResources().getColor(R.color.lightBlack));
			job_description_title.setBackgroundColor(getResources().getColor(R.color.white));
			job_description_title.setTextColor(getResources().getColor(R.color.blue));
		} else if (titleIndex == 1) {
			company_profile_title.setBackgroundColor(getResources().getColor(R.color.white));
			company_profile_title.setTextColor(getResources().getColor(R.color.blue));
			job_description_title.setBackgroundColor(getResources().getColor(R.color.background));
			job_description_title.setTextColor(getResources().getColor(R.color.lightBlack));

		}
	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void initDataThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						initData();
					}
				});
		mThread.start();
	}

	/**
	 * 请求数据，并将返回结果显示在界面上
	 */
	private void initData() {

		String url = "appPersonCenter!companyPostInfo.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Companyid", companyId));
			params.add(new BasicNameValuePair("jobsid", jobId));

			// params.add(new BasicNameValuePair("Companyid", "12725"));
			// params.add(new BasicNameValuePair("jobsid", "94049"));

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				CompanyInfoActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);// 返回结果存放在该json对象中
			if ("0".equals(responseJsonObject.getString("resultcode"))) {
				JSONArray companyList = responseJsonObject.getJSONArray("result");// 长度必须是1
				JSONObject companyInfo = companyList.optJSONObject(0);

				companyName = companyInfo.getString("companyname");
				companyIndustry = companyInfo.getString("companyindustry");
				companyType = companyInfo.getString("companytype");
				companyScale = companyInfo.getString("companyscale");
				companyAddress = companyInfo.getString("companyaddress");
				companyIntro = companyInfo.getString("companyinfo");
				companyContacts = companyInfo.getString("contactsname");
				companyPhone = companyInfo.getString("phone");
				companyEmail = companyInfo.getString("companyemail");
				companyWebsite = companyInfo.getString("companywebsite");
				workingArea = companyInfo.getString("workregionname");

				if (!StringUtil.isBlank(companyInfo.getString("posttime"))) {
					releaseTime = companyInfo.getString("posttime").substring(0, 10);
				} else {
					releaseTime = companyInfo.getString("posttime");
				}

				jobName = companyInfo.getString("jobsname");
				jobDetail = companyInfo.getString("description");

				msg.what = 0;
				handler.sendMessage(msg);

			} else {
				String errorResult = responseJsonObject.getString("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				CompanyInfoActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			CompanyInfoActivity.this.sendExceptionMsg(err);
		}
	}

	/**
	 * 请求完数据，更新界面的数据
	 */
	private void updateUI() {

		tv_companyName.setText(companyName);
		tv_companyIndustry.setText(companyIndustry);
		tv_companyType.setText(companyType);
		tv_companyScale.setText(companyScale);
		tv_companyAddress.setText(companyAddress);
		tv_companyIntro.setText(companyIntro);
		tv_companyContacts.setText(companyContacts+" "+companyPhone);
		tv_companyEmail.setText(companyEmail);
		tv_companyWebsite.setText(companyWebsite);

		tv_companyName2.setText(companyName);
		tv_companyIndustry2.setText(companyIndustry);
		tv_companyType2.setText(companyType);
		tv_companyScale2.setText(companyScale);
		tv_companyAddress2.setText(companyAddress);
		tv_companyContacts2.setText(companyContacts+" "+companyPhone);
		tv_companyEmail2.setText(companyEmail);
		tv_companyWebsite2.setText(companyWebsite);
		tv_workingArea2.setText(workingArea);
		tv_releaseTime2.setText(releaseTime);
		tv_jobName2.setText(jobName);
		tv_jobDetail2.setText(jobDetail);

		rl_layout.setVisibility(View.VISIBLE);

	}

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

		String url = "appPersonCenter!userSendRusume.app";
		Message msg = new Message();

		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			long userId = SharePreferencesUtils.getSharedlongData("userId");
			params.add(new BasicNameValuePair("Userid", userId + ""));
			params.add(new BasicNameValuePair("jobsid", jobId));
			params.add(new BasicNameValuePair("Companyid", companyId));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				CompanyInfoActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);
			if ("0".equals(responseJsonObject.getString("resultcode"))) {// 表示职位收藏成功

				msg.what = 1;
				handler.sendMessage(msg);
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

		String url = "appPersonCenter!insertUserJobInfo.app";
		Message msg = new Message();

		try {
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			long userId = SharePreferencesUtils.getSharedlongData("userId");
			params.add(new BasicNameValuePair("Userid", userId + ""));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				CompanyInfoActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);
			if ("0".equals(responseJsonObject.getString("resultcode"))) {// 表示职位收藏成功
				msg.what = 2;
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
				updateUI();
				closeProcessDialog();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "申请成功", Toast.LENGTH_SHORT).show();
				closeProcessDialog();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
				closeProcessDialog();
				break;

			case Constant.SHARE_COMPLETE:
				Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
				closeProcessDialog();
				break;

			case Constant.SHARE_CANCEL:
				Toast.makeText(getApplicationContext(), "分享取消", Toast.LENGTH_SHORT).show();
				closeProcessDialog();
				break;

			case Constant.SHARE_ERROR:
				Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_SHORT).show();
				closeProcessDialog();
				break;

			}

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.LOGIN_APPLY_JOB_ACTIVITY: {
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				applyThread();
				break;
			}
			case Constant.LOGIN_COLLECT_JOB_ACTIVITY: {
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				collectThread();
				break;
			}
			}
		}
	}

}
