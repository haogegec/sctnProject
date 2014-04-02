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

/**
 * 更多界面
 * 
 * @author xueweiwei
 * 
 */
public class MoreActivity extends BaicActivity {

	private RelativeLayout rl_feedback, rl_update, rl_about;

	private UpdateManager manager = new UpdateManager(MoreActivity.this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);
		setTitleBar(getString(R.string.moreActivityTitle), View.VISIBLE, View.GONE);
		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
		rl_update = (RelativeLayout) findViewById(R.id.rl_update);
		rl_about = (RelativeLayout) findViewById(R.id.rl_about);
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 用户反馈
		rl_feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MoreActivity.this, FeedBackActivity.class);
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
				Intent intent = new Intent(MoreActivity.this, AboutActivity.class);
				startActivity(intent);
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
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MoreActivity.this);
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
