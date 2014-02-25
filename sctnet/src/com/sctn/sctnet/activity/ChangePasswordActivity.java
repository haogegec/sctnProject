package com.sctn.sctnet.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;

public class ChangePasswordActivity extends BaicActivity {

	Button change_password_submit ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password_activity);
		setTitleBar(getString(R.string.change_password), View.VISIBLE, View.GONE);
		
		initAllView();
		reigesterAllEvent();
	}


	@Override
	protected void initAllView() {
		change_password_submit = (Button)findViewById(R.id.change_password_submit);
	}

	@Override
	protected void reigesterAllEvent() {
		
		// 注册
		change_password_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "提交", Toast.LENGTH_SHORT).show();
			}
			
		});
		
	}

}
