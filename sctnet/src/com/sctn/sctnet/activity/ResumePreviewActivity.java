package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.adapter.ExpandAdapter;
import com.sctn.sctnet.entity.Item;
import com.sctn.sctnet.entity.ResumeInfo;

/**
 * 简历预览
 * @author xueweiwei
 *
 */
public class ResumePreviewActivity extends BaicActivity{

	private ExpandableListView mListView = null;
    private ExpandAdapter mAdapter = null;
    private List<List<Item>> childData = new ArrayList<List<Item>>();//子列表数据
    private List<String> groupTitle = new ArrayList<String>();//列表标题
    
    private TextView personalName;
    private TextView sex;
//    private ResumeInfo resumeInfo;
    private ArrayList<ArrayList<HashMap<String, String>>> dataList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_preview_activity);
		setTitleBar(getString(R.string.resumePreviewActivityTitle), View.VISIBLE, View.GONE);
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		dataList = (ArrayList<ArrayList<HashMap<String, String>>>) bundle.getSerializable("resumeInfo");
		
		initData();
		initAllView();
		reigesterAllEvent();
		int groupCount = mAdapter.getGroupCount();

        for (int i=0; i<groupCount; i++) {

        	mListView.expandGroup(i);

         }
	}

	@Override
	protected void initAllView() {
		// TODO Auto-generated method stub
		mListView = (ExpandableListView) findViewById(R.id.expandableListView);
		mAdapter = new ExpandAdapter(this, childData,groupTitle);
        mListView.setAdapter(mAdapter);
        
        personalName = (TextView) findViewById(R.id.personal_name_value);
        sex = (TextView) findViewById(R.id.sex_button);

        if(dataList.get(0).get(0).get("姓名")!=null){
	    	 personalName.setText(dataList.get(0).get(0).get("姓名"));
	     }
	     if(dataList.get(0).get(0).get("性别")!=null){
	    	 sex.setText(dataList.get(0).get(0).get("性别"));
	     }
	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 处理简历管理页面传过来的数据
	 */
	 private void initData() {
		 
		 if(dataList.get(0).get(0).size()!=0){
				groupTitle.add("基本信息");
				
				HashMap<String,String> map = dataList.get(0).get(0);				
				childData.add(getListItem(map));
			}
			if(dataList.get(1).get(0).size()!=0){
				groupTitle.add("个人简介");
				
				HashMap<String,String> map = dataList.get(1).get(0);				
				childData.add(getListItem(map));
			}
			if(dataList.get(2).get(0).size()!=0){
				groupTitle.add("教育情况");
				
				HashMap<String,String> map = dataList.get(2).get(0);				
				childData.add(getListItem(map));
			}
			if(dataList.get(3).get(0).size()!=0){
				groupTitle.add("职业生涯");
				
				HashMap<String,String> map = dataList.get(3).get(0);				
				childData.add(getListItem(map));
			}
			if(dataList.get(4).get(0).size()!=0){
				groupTitle.add("联系方式");
				
				HashMap<String,String> map = dataList.get(4).get(0);				
				childData.add(getListItem(map));
			}
			if(dataList.get(5).get(0).size()!=0){
				groupTitle.add("求职意向");
				
				HashMap<String,String> map = dataList.get(5).get(0);				
				childData.add(getListItem(map));
			}	
					 
	 }
	 
	 /**
	  * 得到childItem数据
	  * @param map
	  * @return
	  */
	 private List<Item> getListItem(HashMap<String,String> map){
		 
		 List<Item> list = new ArrayList<Item>();
		 Iterator<String> it=map.keySet().iterator();
			while(it.hasNext()){
			   String key=(String) it.next();
			   String value = (String) map.get(key);
			   Item item = new Item(key,value);
			   list.add(item);
			} 
		return list;
		 
	 }


}
