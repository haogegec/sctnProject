package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.ItemView;

/**
 * 个人中心界面
 * @author xueweiwei
 *
 */
public class PersonalCenterActivity extends BaicActivity{

	private ItemView itemView1,itemView2,itemView3,itemView4,itemView5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置标题栏为用户自定义标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_center_activity);
		setTitleBar(getString(R.string.personalActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		initView();
		
	}
	private void initView(){
		itemView1 = (ItemView) findViewById(R.id.itemview1);
		itemView2 = (ItemView) findViewById(R.id.itemview2);
		itemView3 = (ItemView) findViewById(R.id.itemview3);
		itemView4 = (ItemView) findViewById(R.id.itemview4);
		itemView5 = (ItemView) findViewById(R.id.itemview5);
		
		itemView1.setBackground(R.drawable.setting_na_bg_mid_pressed);
		itemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView1.setLabel("谁看过我的简历");
		itemView1.setValue("共1条");
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.GONE);
		itemView1.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PersonalCenterActivity.this,GuideActivity.class);
				startActivity(intent);				
			}
			
		});
		
		itemView2.setBackground(R.drawable.setting_na_bg_mid_pressed);
		itemView2.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView2.setLabel("谁看过我的简历");
		itemView2.setValue("共1条");
		itemView2.setDetailImageViewResource(R.drawable.detail);
		
		itemView3.setBackground(R.drawable.setting_na_bg_mid_pressed);
		itemView3.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView3.setLabel("谁看过我的简历");
		itemView3.setValue("共1条");
		itemView3.setDetailImageViewResource(R.drawable.detail);
		itemView3.setDetailImageVisibility(View.GONE);
		
		itemView4.setBackground(R.drawable.setting_na_bg_mid_pressed);
		itemView4.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView4.setLabel("1");
		itemView4.setValue("共1条");
		itemView4.setDetailImageViewResource(R.drawable.detail);
		
		itemView5.setBackground(R.drawable.setting_na_bg_mid_pressed);
		itemView5.setIconImageViewResource(R.drawable.delete);
		itemView5.setLabel("谁看过我的简历");
		itemView5.setValue("共1条");
		itemView5.setDetailImageViewResource(R.drawable.detail);
	}
	@Override
	protected void initAllView() {
		
	}
	@Override
	protected void reigesterAllEvent() {
		
	}
}
