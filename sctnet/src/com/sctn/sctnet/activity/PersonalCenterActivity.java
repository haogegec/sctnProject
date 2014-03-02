package com.sctn.sctnet.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.cache.CacheProcess;
import com.sctn.sctnet.view.ItemView;

/**
 * 个人中心界面
 * 
 * @author xueweiwei
 * 
 */
public class PersonalCenterActivity extends BaicActivity {

	private ItemView itemView1, itemView2, itemView3, itemView4, itemView5, itemView6;
	private TextView postAppCount,postCollCount;// 职位申请记录，职位收藏记录
	private CacheProcess cacheProcess;// 缓存数据
	private long userId;// 用户唯一标识
	private String result;// 服务端返回结果数据
	private ImageView postAppImage;// 职位申请记录控件
	private ImageView postCollImage;// 职位收藏记录
	private TextView username;
	
	String post = "";// 职位申请记录
	String resume = "";// 职位收藏记录
	String company = "";// 几个公司看过我的简历

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_center_activity);
		setTitleBar(getString(R.string.personalActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		cacheProcess = new CacheProcess();
		userId = cacheProcess.getLongCacheValueInSharedPreferences(this, "userId");
		initAllView();

		reigesterAllEvent();
//		initDataTread();3
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
		itemView6 = (ItemView) findViewById(R.id.itemview6);
		
		itemView1.setBackground(R.drawable.item_up_bg);
		itemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView1.setLabel("谁看过我的简历");
		itemView1.setLabelTextColor(getResources().getColor(R.color.blue));
//		itemView1.setValue("共6条");
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.VISIBLE);
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

		itemView2.setBackground(R.drawable.item_up_bg);
		itemView2.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView2.setLabel("职业信息自动推送");
		itemView2.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView2.setDetailImageViewResource(R.drawable.set_on);

		itemView3.setBackground(R.drawable.item_up_bg);
		itemView3.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView3.setLabel("中心自行定制信息内容推送");
		itemView3.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView3.setDetailImageViewResource(R.drawable.set_on);

		itemView4.setBackground(R.drawable.item_down_bg);
		itemView4.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView4.setLabel("修改密码");
		itemView4.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView4.setDetailImageViewResource(R.drawable.detail);
		itemView4.setIconImageVisibility(View.VISIBLE);
		itemView4.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalCenterActivity.this, ChangePasswordActivity.class);
				startActivity(intent);
			}

		});
		
		itemView5.setBackground(R.drawable.item_up_bg);
		itemView5.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView5.setLabel("谁向我发送过面试邀请");
		itemView5.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView5.setDetailImageViewResource(R.drawable.detail);
		itemView5.setIconImageVisibility(View.VISIBLE);
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
		
		itemView6.setBackground(R.drawable.item_up_bg);
		itemView6.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView6.setLabel("谁向我发送过招聘意向");
		itemView6.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView6.setDetailImageViewResource(R.drawable.detail);
		itemView6.setIconImageVisibility(View.VISIBLE);
		itemView6.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalCenterActivity.this, ReadMyResumeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("flag", "jobIntentions");
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
	}

	@Override
	protected void reigesterAllEvent() {
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(), "确定",
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(PersonalCenterActivity.this, ChangePasswordActivity.class);
				startActivity(intent);

			}
		});
		
		// 职位申请记录
		postAppImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalCenterActivity.this, MyAppliedJobActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("flag","postApplication");// 职位申请记录和职位收藏记录进入同一个页面，用flag判断是点击哪个进来的
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
		
		// 职位收藏记录
		postCollImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalCenterActivity.this, MyAppliedJobActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("flag","postCollect");// 职位申请记录和职位收藏记录进入同一个页面，用flag判断是点击哪个进来的
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void initDataTread() {
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

		String url = "appPersonCenter.app?method=execute";

		Message msg = new Message();
		try {

			JSONObject jsonParmter = new JSONObject();
			jsonParmter.put("Userid", userId);
			String parameter = jsonParmter.toString();

			result = getPostHttpContent(url, parameter);

			if (StringUtil.isExcetionInfo(result)) {
				PersonalCenterActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {// 说明该用户没有创建简历
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
		username.setText(userId+"");
		itemView1.setValue("共"+post+"条");
		postAppCount.setText(resume);
		postCollCount.setText(company);
		
	}
	
}
