package com.sctn.sctnet.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.ItemView;
/**
 * 信息咨询界面
 * @author 姜勇男
 *
 */
public class InformationQueryActivity extends BaicActivity{
	
//	private ItemView itemView1,itemView2,itemView3,itemView4,itemView5,itemView6,itemView7,itemView8;
//	private ItemView[] itemViews = {itemView1,itemView2,itemView3,itemView4};
	private String[] labels = {"思维太跳跃的你得小心了","这样的面试者不受欢迎","面试时候的注意事项","怎么巧妙回答面试官的刁难问题"};
	private String[] labels2 = {"想要跳槽成功的四大要点","绝对不要跳槽的其中情形","跳槽对个人发展的利弊","跳槽穷半年 改行穷三年"};
	
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
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.ll_information_query);
		
		for(int i=0; i<labels.length; i++){
			LinearLayout ll = (LinearLayout)getLayoutInflater().inflate(R.layout.itemview_layout, null);
			ItemView itemView = (ItemView)ll.getChildAt(0);
			itemView.setBackground(R.drawable.item_mid);
			itemView.setIconImageViewResource(R.drawable.home_btn_normal);
			itemView.setLabel(labels[i]);
			itemView.setValue("");
			itemView.setDetailImageViewResource(R.drawable.detail);
			itemView.setIconImageVisibility(View.GONE);
			itemView.getRelativeLayout().setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					Toast.makeText(getApplicationContext(), "点击了", Toast.LENGTH_SHORT).show();			
				}
				
			});
			layout.addView(ll, (i+2));
			
		}
		
		for(int i=0; i<labels2.length; i++){
			LinearLayout ll = (LinearLayout)getLayoutInflater().inflate(R.layout.itemview_layout, null);
			ItemView itemView = (ItemView)ll.getChildAt(0);
			itemView.setBackground(R.drawable.item_mid);
			itemView.setIconImageViewResource(R.drawable.home_btn_normal);
			itemView.setLabel(labels2[i]);
			itemView.setValue("");
			itemView.setDetailImageViewResource(R.drawable.detail);
			itemView.setIconImageVisibility(View.GONE);
			itemView.getRelativeLayout().setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					Toast.makeText(getApplicationContext(), "点击了"+view.getId(), Toast.LENGTH_SHORT).show();			
				}
				
			});
			layout.addView(ll, (3+labels.length+i));
			
		}
		
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
