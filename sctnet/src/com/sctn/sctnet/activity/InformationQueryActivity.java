package com.sctn.sctnet.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
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
	private LinearLayout parentLayout;
	private Button moreBtn;
	private int pageNo=0;
	private String bigTitleId;
	//服务端返回结果
	private String result;
	private JSONObject responseJsonObject = null;// 返回结果存放在该json对象中
	private LinearLayout.LayoutParams bigTitlep;
	private LinearLayout.LayoutParams littleTitlep;
	
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
				
				JSONArray dataJsonArray = resultJsonArray.getJSONObject(i).getJSONArray("data");					
				String title = resultJsonArray.getJSONObject(i).getString("title");
				bigTitleId = resultJsonArray.getJSONObject(i).getString("cid");
				LinearLayout l = (LinearLayout)getLayoutInflater().inflate(R.layout.information_big_item, null);
				TextView titleText = (TextView) l.getChildAt(0);
				if(i/2==0){
					titleText.setBackgroundResource(R.color.lightGreen);
				}
				titleText.setText(title);
				parentLayout.addView(l, bigTitlep);
				
				for(int j=0;j<dataJsonArray.length();j++) {
									
					LinearLayout ll = (LinearLayout)getLayoutInflater().inflate(R.layout.itemview_layout, null);
		
					ItemView itemView = (ItemView)ll.getChildAt(0);
					itemView.setBackground(R.drawable.item_mid);
					itemView.setIconImageViewResource(R.drawable.home_btn_normal);
					itemView.setLabel(dataJsonArray.getJSONObject(j).getString("title"));
					itemView.setValue("");
					itemView.setDetailImageViewResource(R.drawable.detail);
					itemView.setIconImageVisibility(View.GONE);
					itemView.getRelativeLayout().setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View view) {
							Toast.makeText(getApplicationContext(), "点击了", Toast.LENGTH_SHORT).show();			
						}
						
					});
					parentLayout.addView(ll, (i+2));
							
				}
				moreBtn = new Button(this);
				moreBtn.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
				moreBtn.setBackgroundColor(0XFFF6F6F6);
				moreBtn.setTextColor(0XFF444242);
				moreBtn.setText("更  多");
				moreBtn.setTextSize(16);
				moreBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pageNo++;
						Toast.makeText(getApplicationContext(), "点击了", Toast.LENGTH_SHORT).show();			
					}
				});
				
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
	}

	@Override
	protected void reigesterAllEvent() {
		
		
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
	
   private void requestData(){
		
		String url = "AppInfoAction.app";

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
			Message m=new Message();
            if(responseJsonObject.get("resultCode").equals("0")) {
								
				m.what = 0;
			}else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				InformationQueryActivity.this.sendExceptionMsg(err);
			}
					
			handler.sendMessage(m);
			
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
