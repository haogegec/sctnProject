package com.sctn.sctnet.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
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

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;

public class RegisterActivity extends BaicActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		setTitleBar(getString(R.string.registerTitle), View.VISIBLE, View.GONE);
		
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
	}

	@Override
	protected void reigesterAllEvent() {
		
	}

}
