package com.sctn.sctnet.Utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


 /**
 *  @author dhx  DateTime 2013-12-24 上午10:35:12    
 *  @version 1.0 
 */
public class ViewUtils {

	/**
	 * ListView 在Scroller中会丢失其高度 需要重新设置其高度
	 * @param listView 
	 * @param height 额外的高度 即计算ListView高度然后在加上 Height
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView,int height) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+height;
		listView.setLayoutParams(params);
	}
	
  
}
