package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.ItemView;

/**
 * 个人中心界面
 * @author xueweiwei
 *
 */
public class PersonalCenterActivity extends BaicActivity{

	private ItemView itemView1,itemView2,itemView3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_center_activity);
		setTitleBar(getString(R.string.personalActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		initView();
		ImageView postImg = (ImageView) findViewById(R.id.postAppImage);
		postImg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PersonalCenterActivity.this,GuideActivity.class);
				startActivity(intent);
			}
			
		});
	}
	private void initView(){
		itemView1 = (ItemView) findViewById(R.id.itemview1);
		itemView2 = (ItemView) findViewById(R.id.itemview2);
		itemView3 = (ItemView) findViewById(R.id.itemview3);
		
		itemView1.setBackground(R.drawable.item_up_bg);
		itemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView1.setLabel("谁看过我的简历");
		itemView1.setLabelTextColor(getResources().getColor(R.color.blue));
		itemView1.setValue("共6条");
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.VISIBLE);
		itemView1.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalCenterActivity.this,ReadMyResumeActivity.class);
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
		
	}
	@Override
	protected void initAllView() {
		
	}
	@Override
	protected void reigesterAllEvent() {
		
	}
}
