package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.sctn.sctnet.R;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.sqlite.DBHelper;
import com.sctn.sctnet.view.ItemView;

/**
 * @author wanghaoc 首页关键字职位搜索
 * 
 */
public class WorkSearchActivity extends Activity {

	private int perSpacing;
	private ArrayList<TextView> pageTitles = new ArrayList<TextView>();
	private ImageView cursor;// 动画图片
	private int currIndex = 0;// 当前页卡编号
	private EditText search_edit;
	private String type = Constant.TYPE_JOB_NAME;// 默认选择职位名

	private SQLiteDatabase database;
	private List<Map<String, String>> logData = new ArrayList<Map<String, String>>();
	
	private ListView listView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.worksearch_activity);
		initAllView();
		reigesterAllEvent();
		initLogThread();
	}

    protected void initLogThread() {
		
		//查询数据可能花费太多时间交给子线程去做，在Handler完成数据的显示
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
			@Override
			public void run() {
				initData();
			}
		});
       mThread.start();
		
		

	}
	private void initData() {
		
		Message msg = new Message();
		Cursor cursor = database.query("searchLog", new String[] { "_id",
				"key"}, null, null, null, null, null);
		while (cursor.moveToNext()) {
             Map<String,String> map = new HashMap<String,String>(); 
             map.put("value", cursor.getString(cursor.getColumnIndex("key")));
            
             logData.add(map);
		}
		cursor.close();
		msg.what = 0;
		handler.sendMessage(msg);

	}
	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				initUI();
				break;
			}
		}
	};
	private void initUI(){
		if(logData==null||logData.size()==0){
			Map<String,String> map = new HashMap<String,String>(); 
            map.put("value", "暂无搜索记录");
            logData.add(map);
	    }	
		listView.setAdapter(new MyAdapter(this,logData,R.layout.select_area_item));
	    
		
	}
	protected void initAllView() {
		// 获取手机屏幕的宽度
		getScreenWidth();
		initPageTitles();
		// 初始化页卡标题下面的橘色条
		cursor = (ImageView) findViewById(R.id.cursor);
		Bitmap bmp = Bitmap
				.createBitmap(perSpacing, 5, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		canvas.drawColor(0xFFef8700);
		cursor.setImageBitmap(bmp);
		// 初始化Edit编辑框
		search_edit = (EditText) findViewById(R.id.et_search_searchtxt);
		
		listView = (ListView) findViewById(R.id.search_history_listview);
		
		listView.setAdapter(new MyAdapter(this,logData,R.layout.select_area_item));
		//初始化本地数据库
	    DBHelper dbHelper = new DBHelper(this,"searchLog");
        database = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(this.database, 1, 2);
	}

	// 自定义适配器
	 	class MyAdapter extends BaseAdapter{
	 		private Context mContext;// 上下文对象
	 		List<Map<String, String>> list;// 这是要绑定的数据
	 		private int resource;// 这是要绑定的 item 布局文件
	 		private LayoutInflater inflater;// 布局填充器，Android系统内置的
	 		
	 		public MyAdapter(Context context, List<Map<String, String>> list, int resource){
	 			this.mContext = context;
	 			this.list = list;
	 			this.resource = resource;
	 			this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 布局填充服务
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
	 			TextView area = null;
	 			
	 			
	 			if(convertView == null){// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
	 				convertView = inflater.inflate(resource, null);
	 				
	 				// 初始化控件
	 				area = (TextView) convertView.findViewById(R.id.area); 				
	 				ViewCache viewCache = new ViewCache();
	 				viewCache.area = area;
	 				convertView.setTag(viewCache);
	 			} else {
	 				// 初始化控件
	 				ViewCache viewCache = (ViewCache)convertView.getTag();
	 				area = viewCache.area;
	 			}
	 			 
	 			area.setText(list.get(position).get("value"));
	 			
	 			return convertView;
	 		}
	 		
	 	}
	 	
	 	private final class ViewCache {
	 		public TextView area;//key值
	 	}

	protected void reigesterAllEvent() {
		setPageTitleOnClickListener();
		search_edit.setOnEditorActionListener(new OnEditorActionListener() { 
            
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { 
               if (actionId == EditorInfo.IME_ACTION_DONE||actionId == KeyEvent.KEYCODE_ENTER||actionId == 0||actionId == 5) { 
            	   Intent intent = new Intent(WorkSearchActivity.this,JobListActivity.class);
            	   Bundle bundle = new Bundle();
            	   bundle.putString("type", type);
            	   bundle.putString("key", search_edit.getText().toString());
            	   bundle.putString("whichActivity", "WorkSearchActivity");
            	   intent.putExtras(bundle);
				   startActivity(intent);
				   finish();
               } 
               return false; 
           } 
       });
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorkSearchActivity.this,JobListActivity.class);
         	   Bundle bundle = new Bundle();
         	   bundle.putString("type", type);
         	   bundle.putString("key", logData.get(position).get("value"));
         	   bundle.putString("whichActivity", "WorkSearchActivity");
         	   intent.putExtras(bundle);
			   startActivity(intent);
			   finish();
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
					cursor.setPadding(cursor.getPaddingLeft() - perSpacing,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());
		
				} else if (currIndex == 2) {
					cursor.setPadding(cursor.getPaddingLeft() - perSpacing*2,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());

				}
				type = Constant.TYPE_JOB_NAME;
				break;
			case 1:
				// 从1 到 2 页面
				if (currIndex == 0) {
					cursor.setPadding(cursor.getPaddingLeft() + perSpacing,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());
				} else if (currIndex == 2) {
					// 从3到2
					cursor.setPadding(cursor.getPaddingLeft() - perSpacing,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());
				}
				type = Constant.TYPE_COMPANY_NAME;
				break;
			case 2:
				// 从1到3
				if (currIndex == 0) {
					cursor.setPadding(cursor.getPaddingLeft() + perSpacing*2,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());
				} else if (currIndex == 1) {
					// 从 2 到3
					cursor.setPadding(cursor.getPaddingLeft() + perSpacing,
							cursor.getPaddingTop(), cursor.getPaddingRight(),
							cursor.getPaddingBottom());
				}
				type = Constant.TYPE_FULL_TEXT;
				break;
			}
			currIndex = index;
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
	 * 获取屏幕宽度/3
	 */
	private int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取屏幕宽度
		perSpacing = screenW / 3;
		return perSpacing;
	}

	
}
