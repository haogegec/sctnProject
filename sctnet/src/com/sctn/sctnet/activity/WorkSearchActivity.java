package com.sctn.sctnet.activity;

import com.sctn.sctnet.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class WorkSearchActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.worksearch_activity);
		
		initAllView();
		reigesterAllEvent();
		
	}

	protected void initAllView() {
		
	}

	protected void reigesterAllEvent() {
		
	}

}
