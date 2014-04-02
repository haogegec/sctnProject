package com.sctn.sctnet.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
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
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* SD卡不存在，下载失败*/
	private static final int DOWNLOAD_FAULT = 3;
	/* 网络连接失败，下载失败*/
	private static final int DOWNLOAD_CONNECT_FAIL = 4;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 
				mDownloadDialog.dismiss();
				Toast.makeText(DownLoadListActivity.this, "文件已下载到手机mnt/sdCard/Download",
						Toast.LENGTH_SHORT).show();
				
				break;
			case DOWNLOAD_FAULT:
				mDownloadDialog.dismiss();
				Toast.makeText(DownLoadListActivity.this, "您的SD卡已拔出，不能下载该文档！",
						Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_CONNECT_FAIL:
				mDownloadDialog.dismiss();
				Toast.makeText(DownLoadListActivity.this, "网络出错，下载失败！",
						Toast.LENGTH_SHORT).show();
		        break;
			default:
				break;
			}
		};
	};
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
		final LayoutInflater inflater = LayoutInflater.from(DownLoadListActivity.this);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
	}

	@Override
	protected void reigesterAllEvent() {
		
		//点击下载文档
		downLoadListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				String urlStr = Constant.DocUrl + items.get(position).get("filePath").toString();
				if(StringUtil.isBlank(items.get(position).get("filePath").toString())){
					urlStr = Constant.DocUrl + "upload/UploadFile/20063/200603141202267448.doc";
				}
			
				String fileName = items.get(position).get("fileName").toString();
				fileName.substring(0, fileName.length()-4);
				showDownloadDialog();
				downloadThread(urlStr,fileName);
			}
			
		});
	}
	/**
	 * 显示软件下载对话框
	 */
	public void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(DownLoadListActivity.this);
		builder.setTitle(R.string.downloading);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(DownLoadListActivity.this);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.cancel, new Dialog.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

			
			
		});
	//	builder.setOnKeyListener(keylistener);
		mDownloadDialog = builder.create();
		mDownloadDialog.setCanceledOnTouchOutside(false);
		mDownloadDialog.show();
		
	}
	private void downloadThread(final String urlStr,final String fileName){
		
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
			@Override
			public void run() {
				try
				{
					// 判断SD卡是否存在，并且是否具有读写权限
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
					{
						// 获得存储卡的路径
						String sdpath = Environment.getExternalStorageDirectory() + "/";
						mSavePath = sdpath + "Download";
					
						URL url = new URL(urlStr);
						// 创建连接
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.connect();
						// 获取文件大小
						int length = conn.getContentLength();
						// 创建输入流
						InputStream is = conn.getInputStream();

						File file = new File(mSavePath);
						// 判断文件目录是否存在
						if (!file.exists())
						{
							file.mkdir();
						}
						File apkFile = new File(mSavePath,"sctnet");
						FileOutputStream fos = new FileOutputStream(apkFile);
						int count = 0;
						// 缓存
						byte buf[] = new byte[1024];
						// 写入到文件中
						do
						{
							int numread = is.read(buf);
							count += numread;
							// 计算进度条位置
							progress = (int) (((float) count / length) * 100);
							// 更新进度
							mHandler.sendEmptyMessage(DOWNLOAD);
							if (numread <= 0)
							{
								// 下载完成
								mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
								break;
							}
							// 写入文件
							fos.write(buf, 0, numread);
						} while (!cancelUpdate);// 点击取消就停止下载.
						fos.close();
						is.close();
					}else {
						mHandler.sendEmptyMessage(DOWNLOAD_FAULT);
					}
				} catch (MalformedURLException e)
				{
					mHandler.sendEmptyMessage(DOWNLOAD_CONNECT_FAIL);
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
					mHandler.sendEmptyMessage(DOWNLOAD_CONNECT_FAIL);
					e.printStackTrace();
				}
			}
		});
       mThread.start();
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

		if (total > pageSize * pageNo) {
			downLoadListView.addFooterView(footViewBar);// 添加list底部更多按钮
		}
		downLoadListView.setAdapter(downLoadListAdapter);
	}

	/**
	 * 滑动list请求数据更新页面
	 */
	private void updateUI() {

		if (total <= pageSize * pageNo) {
			downLoadListView.removeFooterView(footViewBar);// 添加list底部更多按钮
		}
		downLoadListAdapter.notifyDataSetChanged();
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
				ViewCache viewCache = new ViewCache();
				viewCache.firstImage = firstImage;
				viewCache.firstText = firstText;
				
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				firstImage = viewCache.firstImage;
				firstText = viewCache.firstText;
				
			}
			firstText.setText(list.get(position).get("fileName").toString());
			if(position%2==0){
				firstImage.setBackgroundResource(R.drawable.download_first_normal);
			}else{
				firstImage.setBackgroundResource(R.drawable.download_second_normal);
			}
			
			return convertView;
		}
		
	}
	
	private final class ViewCache {
		public ImageView firstImage;
		public TextView firstText;
		
	}


}
