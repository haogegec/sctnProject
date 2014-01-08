package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.List;
import com.sctn.sctnet.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 引导页
 * 
 * @author wanghaoc
 * 
 */
public class GuideActivity extends Activity {

	private ViewPager viewpager;
	private List<View> pageList;
	private MyPagerAdapter viewpagerAdapter;
	private TextView[] bottomTag;
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.guide_activity);
		initViewPages();
		initBottomTags();
	}

	/**
	 * 初始化页卡页面
	 * 
	 * @author wanghaoc
	 */
	private void initViewPages() {
		LayoutInflater inflater = LayoutInflater.from(this);
		pageList = new ArrayList<View>();
		pageList.add(inflater.inflate(R.layout.guide_one_activity, null));
		pageList.add(inflater.inflate(R.layout.guide_two_activity, null));
		pageList.add(inflater.inflate(R.layout.guide_three_activity, null));
		pageList.add(inflater.inflate(R.layout.guide_four_activity, null));
		viewpagerAdapter = new MyPagerAdapter(pageList);
		viewpager = (ViewPager) findViewById(R.id.vp_guide_viewpager);
		viewpager.setAdapter(viewpagerAdapter);
		viewpager.setCurrentItem(0);
		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化底部标签
	 * 
	 * @author wanghaoc
	 */
	private void initBottomTags() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
		bottomTag = new TextView[pageList.size()];
		for (int i = 0; i < pageList.size(); i++) {
			bottomTag[i] = (TextView) ll.getChildAt(i);
			bottomTag[i].setEnabled(true);// 都设为灰色
		}
		currentIndex = 0;
		bottomTag[currentIndex].setBackgroundColor(Color.parseColor("#fde20e"));
	}

	/**
	 * 页面切换监听事件捕获
	 * 
	 * @author wanghaoc
	 * 
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			setCurrentTagState(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * 动态改变底部tag颜色
	 * 
	 * @param position
	 */
	private void setCurrentTagState(int position) {
		if (position < 0 || position > pageList.size() - 1
				|| currentIndex == position) {
			return;
		}
		bottomTag[position].setBackgroundColor(Color.parseColor("#fde20e"));
		bottomTag[currentIndex].setBackgroundColor(Color.parseColor("#ffffff"));
		currentIndex = position;
	}

	/**
	 * viewpage 适配器
	 * 
	 * @author wanghaoc
	 * 
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		// 该方法用于初始化具体的一个页面，比如添加点击事件等。
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			if (arg1 == 0) {

			} else if (arg1 == 1) {

			} else if (arg1 == 2) {

			} else if (arg1 == 3) {

				Button startBtn = (Button) arg0
						.findViewById(R.id.btn_guide_start);
				startBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						goHome();
					}
				});
			}
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 跳转到主页
	 * 
	 * @author wanghaoc
	 */
	private void goHome() {
		startActivity(new Intent(GuideActivity.this, HomeActivity.class));
	}
}
