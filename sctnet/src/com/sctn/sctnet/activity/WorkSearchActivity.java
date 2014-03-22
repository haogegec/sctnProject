package com.sctn.sctnet.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.sctn.sctnet.R;
import com.sctn.sctnet.contants.Constant;

/**
 * @author wanghaoc 首页关键字职位搜索
 * 
 */
public class WorkSearchActivity extends Activity {

	private int perSpacing;
	private ArrayList<TextView> pageTitles = new ArrayList<TextView>();
	private ImageView cursor;// 动画图片
	private int currIndex = 0;// 当前页卡编号
	private EditText search_edit;
	private String type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.worksearch_activity);
		initAllView();
		reigesterAllEvent();
	}

	protected void initAllView() {
		// 获取手机屏幕的宽度
		getScreenWidth();
		initPageTitles();
		// 初始化页卡标题下面的橘色条
		cursor = (ImageView) findViewById(R.id.cursor);
		Bitmap bmp = Bitmap
				.createBitmap(perSpacing, 5, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		canvas.drawColor(0xFFef8700);
		cursor.setImageBitmap(bmp);
		// 初始化Edit编辑框
		search_edit = (EditText) findViewById(R.id.et_search_searchtxt);
	}

	protected void reigesterAllEvent() {
		setPageTitleOnClickListener();
		search_edit.setOnEditorActionListener(new OnEditorActionListener() { 
            
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { 
               if (actionId == EditorInfo.IME_ACTION_DONE||actionId == KeyEvent.KEYCODE_ENTER||actionId == 0) { 
            	   Intent intent = new Intent(WorkSearchActivity.this,JobListActivity.class);
            	   Bundle bundle = new Bundle();
            	   bundle.putString("type", type);
            	   bundle.putString("key", search_edit.getText().toString());
            	   bundle.putString("whichActivity", "WorkSearchActivity");
            	   intent.putExtras(bundle);
				   startActivity(intent);
               } 
               return false; 
           } 
       });
	}

	/**
	 * 设置页卡标题点击事件监听器
	 */
	private void setPageTitleOnClickListener() {
		for (int i = 0; i < pageTitles.size(); i++) {
			pageTitles.get(i).setOnClickListener(new MyOnClickListener(i));
		}
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {

			index = i;
		}

		public void onClick(View v) {

			switch (index) {
			case 0:
				if (currIndex == 1) {
					// 从2到1
					cursor.setPadding(cursor.getPaddingLeft() - perSpacing,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());
		
				} else if (currIndex == 2) {
					cursor.setPadding(cursor.getPaddingLeft() - perSpacing*2,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());

				}
				type = Constant.TYPE_JOB_NAME;
				break;
			case 1:
				// 从1 到 2 页面
				if (currIndex == 0) {
					cursor.setPadding(cursor.getPaddingLeft() + perSpacing,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());
				} else if (currIndex == 2) {
					// 从3到2
					cursor.setPadding(cursor.getPaddingLeft() - perSpacing,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());
				}
				type = Constant.TYPE_COMPANY_NAME;
				break;
			case 2:
				// 从1到3
				if (currIndex == 0) {
					cursor.setPadding(cursor.getPaddingLeft() + perSpacing*2,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());
				} else if (currIndex == 1) {
					// 从 2 到3
					cursor.setPadding(cursor.getPaddingLeft() + perSpacing,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());
				}
				type = Constant.TYPE_FULL_TEXT;
				break;
			}
			currIndex = index;
		};
	}

	/**
	 * 初始化页卡标题
	 */
	private void initPageTitles() {
		pageTitles.clear();
		pageTitles.add((TextView) findViewById(R.id.search_layout_title1));
		pageTitles.add((TextView) findViewById(R.id.search_layout_title2));
		pageTitles.add((TextView) findViewById(R.id.search_layout_title3));
	}

	/**
	 * 获取屏幕宽度/3
	 */
	private int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取屏幕宽度
		perSpacing = screenW / 3;
		return perSpacing;
	}

	
}
