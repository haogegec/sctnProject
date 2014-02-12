package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.sctn.sctnet.R;
import com.sctn.sctnet.adapter.ExpandAdapter;
import com.sctn.sctnet.entity.Item;

/**
 * 简历预览
 * @author xueweiwei
 *
 */
public class ResumePreviewActivity extends BaicActivity{

	private ExpandableListView mListView = null;
    private ExpandAdapter mAdapter = null;
    private List<List<Item>> mData = new ArrayList<List<Item>>();
    private int[] childLabel = new int[] { 
    		R.array.personal_info_label,
    		R.array.job_intention_label
            };

    private int[] childValue = new int[] { 
            R.array.personal_info_value,
            R.array.job_intention_value
            };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_preview_activity);
		setTitleBar(getString(R.string.resumePreviewActivityTitle), View.VISIBLE, View.GONE);
		initAllView();
		reigesterAllEvent();
		initData();
		int groupCount = mAdapter.getGroupCount();

        for (int i=0; i<groupCount; i++) {

        	mListView.expandGroup(i);

         }
	}

	@Override
	protected void initAllView() {
		// TODO Auto-generated method stub
		mListView = (ExpandableListView) findViewById(R.id.expandableListView);
		mAdapter = new ExpandAdapter(this, mData);
        mListView.setAdapter(mAdapter);
        

	}

	@Override
	protected void reigesterAllEvent() {
		// TODO Auto-generated method stub
		
	}
	 private void initData() {
	        for (int i = 0; i < childLabel.length; i++) {
	            List<Item> list = new ArrayList<Item>();
	            String[] childs = getStringArray(childLabel[i]);
	            String[] details = getStringArray(childValue[i]);
	            for (int j = 0; j < childs.length; j++) {
	                Item item = new Item(childs[j], details[j]);
	                list.add(item);
	            }
	            mData.add(list);
	        }
	    }

	    private String[] getStringArray(int resId) {
	        return getResources().getStringArray(resId);
	    }

}
