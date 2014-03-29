package com.sctn.sctnet.activity;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.EncryptUtil;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.Utils.SysException;
import com.sctn.sctnet.cache.SctnAplication;
import com.sctn.sctnet.contants.Constant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaicActivity extends Activity {

	public SctnAplication sctnApp;
	private SharedPreferences sharedPreferences;
	protected ImageView titleLeftButton;// 标题栏左边按钮，默认图标为返回，背景为透明
	private TextView titleTextView;// 标题栏居中标题
	protected ImageView titleRightButton;// 标题栏右边文字，默认图标为刷新，背景为透明

	public Dialog mProgressDialog;// 进度条
	public boolean isCancel = false;// 判断是按了返回键
	private String progressText;// 进度条显示文字，null则取默认值
	private String encryptUsable;

	public String exceptionDesc = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置标题栏为用户自定义标题栏
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sctnApp = SctnAplication.getInstance();
		// 是否加密
		encryptUsable = sharedPreferences.getString("encryptUsable", "");
		// if(cache==null) {
		// cache=new CacheProcess();
		// }
	}

	public ImageView getTitleLeftButton() {
		return titleLeftButton;
	}

	public void setTitleLeftButton(ImageView titleLeftButton) {
		this.titleLeftButton = titleLeftButton;
	}

	public TextView getTitleTextView() {
		return titleTextView;
	}

	public void setTitleTextView(TextView titleTextView) {
		this.titleTextView = titleTextView;
	}

	public ImageView getTitleRightButton() {
		return titleRightButton;
	}

	public void setTitleRightButton(ImageView titleRightButton) {
		this.titleRightButton = titleRightButton;
	}

	/**
	 * 设置标题栏
	 * 
	 * @param titleText
	 *            标题文字
	 * @param leftButtonVisibility
	 *            左侧按钮可见状态
	 * @param rightButtonVisibility
	 *            右侧按钮可见状态
	 * 
	 * 
	 */
	public void setTitleBar(String titleText, int leftButtonVisibility, int rightButtonVisibility) {
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);// 设置标题栏布局文件为title_model
		titleLeftButton = (ImageButton) findViewById(R.id.titlebar_img_btn_left);
		titleTextView = (TextView) findViewById(R.id.titlebar_text);
		titleRightButton = (ImageView) findViewById(R.id.titlebar_img_btn_right);

		setTitleText(titleText);
		setTitleLeftButtonVisibility(leftButtonVisibility);
		setTitleRightButtonVisibility(rightButtonVisibility);

		leftButtonOnClick();
		rightButtonOnClick();

	}

	/**
	 * 设置标题文本
	 * 
	 * @param titleText
	 *            标题文本
	 */
	public void setTitleText(String titleText) {
		titleTextView.setText(titleText);
	}

	/**
	 * 设置左边按钮是否可见
	 * 
	 * @param visibility
	 *            可见状态
	 */
	public void setTitleLeftButtonVisibility(int visibility) {
		titleLeftButton.setVisibility(visibility);
	}

	/**
	 * 设置右边按钮是否可见
	 * 
	 * @param visibility
	 *            可见状态
	 */
	public void setTitleRightButtonVisibility(int visibility) {
		titleRightButton.setVisibility(visibility);
	}

	/**
	 * 设置左侧按钮图标
	 * 
	 * @param leftButtonImg
	 *            图标资源id
	 */
	public void setTitleLeftButtonImg(int leftButtonImg) {
		titleLeftButton.setImageResource(leftButtonImg);
	}

	public void setTitleRightButtonImg(int rightButtonImg) {
		titleRightButton.setImageResource(rightButtonImg);
	}

	/**
	 * 左侧按钮点击事件监听 默认返回主界面
	 */
	public void leftButtonOnClick() {
		titleLeftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	/**
	 * 右侧按钮点击事件监听
	 */
	public void rightButtonOnClick() {
		titleRightButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});
	}

	public void showProcessDialog(final boolean finishActivity) {
		mProgressDialog = new Dialog(this, R.style.process_dialog);// 创建自定义进度条
		mProgressDialog.setContentView(R.layout.progress_dialog);// 自定义进度条的内容
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				isCancel = true;
				if (finishActivity)
					finish();
			}
		});

		if (progressText != null) {
			((TextView) mProgressDialog.findViewById(R.id.progress_text)).setText(progressText);
		}
		mProgressDialog.show();// 显示进度条

	}

	public void closeProcessDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	/**
	 * 设置进度条文字
	 */
	public void setProcessText(String progressText) {
		this.progressText = progressText;
	}

	/**
	 * 退出提示框
	 */
	public void dialogExit() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// android.os.Process.killProcess(android.os.Process.myPid());
				// beautyApp.exit();
				finish();
			}
		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	/**
	 * 重写onRestart()方法，当isExit值为true时退出整个程序
	 */
	@Override
	public void onRestart() {
		if (sctnApp.isExit()) {
			super.onRestart();
			finish();
		} else {
			super.onRestart();
		}
	}

	/**
	 * 初始化组件。用于在Activity装载时初始化相关组件和变量，以减少oncreate方法的庞大和冗余。
	 */
	protected abstract void initAllView();

	/**
	 * 初始化组件。用于在Activity装载时注册事件，以减少oncreate方法的庞大和冗余。
	 */
	protected abstract void reigesterAllEvent();

	/**
	 * 以post方式提交数据
	 * 
	 * @param url
	 *            服务器的地址，直接写Action的地址，如tm/WoHandleAction.do?method=query
	 * @param parameter
	 *            拼接好的json字符串
	 * @return 服务端json字符串，当服务器端返回AppException或SysException时，得到的字符串是一个html文档
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getPostHttpContent(String url, List<BasicNameValuePair> parameter) {
		SctnAplication app = (SctnAplication) this.getApplication();
		// 要是一个单例
		HttpClient client = app.getHttpClient();

		String serverUrl = Constant.ServerURL + url;
		HttpPost post = new HttpPost(serverUrl);
		// post.addHeader("Content-Type", "application/json");
		String result = null;
		byte[] resultByteType = null;
		// String parameterEncrypted = null;
		try {

			UrlEncodedFormEntity resEntity = new UrlEncodedFormEntity(parameter,
					HTTP.UTF_8);
			post.setEntity(resEntity);
			// 获取响应的结果
			HttpResponse response = client.execute(post);
			// 获取HttpEntity
			HttpEntity respEntity = response.getEntity();

			// 获取响应的结果信息
			resultByteType = EntityUtils.toByteArray(respEntity);
			
			if(resultByteType == null || resultByteType.length==0){
				return result;
			}
			
			// 解压返回的数据
			result = StringUtil.unCompress(resultByteType);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = StringUtil.getAppException4MOS("网络连接异常，请检查网络设置！");
		} catch (IOException e) {
			e.printStackTrace();
			result = StringUtil.getAppException4MOS("网络连接异常，请检查网络设置！");
		} catch (ParseException e) {
			e.printStackTrace();
			result = StringUtil.getAppException4MOS("返回结果解析错误！");
		} catch (SysException e) {
			e.printStackTrace();
			result = StringUtil.getAppException4MOS("解压出现异常！");
		}
		return result;
	}

	public void sendExceptionMsg(String res) {
		exceptionDesc = StringUtil.getExceptionDesc(res);
		Message errMsg = new Message();
		errMsg.what = -1;
		baseHandler.sendMessage(errMsg);
	}

	public Handler baseHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1: {// 处理异常信息
				Toast.makeText(getApplicationContext(), exceptionDesc, Toast.LENGTH_SHORT).show();
				closeProcessDialog();
				break;
			}
			case 100: {
				closeProcessDialog();
				break;
			}
			}
		}
	};

}
