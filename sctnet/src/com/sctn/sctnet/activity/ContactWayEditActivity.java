package com.sctn.sctnet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sctn.sctnet.R;

/**
 * 编辑联系方式
 * @author xueweiwei
 *
 */
public class ContactWayEditActivity extends BaicActivity{
	
	private EditText contactsnameValue;
	private String contactsnameStr = "";//第二联系人
	
	private EditText contactsphoneValue;
	private String contactsphoneStr = "";//第二联系人电话
	
	private EditText userphoneValue;
	private String userphoneStr = "";//第二联系人电话
	
	private EditText emailValue;
	private String emailStr = "";//email
	
	private EditText postalcodeValue;
	private String postalcodeStr = "";//邮政编码
	
	private EditText qqmsnValue;
	private String qqmsnStr = "";//QQ号
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_way_edit_activity);
		setTitleBar(getString(R.string.ContactWayEditActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.queding);
		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		contactsnameValue = (EditText) findViewById(R.id.contactsname_value);
		
		contactsphoneValue = (EditText) findViewById(R.id.contactsphone_value);
		
		userphoneValue = (EditText) findViewById(R.id.userphone_value);
		
		emailValue = (EditText) findViewById(R.id.email_value);
		
		postalcodeValue = (EditText) findViewById(R.id.postalcode_value);
		
		qqmsnValue = (EditText) findViewById(R.id.qqmsn_value);
		
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}

}
