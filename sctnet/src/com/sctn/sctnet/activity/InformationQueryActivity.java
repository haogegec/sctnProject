package com.sctn.sctnet.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.ItemView;
/**
 * 薪酬调查界面
 * @author 姜勇男
 *
 */
public class InformationQueryActivity extends BaicActivity{
	
	private ItemView itemView1,itemView2,itemView3,itemView4,itemView5,itemView6,itemView7,itemView8;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_query_activity);
		setTitleBar(getString(R.string.information_query), View.VISIBLE, View.GONE);
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		itemView1 = (ItemView) findViewById(R.id.itemview1);
		itemView2 = (ItemView) findViewById(R.id.itemview2);
		itemView3 = (ItemView) findViewById(R.id.itemview3);
		itemView4 = (ItemView) findViewById(R.id.itemview4);
		itemView5 = (ItemView) findViewById(R.id.itemview5);
		itemView6 = (ItemView) findViewById(R.id.itemview6);
		itemView7 = (ItemView) findViewById(R.id.itemview7);
		itemView8 = (ItemView) findViewById(R.id.itemview8);
		
		itemView1.setBackground(R.drawable.item_mid);
		itemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView1.setLabel("思维太跳跃的你得小心了");
		itemView1.setValue("");
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.GONE);
		
		itemView2.setBackground(R.drawable.item_mid);
		itemView2.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView2.setLabel("这样的面试者不受欢迎");
		itemView2.setValue("");
		itemView2.setDetailImageViewResource(R.drawable.detail);
		itemView2.setIconImageVisibility(View.GONE);
		
		itemView3.setBackground(R.drawable.item_mid);
		itemView3.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView3.setLabel("思维太跳跃的你得小心了");
		itemView3.setValue("");
		itemView3.setDetailImageViewResource(R.drawable.detail);
		itemView3.setIconImageVisibility(View.GONE);
		
		itemView4.setBackground(R.drawable.item_mid);
		itemView4.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView4.setLabel("这样的面试者不受欢迎");
		itemView4.setValue("");
		itemView4.setDetailImageViewResource(R.drawable.detail);
		itemView4.setIconImageVisibility(View.GONE);
		
		itemView5.setBackground(R.drawable.item_mid);
		itemView5.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView5.setLabel("想要跳槽成功的四大要点");
		itemView5.setValue("");
		itemView5.setDetailImageViewResource(R.drawable.detail);
		itemView5.setIconImageVisibility(View.GONE);
		
		itemView6.setBackground(R.drawable.item_mid);
		itemView6.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView6.setLabel("绝对不要跳槽的其中情形");
		itemView6.setValue("");
		itemView6.setDetailImageViewResource(R.drawable.detail);
		itemView6.setIconImageVisibility(View.GONE);
		
		itemView7.setBackground(R.drawable.item_mid);
		itemView7.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView7.setLabel("想要跳槽成功的四大要点");
		itemView7.setValue("");
		itemView7.setDetailImageViewResource(R.drawable.detail);
		itemView7.setIconImageVisibility(View.GONE);
		
		itemView8.setBackground(R.drawable.item_mid);
		itemView8.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView8.setLabel("绝对不要跳槽的其中情形");
		itemView8.setValue("");
		itemView8.setDetailImageViewResource(R.drawable.detail);
		itemView8.setIconImageVisibility(View.GONE);
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.FOREIGNLANGUAGE_REQUEST_CODE: {
				
				
				break;
				
			}
			
			case Constant.JOBEXP_REQUEST_CODE: {
				
				Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
				break;
				
			}
	
			}
		}
	}
}
