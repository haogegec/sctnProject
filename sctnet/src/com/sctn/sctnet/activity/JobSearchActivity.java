package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.helper.StringUtil;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.sctn.sctnet.sqlite.DBHelper;
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
	
	private String areaId="";
	private String industryTypeId="";
	private String positionTypeId="";
	
	private List<Map> backIndustryType;
	private List<Map> backPositionType;
	
	private SQLiteDatabase database;
	
	private List<Map<String, Object>> logData = new ArrayList<Map<String, Object>>();
	
	private LinearLayout.LayoutParams littleTitlep;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_search_activity);
		setTitleBar(getString(R.string.jobsearchTitle), View.VISIBLE, View.GONE);
		
		initAllView();
		
		reigesterAllEvent();
		initLogThread();
	}

	protected void initLogThread() {
		
		//查询数据可能花费太多时间交给子线程去做，在Handler完成数据的显示
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
			@Override
			public void run() {
				initData();
			}
		});
       mThread.start();
		
		

	}
	private void initData() {
		
		Message msg = new Message();
		Cursor cursor = database.query("jobSearchLog", new String[] { "_id",
				"workAreaName", "jobClassName","needProfessionName","workAreaId","jobClassId","needProfessionId","total" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
             Map<String,Object> map = new HashMap<String,Object>(); 
             map.put("workAreaName", cursor.getString(cursor.getColumnIndex("workAreaName")));
             map.put("jobClassName", cursor.getString(cursor.getColumnIndex("jobClassName")));
             map.put("needProfessionName", cursor.getString(cursor.getColumnIndex("needProfessionName")));
             map.put("workAreaId", cursor.getString(cursor.getColumnIndex("workAreaId")));
             map.put("jobClassId", cursor.getString(cursor.getColumnIndex("jobClassId")));
             map.put("needProfessionId", cursor.getString(cursor.getColumnIndex("needProfessionId")));
             map.put("total", cursor.getString(cursor.getColumnIndex("total")));
             logData.add(map);
		}
		cursor.close();
		//database.close();
		msg.what = 0;
		handler.sendMessage(msg);

	}
	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				initUI();
				break;
			}
		}
	};
	private void initUI(){
		
            int begin = 0;
            int end = 0;
            int index = 0;
		    if(logData==null||logData.size()==0){
		    	RelativeLayout layout = (RelativeLayout)getLayoutInflater().inflate(R.layout.job_search_item, null);
				
				ItemView jobSearchLogItem = (ItemView) layout.findViewById(R.id.job_search_log_item);
				jobSearchLogItem.setBackground(R.drawable.item_up_bg);
				jobSearchLogItem.setIconImageViewResource(R.drawable.home_btn_normal);
				jobSearchLogItem.setLabel("暂无搜素记录");
				jobSearchLogItem.setValue("");
				jobSearchLogItem.setDetailImageVisibility(View.GONE);
				jobSearchLogItem.setIconImageVisibility(View.GONE);
				search_history_layout.addView(layout,0);
		    }else{
		    	if(logData.size()>=5){
		    		begin = logData.size()-1;
		    		end = logData.size()-5;
		    	}else{
		    		begin = logData.size()-1;
		    		end = 0;
		    	}
		    	for(int i = begin; i >= end; i--){
					RelativeLayout layout = (RelativeLayout)getLayoutInflater().inflate(R.layout.job_search_item, null);
					final Map<String,Object> map = logData.get(i);
					ItemView jobSearchLogItem = (ItemView) layout.getChildAt(0);
					jobSearchLogItem.setBackground(R.drawable.item_up_bg);
					jobSearchLogItem.setIconImageViewResource(R.drawable.home_btn_normal);
					jobSearchLogItem.setLabel(map.get("workAreaName")+"+"+map.get("jobClassName")+"+"+map.get("needProfessionName"));
					jobSearchLogItem.setValue(map.get("total").toString());
					jobSearchLogItem.setValueTextColor(getResources().getColor(R.color.blue));
					jobSearchLogItem.setDetailImageViewResource(R.drawable.detail);
					jobSearchLogItem.setIconImageVisibility(View.GONE);
			        
					
					jobSearchLogItem.getRelativeLayout().setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							Intent intent = new Intent(JobSearchActivity.this,JobListActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("areaId", map.get("workAreaId").toString());
							bundle.putString("industryTypeId",map.get("jobClassId").toString());
							bundle.putString("positionTypeId",map.get("needProfessionId").toString());
							bundle.putString("areaName", map.get("workAreaName").toString());
							bundle.putString("industryTypeName", map.get("jobClassName").toString());
							bundle.putString("positionTypeName", map.get("needProfessionName").toString());
							intent.putExtras(bundle);
							startActivity(intent);
							finish();
						}
					});
					
					search_history_layout.addView(layout, littleTitlep);
				}
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
		
		//初始化本地数据库
	    DBHelper dbHelper = new DBHelper(this,"jobSearchLog");
        database = dbHelper.getWritableDatabase();
        
        littleTitlep = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
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
				finish();
				
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
