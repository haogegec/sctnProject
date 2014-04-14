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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.view.ItemView;

/**
 * 办事指南
 * 
 * @author xueweiwei
 * 
 */
public class WorkDirectionActivity extends BaicActivity {

	private ItemView itemView1, itemView2, itemView3, itemView4;
	private EditText searchEdit;
	private ImageView searchImg;
	private String result;// 服务端返回的结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_direction_activity);
		setTitleBar(getString(R.string.workDirectionActivityTitle), View.VISIBLE, View.GONE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);
		initAllView();
		reigesterAllEvent();
	//	initThread();
	}

	@Override
	protected void initAllView() {

		searchEdit = (EditText) findViewById(R.id.search_edit_bg);
		searchImg = (ImageView) findViewById(R.id.search_bar);
		
		itemView1 = (ItemView) findViewById(R.id.itemview1);
		itemView2 = (ItemView) findViewById(R.id.itemview2);
		itemView3 = (ItemView) findViewById(R.id.itemview3);
		itemView4 = (ItemView) findViewById(R.id.itemview4);

		itemView1.setBackground(R.drawable.item_up_bg);
		itemView1.setIconImageViewResource(R.drawable.work_direction_item);
		itemView1.setLabel("人事代理指南");
		itemView1.setLabelTextColor(this.getResources().getColor(R.color.blue));
		itemView1.setValue("");
		itemView1.setDetailImageViewResource(R.drawable.detail);
		itemView1.setIconImageVisibility(View.VISIBLE);
		itemView1.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorkDirectionActivity.this, InformationListMoreActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("cid", "71");
				bundle.putString("title", "人事代理指南");
				bundle.putString("url", "workGuid!findByCid.app");
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});

		itemView2.setBackground(R.drawable.item_up_bg);
		itemView2.setIconImageViewResource(R.drawable.work_direction_item);
		itemView2.setLabel("学生入户指南");
		itemView2.setLabelTextColor(this.getResources().getColor(R.color.blue));
		itemView2.setValue("");
		itemView2.setDetailImageViewResource(R.drawable.detail);
		itemView2.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorkDirectionActivity.this, InformationListMoreActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("cid", "71");
				bundle.putString("title", "学生入户指南");
				bundle.putString("url", "workGuid!findByCid.app");
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});

		itemView3.setBackground(R.drawable.item_up_bg);
		itemView3.setIconImageViewResource(R.drawable.work_direction_item);
		itemView3.setLabel("人才工作证办事指南");
		itemView3.setLabelTextColor(this.getResources().getColor(R.color.blue));
		itemView3.setValue("");
		itemView3.setDetailImageViewResource(R.drawable.detail);
		itemView3.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorkDirectionActivity.this, InformationListMoreActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("cid", "71");
				bundle.putString("title", "人才工作证办事指南");
				bundle.putString("url", "workGuid!findByCid.app");
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});

		itemView4.setBackground(R.drawable.item_single_bg);
		itemView4.setIconImageViewResource(R.drawable.work_direction_item);
		itemView4.setLabel("相关资料下载");
		itemView4.setLabelTextColor(this.getResources().getColor(R.color.blue));
		itemView4.setValue("");
		itemView4.setDetailImageViewResource(R.drawable.detail);
		itemView4.getRelativeLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorkDirectionActivity.this, DownLoadListActivity.class);
				;
				startActivity(intent);
			}

		});

	}

	@Override
	protected void reigesterAllEvent() {
//		searchEdit.setOnEditorActionListener(new OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == KeyEvent.KEYCODE_ENTER || actionId == 0) {
//					Intent intent = new Intent(WorkDirectionActivity.this, InformationListMoreActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putString("search", searchEdit.getText().toString());
//					bundle.putString("cid", "71");
//					bundle.putString("title", "搜索结果");
//					bundle.putString("url", "workGuid!search.app");
//					intent.putExtras(bundle);
//					startActivity(intent);
//				}
//				return false;
//			}
//		});
		searchImg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(WorkDirectionActivity.this, InformationListMoreActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("search", searchEdit.getText().toString());
				bundle.putString("cid", "71");
				bundle.putString("title", "搜索结果");
				bundle.putString("url", "workGuid!search.app");
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
			
		});

	}

	private void initThread() {

		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						initData();
					}
				});
		mThread.start();
	}

	private void initData() {

		String url = "AppWorkGuidInfoAction.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

			// params.add(new BasicNameValuePair("pageNo", pageNo + ""));
			// params.add(new BasicNameValuePair("pageSize", pageSize + ""));
			// if (!StringUtil.isBlank(cid)) {
			// params.add(new BasicNameValuePair("cid", cid));
			// }
			// if (!StringUtil.isBlank(search)) {
			// params.add(new BasicNameValuePair("title", search));
			// }

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {
				result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
				sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONArray resultJsonArray = responseJsonObject.getJSONArray("result");
				if (resultJsonArray == null || resultJsonArray.length() == 0) {
					String err = StringUtil.getAppException4MOS("没有您要搜索的结果");
					sendExceptionMsg(err);
					return;
				}
				// total = responseJsonObject.getInt("resultCount");// 总数
				// if (resultJsonArray.length() > 15) {
				// count = 15;
				// } else {
				// count = resultJsonArray.length();
				// }
				// for (int j = 0; j < count; j++) {
				//
				// Map<String, Object> item = new HashMap<String, Object>();
				// item.put("id", resultJsonArray.getJSONObject(j).get("id"));//
				// 小栏目的id
				// item.put("little_column_title",
				// resultJsonArray.getJSONObject(j).get("title"));// 小栏目的名称
				// item.put("content",
				// resultJsonArray.getJSONObject(j).get("content"));// 职位
				//
				// items.add(item);
				// }
				// if (i == 0) {
				// msg.what = 0;
				//
				// } else {
				// msg.what = 1;
				// }
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
			case 0:
				// initUI();
				closeProcessDialog();

				break;
			case 1:
				// updateUI();

				break;

			}

		}
	};
}
