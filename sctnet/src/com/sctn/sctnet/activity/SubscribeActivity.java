package com.sctn.sctnet.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.view.ItemView;
/**
 * 订阅界面
 * @author xueweiwei
 *
 */
public class SubscribeActivity extends BaicActivity{
	
	private EditText subsribeNameEdit;
	private EditText keyWordsEdit;
	private ItemView pushSetItem;
	private Builder builder;
	private Dialog dialog;// 弹出框
	private String[] pushItem = {"每天","每三天","每周","不推送"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribe_activity);
		setTitleBar(getString(R.string.subscribeActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		
		builder = new AlertDialog.Builder(SubscribeActivity.this);
		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		subsribeNameEdit = (EditText) findViewById(R.id.subscribe_name_edit);
		keyWordsEdit = (EditText) findViewById(R.id.keywords_edit);
		pushSetItem = (ItemView) findViewById(R.id.push_set);
		pushSetItem.setBackground(R.drawable.item_up_bg);
		pushSetItem.setIconImageViewResource(R.drawable.home_btn_normal);
		pushSetItem.setLabel("推送设置");
		pushSetItem.setValue("不推送");
		pushSetItem.setDetailImageViewResource(R.drawable.detail);
		pushSetItem.setIconImageVisibility(View.GONE);
	}

	@Override
	protected void reigesterAllEvent() {
		
		
		pushSetItem.getRelativeLayout().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				builder.setTitle("推送设置");
				builder.setSingleChoiceItems(pushItem, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						pushSetItem.setValue(pushItem[which]);
						
						dialog.dismiss();
					}

				});
				dialog = builder.create();
				dialog.show();
				
			}
			
		});
		// 确定按钮
		titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (StringUtil.isBlank(subsribeNameEdit.getText().toString())){

					Toast.makeText(getApplicationContext(), "请输入订阅名称~~", Toast.LENGTH_SHORT).show();
				} else {
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
		
	}
}
