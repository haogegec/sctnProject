package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.contants.Constant;
/**
 * 编辑职业生涯
 * @author xueweiwei
 *
 */
public class WorkExperienceEditActivity extends BaicActivity{

	private EditText companynameValue;
	private String companynameStr = "";//当前公司
	
	private RelativeLayout currentprofessional;
	private TextView currentprofessionalValue;//当前从事行业
	
	private RelativeLayout adminpost;
	private TextView adminpostValue;//当前从事职业
	
	private EditText workexperienceValue;
	private String workexperienceValueStr = "";//工作年限
	
	private RelativeLayout workperformance;
	private TextView workperformanceValue;
	private String workperformanceStr = "";//工作经验
	
	private String currentIndustryId = "";// 目前就职的行业的ID
	private String currentIndustry = "";// 目前就职的行业
	private String jobId = "";// 目前的职位类别ID
	private String job = "";// 目前的职位类别
	
	private String result;// 服务端返回的结果
    private long userId;
	
	private HashMap<String, String> workExperienceMap;

	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> newWorkExperienceMap = new HashMap<String, String>();//基本信息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_experience_edit_activity);
		setTitleBar(getString(R.string.WorkExperienceEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		
		initIntent();
		initAllView();
		reigesterAllEvent();
	}

	protected void initIntent(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		userId = SharePreferencesUtils.getSharedlongData("userId");
		if(bundle!=null&&bundle.getSerializable("workExperienceList")!=null){
			List<HashMap<String, String>> basicInfoList = (List<HashMap<String, String>>) bundle.getSerializable("workExperienceList");
			workExperienceMap = basicInfoList.get(0);
		}
	}
	@Override
	protected void initAllView() {
		
	//	companynameValue = (EditText) findViewById(R.id.companyname_value);
		
		currentprofessional = (RelativeLayout) findViewById(R.id.currentprofessional);
		currentprofessionalValue = (TextView) findViewById(R.id.currentprofessional_value);
		
		adminpost = (RelativeLayout) findViewById(R.id.adminpost);
		adminpostValue = (TextView) findViewById(R.id.adminpost_value);
		
		workexperienceValue = (EditText) findViewById(R.id.workexperience_value);
		
		workperformance = (RelativeLayout) findViewById(R.id.workperformance);
		workperformanceValue = (TextView) findViewById(R.id.workperformance_value);
		
        if(workExperienceMap!=null&&workExperienceMap.size()!=0){
			
			if(workExperienceMap.containsKey("当前从事职业")){
				job = workExperienceMap.get("当前从事职业");
				adminpostValue.setText(job);
			}
//			if(workExperienceMap.containsKey("当前公司")){
//				companynameStr = workExperienceMap.get("当前公司");
//				companynameValue.setText(companynameStr);
//			}
			if(workExperienceMap.containsKey("当前从事行业")){
				currentIndustry = workExperienceMap.get("当前从事行业");
				currentprofessionalValue.setText(currentIndustry);
			}
			if(workExperienceMap.containsKey("工作年限")){
				workexperienceValueStr = workExperienceMap.get("工作年限");
	            workexperienceValue.setText(workexperienceValueStr);
			}
			if(workExperienceMap.containsKey("工作业绩")){
				workperformanceStr = workExperienceMap.get("工作业绩");
				workperformanceValue.setText(workperformanceStr);
			}
	}
}

	@Override
	protected void reigesterAllEvent() {
		
		// 当前从事的行业
		currentprofessional.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(WorkExperienceEditActivity.this, SelectCurrentIndustryActivity.class);
				intent.putExtra("flag", "WorkExperienceEditActivity");
				startActivityForResult(intent, Constant.CURRENT_INDUSTRY_REQUEST_CODE);
				
			}
			
		});
		
		// 当前从事的职业
		adminpost.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(WorkExperienceEditActivity.this, SelectJobActivity.class);
				intent.putExtra("flag", "WorkExperienceEditActivity");
				startActivityForResult(intent, Constant.JOB_REQUEST_CODE);
				
			}
			
		});

		workperformance.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorkExperienceEditActivity.this,WorkPerformanceEditActivity.class);
				intent.putExtra("workperformanceStr", workperformanceStr);
				startActivityForResult(intent,Constant.WORKPERFORMANCE_REQUEST_CODE);
			}
			
		});
		
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

	           if(job.equals(adminpostValue.getText().toString())&&currentIndustry.equals(currentprofessionalValue.getText().toString())
	        		   &&workexperienceValueStr.equals(workexperienceValue.getText().toString())&&workperformanceStr.equals(workperformanceValue.getText().toString())){
	        	   
	        	   Toast.makeText(getApplicationContext(), "请编辑之后再保存吧~~", Toast.LENGTH_SHORT).show();
	        	   
	           }else{
	        	   requestDataThread();
	           }
				
			}
		});

		
	}
	/**
	 * 请求数据线程
	 * 
	 */
	private void requestDataThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestData();
					}
				});
		mThread.start();
	}
	private void requestData() {

		String url = "appPersonInfo!modify.app";

		Message msg = new Message();

		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("Userid", userId+""));
	//	params.add(new BasicNameValuePair("Userid",100020+""));
		params.add(new BasicNameValuePair("WorkExperience",workexperienceValue.getText().toString()));
		params.add(new BasicNameValuePair("WorkPerformance",workperformanceValue.getText().toString()));
		
		if(!jobId.equals("")){
			params.add(new BasicNameValuePair("AdminPost", jobId));
		}
		if(!currentIndustryId.equals("")){

			params.add(new BasicNameValuePair("CurrentProfessional",currentIndustryId));
		}
		
		params.add(new BasicNameValuePair("modifytype", "0"));//保存到简历表中
		
		result = getPostHttpContent(url, params);

		newWorkExperienceMap.put("当前从事职业", adminpostValue.getText().toString());
	//	newWorkExperienceMap.put("当前公司", companynameValue.getText().toString());
		newWorkExperienceMap.put("当前从事行业", currentprofessionalValue.getText().toString());
		newWorkExperienceMap.put("工作年限", workexperienceValue.getText().toString());
		newWorkExperienceMap.put("工作业绩", workperformanceValue.getText().toString());
		list.add(newWorkExperienceMap);
		
		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			sendExceptionMsg(result);
			return;
		}
		
		JSONObject responseJsonObject;
		try {
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.get("resultCode").toString().equals("0")) {
				msg.what = 00;
				handler.sendMessage(msg);
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				sendExceptionMsg(err);
			}
		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			sendExceptionMsg(err);
		}
		
		
	}

	// 处理线程发送的消息
		private Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 00:
					Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("list", list);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
					break;
				}
				closeProcessDialog();
			}
		};
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

				case Constant.CURRENT_INDUSTRY_REQUEST_CODE: 
					currentIndustry = data.getStringExtra("currentIndustry");
					currentIndustryId = data.getStringExtra("currentIndustryId");
					currentprofessionalValue.setText(currentIndustry);
				break;
				case Constant.JOB_REQUEST_CODE: 
					jobId = data.getStringExtra("jobId");
					job = data.getStringExtra("job");
					adminpostValue.setText(job);
					break;
				case Constant.WORKPERFORMANCE_REQUEST_CODE:
				    
				    workperformanceValue.setText(data.getStringExtra("workperformanceStr"));
				break;

			}
		}
	}

}
