package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.sctn.sctnet.R;

/**
 * 更多界面
 * 
 * @author xueweiwei
 * 
 */
public class MoreActivity extends BaicActivity {

	private RelativeLayout rl_feedback, rl_update, rl_about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);
		setTitleBar(getString(R.string.moreActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);
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
//				Intent intent = new Intent(MoreActivity.this, MoreActivity.class);
//				startActivity(intent);
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

}
