package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.entity.ResumeInfo;
import com.sctn.sctnet.view.ItemView;
/**
 * 简历编辑界面
 * @author xueweiwei
 *
 */
public class ResumeEditActivity extends BaicActivity{

	private ItemView itemView1,itemView2,itemView3,itemView4,itemView5,itemView6;
	private Bundle bundle;
//	private ResumeInfo resumeInfo;
	private ArrayList<ArrayList<HashMap<String, String>>> dataList;
	
	private ArrayList<HashMap<String, String>> basicInfoList = new ArrayList<HashMap<String, String>>();// 基本信息
	private ArrayList<HashMap<String, String>> personalExperienceList = new ArrayList<HashMap<String, String>>();// 个人简介
	private ArrayList<HashMap<String, String>> educationExperienceList = new ArrayList<HashMap<String, String>>();// 教育情况
	private ArrayList<HashMap<String, String>> workExperienceList = new ArrayList<HashMap<String, String>>();// 职业生涯
	private ArrayList<HashMap<String, String>> jobIntentionList = new ArrayList<HashMap<String, String>>();// 求职意向
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();// 联系方式
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_edit_activity);
		setTitleBar(getString(R.string.resumeEditActivityTitle), View.VISIBLE, View.GONE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		Intent intent = this.getIntent();
		bundle = intent.getExtras();
		if(bundle!=null){
			dataList = (ArrayList<ArrayList<HashMap<String, String>>>) bundle.getSerializable("resumeInfo");
			basicInfoList = dataList.get(0);
			personalExperienceList = dataList.get(1);
			workExperienceList = dataList.get(2);
			educationExperienceList= dataList.get(3);
			contactList = dataList.get(4);
			jobIntentionList = dataList.get(5);
		}
		
		initAllView();
		reigesterAllEvent();
	}
	
	@Override
	protected void initAllView() {
		itemView1 = (ItemView) findViewById(R.id.itemview1);
		itemView2 = (ItemView) findViewById(R.id.itemview2);
		itemView3 = (ItemView) findViewById(R.id.itemview3);
		itemView6 = (ItemView) findViewById(R.id.itemview4);
		itemView4 = (ItemView) findViewById(R.id.itemview11);
		itemView5 = (ItemView) findViewById(R.id.itemview12);
		
		itemView1.setBackground(R.drawable.item_mid_bg);
		itemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView1.setLabel("基本信息");
		itemView1.setValue("");
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.GONE);
		
		itemView2.setBackground(R.drawable.item_mid_bg);
		itemView2.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView2.setLabel("个人简介");
		itemView2.setValue("");
		itemView2.setDetailImageViewResource(R.drawable.detail);
		itemView2.setIconImageVisibility(View.GONE);
		
		itemView3.setBackground(R.drawable.item_mid_bg);
		itemView3.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView3.setLabel("求职意向");
		itemView3.setValue("");
		itemView3.setDetailImageViewResource(R.drawable.detail);
		itemView3.setIconImageVisibility(View.GONE);
		
		itemView6.setBackground(R.drawable.item_mid_bg);
		itemView6.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView6.setLabel("联系方式");
		itemView6.setValue("");
		itemView6.setDetailImageViewResource(R.drawable.detail);
		itemView6.setIconImageVisibility(View.GONE);
		
		
		itemView4.setBackground(R.drawable.item_mid_bg);
		itemView4.setIconImageViewResource(R.drawable.home_btn_normal);
		itemView4.setLabel("教育情况");
		itemView4.setValue("");
		itemView4.setDetailImageViewResource(R.drawable.detail);
		itemView4.setIconImageVisibility(View.GONE);
		
		itemView5.setBackground(R.drawable.item_mid_bg);
		itemView5.setIconImageViewResource(R.drawable.delete);
		itemView5.setLabel("职业生涯");
		itemView5.setValue("");
		itemView5.setDetailImageViewResource(R.drawable.detail);
		itemView5.setIconImageVisibility(View.GONE);
		
		
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 基本信息
		itemView1.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,BasicInfoEditActivity.class);
				if(dataList!=null&&dataList.size()!=0){
					Bundle bundle = new Bundle();
					bundle.putSerializable("basicInfoList",basicInfoList);
					intent.putExtras(bundle);
				}
				
				startActivityForResult(intent,1);				
			}
			
		});
		
		// 个人简介
		itemView2.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,PersonalProfileEditActivity.class);
				if(dataList!=null&&dataList.size()!=0){
					Bundle bundle = new Bundle();
					bundle.putSerializable("personalExperienceList", personalExperienceList);
					intent.putExtras(bundle);
				}
				
				startActivityForResult(intent,2);		
			}
			
		});
		
		// 求职意向
		itemView3.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,JobIntentionEditActivity.class);
				if(dataList!=null&&dataList.size()!=0){
					Bundle bundle = new Bundle();
					bundle.putSerializable("jobIntentionList", jobIntentionList);
					intent.putExtras(bundle);
				}
				
				startActivityForResult(intent,3);				
			}
			
		});
		
		// 联系方式
		itemView6.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,ContactWayEditActivity.class);
				if(dataList!=null&&dataList.size()!=0){
					Bundle bundle = new Bundle();
					bundle.putSerializable("contactWayList", contactList);
					intent.putExtras(bundle);
				}
				
				startActivityForResult(intent,6);				
			}
			
		});
		
		// 教育情况
		itemView4.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,EducationExperienceEditActivity.class);
				if(dataList!=null&&dataList.size()!=0){
					Bundle bundle = new Bundle();
					bundle.putSerializable("educationExperienceList", educationExperienceList);
					intent.putExtras(bundle);
				}
				startActivityForResult(intent,4);			
			}
			
		});
		
		// 职业生涯
		itemView5.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,WorkExperienceEditActivity.class);
				if(dataList!=null&&dataList.size()!=0){
					Bundle bundle = new Bundle();
					bundle.putSerializable("workExperienceList", workExperienceList);
					intent.putExtras(bundle);
				}
				startActivityForResult(intent,5);			
			}
			
		});
		
		super.getTitleLeftButton().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this, ResumeManageActivity.class);
				startActivity(intent);
				finish();
			}
			
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_HOME))
				&& event.getRepeatCount() == 0) {
			Intent intent = new Intent(ResumeEditActivity.this, ResumeManageActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				Bundle bundle1 = data.getExtras();
				basicInfoList = (ArrayList<HashMap<String, String>>) bundle1.getSerializable("list");
				break;

			case 2:
				Bundle bundle2 = data.getExtras();
				personalExperienceList = (ArrayList<HashMap<String, String>>) bundle2.getSerializable("list");
				break;
            case 3:
            	Bundle bundle3 = data.getExtras();
            	jobIntentionList = (ArrayList<HashMap<String, String>>) bundle3.getSerializable("list");
				break;
            case 4:
            	Bundle bundle4 = data.getExtras();
            	educationExperienceList = (ArrayList<HashMap<String, String>>) bundle4.getSerializable("list");
				break;
            case 5:
            	Bundle bundle5 = data.getExtras();
            	workExperienceList = (ArrayList<HashMap<String, String>>) bundle5.getSerializable("list");
				break;
            case 6:
            	Bundle bundle6 = data.getExtras();
            	contactList = (ArrayList<HashMap<String, String>>) bundle6.getSerializable("list");
				break;
			}
		}
	}
}
