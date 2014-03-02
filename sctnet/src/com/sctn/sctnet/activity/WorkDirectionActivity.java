package com.sctn.sctnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.ItemView;

/**
 * 办事指南
 * @author xueweiwei
 *
 */
public class WorkDirectionActivity extends BaicActivity{
	
	private ItemView itemView1,itemView2,itemView3,itemView4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_direction_activity);
		setTitleBar(getString(R.string.workDirectionActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		// TODO Auto-generated method stub
		itemView1 = (ItemView) findViewById(R.id.itemview1);
		itemView2 = (ItemView) findViewById(R.id.itemview2);
		itemView3 = (ItemView) findViewById(R.id.itemview3);
		itemView4 = (ItemView) findViewById(R.id.itemview4);
		
		itemView1.setBackground(R.drawable.item_up_bg);
		itemView1.setIconImageViewResource(R.drawable.work_direction_item);
		itemView1.setLabel("人事代理指南");
		itemView1.setLabelTextColor(this.getResources().getColor(R.color.blue));
		itemView1.setValue("");
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.VISIBLE);
		itemView1.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorkDirectionActivity.this,GuideActivity.class);
				startActivity(intent);				
			}
			
		});
		
		itemView2.setBackground(R.drawable.item_up_bg);
		itemView2.setIconImageViewResource(R.drawable.work_direction_item);
		itemView2.setLabel("学生入户指南");
		itemView2.setLabelTextColor(this.getResources().getColor(R.color.blue));
		itemView2.setValue("");
		itemView2.setDetailImageViewResource(R.drawable.detail);
		
		itemView3.setBackground(R.drawable.item_up_bg);
		itemView3.setIconImageViewResource(R.drawable.work_direction_item);
		itemView3.setLabel("人才工作证办事指南");
		itemView3.setLabelTextColor(this.getResources().getColor(R.color.blue));
		itemView3.setValue("");
		itemView3.setDetailImageViewResource(R.drawable.detail);
		
		itemView4.setBackground(R.drawable.item_mid_bg);
		itemView4.setIconImageViewResource(R.drawable.work_direction_item);
		itemView4.setLabel("相关资料下载");
		itemView4.setLabelTextColor(this.getResources().getColor(R.color.blue));
		itemView4.setValue("");
		itemView4.setDetailImageViewResource(R.drawable.detail);
	
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}

}
