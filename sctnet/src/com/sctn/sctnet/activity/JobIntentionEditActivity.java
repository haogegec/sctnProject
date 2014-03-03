package com.sctn.sctnet.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sctn.sctnet.R;

/**
 * 编辑求职意向
 * @author xueweiwei
 *
 */
public class JobIntentionEditActivity extends BaicActivity{
	
	private RelativeLayout workArea;
	private TextView workAreaValue;
	private String workAreaStr = "";//工作地区
	
	private RelativeLayout workState;
	private TextView workStateValue;
	private String workStateStr = "";//工作性质
	
	private RelativeLayout workmanner;
	private TextView workmannerValue;
	private String workmannerStr = "";//行业
	
	private RelativeLayout companyType;
	private TextView companyTypeValue;
	private String companyTypeStr = "";//企业类型
	
	private RelativeLayout wage;
	private TextView wageValue;
	private String wageStr = "";//薪资范围
	
	private EditText housewhereValue;
	private String housewhereStr = "";//住房要求
	
	private String[] workStateDialogText = {"全职","兼职"};// 工作性质
	private String[] companyTypeDialogText = {"国企","外企","私企"};// 企业类型
	private String[] wageDialogText = {"面议","1500以下","1500-1999","2000-2999","3000-3999","4000-4999","5000以上"};// 薪资范围
	
	private Builder builder;
	private Dialog dialog;//弹出框
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_intention_edit_activity);
		setTitleBar(getString(R.string.ProvincesActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		workArea = (RelativeLayout) findViewById(R.id.workarea);
		workAreaValue = (TextView) findViewById(R.id.workarea_value);
		
		workState = (RelativeLayout) findViewById(R.id.workstate);
		workStateValue = (TextView) findViewById(R.id.workstate_value);
		
		workmanner = (RelativeLayout) findViewById(R.id.workmanner);
		workmannerValue = (TextView) findViewById(R.id.workmanner_value);
		
		companyType = (RelativeLayout) findViewById(R.id.company_type);
		companyTypeValue = (TextView) findViewById(R.id.company_type_value);
		
		wage = (RelativeLayout) findViewById(R.id.wage);
		wageValue = (TextView) findViewById(R.id.wage_value);
		
		housewhereValue = (EditText) findViewById(R.id.housewhere_value);
		
		builder = new AlertDialog.Builder(JobIntentionEditActivity.this);
		
	}

	@Override
	protected void reigesterAllEvent() {
		
		workState.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择工作性质");
				builder.setSingleChoiceItems(workStateDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						workStateValue.setText(workStateDialogText[which]);
						dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
				
			}
			
		});
		
		companyType.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择企业类型");
				builder.setSingleChoiceItems(companyTypeDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						companyTypeValue.setText(companyTypeDialogText[which]);
						dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
				
			}
			
		});
		
		wage.setOnClickListener(new ImageView.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				builder.setTitle("请选择薪资范围");
				builder.setSingleChoiceItems(wageDialogText, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						wageValue.setText(wageDialogText[which]);
						dialog.dismiss();
					}
					
				});
				dialog = builder.create();
				dialog.show();
				
			}
			
		});
		
	}

}
