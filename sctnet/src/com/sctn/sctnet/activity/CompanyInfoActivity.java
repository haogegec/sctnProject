package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.ItemView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author 姜勇男 《谁看了我的简历》界面
 * 
 */
public class CompanyInfoActivity extends BaicActivity {

	private ListView lv_company;
	private static String[] companies = { "苹果", "三星", "摩托罗拉", "诺基亚", "小米", "华为" };
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

	private ViewPager mPager;// 页卡控件
	private List<View> listViews; // Tab页面列表
	private TextView company_profile_title, job_description_title;

	private Button btn_apply;// 申请
	private Button btn_collect;// 收藏
	private Button btn_share;// 分享

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_info_activity);
		setTitleBar(getString(R.string.companyProfile), View.VISIBLE, View.GONE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);

		initTextView();
		initViewPager();
		initAllView();
		reigesterAllEvent();
	}

	/**
	 * 初始化头标
	 */
	private void initTextView() {
		company_profile_title = (TextView) findViewById(R.id.company_profile_title);
		job_description_title = (TextView) findViewById(R.id.job_description_title);

		company_profile_title.setOnClickListener(new MyOnClickListener(0));
		job_description_title.setOnClickListener(new MyOnClickListener(1));
	}

	/**
	 * 对tab页，和tab页的title初始化以及tab页之间的切换
	 */
	public void initViewPager() {
		// 初始化tab页
		mPager = (ViewPager) findViewById(R.id.vp_company_info);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.company_info_layout, null));
		listViews.add(mInflater.inflate(R.layout.job_detail_layout, null));

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	protected void initAllView() {
		btn_apply = (Button) findViewById(R.id.btn_apply);
		btn_collect = (Button) findViewById(R.id.btn_collect);
		btn_share = (Button) findViewById(R.id.btn_share);
	}

	@Override
	protected void reigesterAllEvent() {
		// 申请
		btn_apply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "申请", Toast.LENGTH_LONG).show();
			}
		});

		// 收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "收藏", Toast.LENGTH_LONG).show();
			}
		});

		// 分享
		btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "分享", Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * 头标监听器
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 将两个个页卡界面装入其中，默认显示第一个页卡，还需要实现一个适配器。 ViewPager适配器
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

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
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
	 * 页卡切换监听
	 * 
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			setPageTitlesColor(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * 设置tab的title文字颜色、背景
	 * 
	 * @param titleIndex
	 *            tab页序号
	 */
	private void setPageTitlesColor(int titleIndex) {
		if (titleIndex == 0) {
			company_profile_title.setBackgroundColor(getResources().getColor(R.color.white));
			company_profile_title.setTextColor(getResources().getColor(R.color.blue));
			job_description_title.setBackgroundColor(getResources().getColor(R.color.background));
			job_description_title.setTextColor(getResources().getColor(R.color.lightBlack));
		} else if (titleIndex == 1) {
			company_profile_title.setBackgroundColor(getResources().getColor(R.color.background));
			company_profile_title.setTextColor(getResources().getColor(R.color.lightBlack));
			job_description_title.setBackgroundColor(getResources().getColor(R.color.white));
			job_description_title.setTextColor(getResources().getColor(R.color.blue));
		}
	}

}
