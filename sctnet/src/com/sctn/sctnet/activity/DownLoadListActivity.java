package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.activity.JobListActivity.MyAdapter;
import com.sctn.sctnet.contants.Constant;
/**
 * 相关资料下载
 * @author xueweiwei
 *
 */
public class DownLoadListActivity extends BaicActivity{
	
	private MyAdapter downLoadListAdapter;
	private ListView downLoadListView;
	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	
	private View footViewBar;// 下滑加载条
	private int count;// 一次可以显示的条数（=pageSize或者小于）
	// 请求数据
	private int pageNo = 1;
	private int pageSize = Constant.PageSize;
	private String cid;// 大栏目id
	private String url;
	// 返回数据
	private int total;// 总条数
	private String result;// 服务端返回的json字符串
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_list_more_activity);
		setTitleBar(getString(R.string.downLoadListActivityTitle), View.VISIBLE, View.GONE);
		initAllView();
		reigesterAllEvent();
		requestDataThread(0);// 第一次请求数据
	}
	

	@Override
	protected void initAllView() {
		

		downLoadListView = (ListView) findViewById(R.id.information_list);
		footViewBar = View.inflate(DownLoadListActivity.this, R.layout.foot_view_loading, null);
		downLoadListAdapter = new MyAdapter(this, items, R.layout.download_list_item);
		downLoadListView.setAdapter(downLoadListAdapter);
		downLoadListView.setOnScrollListener(listener);
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 请求数据线程
	 * 
	 * @param i判断是哪次请求数据
	 *            ，如果第一次请求则为0，如果是滑动list请求数据则为1
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

		String url = "appDownload.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

			params.add(new BasicNameValuePair("pageNo", pageNo + ""));
			
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				DownLoadListActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {
				result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
				DownLoadListActivity.this.sendExceptionMsg(result);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONArray resultJsonArray = responseJsonObject
						.getJSONArray("result");
				if(resultJsonArray==null||resultJsonArray.length()==0){
					String err = StringUtil.getAppException4MOS("没有您要搜索的结果");
					DownLoadListActivity.this.sendExceptionMsg(err);
					return;
				}
				total = responseJsonObject.getInt("resultCount");// 总数
				if (resultJsonArray.length() > 15) {
					count = 15;
				} else {
					count = resultJsonArray.length();
				}
				for (int j = 0; j < count/2; j++) {

					Map<String, Object> item = new HashMap<String, Object>();
					item.put("fileName", resultJsonArray.getJSONObject(j).get("filename"));
					item.put("filePath", resultJsonArray
							.getJSONObject(j).get("filepath"));
					item.put("fileSize",
							resultJsonArray.getJSONObject(j).get("filesize"));
					
					items.add(item);
				}
				if (i == 0) {
					msg.what = 0;

				} else {
					msg.what = 1;
				}
				handler.sendMessage(msg);

			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				DownLoadListActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			DownLoadListActivity.this.sendExceptionMsg(err);
		}
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

	/**
	 * 第一次请求数据初始化页面
	 */
	private void initUI() {
		
		downLoadListView.setAdapter(downLoadListAdapter);

		if (total > pageSize * pageNo) {
			downLoadListView.addFooterView(footViewBar);// 添加list底部更多按钮
		}
		
	}

	/**
	 * 滑动list请求数据更新页面
	 */
	private void updateUI() {

		if (total <= pageSize * pageNo) {
			downLoadListView.removeFooterView(footViewBar);// 添加list底部更多按钮
		}
		downLoadListAdapter.notifyDataSetChanged();
		downLoadListView.setAdapter(downLoadListAdapter);
		downLoadListView.setSelection((pageNo - 1) * 10-5);
	}

	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				pageNo++;
				requestDataThread(1);// 滑动list请求数据
			}
			

		}
	};
	
class MyAdapter extends BaseAdapter {
		
		private Context mContext;// 上下文对象
		List<Map<String, Object>> list;// 这是要绑定的数据
		private int resource;// 这是要绑定的 item 布局文件
		private LayoutInflater inflater;// 布局填充器，Android系统内置的
		
		public MyAdapter(Context context, List<Map<String, Object>> list, int resource) {
			this.mContext = context;
			this.list = list;
			this.resource = resource;
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 布局填充服务
		}

		@Override
		public int getCount() {// 数据的总数量
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ImageView firstImage;
			TextView firstText;
			ImageView secondImage;
			TextView secondText;
			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);
				
				// 初始化控件
				firstImage = (ImageView) convertView.findViewById(R.id.first_download);
				firstText = (TextView) convertView.findViewById(R.id.download_first_name);
				secondImage = (ImageView) convertView.findViewById(R.id.second_download);
				secondText = (TextView) convertView.findViewById(R.id.download_second_name);
				ViewCache viewCache = new ViewCache();
				viewCache.firstImage = firstImage;
				viewCache.firstText = firstText;
				viewCache.secondImage = secondImage;
				viewCache.secondText = secondText;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				firstImage = viewCache.firstImage;
				firstText = viewCache.firstText;
				secondImage = viewCache.secondImage;
				secondText = viewCache.secondText;
				
			}
			firstText.setText(list.get(position).get("fileName").toString());
			if(list.size()>=position+1){
				secondText.setText(list.get(position+1).get("fileName").toString());
			}
			
			firstImage.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			secondImage.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			return convertView;
		}
		
	}
	
	private final class ViewCache {
		public ImageView firstImage;
		public TextView firstText;
		public ImageView secondImage;
		public TextView secondText;
	}


}
