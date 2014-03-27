package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.view.ItemView;

/**
 * 编辑个人简介
 * @author xueweiwei
 *
 */
public class PersonalProfileEditActivity extends BaicActivity{

	private EditText reccontentEdit;
	private EditText specialtyContentEdit;
	private EditText personalResumeEdit;
	
	private String reccontentStr = "";
	private String specialtyContentStr = "";
	private String personalResumeStr = "";
	
	private String result;// 服务端返回的结果
	private long userId;
	
	private HashMap<String, String> personalExperienceMap;//基本信息
	
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private HashMap<String, String> newPersonalExperienceMap = new HashMap<String, String>();//基本信息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_profile_edit_activity);
		setTitleBar(getString(R.string.ProvincesActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		initBundle();
		initAllView();
		reigesterAllEvent();
	}
	private void initBundle(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		userId = SharePreferencesUtils.getSharedlongData("userId");
		if(bundle!=null&&bundle.getSerializable("personalExperienceList")!=null){
			List<HashMap<String, String>> personalExperienceList = (List<HashMap<String, String>>) bundle.getSerializable("personalExperienceList");
			personalExperienceMap = personalExperienceList.get(0);
		}
	}
	@Override
	protected void initAllView() {

		reccontentEdit = (EditText) findViewById(R.id.reccontent_edit);
		
		specialtyContentEdit = (EditText) findViewById(R.id.specialtyContent_edit);
		personalResumeEdit = (EditText) findViewById(R.id.personalResume_edit);
		
        if(personalExperienceMap!=null&&personalExperienceMap.size()!=0){
			
			if(personalExperienceMap.containsKey("推荐自己")){
				reccontentStr = personalExperienceMap.get("推荐自己");
				reccontentEdit.setText(reccontentStr);
			}
			if(personalExperienceMap.containsKey("特长")){
				specialtyContentStr = personalExperienceMap.get("特长");
				specialtyContentEdit.setText(specialtyContentStr);
			}
			if(personalExperienceMap.containsKey("个人经历")){
				personalResumeStr = personalExperienceMap.get("个人经历");
				personalResumeEdit.setText(personalResumeStr);
			}
        }
		reccontentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});
		specialtyContentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});
		personalResumeEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3500)});
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		super.getTitleRightButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(reccontentStr.equals(reccontentEdit.getText().toString())&&specialtyContentStr.equals(specialtyContentEdit.getText().toString())
						&&personalResumeStr.equals(personalResumeEdit.getText().toString())){
					
					Toast.makeText(getApplicationContext(), "请编辑之后再保存吧~~", Toast.LENGTH_SHORT).show();
				}else{
					requestDataThread();
				}
				//保存thread
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
		//params.add(new BasicNameValuePair("Userid", userId+""));
		params.add(new BasicNameValuePair("Userid",100020+""));
		params.add(new BasicNameValuePair("RecContent", reccontentEdit.getText().toString()));
		params.add(new BasicNameValuePair("SpecialtyContent",specialtyContentEdit.getText().toString()));
		params.add(new BasicNameValuePair("Resume",personalResumeEdit.getText().toString()));
		
		params.add(new BasicNameValuePair("modifytype", "0"));//保存到简历表中
		
		result = getPostHttpContent(url, params);

		newPersonalExperienceMap.put("推荐自己", reccontentEdit.getText().toString());
		newPersonalExperienceMap.put("特长", specialtyContentEdit.getText().toString());
		newPersonalExperienceMap.put("个人经历", personalResumeEdit.getText().toString());
		list.add(newPersonalExperienceMap);
		
		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			sendExceptionMsg(result);
			return;
		}
		
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultCode").toString().equals("0")) {
			msg.what = 00;
			handler.sendMessage(msg);
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
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
			}
		};
			
}
