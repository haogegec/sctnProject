package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.ItemView;
/**
 * 简历编辑界面
 * @author xueweiwei
 *
 */
public class ResumeEditActivity extends BaicActivity{

	private ItemView itemView1,itemView2,itemView3,itemView4,itemView5,itemView6;
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_edit_activity);
		setTitleBar(getString(R.string.resumeEditActivityTitle), View.VISIBLE, View.GONE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		Intent intent = this.getIntent();
		bundle = intent.getExtras();
		initAllView();
		reigesterAllEvent();
	}
	
	@Override
	protected void initAllView() {
		// TODO Auto-generated method stub
		itemView1 = (ItemView) findViewById(R.id.itemview1);
		itemView2 = (ItemView) findViewById(R.id.itemview2);
		itemView3 = (ItemView) findViewById(R.id.itemview3);
		itemView6 = (ItemView) findViewById(R.id.itemview4);
		itemView4 = (ItemView) findViewById(R.id.itemview11);
		itemView5 = (ItemView) findViewById(R.id.itemview12);
		
		itemView1.setBackground(R.drawable.item_mid);
		itemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView1.setLabel("基本信息");
		itemView1.setValue("");
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.GONE);
		
		itemView2.setBackground(R.drawable.item_mid);
		itemView2.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView2.setLabel("个人简介");
		itemView2.setValue("");
		itemView2.setDetailImageViewResource(R.drawable.detail);
		itemView2.setIconImageVisibility(View.GONE);
		
		itemView3.setBackground(R.drawable.item_mid);
		itemView3.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView3.setLabel("求职意向");
		itemView3.setValue("");
		itemView3.setDetailImageViewResource(R.drawable.detail);
		itemView3.setIconImageVisibility(View.GONE);
		
		itemView6.setBackground(R.drawable.item_mid);
		itemView6.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView6.setLabel("联系方式");
		itemView6.setValue("");
		itemView6.setDetailImageViewResource(R.drawable.detail);
		itemView6.setIconImageVisibility(View.GONE);
		
		
		itemView4.setBackground(R.drawable.item_mid);
		itemView4.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView4.setLabel("教育情况");
		itemView4.setValue("");
		itemView4.setDetailImageViewResource(R.drawable.detail);
		itemView4.setIconImageVisibility(View.GONE);
		
		itemView5.setBackground(R.drawable.item_mid);
		itemView5.setIconImageViewResource(R.drawable.delete);
		itemView5.setLabel("职业生涯");
		itemView5.setValue("");
		itemView5.setDetailImageViewResource(R.drawable.detail);
		itemView5.setIconImageVisibility(View.GONE);
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		itemView1.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ResumeEditActivity.this,PersonalInfoActivity.class);
				startActivity(intent);				
			}
			
		});
	}

}
