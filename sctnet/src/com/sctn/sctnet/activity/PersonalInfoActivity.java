package com.sctn.sctnet.activity;

import android.os.Bundle;
import android.view.View;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.ItemView;

/**
 * 个人信息
 * @author xueweiwei
 *
 */
public class PersonalInfoActivity extends BaicActivity{

	private ItemView itemView1,itemView2,itemView3,itemView4,itemView5,itemView6,itemView7;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info_activity);
		setTitleBar(getString(R.string.personalInfoActivityTitle), View.VISIBLE, View.GONE);
		initAllView();
		
	}
	@Override
	protected void initAllView() {
		// TODO Auto-generated method stub
		itemView1 = (ItemView) findViewById(R.id.itemview1);
		itemView2 = (ItemView) findViewById(R.id.itemview2);
		itemView3 = (ItemView) findViewById(R.id.itemview3);
		itemView4 = (ItemView) findViewById(R.id.itemview4);
		itemView5 = (ItemView) findViewById(R.id.itemview11);
		itemView6 = (ItemView) findViewById(R.id.itemview12);
		itemView7 = (ItemView) findViewById(R.id.itemview13);
		
		itemView1.setBackground(R.drawable.item_mid);
		itemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView1.setLabel("出生日期");
		itemView1.setValue("1989-01");
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.GONE);
		
		itemView2.setBackground(R.drawable.item_mid);
		itemView2.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView2.setLabel("参加工作时间");
		itemView2.setValue("2012-07");
		itemView2.setDetailImageViewResource(R.drawable.detail);
		itemView2.setIconImageVisibility(View.GONE);
		
		itemView3.setBackground(R.drawable.item_mid);
		itemView3.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView3.setLabel("现居城市");
		itemView3.setValue("北京");
		itemView3.setDetailImageViewResource(R.drawable.detail);
		itemView3.setIconImageVisibility(View.GONE);
		
		itemView4.setBackground(R.drawable.item_mid);
		itemView4.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView4.setLabel("婚姻状况");
		itemView4.setValue("未婚");
		itemView4.setDetailImageViewResource(R.drawable.detail);
		itemView4.setIconImageVisibility(View.GONE);
		
		itemView5.setBackground(R.drawable.item_mid);
		itemView5.setIconImageViewResource(R.drawable.delete);
		itemView5.setLabel("证件类型");
		itemView5.setValue("身份证");
		itemView5.setDetailImageViewResource(R.drawable.detail);
		itemView5.setIconImageVisibility(View.GONE);
		
		itemView6.setBackground(R.drawable.item_mid);
		itemView6.setIconImageViewResource(R.drawable.delete);
		itemView6.setLabel("证件号码");
		itemView6.setValue("410718199009089080");
		itemView6.setDetailImageViewResource(R.drawable.detail);
		itemView6.setIconImageVisibility(View.GONE);
		
		itemView7.setBackground(R.drawable.item_mid);
		itemView7.setIconImageViewResource(R.drawable.delete);
		itemView7.setLabel("户口所在地");
		itemView7.setValue("北京");
		itemView7.setDetailImageViewResource(R.drawable.detail);
		itemView7.setIconImageVisibility(View.GONE);
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}

}
