package com.sctn.sctnet.adapter;

import java.util.List;

import com.sctn.sctnet.R;
import com.sctn.sctnet.entity.Item;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * ExpandableListView的适配器
 * @author xueweiwei
 *
 */
public class ExpandAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater mInflater = null;
    private String[]   mGroupStrings = null;
    private List<List<Item>>   mData = null;

    public ExpandAdapter(Context ctx, List<List<Item>> list) {
        mContext = ctx;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupStrings = mContext.getResources().getStringArray(R.array.groups);
        mData = list;
    }

    public void setData(List<List<Item>> list) {
        mData = list;
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return mData.get(groupPosition).size();
    }

    @Override
    public List<Item> getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return mData.get(groupPosition);
    }

    @Override
    public Item getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return mData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_item_layout, null);
        }
        GroupViewHolder holder = new GroupViewHolder();
        holder.mGroupName = (TextView) convertView
                .findViewById(R.id.item_title);
        holder.mGroupName.setText(mGroupStrings[groupPosition]);
        
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_item_layout, null);
        }
        ChildViewHolder holder = new ChildViewHolder();
        
        holder.labelText = (TextView) convertView.findViewById(R.id.child_item_label);
        holder.labelText.setText(getChild(groupPosition, childPosition)
                .getLabel());
        holder.valueText = (TextView) convertView.findViewById(R.id.child_item_value);
        holder.valueText.setText(getChild(groupPosition, childPosition)
                .getValue());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        /*很重要：实现ChildView点击事件，必须返回true*/
        return false;
    }

    private class GroupViewHolder {
        TextView mGroupName;
    }

    private class ChildViewHolder {
        
        TextView labelText;
        TextView valueText;
    }

}
