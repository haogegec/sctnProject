package com.sctn.sctnet.cache;

import android.app.Application;

public class sctnAplication extends Application{

	private static sctnAplication  instance  = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;
	}
	
	public static sctnAplication getInstance(){
		return instance;
	}
	
}
