package com.sctn.sctnet.activity;


import com.sctn.sctnet.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class SearchResultActivity  extends BaicActivity{

	private GridView mGridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.search_result_activity);
		setTitleBar(getString(R.string.jobsearchResult), View.GONE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.login_btn_bg);
		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		
		   mGridView=(GridView) findViewById(R.id.GridView_work_order_handle_toolbar);
	       GridViewAdapter adapter=new GridViewAdapter(SearchResultActivity.this);  
	       mGridView.setAdapter(adapter); 
	       mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {

				case 0:
					// 收藏职位
					System.out.println("11111111收藏职位");
					break;
				case 1:
					// 申请职位
					System.out.println("11111111222222222申请职位");
					break;
				case 2:
					// 明细
					System.out.println("11111111333333明细");
					break;
				}
			}
		});
		
	}

	@Override
	protected void reigesterAllEvent() {
		
	}

	// GridViewAdapter 适配器
		public class GridViewAdapter extends BaseAdapter {

			Context mContext;
			LayoutInflater mLayoutInflater;

			public GridViewAdapter(Context context) {
				mContext = context;
				mLayoutInflater = LayoutInflater.from(context);
			}

			@Override
			public int getCount() {
				return 3;
			}

			@Override
			public Object getItem(int position) {
				
				return position;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = mLayoutInflater.inflate(
							R.layout.search_result_handle_toolbar_item, null);
				}
				
				ImageView imageView = (ImageView) convertView.findViewById(R.id.toolbar_item_image);
			
				if(position==0) {
					imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.wo_toolbar_action_pressed));
				}else if(position==1) {
						imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.wo_toolbar_busi_normal));
				}else if(position==2) {
						imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.wo_toolbar_fetch_pressed));
				}
				return convertView;
			 }
		   }

	
}
