package com.sctn.sctnet.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sctn.sctnet.R;

public class CustomDialog extends AlertDialog {
	private Context context;
	private View customView;

	private TextView title;
	private TextView message;
	private TextView positiveBtn;
	private TextView negetiveBtn;

	public CustomDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		customView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);

		title = (TextView) customView.findViewById(R.id.title);
		message = (TextView) customView.findViewById(R.id.message);
		positiveBtn = (TextView) customView.findViewById(R.id.dialog_button_ok);
		negetiveBtn = (TextView) customView.findViewById(R.id.dialog_button_cancel);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(customView);// 这行是【重点】，不能直接 this.setContentView(R.layout.custom_dialog_layout) 这样。 原因如下：
//		setContentView(R.layout.main)在Android里面，这句话是什么意思？
//		R.layout.main是个布局文件即控件都是如何摆放如何显示的，setContentView就是设置一个Activity的显示界面，这句话就是设置这个这句话所再的Activity采用R.layout下的main布局文件进行布局。
//		使用setContentView可以在Activity中动态切换显示的View,这样，不需要多个Activity就可以显示不同的界面，因此不再需要在Activity间传送数据，变量可以直接引用。
//		但是，在android SDK给我们建的默认的Hello World程序中，调用的是setContentView(int layoutResID)方法，如果使用该方法切换view,在切换后再切换回，无法显示切换前修改后的样子，
//		也就是说，相当于重新显示一个view,并非是把原来的view隐藏后再显示。其实setContentView是个多态方法，我们可以先用LayoutInflater把布局xml文件引入成View对象，
//		再通过setContentView(View view)方法来切换视图。因为所有对View的修改都保存在View对象里，所以，当切换回原来的view时，就可以直接显示原来修改后的样子。
	}


	public void setTitle(CharSequence sTitle) {
		title.setText(sTitle);
	}

	public String getTitle() {
		return title.getText().toString();
	}

	public void setMessage(CharSequence sMessage) {
		message.setText(sMessage);
	}

	public String getMessage() {
		return message.getText().toString();
	}
	
	 /**
     * 确定键监听器
     */
    public void setOnPositiveListener(CharSequence text, View.OnClickListener listener){
    	positiveBtn.setVisibility(View.VISIBLE);
    	positiveBtn.setText(text);
    	positiveBtn.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     */
    public void setOnNegativeListener(CharSequence text, View.OnClickListener listener){
    	negetiveBtn.setVisibility(View.VISIBLE);
    	negetiveBtn.setText(text);
    	negetiveBtn.setOnClickListener(listener);
    }

}
