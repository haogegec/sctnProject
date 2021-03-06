package com.sctn.sctnet.cache;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.exception.CrashHandler;

public class SctnAplication extends Application{

	private static SctnAplication  instance  = null;
	private boolean isExit = false;//退出状态，用于整个程序的退出
	private boolean isReLogin = false;
	private HttpClient httpClient;
	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;
		httpClient = createHttpClient();
		
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
	    JPushInterface.init(this);     		// 初始化 JPush
	    
	 // 捕获程序崩溃日志，并将日志发送到服务端
		 CrashHandler crashHandler = CrashHandler.getInstance();
		 crashHandler.init(getApplicationContext());
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
	 * 外部接口获得HttpClient的方法
	 * 
	 * @return
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}
	// 创建HttpClient对象
		private HttpClient createHttpClient() {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, Constant.REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, Constant.SO_TIMEOUT);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https",
					SSLSocketFactory.getSocketFactory(), 443));

			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);

			return new DefaultHttpClient(conMgr, params);
		}
	/**
	 * 退出程序
	 */
	public void exit() {
		this.isExit = true;
		System.gc();
	}
	
	public boolean isReLogin() {
		return isReLogin;
	}
	
	public void setReLogin(boolean isReLogin) {
		this.isReLogin = isReLogin;
	}
	
}
