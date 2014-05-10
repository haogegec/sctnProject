package com.sctn.sctnet.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.activity.SelectPositionDetailActivity.MyClickListener;
import com.sctn.sctnet.contants.Constant;

public class SelectPositionActivity extends BaicActivity {

	private ListView lv_area;
	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
	
	// 已选择栏
	private RelativeLayout layout;
	private ImageView triangle;
	private TextView count;
	private LinearLayout already_selected;
	
	//服务端返回结果
	private String result;
	private com.alibaba.fastjson.JSONObject responseJsonObject = null;// 返回结果存放在该json对象中
	private List<Map<String,String>> backPositionType;

	private int page = 1;
	private int total;// 总条数
	private int pageSize = Constant.PageSize;
	private int pageCount;// 一次可以显示的条数（=pageSize或者小于）
	private View footViewBar;// 下滑加载条
	private MyAdapter myAdapter;

	private String[] positionIds = { "80010000", "80020000", "80030000", "80040000", "80050000", "80060000", "80070000", "80080000", "80100000", "80110000", "80120000", "80130000", "80140000", "80150000", "80160000", "80170000", "80180000", "80190000", "80200000", "80210000", "80220000", "80230000", };
	private String[] positions = { "经营·管理·策划类", "财务·金融·审计类", "销售·业务类", "市场·公关·广告类", "设计类", "行政·人事类", "文职类", "工业·生产·制造类", "计算机·网络类", "电子·电气·通讯类", "机械类", "房地产·建筑类", "教育·法律类", "文化·艺术·体育类", "化工·生物·环保类", "轻工·食品·纺织类", "卫生医疗·美容保健类", "能源·电力·水利类", "商业·贸易·物流类", "客服·后勤·服务业类", "技工类", "其他类", };
	
	private int alreadySelected = 0 ;// 已选择的总职能（小类）数
	
	// 存储已选择的职能的大类ID
	private List<String> industries = new ArrayList<String>();
	
	private Map<String,Boolean> state = new HashMap<String,Boolean>();// 标识哪几个大类的职位已被选中
	private Map<Integer, Boolean> checkBoxState = null;// 标识哪几个小类的职位已被选中
	// key是大类职位的ID，value是checkBoxState
	private Map<String,Map<Integer,Boolean>> positionMap = new HashMap<String,Map<Integer,Boolean>>();
	
	// Map存的是：已选择的小类职位的id和职位名称
	private List<Map<String,String>> positionList = new ArrayList<Map<String,String>>();
	
	private Map<String,List<Map<String,String>>> positionListMap = new HashMap<String,List<Map<String,String>>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_position_listview);
		super.setTitleBar("选择职能", View.VISIBLE, View.VISIBLE);

		initIntent();
		initAllView();
		reigesterAllEvent();
		//		requestDataThread(0);

		initData();

	}

	private void initIntent(){
		Intent intent = getIntent();
		state = (Map<String, Boolean>) intent.getSerializableExtra("state");
		if(state==null){
			state = new HashMap<String,Boolean>();
		}
		checkBoxState = (Map<Integer, Boolean>) intent.getSerializableExtra("checkBoxState");
		if(checkBoxState == null){
			checkBoxState = new HashMap<Integer, Boolean>();
		}
		positionMap = (Map<String, Map<Integer, Boolean>>) intent.getSerializableExtra("positionMap");
		if(positionMap == null){
			positionMap = new HashMap<String, Map<Integer,Boolean>>();
		}
//		backPositionType = (List<Map>) intent.getSerializableExtra("backPositionType");
//		if(backPositionType == null){
//			backPositionType = new ArrayList<Map>();
//		}
		positionList = (List<Map<String, String>>) intent.getSerializableExtra("positionList");
		if(positionList == null){
			positionList = new ArrayList<Map<String,String>>();
		}
		alreadySelected = positionList.size();
		
		positionListMap = (HashMap<String,List<Map<String,String>>>) intent.getSerializableExtra("positionListMap");
		if(positionListMap == null){
			positionListMap = new HashMap<String,List<Map<String,String>>>();
		}
	}
	
	private void initData() {
		for (int j = 0; j < positionIds.length; j++) {
			Map<String, String> map = new HashMap<String, String>();
			String key = positionIds[j];
			String value = positions[j];
			map.put("id", key);
			map.put("value", value);
			listItems.add(map);
		}
		
		if(alreadySelected > 0){
			already_selected.removeAllViews();
			int i = 0;
			for (Map<String, String> map : positionList) {
				TextView tv_already_selected = new TextView(SelectPositionActivity.this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params.setMargins(0, 5, 0, 0);
				tv_already_selected.setLayoutParams(params);
				tv_already_selected.setBackgroundColor(getResources().getColor(R.color.white));
				tv_already_selected.setCompoundDrawablePadding(10);
				tv_already_selected.setPadding(10, 10, 10, 10);
				tv_already_selected.setText(map.get("value"));
				tv_already_selected.setTextSize(16);
				tv_already_selected.setTextColor(getResources().getColor(R.color.lightBlack));
				already_selected.addView(tv_already_selected, i);
				i++;
			}
			layout.setClickable(true);
			count.setText(positionList.size() + "/5");
			alreadySelected = positionList.size();
		}
		
		
		initUI();
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

	private void requestData(int i) {

		String url = "appCmbShow.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", Constant.POSITION_TYPE + ""));
		params.add(new BasicNameValuePair("key", "1"));
		params.add(new BasicNameValuePair("page", page + ""));
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			SelectPositionActivity.this.sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			SelectPositionActivity.this.sendExceptionMsg(result);
			return;
		}
		Message m = new Message();
		responseJsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
		if (responseJsonObject.get("resultcode").toString().equals("0")) {

			JSONArray json = responseJsonObject.getJSONArray("result");

			total = (Integer) responseJsonObject.get("resultCount");// 总数
			if (json.size() > 15) {
				pageCount = 15;
			} else {
				pageCount = json.size();
			}
			for (int j = 0; j < pageCount; j++) {

				String key = json.getJSONObject(j).getString("key");
				String value = json.getJSONObject(j).getString("value");
				Map<String, String> map = new HashMap<String, String>();

				map.put("id", key);
				map.put("value", value);
				listItems.add(map);
			}
			if (i == 0) {
				m.what = 0;

			} else {
				m.what = 1;
			}
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			SelectPositionActivity.this.sendExceptionMsg(err);
		}

		handler.sendMessage(m);
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				initUI();
				closeProcessDialog();
				break;
			case 1:
				updateUI();

				break;

			}

		}
	};

	@Override
	protected void initAllView() {
		super.titleRightButton.setImageResource(R.drawable.queding);
		layout = (RelativeLayout) findViewById(R.id.rl_layout);
		layout.setOnClickListener(new MyClickListener());
		already_selected = (LinearLayout) findViewById(R.id.already_selected);
		if(already_selected.getChildCount() == 0)layout.setClickable(false);
		triangle = (ImageView) findViewById(R.id.triangle);
		count = (TextView) findViewById(R.id.count);
		already_selected = (LinearLayout) findViewById(R.id.already_selected);
		lv_area = (ListView) findViewById(R.id.lv_area);
//		footViewBar = View.inflate(SelectPositionActivity.this, R.layout.foot_view_loading, null);
		lv_area.setAdapter(new MyAdapter(this, listItems, R.layout.select_area_item));
//		lv_area.setOnScrollListener(listener);
	}

	@Override
	protected void reigesterAllEvent() {

		lv_area.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(SelectPositionActivity.this, SelectPositionDetailActivity.class);
				intent.putExtra("content", listItems.get(position).get("value"));
				intent.putExtra("id", listItems.get(position).get("id"));
				intent.putExtra("checkBoxState", (Serializable)positionMap.get(listItems.get(position).get("id")));
				intent.putExtra("alreadySelected", alreadySelected);
				startActivityForResult(intent, Constant.POSITION_TYPE);
			}

		});
		
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				ArrayList list = new ArrayList();
				list.add(positionList);
				intent.putExtra("list", list);
				intent.putExtra("state", (Serializable)state);//
				intent.putExtra("checkBoxState", (Serializable)checkBoxState);//
				intent.putExtra("positionMap", (Serializable)positionMap);//
//				intent.putExtra("backPositionType", (Serializable)backPositionType);//
				intent.putExtra("positionList", (Serializable)positionList);
				intent.putExtra("positionListMap", (Serializable)positionListMap);
				
				
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

	//初始化城市列表
	protected void initUI() {

//		if (total > pageSize * page) {
//			lv_area.addFooterView(footViewBar);// 添加list底部更多按钮
//		}
		myAdapter = new MyAdapter(this, listItems, R.layout.select_area_item);
		lv_area.setAdapter(myAdapter);

	}

	/**
	 * 滑动list请求数据更新页面
	 */
	private void updateUI() {

		if (total <= pageSize * page) {
			lv_area.removeFooterView(footViewBar);// 添加list底部更多按钮
		}
		myAdapter.notifyDataSetChanged();

	}

	// 自定义适配器
	class MyAdapter extends BaseAdapter {
		private Context mContext;// 上下文对象
		List<Map<String, String>> list;// 这是要绑定的数据
		private int resource;// 这是要绑定的 item 布局文件
		private LayoutInflater inflater;// 布局填充器，Android系统内置的

		public MyAdapter(Context context, List<Map<String, String>> list, int resource) {
			this.mContext = context;
			this.list = list;
			this.resource = resource;
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 布局填充服务
		}

		@Override
		public int getCount() {//数据的总数量
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv_position = null;
			ImageView iv_check = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				tv_position = (TextView) convertView.findViewById(R.id.area);
				iv_check = (ImageView) convertView.findViewById(R.id.image);
				
				ViewCache viewCache = new ViewCache();
				viewCache.tv_position = tv_position;
				viewCache.iv_check = iv_check;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				tv_position = viewCache.tv_position;
				iv_check = viewCache.iv_check;
			}
			
			if(backPositionType == null || backPositionType.size() == 0){
				iv_check.setVisibility(View.INVISIBLE);
			} else {
				for(int i=0; i<industries.size(); i++){
					if(industries.get(i).equals(positionIds[position])){
						iv_check.setVisibility(View.VISIBLE);
					}
				}
			}

			
			tv_position.setText(list.get(position).get("value"));
			iv_check.setVisibility(state.get(positionIds[position])==null?View.INVISIBLE:View.VISIBLE);
			
			
			return convertView;
		}

	}

	private final class ViewCache {
		public TextView tv_position;// 职务;
		public ImageView iv_check;// 标记已选择的职务
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case Constant.POSITION_TYPE: {
				backPositionType = (List<Map<String, String>>) ((List) data.getSerializableExtra("list")).get(0);
				checkBoxState = (HashMap<Integer, Boolean>) data.getSerializableExtra("checkBoxState");
				String industryId = data.getStringExtra("industryId");

				List<String> tmpIndustries = new ArrayList<String>();// 不能在对一个List进行遍历的时候将其中的元素删除或者增加掉，所以临时创建一个list
				for (String s : industries) {
					tmpIndustries.add(s);
				}

				int currentJobPosition = positionListMap.get(industryId) == null ? 0 : positionListMap.get(industryId).size();
				if (checkBoxState.size() > 0 && !"".equals(industryId) && backPositionType.size() >= currentJobPosition) {// 或者用 backIndustryType.size() == 0 来判断,0表示没选

					if (industries.size() == 0) {
						industries.add(industryId);
						state.put(industryId, true);
						positionMap.put(industryId, checkBoxState);
						for (Map<String, String> map : backPositionType) {
							positionList.add(map);

						}
					} else {
						for (int i = 0; i < tmpIndustries.size(); i++) {
							String id = tmpIndustries.get(i);
							if (!id.equals(industryId)) {
								industries.add(industryId);
								state.put(industryId, true);
								positionMap.put(industryId, checkBoxState);
								for (Map<String, String> map : backPositionType) {
									positionList.add(map);
								}
								break;
							} else {
								positionMap.put(industryId, checkBoxState);
							}
						}
					}
					positionListMap.put(industryId, backPositionType);
					myAdapter.notifyDataSetChanged();
				} else {//
					List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();

					List<Map<String, String>> list = positionListMap.get(industryId);
					if (list != null && list.size() > 0) {
						for (Map map : list) {
							tempList.add(map);
						}
					}
						
					if (backPositionType.size() == 0) {

						List<String> tmp = new ArrayList<String>();
						for (String s : industries) {
							tmp.add(s);
						}

						for (String s : tmp) {
							if (industryId.equals(s)) {
								industries.remove(s);
							}
						}
						state.remove(industryId);
					}
					for (int i = 0; i < tempList.size(); i++) {
						Map<String, String> tempMap = tempList.get(i);
						boolean flag = false;
						List<String> deleteList = new ArrayList<String>();
						for (Map map : backPositionType) {
							if (tempMap.get("id").equals(map.get("id"))) {
								flag = true;
								break;
							}
						}
						if (!flag) {
							deleteList.add(tempList.get(i).get("id"));
							int[] position = new int[deleteList.size()];// 标记当前要删的ID在positionList里的索引值
							for (int j = 0; j < deleteList.size(); j++) {
								String id = deleteList.get(j);
								if(positionList.size() > 0){
									for(int k=0; k<positionList.size(); k++){
										if(id.equals(positionList.get(k).get("id"))){
											position[j] = k;
										}
									}
									
								}
							}
							for(int l=0; l<position.length; l++){
								positionList.remove(position[l]);
							}
							positionListMap.remove(industryId);
							if (backPositionType.size() != 0) {
								positionListMap.put(industryId, backPositionType);
							}
						}
					}
					positionMap.remove(industryId);
					positionMap.put(industryId, checkBoxState);

					myAdapter.notifyDataSetChanged();
				}

				if (positionList.size() > 0) {
					already_selected.removeAllViews();
					int i = 0;
					for (Map<String, String> map : positionList) {
						TextView tv_already_selected = new TextView(SelectPositionActivity.this);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						params.setMargins(0, 5, 0, 0);
						tv_already_selected.setLayoutParams(params);
						tv_already_selected.setBackgroundColor(getResources().getColor(R.color.white));
						tv_already_selected.setCompoundDrawablePadding(10);
						tv_already_selected.setPadding(10, 10, 10, 10);
						tv_already_selected.setText(map.get("value"));
						tv_already_selected.setTextSize(16);
						tv_already_selected.setTextColor(getResources().getColor(R.color.lightBlack));
						already_selected.addView(tv_already_selected, i);
						i++;
					}
					layout.setClickable(true);
					count.setText(positionList.size() + "/5");
					alreadySelected = positionList.size();
				} else {
					already_selected.removeAllViews();
					layout.setClickable(false);
					count.setText("0/5");
					triangle.setImageResource(R.drawable.triangle_down);
					alreadySelected = 0;
				}

				break;
			}

			}
		}
	}

	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				page++;
				requestDataThread(1);// 滑动list请求数据
			}

		}
	};
}
