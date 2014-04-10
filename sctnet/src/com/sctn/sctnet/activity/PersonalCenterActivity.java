package com.sctn.sctnet.activity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;
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
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.PhoneUtil;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.cache.CacheProcess;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.entity.LoginInfo;
import com.sctn.sctnet.view.CustomDialog;
import com.sctn.sctnet.view.ItemView;

/**
 * 个人中心界面
 * 
 * @author xueweiwei
 * 
 */
public class PersonalCenterActivity extends BaicActivity {

	private ItemView itemView1, itemView2, itemView3, itemView4, itemView5;
	private TextView postAppCount, postCollCount;// 职位申请记录，职位收藏记录
	private CacheProcess cacheProcess;// 缓存数据
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
	private boolean subscribe;
	boolean selfSubscribePushAuto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_center_activity);
		setTitleBar(getString(R.string.personalActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		cacheProcess = new CacheProcess();
		userId = cacheProcess.getLongCacheValueInSharedPreferences(this, "userId");
		// userId = SharePreferencesUtils.getSharedlongData("userId");
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
		itemView1.setIconImageViewResource(R.drawable.personal_readed);
		itemView1.setLabel("谁看过我的简历");
		itemView1.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.VISIBLE);

		itemView2.setBackground(R.drawable.item_up_bg);
		itemView2.setIconImageViewResource(R.drawable.personal_push);
		itemView2.setLabel("职业信息自动推送");
		itemView2.setLabelTextColor(getResources().getColor(R.color.blue));
		if (jobInformationPushAuto) {
			itemView2.setDetailImageViewResource(R.drawable.set_on);
		} else {
			itemView2.setDetailImageViewResource(R.drawable.set_off);
		}
		itemView2.setIconImageVisibility(View.VISIBLE);
		
		itemView3.setBackground(R.drawable.item_up_bg);
		itemView3.setIconImageViewResource(R.drawable.subscribe);
		itemView3.setLabel("中心自行定制信息内容推送");
		itemView3.setLabelTextColor(getResources().getColor(R.color.blue));
		if (selfSubscribePushAuto) {
			itemView3.setDetailImageViewResource(R.drawable.set_on);
		} else {
			itemView3.setDetailImageViewResource(R.drawable.set_off);
		}
		itemView3.setIconImageVisibility(View.VISIBLE);

		itemView4.setBackground(R.drawable.item_down_bg);
		itemView4.setIconImageViewResource(R.drawable.password_img);
		itemView4.setLabel("修改密码");
		itemView4.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView4.setDetailImageViewResource(R.drawable.detail);
		itemView4.setIconImageVisibility(View.VISIBLE);

		itemView5.setBackground(R.drawable.item_up_bg);
		itemView5.setIconImageViewResource(R.drawable.personal_interview);
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

				final CustomDialog dialog = new CustomDialog(PersonalCenterActivity.this, R.style.CustomDialog);
//				dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
				dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
				dialog.setTitle("友情提示");
				dialog.setMessage("确定要注销吗？");
				dialog.setOnPositiveListener("确定",new OnClickListener(){

					@Override
					public void onClick(View v) {
						// 将本地保存的登录信息清空
						LoginInfo.logOut();
						Toast.makeText(PersonalCenterActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PersonalCenterActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
					}
					
				});
				dialog.setOnNegativeListener("取消", new OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
					
				});
				dialog.show();

			}
		});

		// 谁看过我的简历
		itemView1.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (LoginInfo.hasResume(userId+"")) {// 如果当前用户已经有简历
					if ("0".equals(company)) {
						final CustomDialog dialog = new CustomDialog(PersonalCenterActivity.this, R.style.CustomDialog);
//						dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
						dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
						dialog.setTitle("友情提示");
						dialog.setMessage("近期还没有公司关注你的简历，刷新简历可以引起更多注意哦！");
						dialog.setOnPositiveListener("确定",new OnClickListener(){

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
							
						});
						dialog.show();
					} else {
						Intent intent = new Intent(PersonalCenterActivity.this, ReadMyResumeActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("flag", "readMyResume");
						intent.putExtras(bundle);
						startActivity(intent);
					}
				} else {// 如果当前用户没有简历
					final CustomDialog dialog = new CustomDialog(PersonalCenterActivity.this, R.style.CustomDialog);
//					dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
					dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
					dialog.setTitle("您还没有创建简历");
					dialog.setMessage("是否要创建简历？");
					dialog.setOnPositiveListener("确定",new OnClickListener(){

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							Intent intent = new Intent(PersonalCenterActivity.this,ResumeCreateActivity.class);
							startActivity(intent);
						}
						
					});
					dialog.setOnNegativeListener("取消", new OnClickListener(){

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
						
					});
					dialog.show();
				}
				
				
			}

		});

		// 职业信息自动推送
		itemView2.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				autoPushThread();
			}

		});

		// 中心自行定制信息内容推送
		itemView3.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalCenterActivity.this, SubscribeActivity.class);
				startActivityForResult(intent, 100000);
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
				
				if (LoginInfo.hasResume(userId+"")) {// 如果当前用户已经有简历
					if ("0".equals(invite)) {
						final CustomDialog dialog = new CustomDialog(PersonalCenterActivity.this, R.style.CustomDialog);
//						dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
						dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
						dialog.setTitle("友情提示");
						dialog.setMessage("还没有公司向你发送过面试邀请，完善简历可以吸引更多的公司哦！");
						dialog.setOnPositiveListener("确定",new OnClickListener(){

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
							
						});
						dialog.show();
					} else {
						Intent intent = new Intent(PersonalCenterActivity.this, ReadMyResumeActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("flag", "interviewInvitation");
						intent.putExtras(bundle);
						startActivity(intent);
					}
				} else {
					final CustomDialog dialog = new CustomDialog(PersonalCenterActivity.this, R.style.CustomDialog);
//					dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
					dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
					dialog.setTitle("您还没有创建简历");
					dialog.setMessage("是否要创建简历？");
					dialog.setOnPositiveListener("确定",new OnClickListener(){

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							Intent intent = new Intent(PersonalCenterActivity.this,ResumeCreateActivity.class);
							startActivity(intent);
						}
						
					});
					dialog.setOnNegativeListener("取消", new OnClickListener(){

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
						
					});
					dialog.show();
				}
				
				
			}

		});

		// 职位申请记录
		postAppImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (StringUtil.isBlank(resume) || "0".equals(resume)) {
					final CustomDialog dialog = new CustomDialog(PersonalCenterActivity.this, R.style.CustomDialog);
//					dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
					dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
					dialog.setTitle("友情提示");
					dialog.setMessage("您近期没有申请过职位，您可以去“职位搜索”看看自己感兴趣的职位哦！");
					dialog.setOnPositiveListener("确定",new OnClickListener(){

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
						
					});
					dialog.show();
				} else {
					Intent intent = new Intent(PersonalCenterActivity.this, MyAppliedJobActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag", "postApplication");// 职位申请记录和职位收藏记录进入同一个页面，用flag判断是点击哪个进来的
					intent.putExtras(bundle);
					startActivity(intent);
				}

			}
		});

		// 职位收藏记录
		postCollImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (StringUtil.isBlank(post) || "0".equals(post)) {
					final CustomDialog dialog = new CustomDialog(PersonalCenterActivity.this, R.style.CustomDialog);
//					dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
					dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
					dialog.setTitle("友情提示");
					dialog.setMessage("您没有收藏过任何职位，您可以去“职位搜索”看看自己感兴趣的职位哦！");
					dialog.setOnPositiveListener("确定",new OnClickListener(){

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
						
					});
					dialog.show();
				
				} else {
					Intent intent = new Intent(PersonalCenterActivity.this, MyAppliedJobActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("flag", "postCollect");// 职位申请记录和职位收藏记录进入同一个页面，用flag判断是点击哪个进来的
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
			params.add(new BasicNameValuePair("Userid", userId + ""));
			// params.add(new BasicNameValuePair("Userid", 217294+""));
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
				closeProcessDialog();
				break;

			case Constant.AUTO_PUSH_SUCCESS:

				JPushInterface.setAlias(PersonalCenterActivity.this, PhoneUtil.DEVICE_ID, mAliasCallback);

				if (jobInformationPushAuto) {
					itemView2.setDetailImageViewResource(R.drawable.set_off);
					SharePreferencesUtils.setSharedBooleanData("jobInformationPushAuto", false);
				} else {
					itemView2.setDetailImageViewResource(R.drawable.set_on);
					SharePreferencesUtils.setSharedBooleanData("jobInformationPushAuto", true);
				}

				break;

			}

		}
	};

	/**
	 * 请求完数据，更新界面的数据
	 */
	private void updateUI() {
		username.setText(userName);
		itemView1.setValue("共" + company + "条");
		itemView5.setValue("共" + invite + "条");
		postAppCount.setText(resume);
		postCollCount.setText(post);

	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void autoPushThread() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						autoPush();
					}
				});
		mThread.start();
	}

	/**
	 * 请求数据，并将返回结果显示在界面上
	 */
	private void autoPush() {

		String url = "appMessageSend.app";

		Message msg = new Message();

		jobInformationPushAuto = SharePreferencesUtils.getSharedBooleanData("jobInformationPushAuto");

		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("DeviceId", PhoneUtil.DEVICE_ID));
			params.add(new BasicNameValuePair("RegisterrationId", JPushInterface.getRegistrationID(getApplicationContext())));
			params.add(new BasicNameValuePair("DeviceName", "默认设备名称"));
			params.add(new BasicNameValuePair("DeviceModel", PhoneUtil.MODEL));
			params.add(new BasicNameValuePair("flag", Constant.PUSH_BY_ALIAS));
			if (jobInformationPushAuto) {// true表示当前是开着的，正关闭开关
				params.add(new BasicNameValuePair("AuthPush", "N"));
			} else {
				params.add(new BasicNameValuePair("AuthPush", "Y"));
			}
			if (subscribe) {// true表示当前是开着的，正关闭开关
				params.add(new BasicNameValuePair("UserDefinedPush", "N"));
			} else {
				params.add(new BasicNameValuePair("UserDefinedPush", "Y"));
			}
			// params.add(new BasicNameValuePair("Tags", "AUTO_PUSH"));

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = new JSONObject(result);// 返回结果存放在该json对象中
			if ("0".equals(responseJsonObject.getString("resultCode"))) {
				msg.what = Constant.AUTO_PUSH_SUCCESS;
				handler.sendMessage(msg);
			} else {
				String errorResult = responseJsonObject.getString("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			sendExceptionMsg(err);
		}
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				break;

			default:
				logs = "Failed with errorCode = " + code;
			}

			Toast.makeText(getApplicationContext(), logs, Toast.LENGTH_LONG).show();
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 100000:
				
				Set<String> tags = new HashSet<String>();
				tags.add("总经理");
				JPushInterface.setTags(PersonalCenterActivity.this, tags, mAliasCallback);
				
				if (subscribe) {
					itemView3.setDetailImageViewResource(R.drawable.set_off);
					SharePreferencesUtils.setSharedBooleanData("subscribe", false);
				} else {
					itemView3.setDetailImageViewResource(R.drawable.set_on);
					SharePreferencesUtils.setSharedBooleanData("subscribe", true);
				}
				break;
			}
		}

	}

}
