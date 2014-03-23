package com.sctn.sctnet.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.entity.LoginInfo;
import com.sctn.sctnet.view.ItemView;

/**
 * 个人中心界面
 * 
 * @author xueweiwei
 * 
 */
public class PersonalCenterActivity extends BaicActivity {

	private ItemView itemView1, itemView2, itemView3, itemView4, itemView5;
	private TextView postAppCount,postCollCount;// 职位申请记录，职位收藏记录
//	private CacheProcess cacheProcess;// 缓存数据
	private long userId;// 用户唯一标识
	private String result;// 服务端返回结果数据
	private ImageView postAppImage;// 职位申请记录控件
	private ImageView postCollImage;// 职位收藏记录
	private TextView username;
	
	private String userName;// 登录名
	private String post = "";// 职位申请记录
	private String resume = "";// 职位收藏记录
	private String company = "";// 几个公司看过我的简历
	private String invite = "";// 向我发送过面试邀请的公司个数
	private boolean jobInformationPushAuto;
	boolean selfSubscribePushAuto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_center_activity);
		setTitleBar(getString(R.string.personalActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
//		cacheProcess = new CacheProcess();
//		userId = cacheProcess.getLongCacheValueInSharedPreferences(this, "userId");
		userId = SharePreferencesUtils.getSharedlongData("userId");
		userName = SharePreferencesUtils.getSharedStringData("userName");
		
		initAllView();
		reigesterAllEvent();
		initDataThread();
	}

	@Override
	protected void initAllView() {
		username = (TextView) findViewById(R.id.userNameValue);
		
		postAppCount = (TextView) findViewById(R.id.postAppCount);
		postCollCount = (TextView) findViewById(R.id.postCollCount);
		
		postAppImage = (ImageView) findViewById(R.id.postAppImage);
		postCollImage = (ImageView) findViewById(R.id.postCollImage);
		
		itemView1 = (ItemView) findViewById(R.id.itemview1);
		itemView2 = (ItemView) findViewById(R.id.itemview2);
		itemView3 = (ItemView) findViewById(R.id.itemview3);
		itemView4 = (ItemView) findViewById(R.id.itemview4);
		itemView5 = (ItemView) findViewById(R.id.itemview5);
		
		jobInformationPushAuto = SharePreferencesUtils.getSharedBooleanData("jobInformationPushAuto");
		selfSubscribePushAuto = SharePreferencesUtils.getSharedBooleanData("selfSubscribePushAuto");
		
		itemView1.setBackground(R.drawable.item_up_bg);
		itemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView1.setLabel("谁看过我的简历");
		itemView1.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.VISIBLE);

		itemView2.setBackground(R.drawable.item_up_bg);
		itemView2.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView2.setLabel("职业信息自动推送");
		itemView2.setLabelTextColor(getResources().getColor(R.color.blue));
		if(jobInformationPushAuto){
			itemView2.setDetailImageViewResource(R.drawable.set_on);
		} else {
			itemView2.setDetailImageViewResource(R.drawable.set_off);
		}

		itemView3.setBackground(R.drawable.item_up_bg);
		itemView3.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView3.setLabel("中心自行定制信息内容推送");
		itemView3.setLabelTextColor(getResources().getColor(R.color.blue));
		if(selfSubscribePushAuto){
			itemView3.setDetailImageViewResource(R.drawable.set_on);
		} else {
			itemView3.setDetailImageViewResource(R.drawable.set_off);
		}

		itemView4.setBackground(R.drawable.item_down_bg);
		itemView4.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView4.setLabel("修改密码");
		itemView4.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView4.setDetailImageViewResource(R.drawable.detail);
		itemView4.setIconImageVisibility(View.VISIBLE);
		
		
		itemView5.setBackground(R.drawable.item_up_bg);
		itemView5.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView5.setLabel("谁向我发送过面试邀请");
		itemView5.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView5.setDetailImageViewResource(R.drawable.detail);
		itemView5.setIconImageVisibility(View.VISIBLE);
		
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 注销
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				new  AlertDialog.Builder(PersonalCenterActivity.this)   
				.setTitle("提示" )  
				.setMessage("确定要注销吗？" )  
				.setPositiveButton("确定" ,  new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 将本地保存的登录信息清空
						LoginInfo.logOut();
						// ->直接跳转到 HomeActivity(设置成单例) 同时清空栈中 HomeActivity 之前的 Activity
						Toast.makeText(PersonalCenterActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PersonalCenterActivity.this, HomeActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 利用ClearTop标志
						startActivity(intent);
					}
				} )  
				.setNegativeButton("取消" , null)  
				.show();  
				
				
			}
		});
		
		// 谁看过我的简历
		itemView1.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalCenterActivity.this, ReadMyResumeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("flag", "readMyResume");
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		
		// 职业信息自动推送
		itemView2.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jobInformationPushAuto = SharePreferencesUtils.getSharedBooleanData("jobInformationPushAuto");
				if(jobInformationPushAuto){
					itemView2.setDetailImageViewResource(R.drawable.set_off);
					SharePreferencesUtils.setSharedBooleanData("jobInformationPushAuto",false);
				} else {
					itemView2.setDetailImageViewResource(R.drawable.set_on);
					SharePreferencesUtils.setSharedBooleanData("jobInformationPushAuto",true);
				}
			}

		});
		
		// 中心自行定制信息内容推送
		itemView3.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selfSubscribePushAuto = SharePreferencesUtils.getSharedBooleanData("selfSubscribePushAuto");
				if(selfSubscribePushAuto){
					itemView3.setDetailImageViewResource(R.drawable.set_off);
					SharePreferencesUtils.setSharedBooleanData("selfSubscribePushAuto",false);
				} else {
					itemView3.setDetailImageViewResource(R.drawable.set_on);
					SharePreferencesUtils.setSharedBooleanData("selfSubscribePushAuto",true);
				}
			}

		});
		
		// 修改密码
		itemView4.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalCenterActivity.this, ChangePasswordActivity.class);
				startActivity(intent);
			}

		});
		
		// 谁向我发送过面试邀请
		itemView5.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalCenterActivity.this, ReadMyResumeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("flag", "interviewInvitation");
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		
		// 职位申请记录
		postAppImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(StringUtil.isBlank(post) || "0".equals(post)){
					new  AlertDialog.Builder(PersonalCenterActivity.this)    
					.setMessage("您近期没有申请过职位，您可以去“职位搜索”看看自己感兴趣的职位哦！" )  
	                .setPositiveButton("确定" ,  null)  
	                .show();  
				} else {
					Intent intent = new Intent(PersonalCenterActivity.this, MyAppliedJobActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag","postApplication");// 职位申请记录和职位收藏记录进入同一个页面，用flag判断是点击哪个进来的
					intent.putExtras(bundle);
					startActivity(intent);
				}

			}
		});
		
		// 职位收藏记录
		postCollImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(StringUtil.isBlank(resume) || "0".equals(resume)){
					new  AlertDialog.Builder(PersonalCenterActivity.this)
					.setTitle("友情提示")
					.setMessage("您没有收藏过任何职位，您可以去“职位搜索”看看自己感兴趣的职位哦！" )  
	                .setPositiveButton("确定" ,  null)  
	                .show();  
				} else {
					Intent intent = new Intent(PersonalCenterActivity.this, MyAppliedJobActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag","postCollect");// 职位申请记录和职位收藏记录进入同一个页面，用flag判断是点击哪个进来的
					intent.putExtras(bundle);
					startActivity(intent);
				}
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
						initData();
					}
				});
		mThread.start();
	}

	/**
	 * 请求数据，并将返回结果显示在界面上
	 */
	private void initData() {

		String url = "appPersonCenter.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid", userId+""));
//			params.add(new BasicNameValuePair("Userid", 217294+""));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				PersonalCenterActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");

				post = resultJsonObject.getString("post");// 职位申请记录
				resume = resultJsonObject.getString("resume");// 职位收藏记录
				company = resultJsonObject.getString("company");// 几个公司看过我的简历
				invite = resultJsonObject.getString("invite");// 向我发送过面试邀请的公司个数

				msg.what = 0;
				handler.sendMessage(msg);
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				PersonalCenterActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			PersonalCenterActivity.this.sendExceptionMsg(err);
		}
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				updateUI();
				break;

			}
			closeProcessDialog();
		}
	};

	/**
	 * 请求完数据，更新界面的数据
	 */
	private void updateUI(){
		username.setText(userName);
		itemView1.setValue("共"+company+"条");
		itemView5.setValue("共"+invite+"条");
		postAppCount.setText(post);
		postCollCount.setText(resume);
		
	}
	
}
