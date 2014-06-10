package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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

	private ItemView itemView1,itemView2,itemView3,itemView4,itemView5,itemView6;//itemView7,itemView8,itemView9,itemView10;
	private Bundle bundle;
//	private ResumeInfo resumeInfo;
	private ArrayList<ArrayList<HashMap<String, String>>> dataList;
	
	
	private ArrayList<HashMap<String, String>> basicInfoList = new ArrayList<HashMap<String, String>>();// 基本信息
	private ArrayList<HashMap<String, String>> personalExperienceList = new ArrayList<HashMap<String, String>>();// 个人简介
	private ArrayList<HashMap<String, String>> educationExperienceList = new ArrayList<HashMap<String, String>>();// 教育情况
	private ArrayList<HashMap<String, String>> workExperienceList = new ArrayList<HashMap<String, String>>();// 职业生涯
	private ArrayList<HashMap<String, String>> jobIntentionList = new ArrayList<HashMap<String, String>>();// 求职意向
//	private ArrayList<HashMap<String, String>> flagIdList = new ArrayList<HashMap<String, String>>();// 求职意向
	ArrayList<String> flagidList = null;
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();// 联系方式
	
	private String whichActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_edit_activity);
		setTitleBar(getString(R.string.resumeEditActivityTitle), View.VISIBLE, View.GONE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		Intent intent = this.getIntent();
		bundle = intent.getExtras();
		if(bundle!=null){
			whichActivity = bundle.getString("whichActivity");
			if("ResumeCreate".equals(whichActivity)){
//				flagIdList =  (ArrayList<HashMap<String, String>>) bundle.getSerializable("jobIntentionList");
			} else {//if("ResumeManage".equals(whichActivity))
				dataList = (ArrayList<ArrayList<HashMap<String, String>>>) bundle.getSerializable("resumeInfo");
				basicInfoList = dataList.get(0);
				personalExperienceList = dataList.get(1);
				workExperienceList = dataList.get(2);
				educationExperienceList= dataList.get(3);
				contactList = dataList.get(4);
				jobIntentionList = dataList.get(5);
			}
			
		}
		initIntent();
		initAllView();
		reigesterAllEvent();
	}
	
	protected void initIntent(){
		Bundle bundle = getIntent().getExtras();
		flagidList = (ArrayList<String>)bundle.getSerializable("flagIdList");
		jobIntentionList = (ArrayList<HashMap<String, String>>)bundle.getSerializable("jobIntentionList");
	}
	
	@Override
	protected void initAllView() {
		itemView1 = (ItemView) findViewById(R.id.itemview1);
		itemView2 = (ItemView) findViewById(R.id.itemview2);
		itemView3 = (ItemView) findViewById(R.id.itemview3);
		itemView6 = (ItemView) findViewById(R.id.itemview4);
		itemView4 = (ItemView) findViewById(R.id.itemview11);
		itemView5 = (ItemView) findViewById(R.id.itemview12);
		
//		itemView7 = (ItemView) findViewById(R.id.itemview6);
//		itemView8 = (ItemView) findViewById(R.id.itemview7);
//		itemView9 = (ItemView) findViewById(R.id.itemview8);
//		itemView10 = (ItemView) findViewById(R.id.itemview9);
		
		
		
		
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
		
//		itemView7.setBackground(R.drawable.item_mid_bg);
//		itemView7.setIconImageViewResource(R.drawable.home_btn_normal);
//		itemView7.setLabel("求职意向2");
//		itemView7.setValue("");
//		itemView7.setDetailImageViewResource(R.drawable.detail);
//		itemView7.setIconImageVisibility(View.GONE);
//		
//		itemView8.setBackground(R.drawable.item_mid_bg);
//		itemView8.setIconImageViewResource(R.drawable.home_btn_normal);
//		itemView8.setLabel("求职意向3");
//		itemView8.setValue("");
//		itemView8.setDetailImageViewResource(R.drawable.detail);
//		itemView8.setIconImageVisibility(View.GONE);
//		
//		itemView9.setBackground(R.drawable.item_mid_bg);
//		itemView9.setIconImageViewResource(R.drawable.home_btn_normal);
//		itemView9.setLabel("求职意向4");
//		itemView9.setValue("");
//		itemView9.setDetailImageViewResource(R.drawable.detail);
//		itemView9.setIconImageVisibility(View.GONE);
//		
//		itemView10.setBackground(R.drawable.item_mid_bg);
//		itemView10.setIconImageViewResource(R.drawable.home_btn_normal);
//		itemView10.setLabel("求职意向5");
//		itemView10.setValue("");
//		itemView10.setDetailImageViewResource(R.drawable.detail);
//		itemView10.setIconImageVisibility(View.GONE);
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 基本信息
		itemView1.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,BasicInfoEditActivity.class);
				Bundle bundle = new Bundle();
				if("ResumeCreate".equals(whichActivity)){
					bundle.putString("isFirstCreate", "1");
				}else {
					bundle.putString("isFirstCreate", "0");
				}
				if(basicInfoList!=null&&basicInfoList.size()!=0){
					bundle.putSerializable("basicInfoList",basicInfoList);
				}
				intent.putExtras(bundle);
				startActivityForResult(intent,1);				
			}
			
		});
		
		// 个人简介
		itemView2.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,PersonalProfileEditActivity.class);
				Bundle bundle = new Bundle();
				if("ResumeCreate".equals(whichActivity)){
					bundle.putString("isFirstCreate", "1");
				}else {
					bundle.putString("isFirstCreate", "0");
				}
				if(personalExperienceList!=null&&personalExperienceList.size()!=0){
					bundle.putSerializable("personalExperienceList", personalExperienceList);
				}
				intent.putExtras(bundle);
				startActivityForResult(intent,2);		
			}
			
		});
		
		// 求职意向
		itemView3.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
//				Intent intent = null;
//				Bundle bundle = new Bundle();
//				if(flagIdList.size() == 5){
//					intent = new Intent(ResumeEditActivity.this, JobIntentionEditActivity.class);
////					Bundle bundle = new Bundle();
//					bundle.putString("flagId", flagIdList.get(0).get("flagId"));
//				} else {
//					intent = new Intent(ResumeEditActivity.this, JobIntentionListActivity.class);
////					Bundle bundle = new Bundle();
//					bundle.putSerializable("jobIntentionList", jobIntentionList);
//				}
//				intent.putExtras(bundle);
//				startActivityForResult(intent,3);		
				
				Intent intent = new Intent(ResumeEditActivity.this, JobIntentionListActivity.class);
				Bundle bundle = new Bundle();
				if("ResumeCreate".equals(whichActivity)){
					bundle.putString("isFirstCreate", "1");
				}else {
					bundle.putString("isFirstCreate", "0");
				}
				bundle.putSerializable("flagIdList",flagidList);
				bundle.putSerializable("jobIntentionList",jobIntentionList);
				bundle.putString("whichActivity", whichActivity);
				intent.putExtras(bundle);
				startActivityForResult(intent,3);	
				
			}
			
		});
		
		// 联系方式
		itemView6.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,ContactWayEditActivity.class);
				Bundle bundle = new Bundle();
				if("ResumeCreate".equals(whichActivity)){
					bundle.putString("isFirstCreate", "1");
				}else {
					bundle.putString("isFirstCreate", "0");
				}
				if(contactList!=null&&contactList.size()!=0){
					bundle.putSerializable("contactWayList", contactList);
				}
				intent.putExtras(bundle);
				startActivityForResult(intent,6);				
			}
			
		});
		
		// 教育情况
		itemView4.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,EducationExperienceEditActivity.class);
				Bundle bundle = new Bundle();
				if("ResumeCreate".equals(whichActivity)){
					bundle.putString("isFirstCreate", "1");
				}else {
					bundle.putString("isFirstCreate", "0");
				}
				if(educationExperienceList!=null&&educationExperienceList.size()!=0){
					bundle.putSerializable("educationExperienceList", educationExperienceList);
				}
				intent.putExtras(bundle);
				startActivityForResult(intent,4);			
			}
			
		});
		
		// 职业生涯
		itemView5.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResumeEditActivity.this,WorkExperienceEditActivity.class);
				Bundle bundle = new Bundle();
				if("ResumeCreate".equals(whichActivity)){
					bundle.putString("isFirstCreate", "1");
				}else {
					bundle.putString("isFirstCreate", "0");
				}
				if(workExperienceList!=null&&workExperienceList.size()!=0){
					bundle.putSerializable("workExperienceList", workExperienceList);
				}
				intent.putExtras(bundle);
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
//            	Bundle bundle3 = data.getExtras();
//            	jobIntentionList = (ArrayList<HashMap<String, String>>) bundle3.getSerializable("list");
//            	flagIdList.remove(0);
            	whichActivity = null;
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
