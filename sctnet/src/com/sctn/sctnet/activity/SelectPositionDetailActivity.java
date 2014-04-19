package com.sctn.sctnet.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.alibaba.fastjson.JSONArray;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.activity.SelectIndustryActivity.ItemEntity;
import com.sctn.sctnet.activity.SelectIndustryActivity.MyClickListener;
import com.sctn.sctnet.activity.SelectIndustryActivity.PinnedAdapter;
import com.sctn.sctnet.activity.SelectPositionActivity.MyAdapter;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.view.CustomDialog;
import com.sctn.sctnet.view.PinnedHeaderListView;
import com.sctn.sctnet.view.PinnedHeaderListView.PinnedHeaderAdapter;

public class SelectPositionDetailActivity extends BaicActivity{
	
	private PinnedHeaderListView listView;
	private List<ItemEntity> data = new ArrayList<ItemEntity>();
	private View headerView;
	private PinnedAdapter pinnedAdapter;
	private List<Integer> list = new ArrayList<Integer>();// 存储map的key，删除的时候从list查出当前key（当前用户选择的checkbox的position是第几个view）

	private ImageView triangle;// 右侧箭头
	private LinearLayout already_selected;// 已选择的行业
	private RelativeLayout rl_layout;
	private TextView count;// 已选择的行业个数，最多同时能选5个行业
	int i = 0;
	Map<Integer, Boolean> checkBoxState = new HashMap<Integer, Boolean>();// 记录checkbox的状态
	// 标记当前职位大类里选择的是哪几个小类
	private boolean[] tmp;
	
	private String industryContent;
	private String industryId;
	//服务端返回结果
	private String result;
	private com.alibaba.fastjson.JSONObject responseJsonObject = null;// 返回结果存放在该json对象中
	
	private List backList = new ArrayList();//回传给职位搜索页面的数据，存的是：详细职业的 id和name

	private int page = 1;
	private int total;// 总条数
	private int pageSize = Constant.PageSize;
	private int pageCount;// 一次可以显示的条数（=pageSize或者小于）
	private View footViewBar;// 下滑加载条
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_industry);
		setTitleBar("选择详细职业", View.VISIBLE, View.VISIBLE);

		initIntent();
		initAllView();
		reigesterAllEvent();
		requestDataThread(0);
	}
	
	private void initIntent(){
		Intent intent = getIntent();
		if(intent.getSerializableExtra("checkBoxState") != null){
			checkBoxState = (HashMap<Integer,Boolean>)intent.getSerializableExtra("checkBoxState");
		}
	}
	
	/**
	 * 请求数据线程
	 * 
	 */
	private void requestDataThread(final int i) {
		
		if (i == 0) {
			showProcessDialog(true);
		}
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						requestData(i);
					}
				});
		mThread.start();
	}
	private void requestData(int i){
		
		String url = "appCmbShow.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", Constant.POSITION_DETAIL_TYPE+""));
		params.add(new BasicNameValuePair("key", industryId));
		params.add(new BasicNameValuePair("page", page+""));
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			SelectPositionDetailActivity.this.sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			SelectPositionDetailActivity.this.sendExceptionMsg(result);
			return;
		}
		Message m=new Message();
		responseJsonObject = com.alibaba.fastjson.JSONObject
				.parseObject(result);
		if(responseJsonObject.get("resultcode").toString().equals("0")) {
			
            JSONArray json = responseJsonObject.getJSONArray("result");
					
			total = (Integer) responseJsonObject.get("resultCount");// 总数
			if (json.size() > 15) {
				pageCount = 15;
			} else {
				pageCount = json.size();
			}
            for(int j=0;j<pageCount;j++){
				
				String key = json.getJSONObject(j).getString("key");
				String value = json.getJSONObject(j).getString("value");
				ItemEntity itemEntity1 = new ItemEntity(industryContent,value,key);
				data.add(itemEntity1);
			}
            if (i == 0) {
				m.what = 0;

			} else {
				m.what = 1;
			}
		}else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			SelectPositionDetailActivity.this.sendExceptionMsg(err);
		}
				
		handler.sendMessage(m);
 }
   
// 处理线程发送的消息
		private Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					initCheckBox();
					initUI();
					closeProcessDialog();
					break;
				case 1:
					updateUI();
					break;

				}
				
			}
		};
		
		private void initCheckBox(){
			tmp = new boolean[total];
			for(Map.Entry<Integer, Boolean> entry: checkBoxState.entrySet()) {
				tmp[entry.getKey()] = entry.getValue();
			}

			count.setText(checkBoxState.size() + "/5");
			for(int j=0; j<tmp.length; j++){
				if(tmp[j]){
					TextView tv_already_selected = new TextView(SelectPositionDetailActivity.this);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 5, 0, 0);
					tv_already_selected.setLayoutParams(params);
					tv_already_selected.setBackgroundColor(getResources().getColor(R.color.white));
					tv_already_selected.setCompoundDrawablePadding(10);
					tv_already_selected.setPadding(10, 10, 10, 10);
					tv_already_selected.setText(data.get(j).getContent());
					tv_already_selected.setTextSize(16);
					tv_already_selected.setTextColor(getResources().getColor(R.color.lightBlack));
					already_selected.addView(tv_already_selected, i);
					i++;
					rl_layout.setClickable(true);
				}
			}
		
		}

		private void initUI(){
			if (total > pageSize * page) {
				listView.addFooterView(footViewBar);// 添加list底部更多按钮
			}
			listView.setAdapter(pinnedAdapter);
			pinnedAdapter.notifyDataSetChanged();
		}

		/**
		 * 滑动list请求数据更新页面
		 */
		private void updateUI() {

			if (total <= pageSize * page) {
				listView.removeFooterView(footViewBar);// 添加list底部更多按钮
			}
			pinnedAdapter.notifyDataSetChanged();

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
		
		Intent intent = this.getIntent();
		industryContent = intent.getStringExtra("content");
		industryId = intent.getStringExtra("id");
		footViewBar = View.inflate(SelectPositionDetailActivity.this, R.layout.foot_view_loading, null);
		listView = (PinnedHeaderListView) findViewById(R.id.listview);

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
				Intent intent = getIntent();
				ArrayList list = new ArrayList();
				list.add(backList);
				intent.putExtra("list", list);
//				if(checkBoxState.size()==0) intent.putExtra("industryId", "");
				intent.putExtra("industryId", industryId);
				intent.putExtra("checkBoxState", (Serializable)checkBoxState);
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		
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

	public class ItemEntity {
		private String mTitle;
		private String mContent;
		private String mContentId;

		public ItemEntity(String pTitle, String pContent,String pContentId) {
			mTitle = pTitle;
			mContent = pContent;
			mContentId = pContentId;
		}

		public String getTitle() {
			return mTitle;
		}

		public String getContent() {
			return mContent;
		}

		public String getmContentId() {
			return mContentId;
		}

		
	}

	class PinnedAdapter extends BaseAdapter implements OnScrollListener, PinnedHeaderAdapter {

		// private static final String TAG =
		// PinnedAdapter.class.getSimpleName();

		private Context mContext;
		private List<ItemEntity> mData;
		private LayoutInflater mLayoutInflater;
		

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
			
			// 初始化从JobSearchActivity页面传过来已选择的checkbox
			if(tmp.length != 0){
				if (tmp[position]) {
					list.add(position);
					HashMap map = new HashMap();
					map.put("id", data.get(position).getmContentId());
					map.put("value", data.get(position).getContent());
					backList.add(map);
					tmp[position] = false;
				}
			}
			

			// 这里不用onCheckedChangeListener的原因：因为要判断checkBoxState.size() >= 5，如果 true，则提示弹出框。
			// 但是：listview滚动的时候也会执行onChanged方法(因为每显示一个view，它都会执行adapter的getView()方法)，所以滚动的时候如果checkBoxState.size() >= 5，它仍然会提示弹出框。
			// 所以只好用click事件，弃用checked事件
			viewHolder.checkbox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(((CheckBox)v).isChecked()){
						if(checkBoxState.size() >= 5){
					
							final CustomDialog dialog = new CustomDialog(SelectPositionDetailActivity.this, R.style.CustomDialog);
//							dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
						//	dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
							dialog.setTitle("友情提示");
							dialog.setMessage("最多可以选择5个职业，不要太贪心哦");
							
							dialog.setOnPositiveListener("确定", new OnClickListener(){

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
								
							});
							dialog.show();
							((CheckBox)v).setChecked(false);
						} else {
							checkBoxState.put(position, ((CheckBox)v).isChecked());
							list.add(position);
							count.setText(checkBoxState.size()+"/5");
							HashMap map = new HashMap();
							map.put("id", data.get(position).getmContentId());
							map.put("value", data.get(position).getContent());
							backList.add(map);
							TextView tv_already_selected = new TextView(SelectPositionDetailActivity.this);
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
						
						for(int i=0; i<backList.size(); i++){
							Map<String,String> map = (Map<String,String>)backList.get(i);
							if(map.get("id").equals(data.get(position).getmContentId())){
								backList.remove(map);
							}
						}
						
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
			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				page++;
				requestDataThread(1);// 滑动list请求数据
			}
			
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
