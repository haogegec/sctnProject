package com.sctn.sctnet.cache;

import android.app.Application;

public class SctnAplication extends Application{

	private static SctnAplication  instance  = null;
	private boolean isExit = false;//退出状态，用于整个程序的退出
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;
	}
	
	public static SctnAplication getInstance(){
		return instance;
	}
	
	public boolean isExit() {
		return isExit;
	}

	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}
	
	/**
	 * 退出程序
	 */
	public void exit() {
		this.isExit = true;
		System.gc();
	}
}
