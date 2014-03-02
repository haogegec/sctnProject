package com.sctn.sctnet.view;

import com.sctn.sctnet.R;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyTextView extends TextView{

	private Drawable dLeft;
	private Drawable dRight;
	private Rect rBounds;
	
	public MyTextView(Context context) {
		super(context);
	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top,
			Drawable right, Drawable bottom) {
		if (right != null) {
			dRight = right;
		}
		if (left != null) {
			dLeft = left;
		}
		super.setCompoundDrawables(left, top, right, bottom);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("Demo:","子View的onTouchEvent方法执行了");
		if (event.getAction() == MotionEvent.ACTION_DOWN ) {
			rBounds = dRight.getBounds();
//			final int x = (int) event.getRawX();//获取在屏幕的绝对X.Y 坐标
//			final int y = (int) event.getRawY();
			
			final int x = (int) event.getX();//获取相对于TextView的X.Y 坐标
			final int y = (int) event.getY();
			
			/**
			 * 当我们触摸抬起（就是ACTION_UP的时候）的范围  大于输入框左侧到清除图标左侧的距离，小与输入框左侧到清除图片右侧的距离，我们则认为是点击清除图片
			 */
			boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));
			
			if(touchable){
				this.setText("");
				LinearLayout layout = (LinearLayout)this.getParent();
				layout.removeView(this);
				if(layout.getChildCount()==0){
					layout.setVisibility(View.GONE);
					LinearLayout layout2 = (LinearLayout)layout.getParent();
					RelativeLayout layout3 = (RelativeLayout)layout2.getChildAt(0);
					ImageView imageView = (ImageView)layout3.getChildAt(0);
					imageView.setImageResource(R.drawable.triangle_down);
					layout3.setClickable(false);
				}
				super.setCompoundDrawables(dLeft, null, null, null);
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		dRight = null;
		dLeft = null;
		rBounds = null;
		super.finalize();
	}

	public Drawable getdRight() {
		return dRight;
	}

	public Drawable getdLeft() {
		return dLeft;
	}
}
