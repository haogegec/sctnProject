package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.helper.StringUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.cache.CacheProcess;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.ItemView;

/**
 * @author wanghaoc 职位搜索功能点
 * 
 */
public class JobSearchActivity extends BaicActivity {

	private ItemView searchitemView1;
	private ItemView searchitemView2;
	private ItemView searchitemView3;

	private EditText search_edit;
	private ScrollView sv_scroll;
	private LinearLayout search_history_layout;
	private Button search_btn;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
	private CacheProcess cacheProcess;
	
	private String areaId="";
	private String industryTypeId="";
	private String positionTypeId="";
	
	private List<Map> backIndustryType;
	private List<Map> backPositionType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_search_activity);
		setTitleBar(getString(R.string.jobsearchTitle), View.VISIBLE, View.GONE);
		
		cacheProcess = new CacheProcess();
		listItems = cacheProcess.JobSearchLogInSharedPreferences(JobSearchActivity.this);
		initAllView();
		initIntent();
		reigesterAllEvent();
	}

	protected void initIntent() {
		
		for(int i = 0; i < listItems.size(); i++){
			RelativeLayout layout = (RelativeLayout)getLayoutInflater().inflate(R.layout.job_search_item, null);
			
			ItemView jobSearchLogItem = (ItemView) layout.findViewById(R.id.job_search_log_item);
			jobSearchLogItem.setBackground(R.drawable.item_up_bg);
			jobSearchLogItem.setIconImageViewResource(R.drawable.home_btn_normal);
			jobSearchLogItem.setLabel(listItems.get(i).get("list_" + i+"areaName")+listItems.get(i).get("list_" + i+"JobsClassName")+listItems.get(i).get("list_" + i+"NeedProfessionName"));
			jobSearchLogItem.setValue(listItems.get(i).get("count"));
			jobSearchLogItem.setValueTextColor(getResources().getColor(R.color.blue));
			jobSearchLogItem.setDetailImageViewResource(R.drawable.detail);
			jobSearchLogItem.setIconImageVisibility(View.GONE);
	
			search_history_layout.addView(layout,i);
			layout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(JobSearchActivity.this,JobListActivity.class);
					startActivity(intent);
					
				}
			});
		}
		if(listItems.size()==0){
			
            RelativeLayout layout = (RelativeLayout)getLayoutInflater().inflate(R.layout.job_search_item, null);
			
			ItemView jobSearchLogItem = (ItemView) layout.findViewById(R.id.job_search_log_item);
			jobSearchLogItem.setBackground(R.drawable.item_up_bg);
			jobSearchLogItem.setIconImageViewResource(R.drawable.home_btn_normal);
			jobSearchLogItem.setLabel("暂无搜素记录");
			jobSearchLogItem.setValue("");
			jobSearchLogItem.setDetailImageVisibility(View.GONE);
			jobSearchLogItem.setIconImageVisibility(View.GONE);
			search_history_layout.addView(layout,0);
		}

	}

	@Override
	protected void initAllView() {

		search_edit = (EditText) findViewById(R.id.search_edit_bg);

		searchitemView1 = (ItemView) findViewById(R.id.itemview1);
		searchitemView1.setBackground(R.drawable.item_up_bg);
		searchitemView1.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView1.setLabel("地区");
		searchitemView1.setValue("全国");
		searchitemView1.setDetailImageViewResource(R.drawable.detail);
		searchitemView1.setIconImageVisibility(View.GONE);

		searchitemView2 = (ItemView) findViewById(R.id.itemview2);
		searchitemView2.setBackground(R.drawable.item_up_bg);
		searchitemView2.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView2.setLabel("行业");
		searchitemView2.setDetailImageViewResource(R.drawable.detail);
		searchitemView2.setValue("所有行业");
		searchitemView2.setIconImageVisibility(View.GONE);

		searchitemView3 = (ItemView) findViewById(R.id.itemview3);
		searchitemView3.setBackground(R.drawable.item_up_bg);
		searchitemView3.setIconImageViewResource(R.drawable.home_btn_normal);
		searchitemView3.setLabel("职能");
		searchitemView3.setValue("所有职能");
		searchitemView3.setDetailImageViewResource(R.drawable.detail);
		searchitemView3.setIconImageVisibility(View.GONE);

		search_btn = (Button) findViewById(R.id.search_btn);
		sv_scroll = (ScrollView) findViewById(R.id.sv_scroll);
		search_history_layout = (LinearLayout) findViewById(R.id.search_history_layout);
	}

	@Override
	protected void reigesterAllEvent() {

		search_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JobSearchActivity.this, WorkSearchActivity.class);
				startActivity(intent);
			}
		});

		// 选择工作地区
		searchitemView1.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JobSearchActivity.this,SelectWorkAreaActivity.class);
				startActivityForResult(intent,Constant.WORKINGAREA_REQUEST_CODE);
			}
		});

		// 选择行业
		searchitemView2.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(JobSearchActivity.this,SelectIndustryActivity.class);
				 startActivityForResult(intent,Constant.INDUSTRY_REQUEST_CODE);
			}

		});

		// 选择职能
		searchitemView3.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JobSearchActivity.this, SelectPositionActivity.class);
				startActivityForResult(intent,Constant.POSITION_TYPE);
			}
		});
		
		search_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(JobSearchActivity.this,JobListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("areaId", areaId);
				bundle.putString("industryTypeId",industryTypeId);
				bundle.putString("positionTypeId", positionTypeId);
				bundle.putString("areaName", searchitemView1.getValue());
				bundle.putString("industryTypeName", searchitemView2.getValue());
				bundle.putString("positionTypeName", searchitemView3.getValue());
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
		});

	}

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.WORKINGAREA_REQUEST_CODE: {
				
				String area = data.getStringExtra("value");
				searchitemView1.setValue(area);
				areaId = data.getStringExtra("id");
				break;
			}
			
			case Constant.INDUSTRY_REQUEST_CODE: {
				backIndustryType = (List<Map>) ((List) data.getSerializableExtra("list")).get(0);
				String industryTypeTitle="";
				for(int i=0;i<backIndustryType.size();i++){
					industryTypeTitle = industryTypeTitle+backIndustryType.get(i).get("value").toString();
					if(i==backIndustryType.size()-1){
						industryTypeId = industryTypeId+backIndustryType.get(i).get("id").toString();
					}else{
						industryTypeId = industryTypeId+backIndustryType.get(i).get("id").toString()+",";
					}
					
				}
				searchitemView2.setValue(industryTypeTitle);
				break;
			}
			case Constant.POSITION_TYPE: {
				backPositionType = (List<Map>) ((List) data.getSerializableExtra("list")).get(0);
				String industryTypeTitle="";
				for(int i=0;i<backPositionType.size();i++){
					industryTypeTitle = industryTypeTitle+backPositionType.get(i).get("value").toString();
					if(i==backPositionType.size()-1){
						positionTypeId =positionTypeId+ backPositionType.get(i).get("id").toString();
					}else{
						positionTypeId =positionTypeId+ backPositionType.get(i).get("id").toString()+",";
					}
					
				}
				searchitemView3.setValue(industryTypeTitle);
				break;
			}
			}
		}
	}

}
