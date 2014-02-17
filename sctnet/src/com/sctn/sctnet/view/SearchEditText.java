package com.sctn.sctnet.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SearchEditText extends EditText {

	private Drawable dRight;

	private Drawable dLeft;

	private Rect rBounds;


	public SearchEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public SearchEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public SearchEditText(Context context) {
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

		if (event.getAction() == MotionEvent.ACTION_UP && dRight != null) {
			rBounds = dRight.getBounds();
//			final int x = (int) event.getRawX();//获取在屏幕的绝对X.Y 坐标
//			final int y = (int) event.getRawY();
			
			final int x = (int) event.getX();//获取在屏幕的绝对X.Y 坐标
			final int y = (int) event.getY();
			// 如果点击的X、Y坐标在  范围内 相应取消事件
			if (x >= (this.getRight() - rBounds.width()-5)
					&& x <= (this.getRight() - this.getPaddingRight()+5)
					&& y >= this.getBottom() - rBounds.height()-20
					&& y <= (this.getBottom() - this.getPaddingBottom()+30)) {
				this.setText("");
				super.setCompoundDrawables(dLeft, null, null, null);
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.onTouchEvent(event);
	}

	public void setsetCompoundDrawables(Drawable left, Drawable right) {
		super.setCompoundDrawables(left, null, right, null);
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
