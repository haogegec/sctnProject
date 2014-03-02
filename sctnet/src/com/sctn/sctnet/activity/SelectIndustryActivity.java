package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.view.MyTextView;
import com.sctn.sctnet.view.PinnedHeaderListView;
import com.sctn.sctnet.view.PinnedHeaderListView.PinnedHeaderAdapter;

/**
 * 行业选择界面
 * 
 * @author 姜勇男
 * 
 */
public class SelectIndustryActivity extends BaicActivity {

	private PinnedHeaderListView listView;
	private List<ItemEntity> data;
	private View headerView;
	private PinnedAdapter pinnedAdapter;
	private List<Integer> list = new ArrayList<Integer>();// 存储map的key，删除的时候从list查出当前key（当前用户选择的checkbox的position是第几个view）

	private ImageView triangle;// 右侧箭头
	private LinearLayout already_selected;// 已选择的行业
	private RelativeLayout rl_layout;
	private TextView count;// 已选择的行业个数，最多同时能选5个行业
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_industry);
		setTitleBar(getString(R.string.select_industry), View.VISIBLE, View.VISIBLE);

		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {
		super.titleRightButton.setImageResource(R.drawable.queding);
		triangle = (ImageView) findViewById(R.id.triangle);
		rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
		// 这里之所以写一个类实现ClickListener，是因为下边already_selected判断之后，rl_layout要设置成不可点击。
		// 但是要写成内部类的话，setClickable()方法不起作用，你只要内部类实现，你点击的时候，它会自动把clickable设置成true，所以设置成不可点击是多此一举。
		rl_layout.setOnClickListener(new MyClickListener());
		already_selected = (LinearLayout) findViewById(R.id.already_selected);
		if(already_selected.getChildCount() == 0)rl_layout.setClickable(false);
		count = (TextView) findViewById(R.id.count);

		listView = (PinnedHeaderListView) findViewById(R.id.listview);
		data = createTestData();
		// * 创建新的HeaderView，即置顶的HeaderView
		headerView = getLayoutInflater().inflate(R.layout.pinned_header_listview_item_header, listView, false);
		listView.setPinnedHeader(headerView);
		pinnedAdapter = new PinnedAdapter(this, data);
		listView.setAdapter(pinnedAdapter);
		listView.setOnScrollListener(pinnedAdapter);
		pinnedAdapter.notifyDataSetChanged();
	}

	@Override
	protected void reigesterAllEvent() {
		
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = getIntent();
//				intent.putExtra("", "");
				setResult(RESULT_OK);
				finish();
			}
		});
		
//		rl_layout.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (already_selected.getVisibility() == View.VISIBLE) {// 如果是可见状态，则把它隐藏掉，同时把图片换成下箭头
//					already_selected.setVisibility(View.GONE);
//					triangle.setImageResource(R.drawable.triangle_down);
//				} else {
//					already_selected.setVisibility(View.VISIBLE);
//					triangle.setImageResource(R.drawable.triangle_up);
//				}
//			}
//		});
	}
	
	public class MyClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if (already_selected.getVisibility() == View.VISIBLE) {// 如果是可见状态，则把它隐藏掉，同时把图片换成下箭头
				already_selected.setVisibility(View.GONE);
				triangle.setImageResource(R.drawable.triangle_down);
			} else {
				already_selected.setVisibility(View.VISIBLE);
				triangle.setImageResource(R.drawable.triangle_up);
			}
		}
	}

	private List<ItemEntity> createTestData() {

		List<ItemEntity> data = new ArrayList<ItemEntity>();

		ItemEntity itemEntity1 = new ItemEntity("IT/互联网", "计算机软件");
		ItemEntity itemEntity2 = new ItemEntity("IT/互联网", "电子商务");
		ItemEntity itemEntity3 = new ItemEntity("金融业", "基金/证券");
		ItemEntity itemEntity4 = new ItemEntity("金融业", "保险/银行");
		ItemEntity itemEntity5 = new ItemEntity("房地产/建筑", "建材/工程");
		ItemEntity itemEntity6 = new ItemEntity("房地产/建筑", "装饰装潢");
		ItemEntity itemEntity7 = new ItemEntity("房地产/建筑", "商业中心");
		ItemEntity itemEntity8 = new ItemEntity("商业服务", "广告");
		ItemEntity itemEntity9 = new ItemEntity("商业服务", "会展");
		ItemEntity itemEntity10 = new ItemEntity("商业服务", "公关");
		ItemEntity itemEntity11 = new ItemEntity("贸易/批发", "零售");
		ItemEntity itemEntity12 = new ItemEntity("贸易/批发", "耐用消费品");
		ItemEntity itemEntity13 = new ItemEntity("文体教育", "教育/培训");
		ItemEntity itemEntity14 = new ItemEntity("文体教育", "工艺美术/奢侈品");
		ItemEntity itemEntity15 = new ItemEntity("生产加工/制造", "汽车/摩托车");
		ItemEntity itemEntity16 = new ItemEntity("生产加工/制造", "机电设备");
		ItemEntity itemEntity17 = new ItemEntity("生产加工/制造", "仪器表及工业自动化");
		ItemEntity itemEntity18 = new ItemEntity("生产加工/制造", "印刷/造纸");
		ItemEntity itemEntity19 = new ItemEntity("生产加工/制造", "医疗设备/器械");
		ItemEntity itemEntity20 = new ItemEntity("生产加工/制造", "航天设备");

		data.add(itemEntity1);
		data.add(itemEntity2);
		data.add(itemEntity3);
		data.add(itemEntity4);
		data.add(itemEntity5);
		data.add(itemEntity6);
		data.add(itemEntity7);
		data.add(itemEntity8);
		data.add(itemEntity9);
		data.add(itemEntity10);
		data.add(itemEntity11);
		data.add(itemEntity12);
		data.add(itemEntity13);
		data.add(itemEntity14);
		data.add(itemEntity15);
		data.add(itemEntity16);
		data.add(itemEntity17);
		data.add(itemEntity18);
		data.add(itemEntity19);
		data.add(itemEntity20);

		return data;

	}

	public class ItemEntity {
		private String mTitle;
		private String mContent;

		public ItemEntity(String pTitle, String pContent) {
			mTitle = pTitle;
			mContent = pContent;
		}

		public String getTitle() {
			return mTitle;
		}

		public String getContent() {
			return mContent;
		}
	}

	class PinnedAdapter extends BaseAdapter implements OnScrollListener, PinnedHeaderAdapter {

		// private static final String TAG =
		// PinnedAdapter.class.getSimpleName();

		private Context mContext;
		private List<ItemEntity> mData;
		private LayoutInflater mLayoutInflater;
		Map<Integer, Boolean> checkBoxState = new HashMap<Integer, Boolean>();// 记录checkbox的状态

		public PinnedAdapter(Context pContext, List<ItemEntity> pData) {
			mContext = pContext;
			mData = pData;

			mLayoutInflater = LayoutInflater.from(mContext);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = mLayoutInflater.inflate(R.layout.pinned_header_listview_item, null);

				viewHolder = new ViewHolder();
				viewHolder.title = (TextView) convertView.findViewById(R.id.title);
				viewHolder.content = (TextView) convertView.findViewById(R.id.content);
				viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// 获取数据
			ItemEntity itemEntity = (ItemEntity) getItem(position);
			viewHolder.content.setText(itemEntity.getContent());

			if (needTitle(position)) {
				// 显示标题并设置内容
				viewHolder.title.setText(itemEntity.getTitle());
				viewHolder.title.setVisibility(View.VISIBLE);
			} else {
				// 内容项隐藏标题
				viewHolder.title.setVisibility(View.GONE);
			}
			
//			// checkbox 点击之后，先触发change，再触发click
//			viewHolder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//					if (isChecked) {
//						if(checkBoxState.size() >= 5){
//							new AlertDialog.Builder(SelectIndustryActivity.this)
//							.setMessage("最多可以选择5个行业，不要太贪心哦")
//							.setPositiveButton("确定", null)
//							.show();
//							buttonView.setChecked(false);
//							checkBoxState.put(position,false);
//						} else {
//							checkBoxState.put(position, isChecked);
//						}
//					} else {
//						checkBoxState.remove(position);
//					}
//				}
//			});
			
			// 这里不用onCheckedChangeListener的原因：因为要判断checkBoxState.size() >= 5，如果 true，则提示弹出框。
			// 但是：listview滚动的时候也会执行onChanged方法(因为每显示一个view，它都会执行adapter的getView()方法)，所以滚动的时候如果checkBoxState.size() >= 5，它仍然会提示弹出框。
			// 所以只好用click事件，弃用checked事件
			viewHolder.checkbox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(((CheckBox)v).isChecked()){
						if(checkBoxState.size() >= 5){
							new AlertDialog.Builder(SelectIndustryActivity.this)
							.setMessage("最多可以选择5个行业，不要太贪心哦")
							.setPositiveButton("确定", null)
							.show();
							((CheckBox)v).setChecked(false);
						} else {
							checkBoxState.put(position, ((CheckBox)v).isChecked());
							list.add(position);
							count.setText(checkBoxState.size()+"/5");
							
							TextView tv_already_selected = new TextView(SelectIndustryActivity.this);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
							params.setMargins(0, 5, 0, 0);
							tv_already_selected.setLayoutParams(params);
							tv_already_selected.setBackgroundColor(getResources().getColor(R.color.white));
							tv_already_selected.setCompoundDrawablePadding(10);
							tv_already_selected.setPadding(10, 10, 10, 10);
							tv_already_selected.setText(data.get(position).getContent());
							tv_already_selected.setTextSize(16);
							tv_already_selected.setTextColor(getResources().getColor(R.color.lightBlack));
//							Drawable drawableRight = getResources().getDrawable(R.drawable.delete);
//							//  动态添加左右图片
//							drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
//							tv_already_selected.setCompoundDrawables(null, null, drawableRight, null);// 左，上，右，下
							already_selected.addView(tv_already_selected,i);
							i++;
							rl_layout.setClickable(true);
						}
					}else {
						checkBoxState.remove(position);
						
						int index = -1;
						// 循环结束后，index的值就是当前position在已选行业当中的第几个了
						for(int i=0; i<list.size(); i++){
							if(position==list.get(i)){
								index = i;
							}
						}
						list.remove(index);
						count.setText(checkBoxState.size()+"/5");
						already_selected.removeViewAt(index);
						i--;
						if(i==0){
							already_selected.setVisibility(View.GONE);
							triangle.setImageResource(R.drawable.triangle_down);
							rl_layout.setClickable(false);
						}
					}
				}});

			viewHolder.checkbox.setChecked(checkBoxState.get(position) == null ? false : true);

			return convertView;
		}

		@Override
		public int getCount() {
			if (null != mData) {
				return mData.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (null != mData && position < getCount()) {
				return mData.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			if (view instanceof PinnedHeaderListView) {
				((PinnedHeaderListView) view).controlPinnedHeader(firstVisibleItem);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public int getPinnedHeaderState(int position) {
			if (getCount() == 0 || position < 0) {
				return PinnedHeaderAdapter.PINNED_HEADER_GONE;
			}

			if (isMove(position) == true) {
				return PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
			}

			return PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
		}

		@Override
		public void configurePinnedHeader(View headerView, int position, int alpaha) {
			// 设置标题的内容
			ItemEntity itemEntity = (ItemEntity) getItem(position);
			String headerValue = itemEntity.getTitle();

			Log.i("asdf", "header = " + headerValue);

			if (!TextUtils.isEmpty(headerValue)) {
				TextView headerTextView = (TextView) headerView.findViewById(R.id.header);
				headerTextView.setText(headerValue);
			}

		}

		/**
		 * 判断是否需要显示标题
		 * 
		 * @param position
		 * @return
		 */
		private boolean needTitle(int position) {
			// 第一个肯定是分类
			if (position == 0) {
				return true;
			}

			// 异常处理
			if (position < 0) {
				return false;
			}

			// 当前 // 上一个
			ItemEntity currentEntity = (ItemEntity) getItem(position);
			ItemEntity previousEntity = (ItemEntity) getItem(position - 1);
			if (null == currentEntity || null == previousEntity) {
				return false;
			}

			String currentTitle = currentEntity.getTitle();
			String previousTitle = previousEntity.getTitle();
			if (null == previousTitle || null == currentTitle) {
				return false;
			}

			// 当前item分类名和上一个item分类名不同，则表示两item属于不同分类
			if (currentTitle.equals(previousTitle)) {
				return false;
			}

			return true;
		}

		private boolean isMove(int position) {
			// 获取当前与下一项
			ItemEntity currentEntity = (ItemEntity) getItem(position);
			ItemEntity nextEntity = (ItemEntity) getItem(position + 1);
			if (null == currentEntity || null == nextEntity) {
				return false;
			}

			// 获取两项header内容
			String currentTitle = currentEntity.getTitle();
			String nextTitle = nextEntity.getTitle();
			if (null == currentTitle || null == nextTitle) {
				return false;
			}

			// 当前不等于下一项header，当前项需要移动了
			if (!currentTitle.equals(nextTitle)) {
				return true;
			}

			return false;
		}

		private class ViewHolder {
			TextView title;
			TextView content;
			CheckBox checkbox;
		}
	}

}