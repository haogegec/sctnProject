package com.sctn.sctnet.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.view.ItemView;
import com.sctn.sctnet.view.SearchEditText;
/**
 * 信息咨询界面
 * @author 姜勇男
 *
 */
public class InformationQueryActivity extends BaicActivity{
	
    private SearchEditText searchEdit;
	private LinearLayout parentLayout;
	private int pageNo=0;
	private String bigTitleId;
	private String title;
//	private String id;
	private String searchStr;
	//服务端返回结果
	private String result;
	private JSONObject responseJsonObject = null;// 返回结果存放在该json对象中
	private LinearLayout.LayoutParams bigTitlep;
	private LinearLayout.LayoutParams littleTitlep;
	private LinearLayout.LayoutParams buttonlep;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_query_activity);
		setTitleBar(getString(R.string.information_query), View.VISIBLE, View.GONE);
		initAllView();
		reigesterAllEvent();
		requestDataThread();
	}

	private void initUI(){
		
		JSONArray resultJsonArray;
		try {
			resultJsonArray = responseJsonObject.getJSONArray("result");
			for(int i=0;i<resultJsonArray.length();i++) {
				
				JSONArray dataJsonArray = resultJsonArray.getJSONObject(i).getJSONArray("list");					
				title = resultJsonArray.getJSONObject(i).getString("colname");
				bigTitleId = resultJsonArray.getJSONObject(i).getString("id");
				LinearLayout l = (LinearLayout)getLayoutInflater().inflate(R.layout.information_big_item, null);
				TextView titleText = (TextView) l.getChildAt(0);
				if(i%2==0){
					titleText.setBackgroundResource(R.color.lightGreen);
				}
				titleText.setText(title);
				parentLayout.addView(l, bigTitlep);
				int count;
				if(dataJsonArray.length()>=3){
					count = 3;
				}else{
					count = dataJsonArray.length();
				}
				
				for(int j=0;j<3;j++) {
					LinearLayout ll = (LinearLayout)getLayoutInflater().inflate(R.layout.itemview_layout, null);
	
					final String id = dataJsonArray.getJSONObject(j).getString("id");
					ItemView itemView = (ItemView)ll.getChildAt(0);
					itemView.setBackground(R.drawable.item_mid_bg);
					itemView.setIconImageViewResource(R.drawable.home_btn_normal);
					itemView.setLabel(dataJsonArray.getJSONObject(j).getString("title"));
					itemView.setValue("");
					itemView.setDetailImageViewResource(R.drawable.detail);
					itemView.setIconImageVisibility(View.GONE);
					itemView.getRelativeLayout().setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View view) {
							Intent intent = new Intent(InformationQueryActivity.this,InformationDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("title", title);
							bundle.putString("id", id);
							intent.putExtras(bundle);
							startActivity(intent);
						}
						
					});
					parentLayout.addView(ll, littleTitlep);
							
				}
				if(count>=3){
				
					LinearLayout more = (LinearLayout)getLayoutInflater().inflate(R.layout.more_button, null);
					Button moreBtn = (Button) more.getChildAt(0);
					moreBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(InformationQueryActivity.this,InformationListMoreActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("title", title);
							bundle.putString("cid", bigTitleId);
							bundle.putString("url", "appInfo!findByCid.app");
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
					
					parentLayout.addView(more, buttonlep);
				}
				
				
			}
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "解析返回Json出错", Toast.LENGTH_SHORT).show();			
		}
		
		
	}
	
	@Override
	protected void initAllView() {
		
		parentLayout = (LinearLayout)findViewById(R.id.ll_information_query);
		
		bigTitlep = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		bigTitlep.topMargin = 10;
		littleTitlep = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		buttonlep = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		searchEdit = (SearchEditText) findViewById(R.id.et_search_searchtxt);
	}

	@Override
	protected void reigesterAllEvent() {
		
		//searchStr = searchEdit.getText().toString();
		
		searchEdit.setOnEditorActionListener(new OnEditorActionListener() { 
            
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { 
               if (actionId == EditorInfo.IME_ACTION_DONE||actionId == KeyEvent.KEYCODE_ENTER||actionId == 0) { 
            	   Intent intent = new Intent(InformationQueryActivity.this,InformationListMoreActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", "搜索结果");
					bundle.putString("search", searchEdit.getText().toString());
					bundle.putString("url", "appInfo!search.app");
					intent.putExtras(bundle);
					startActivity(intent);
               } 
               return false; 
           } 
       }); 
		
	}
	
	/**
	 * 请求数据线程
	 * 
	 */
	private void requestDataThread() {
		showProcessDialog(true);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestData();
					}
				});
		mThread.start();
	}
	
   private void requestData(){
		
		String url = "appInfo.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				InformationQueryActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {
				result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
				InformationQueryActivity.this.sendExceptionMsg(result);
				return;
			}
			responseJsonObject = new JSONObject(result);
			Message m=new Message();
            if(responseJsonObject.get("resultCode").toString().equals("0")) {
								
				m.what = 0;
				handler.sendMessage(m);
			}else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				InformationQueryActivity.this.sendExceptionMsg(err);
				return;
			}
					
			
			
	}catch (JSONException e) {
		String err = StringUtil.getAppException4MOS("解析json出错！");
		InformationQueryActivity.this.sendExceptionMsg(err);
	}
 }
   
// 处理线程发送的消息
		private Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					initUI();
					break;

				}
				closeProcessDialog();
			}
		};

	
}
