package com.sctn.sctnet.httpConnect;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.cache.SctnAplication;
import com.sctn.sctnet.contants.Constant;
import com.sctn.sctnet.entity.RequestHeadData;
import com.sctn.sctnet.entity.ReturnHeadData;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

/**
 * 用于和服务器端通信
 * 
 * @author wanghao
 * 
 */
public class Communication {

	private static HttpClient httpClient;
	public static HttpPost post;
	private String className;
	private String methodName;
	private String url;
	private String parameter;
	private boolean isFinish;// 判断是按了返回键
	Message m = new Message();
	Class activity;
	Class superClass;
	Object superObject;
	Method showProcessDialog;
	Method closeProcessDialog;
	/**
	 * 获取HttpClient静态对象，用于和服务器端通信
	 * 
	 * @return httpClient
	 */
	public static HttpClient createHttpClient() {
		if (httpClient == null) {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 500000);
			HttpConnectionParams.setSoTimeout(params, 1800000);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));



			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			httpClient = new DefaultHttpClient(conMgr, params);

		}
		return httpClient;

	}
	
	/**
	 * 以post方式提交数据
	 * 
	 * @param url
	 *            服务器的地址，直接写Action的地址，如tm/WoHandleAction.do?method=query
	 * @param parameter
	 *            拼接好的json字符串
	 * @return 服务端json字符串，当服务器端返回AppException或SysException时，得到的字符串是一个html文档
	 * @throws SysException
	 * @throws Exception
	 */
	public static String getPostResponse(String url, String parameter)
			throws ClientProtocolException, IOException {
		
		HttpClient client = createHttpClient();
		String serverUrl = Constant.ServerURL + url;
		HttpPost post = new HttpPost(serverUrl);
		post.addHeader("Content-Type", "application/json");
		String result = null;

			String encoderJson = URLEncoder.encode(parameter, HTTP.UTF_8);
			StringEntity resEntity = new StringEntity(encoderJson,
					"UTF-8");
			post.setEntity(resEntity);
			// 获取响应的结果
			HttpResponse response = client.execute(post);
			// 获取HttpEntity
			HttpEntity respEntity = response.getEntity();
			// 获取响应的结果信息
			byte[] resultByteType = EntityUtils.toByteArray(respEntity);
//			 解压返回的字符串
			result = StringUtil.unCompress(resultByteType); 
		   return result;
	}

	/**
	 * 
	 * @param url
	 *            服务器的地址，直接写Action的地址，如tm/WoHandleAction.do?method=query
	 * @param parameter
	 *            请求的json字符串
	 * @param className
	 *            哪个Activity调用的该方法
	 * @param methodName
	 *            服务端传来数据后，该Acitivity对数据处理的方法名
	 * @param isFinish
	 *            进度条是否按的返回键
	 * @param superObject
	 *            反射的实例(Activity)
	 */
	public void getPostHttpContent(String url, String parameter,
			String className, String methodName, Boolean isFinish,Object superObject) {

		JSONObject requestHeadData;
		try {
			requestHeadData = RequestHeadData.getRequestHeadData();
			if(parameter!=null&&!"".equals(parameter)){
			requestHeadData.put("mobileBody", new JSONObject(parameter));}
		} catch (JSONException e) {
			Toast.makeText(SctnAplication.getInstance().getApplicationContext(),
					"获得请求信息出错！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		this.className = className;
		this.methodName = methodName;
		this.superObject=superObject;
		this.parameter = requestHeadData.toString();
		this.url = url;
		this.isFinish = isFinish;
		beautyThread();
	}

	/**
	 * 请求数据的线程
	 */
	private void beautyThread() {

		try {
			activity = Class.forName(Constant.packageName+".activity."+className);
			superClass = activity.getSuperclass(); 
			 Class[] argsClass = new Class[1];
			 argsClass[0] = boolean.class;
		  if(isFinish) {
			showProcessDialog = activity.getMethod("showProcessDialog",
					argsClass); 
			closeProcessDialog=activity.getMethod("closeProcessDialog",null);
			
				showProcessDialog.invoke(superObject, isFinish);
			}
			
		} catch (Exception e1) {
//			Toast.makeText(BeautyApp.getInstance().getApplicationContext(),
//					"调用显示进度条时java反射机制错误", Toast.LENGTH_SHORT).show();
			return;
		}

		// basicActivity = new BasicActivity();
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() { 
				// TODO Auto-generated method stub
				HttpClient client = createHttpClient();

				String serverUrl = Constant.ServerURL + url;
				HttpPost post = new HttpPost(serverUrl);
				post.addHeader("Content-Type", "application/json");
				String result = null;

				try {
					String encoderJson = URLEncoder.encode(parameter, HTTP.UTF_8);
					StringEntity resEntity = new StringEntity(encoderJson,
							"UTF-8");
					post.setEntity(resEntity);
					// 获取响应的结果
					HttpResponse response = client.execute(post);
					// 获取HttpEntity
					HttpEntity respEntity = response.getEntity();
//					// 获取响应的结果信息
//					result = EntityUtils.toString(respEntity, "UTF-8");
					// 获取响应的结果信息
					byte[] resultByteType = EntityUtils.toByteArray(respEntity);
//					 解压返回的字符串
					result = StringUtil.unCompress(resultByteType);
					
					if (result == null || "".equals(result)) {
						m.what = 2;
						baseHandler.sendMessage(m);
					} else {
						ReturnHeadData returnHeadData = ReturnHeadData
								.parserReturnHeadFromStr(result);
						if (returnHeadData.returnCode.equals("0")) {
							Bundle bundle = new Bundle();
							bundle.putString("errorMsg",
									returnHeadData.errorMsg);
							m.setData(bundle);
							m.what = 3;
							baseHandler.sendMessage(m);
						} else {
							Bundle bundle = new Bundle();
							bundle.putString("result", result);
							m.setData(bundle);
							m.what = 1;
							baseHandler.sendMessage(m);
						}

					}

				} catch (ClientProtocolException e) {
					m.what = -1;
					baseHandler.sendMessage(m);
				} catch (IOException e) { 
					m.what = -1;
					baseHandler.sendMessage(m);
				} 
			}
			
		});
		thread.start();
	}

	// 消息发送
	public Handler baseHandler = new Handler() { 
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1: {// 处理异常信息

				Toast.makeText(SctnAplication.getInstance().getApplicationContext(),
						"网络连接异常，请检查网络设置！", Toast.LENGTH_SHORT).show();

				break;
			}
			case 1: {// 服务器成功返回数据
				Method method;
				try { 
					 Class[] argsClass = new Class[1];
					 argsClass[0] = String.class;
					 
					 if(methodName!=null&&!"".equals(methodName)){
					Method activityMethod = activity.getMethod(methodName,
							argsClass);
					String result = m.getData().getString("result"); 
					activityMethod.invoke(superObject, result);
					 }
				} catch (Exception e) {
					e.printStackTrace(); 
//					Toast.makeText(
//							BeautyApp.getInstance().getApplicationContext(),
//							"调用Activity的方法时java反射机制出错", Toast.LENGTH_SHORT).show();
					
				}

				break;
			}
			case 2: {// 未获得服务器响应结果

				Toast.makeText(SctnAplication.getInstance().getApplicationContext(),
						"未获得服务器响应结果!", Toast.LENGTH_SHORT).show();
				break;
			}
			case 3: {// 服务端结果错误
				
				String errorMsg = m.getData().getString("errorMsg");
				Toast.makeText(SctnAplication.getInstance().getApplicationContext(),
						errorMsg, Toast.LENGTH_SHORT).show();
				break;
			}
			} 
			try {  
				if(isFinish) {
				closeProcessDialog.invoke(superObject, null);
				}
			} catch (Exception e) {

//				Toast.makeText(BeautyApp.getInstance().getApplicationContext(),
//						"调用关闭进度条时java反射机制错误", Toast.LENGTH_SHORT).show();
				return;
			}
		}
	};
 
}
