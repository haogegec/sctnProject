package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.ItemView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author 姜勇男
 * 《谁看了我的简历》界面
 *
 */
public class CompanyInfoActivity extends BaicActivity {

	private ListView lv_company;
	private static String[] companies = {"苹果","三星","摩托罗拉","诺基亚","小米","华为"};
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_info_activity);
		setTitleBar(getString(R.string.personalActivityTitle), View.VISIBLE, View.GONE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);
		
//		initAllView();
//		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
	}

	@Override
	protected void reigesterAllEvent() {
		
	}
}
