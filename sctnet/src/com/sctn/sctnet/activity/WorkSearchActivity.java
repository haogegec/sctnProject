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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sctn.sctnet.R;

/**
 * @author wanghaoc 首页关键字职位搜索
 * 
 */
public class WorkSearchActivity extends Activity {

	private int perSpacing;
	private ArrayList<TextView> pageTitles = new ArrayList<TextView>();
	private ImageView cursor;// 动画图片
	private int currIndex = 0;// 当前页卡编号
	private Animation animation = null;
	private EditText search_edit;

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
		initImageView();
		// 初始化Edit编辑框
		search_edit = (EditText) findViewById(R.id.search_edit_bg);
	}

	protected void reigesterAllEvent() {
		setPageTitleOnClickListener();
		search_edit.setOnKeyListener(new OnKeyListener() {
			// 输入完后按键盘上的搜索键【回车键改为了搜索键】
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {// 修改回车键功能
					// 先隐藏键盘
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(WorkSearchActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

					// 跳转页面
					Intent intent = new Intent(WorkSearchActivity.this,
							SearchResultActivity.class);
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
					moveOnetoLeft(perSpacing);

				} else if (currIndex == 2) {
					// 从3到1
					moveTwoToLeft(perSpacing);
				}
				currIndex = index;
				break;
			case 1:
				// 从1 到 2 页面
				if (currIndex == 0) {
					moveOneToRight(perSpacing);
				} else if (currIndex == 2) {
					// 从3到2
					moveOnetoLeft(perSpacing);
				}
				currIndex = index;
				break;
			case 2:
				// 从1到3
				if (currIndex == 0) {
					moveTwoToRight(perSpacing);
				} else if (currIndex == 1) {
					// 从 2 到3
					moveOneToRight(perSpacing);
				}
				currIndex = index;
				break;
			}
			// setPageTitlesColor(arg0);
			// currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
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
	 * 获取屏幕宽度/4
	 */
	private int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取屏幕宽度
		perSpacing = screenW / 3;
		return perSpacing;
	}

	/**
	 * 初始化动画，生成cursor图片
	 */
	private void initImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		getScreenWidth();
		Bitmap bmp = Bitmap
				.createBitmap(perSpacing, 5, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		canvas.drawColor(0xFFBC1E28);
		cursor.setImageBitmap(bmp);
		setCursorPosition();
	}

	/**
	 * 设置cursor初始位置
	 */
	private void setCursorPosition() {
		Matrix matrix = new Matrix();
		matrix.postTranslate(0, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * @param one
	 *            向左移动一个单位
	 */
	private void moveOnetoLeft(int one) {
		if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
			animation = new TranslateAnimation(3 * one, 2 * one, 0, 0);
		} else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
				&& (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
			animation = new TranslateAnimation(2 * one, one, 0, 0);
		} else if ((pageTitles.get(currIndex).getLeft() > one / 2)
				&& (pageTitles.get(currIndex).getLeft() < one * 3 / 2)) {
			animation = new TranslateAnimation(one, 0, 0, 0);
		} else if (pageTitles.get(currIndex).getLeft() < one / 2) {
			LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) pageTitles
					.get(0).getLayoutParams();
			params1.leftMargin = pageTitles.get(0).getLeft() + one;
			pageTitles.get(0).setLayoutParams(params1);
			animation = new TranslateAnimation(0, 0, 0, 0);
		}

	}

	/**
	 * @param one
	 *            向左移动2个单位
	 */
	private void moveTwoToLeft(int one) {
		if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
			animation = new TranslateAnimation(3 * one, one, 0, 0);
		} else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
				&& (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
			animation = new TranslateAnimation(2 * one, 0, 0, 0);
		}
	}

	/**
	 * @param one
	 *            向右移2个单位
	 */
	private void moveTwoToRight(int one) {
		if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
			LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) pageTitles
					.get(0).getLayoutParams();
			params1.leftMargin = pageTitles.get(0).getLeft() - one;
			pageTitles.get(0).setLayoutParams(params1);
			animation = new TranslateAnimation(3 * one, 3 * one, 0, 0);
		} else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
				&& (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
			animation = new TranslateAnimation(2 * one, 3 * one, 0, 0);
		} else if ((pageTitles.get(currIndex).getLeft() > one / 2)
				&& (pageTitles.get(currIndex).getLeft() < one * 3 / 2)) {
			animation = new TranslateAnimation(one, 3 * one, 0, 0);
		} else if (pageTitles.get(currIndex).getLeft() < one / 2) {
			animation = new TranslateAnimation(0, 2 * one, 0, 0);
		}
	}

	/**
	 * @param one
	 *            向右移动1个单位
	 */
	private void moveOneToRight(int one) {
		if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
			LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) pageTitles
					.get(0).getLayoutParams();
			params1.leftMargin = pageTitles.get(0).getLeft() - one;
			pageTitles.get(0).setLayoutParams(params1);
			animation = new TranslateAnimation(3 * one, 3 * one, 0, 0);
		} else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
				&& (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
			animation = new TranslateAnimation(2 * one, 3 * one, 0, 0);
		} else if ((pageTitles.get(currIndex).getLeft() > one / 2)
				&& (pageTitles.get(currIndex).getLeft() < one * 3 / 2)) {
			animation = new TranslateAnimation(one, 2 * one, 0, 0);
		} else if (pageTitles.get(currIndex).getLeft() < one / 2) {
			animation = new TranslateAnimation(0, one, 0, 0);
		}
	}
}
