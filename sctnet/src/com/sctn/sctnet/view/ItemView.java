package com.sctn.sctnet.view;

import com.sctn.sctnet.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 自定义职位选择、个人中心条目
 * @author xueweiwei
 *
 */
public class ItemView extends LinearLayout{

	private String label=""; //标签,如地区
	private String value="";//属性值,如北京
	
	ImageView iconImageView = null;
	TextView labelText=null;
	TextView valueText=null;
	ImageView detailImageView = null;
	RelativeLayout relativeLayout = null;
	
	public ItemView(Context context){
		this(context,null);
	}
	

	public ItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		int labelResouceId = -1;
		int valueResouceId=1;
		int iconImageId = 2;
		int detailImageId = 3;
		
		iconImageView = new ImageView(context);
		labelText=new TextView(context); 
		valueText=new TextView(context);
		detailImageView = new ImageView(context);
		
		 MarginLayoutParams mp = new MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);  //item的宽高
	     mp.setMargins(35, 0, 0, 0);//分别是margin_top那四个属性
	     RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mp);
	     lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	     lp.addRule(RelativeLayout.CENTER_VERTICAL);
	     iconImageView.setLayoutParams(lp);
	     iconImageView.setId(iconImageId);
		
		labelResouceId = attrs.getAttributeResourceValue(null, "label", 0);
		if (labelResouceId > 0) {
            label = context.getResources().getText(labelResouceId).toString();
        } else {
            label = "";
        }
		
		labelText.setText(label);
		MarginLayoutParams mp1 = new MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);  //item的宽高
	    mp1.setMargins(0, 0, 0, 0);//分别是margin_top那四个属性
	    RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(mp1);
	    lp1.addRule(RelativeLayout.RIGHT_OF,iconImageId);
	    lp1.addRule(RelativeLayout.CENTER_VERTICAL);
	    labelText.setMaxEms(20);
	    labelText.setLayoutParams(lp1);
	    labelText.setText(label);
	    labelText.setPadding(5, 3,5, 0);
	    labelText.setTextColor(this.getResources().getColor(R.color.black));
	    labelText.setSingleLine(true);
		
	    MarginLayoutParams mp2 = new MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);  //item的宽高
	    mp2.setMargins(0, 0, 40, 0);//分别是margin_top那四个属性
	    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(mp2);
	    lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	    lp2.addRule(RelativeLayout.CENTER_VERTICAL);
	    detailImageView.setLayoutParams(lp2);
	    detailImageView.setId(detailImageId);

	    
		valueResouceId = attrs.getAttributeResourceValue(null, "value", 0);
		if (valueResouceId > 0) {
            value = context.getResources().getText(valueResouceId).toString();
        } else {
            value = "";
        }
		
		MarginLayoutParams mp3 = new MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);  //item的宽高
	    mp3.setMargins(0, 0, 0, 0);//分别是margin_top那四个属性
	    RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(mp3);
	    lp3.addRule(RelativeLayout.LEFT_OF,detailImageId);
	    lp3.addRule(RelativeLayout.RIGHT_OF, labelResouceId);
	    lp3.addRule(RelativeLayout.CENTER_VERTICAL);
	    valueText.setLayoutParams(lp3);
		valueText.setText(value);
		valueText.setPadding(0, 3,3, 0);
		valueText.setMaxEms(20);
		valueText.setSingleLine(true);
		
		relativeLayout = new RelativeLayout(context);
		MarginLayoutParams mp4 = new MarginLayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);  //item的宽高
	    mp3.setMargins(10, 0, 10, 0);//分别是margin_top那四个属性
	    RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(mp4);
	    lp4.addRule(RelativeLayout.CENTER_VERTICAL);
		relativeLayout.setLayoutParams(lp4);
	    relativeLayout.addView(iconImageView);
		relativeLayout.addView(labelText);
		relativeLayout.addView(detailImageView);
		relativeLayout.addView(valueText);
		
		addView(relativeLayout);
		this.setGravity(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_VERTICAL);
	}
	/**
	 * 设置条目的背景图片
	 * @param resource
	 */
	public void setBackground(int resource){
		relativeLayout.setBackgroundResource(resource);
	}
	/**
	 * 设置条目左侧图标
	 * @param iconImgResource 图片资源id
	 */
	public void setIconImageViewResource(int iconImgResource){
		iconImageView.setImageResource(iconImgResource);
	}
	/**
	 * 设置标签名
	 * @param alabel 
	 */
	public void setLabel(String alabel) {
		labelText.setText(alabel);
	}
	/**
	 * 设置标签属性
	 * @param aValue
	 */
	public void setValue(String aValue) {
		valueText.setText(aValue);
	}
	/**
	 * 设置条目最右侧图标的样式
	 * @param detailImageResource
	 */
	public void setDetailImageViewResource(int detailImageResource){
		detailImageView.setImageResource(detailImageResource);
	}
	/**
	 * 设置左侧图标是否可见
	 * @param visibility
	 */
	public void setIconImageVisibility(int visibility){
		iconImageView.setVisibility(visibility);
		if(visibility==View.GONE){
			MarginLayoutParams mp1 = new MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);  //item的宽高
		    mp1.setMargins(35, 0, 0, 0);//分别是margin_top那四个属性
		    RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(mp1);
		    lp1.addRule(RelativeLayout.CENTER_VERTICAL);
		    labelText.setLayoutParams(lp1);
		}
	}
	/**
	 * 设置右侧图标是否可见
	 * @param visibility
	 */
	public void setDetailImageVisibility(int visibility){
		detailImageView.setVisibility(visibility);
		if(visibility==View.GONE){
			MarginLayoutParams mp3 = new MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);  //item的宽高
			mp3.setMargins(0, 0, 37, 0);//分别是margin_top那四个属性
		    RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(mp3);
		    lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		    lp3.addRule(RelativeLayout.CENTER_VERTICAL);
		    valueText.setLayoutParams(lp3);
		}
	}
	/**
	 * 设置标签值颜色
	 * @param color
	 */
	public void setLabelTextColor(int color){
		labelText.setTextColor(color);
	}
	/**
	 * 设置标签属性值的颜色
	 * @param color
	 */
	public void setValueTextColor(int color){
		valueText.setTextColor(this.getResources().getColor(color));
	}
	/**
	 * 获取标签属性值
	 * @return
	 */
	public String getValue() {
		return valueText.getText().toString();
	}


	public TextView getLabelText() {
		return labelText;
	}


	public void setLabelText(TextView labelText) {
		this.labelText = labelText;
	}


	public TextView getValueText() {
		return valueText;
	}


	public void setValueText(TextView valueText) {
		this.valueText = valueText;
	}

	public void setRelativeLayout(RelativeLayout relativeLayout){
		this.relativeLayout = relativeLayout;
	}
	public RelativeLayout getRelativeLayout(){
		return relativeLayout;
	}

}
