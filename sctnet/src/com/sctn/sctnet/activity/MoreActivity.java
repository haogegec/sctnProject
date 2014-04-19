package com.sctn.sctnet.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.entity.LoginInfo;
import com.sctn.sctnet.view.CustomDialog;

/**
 * 更多界面
 * 
 * @author xueweiwei
 * 
 */
public class MoreActivity extends BaicActivity {

	private RelativeLayout rl_feedback, rl_update, rl_about,rl_logout;

	private UpdateManager manager = new UpdateManager(MoreActivity.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);
		setTitleBar(getString(R.string.moreActivityTitle), View.VISIBLE,
				View.GONE);

		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
		rl_update = (RelativeLayout) findViewById(R.id.rl_update);
		rl_about = (RelativeLayout) findViewById(R.id.rl_about);
		rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
		
		// 如果当前是未登录状态，则把【退出登录】选项隐藏，并改变【关于】选项的背景图
		if(!LoginInfo.isLogin()){
			rl_logout.setVisibility(View.GONE);
			rl_about.setBackgroundResource(R.drawable.setting_bg_down);
		}
	}

	@Override
	protected void reigesterAllEvent() {

		// 用户反馈
		rl_feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MoreActivity.this,
						FeedBackActivity.class);
				startActivity(intent);
			}
		});

		// 检测更新
		rl_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 检查软件更新
				Thread checkThread = new Thread(new Runnable() {// 启动新的线程，
							@Override
							public void run() {
								Message msg = new Message();
								try {
									// 检查软件更新
									if (manager.isUpdate()) {
										msg.what = 0;
										handler.sendMessage(msg);
									} else {
										msg.what = 1;
										handler.sendMessage(msg);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
				checkThread.start();
			}
		});

		// 关于
		rl_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MoreActivity.this,
						AboutActivity.class);
				startActivity(intent);
			}
		});

		// 退出登录
		rl_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				final CustomDialog dialog = new CustomDialog(
						MoreActivity.this, R.style.CustomDialog);
				// dialog.setCanceledOnTouchOutside(false);//
				// 点击dialog外边，对话框不会消失，按返回键对话框消失
				// dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
				dialog.setTitle("友情提示");
				dialog.setMessage("退出登录？");
				dialog.setOnPositiveListener("确定", new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 将本地保存的登录信息清空
						LoginInfo.logOut();
						Toast.makeText(MoreActivity.this, "注销成功",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(MoreActivity.this,
								HomeActivity.class);
						startActivity(intent);
						finish();
					}

				});
				dialog.setOnNegativeListener("取消", new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}

				});
				dialog.show();

			}
		});
	}

	// 用于接收线程发送的消息
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {
				showNoticeDialog();
			}
				break;
			case 1: {
				Toast.makeText(getApplicationContext(), "已经是最新版本！",
						Toast.LENGTH_SHORT).show();
			}
			}
		}
	};

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog() {
		// 构造对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(manager.sctnetVersionSVO.getVersionDesc());
		// 更新
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 显示下载对话框
						manager.showDownloadDialog();
					}
				});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (manager.sctnetVersionSVO.getIsForce().equals("Y")) {

							finish();
						} else {
							dialog.dismiss();

						}
					}
				});

		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

}
