package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.WheelViewUtils;
import com.sctn.sctnet.view.TosGallery;
import com.sctn.sctnet.view.WheelView;

public class WheelViewDateActivity extends Activity {
	ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
	ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
	ArrayList<TextInfo> mDates = new ArrayList<TextInfo>();

	TextView mSelDateTxt = null;
	WheelView mYearWheel = null;
	WheelView mMonthWheel = null;
	WheelView mDateWheel = null;
	
	private Button btn_confirm;

	int mCurYear = 0;
	int mCurMonth = 0;
	int mCurDate = 0;
	
	private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {
		@Override
		public void onEndFling(TosGallery v) {
			int pos = v.getSelectedItemPosition();

			if (v == mDateWheel) {
				TextInfo info = mDates.get(pos);
				setDate(info.mIndex);
			} else if (v == mMonthWheel) {
				TextInfo info = mMonths.get(pos);
				setMonth(info.mIndex);
			} else if (v == mYearWheel) {
				TextInfo info = mYears.get(pos);
				setYear(info.mIndex);
			}

			mSelDateTxt.setText(formatDate());
		}
	};

	private String formatDate() {
		return String.format("Date: %d/%02d/%02d", mCurYear, mCurMonth + 1, mCurDate);
	}

	private void setDate(int date) {
		if (date != mCurDate) {
			mCurDate = date;
		}
	}

	private void setYear(int year) {
		if (year != mCurYear) {
			mCurYear = year;
		}
	}

	private void setMonth(int month) {
		if (month != mCurMonth) {
			mCurMonth = month;

			Calendar calendar = Calendar.getInstance();
			int date = calendar.get(Calendar.DATE);
			prepareDayData(mCurYear, month, date);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wheel_date);
		
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		
		mSelDateTxt = (TextView) findViewById(R.id.sel_date);

		mYearWheel = (WheelView) findViewById(R.id.wheel_year);
		mMonthWheel = (WheelView) findViewById(R.id.wheel_month);
		mDateWheel = (WheelView) findViewById(R.id.wheel_date);

		btn_confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				intent.putExtra("birthday", mCurYear+"-"+(mCurMonth+1)+"-"+mCurDate);
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		
		mYearWheel.setOnEndFlingListener(mListener);
		mMonthWheel.setOnEndFlingListener(mListener);
		mDateWheel.setOnEndFlingListener(mListener);

		mYearWheel.setSoundEffectsEnabled(true);
		mMonthWheel.setSoundEffectsEnabled(true);
		mDateWheel.setSoundEffectsEnabled(true);

		mYearWheel.setAdapter(new WheelTextAdapter(this));
		mMonthWheel.setAdapter(new WheelTextAdapter(this));
		mDateWheel.setAdapter(new WheelTextAdapter(this));

		prepareData();

		mSelDateTxt.setText(formatDate());

		findViewById(R.id.btn_now).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DATE);
				int startYear = 2012;

				mYearWheel.setSelection(year - startYear, true);
				mMonthWheel.setSelection(month, true);
				mDateWheel.setSelection(day - 1, true);
			}
		});
	}

	private static final int[] DAYS_PER_MONTH = { 31, 28, 31, 30, 31, 31, 30, 31, 30, 31, 30, 31 };

	private static final String[] MONTH_NAME = { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };

	private boolean isLeapYear(int year) {
		return ((0 == year % 4) && (0 != year % 100) || (0 == year % 400));
	}

	private void prepareData() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int startYear = year-100;
		int endYear = year;

		mCurDate = day;
		mCurMonth = month;
		mCurYear = year;

		for (int i = 0; i < MONTH_NAME.length; ++i) {
			mMonths.add(new TextInfo(i, MONTH_NAME[i], (i == month)));
		}

		for (int i = startYear; i <= endYear; ++i) {
			mYears.add(new TextInfo(i, String.valueOf(i), (i == year)));
		}

		((WheelTextAdapter) mMonthWheel.getAdapter()).setData(mMonths);
		((WheelTextAdapter) mYearWheel.getAdapter()).setData(mYears);

		prepareDayData(year, month, day);

		mMonthWheel.setSelection(month);
		mYearWheel.setSelection(year - startYear);
		mDateWheel.setSelection(day - 1);
	}

	private void prepareDayData(int year, int month, int curDate) {
		mDates.clear();

		int days = DAYS_PER_MONTH[month];

		// The February.
		if (1 == month) {
			days = isLeapYear(year) ? 29 : 28;
		}

		for (int i = 1; i <= days; ++i) {
			mDates.add(new TextInfo(i, String.valueOf(i), (i == curDate)));
		}

		((WheelTextAdapter) mDateWheel.getAdapter()).setData(mDates);
	}

	protected class TextInfo {
		public TextInfo(int index, String text, boolean isSelected) {
			mIndex = index;
			mText = text;
			mIsSelected = isSelected;

			if (isSelected) {
				mColor = Color.BLUE;
			}
		}

		public int mIndex;
		public String mText;
		public boolean mIsSelected = false;
		public int mColor = Color.BLACK;
	}

	protected class WheelTextAdapter extends BaseAdapter {
		ArrayList<TextInfo> mData = null;
		int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
		int mHeight = 50;
		Context mContext = null;

		public WheelTextAdapter(Context context) {
			mContext = context;
			mHeight = (int) WheelViewUtils.pixelToDp(context, mHeight);
		}

		public void setData(ArrayList<TextInfo> data) {
			mData = data;
			this.notifyDataSetChanged();
		}

		public void setItemSize(int width, int height) {
			mWidth = width;
			mHeight = (int) WheelViewUtils.pixelToDp(mContext, height);
		}

		@Override
		public int getCount() {
			return (null != mData) ? mData.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = null;

			if (null == convertView) {
				convertView = new TextView(mContext);
				convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
				textView = (TextView) convertView;
				textView.setGravity(Gravity.CENTER);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				textView.setTextColor(Color.BLACK);
			}

			if (null == textView) {
				textView = (TextView) convertView;
			}

			TextInfo info = mData.get(position);
			textView.setText(info.mText);
			textView.setTextColor(info.mColor);

			return convertView;
		}
	}
}
