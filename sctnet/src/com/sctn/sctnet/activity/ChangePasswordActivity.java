package com.sctn.sctnet.activity;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.Md5Builder;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;

public class ChangePasswordActivity extends BaicActivity {

	private EditText current_password;
	private EditText new_password;
	private EditText new_repassword;
	private Button change_password_submit;

	private String currentPassword;
	private String newPassword;
	private String newRePassword;
	private String newMD5Password;// 加密之后的密码

	private long sp_userId;
	private String sp_username;
	private String sp_password;

	private String response;// 服务器返回的结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password_activity);
		setTitleBar(getString(R.string.change_password), View.VISIBLE,
				View.GONE);

		initAllView();
		reigesterAllEvent();
	}

	@Override
	protected void initAllView() {

		current_password = (EditText) findViewById(R.id.current_password);
		new_password = (EditText) findViewById(R.id.new_password);
		new_repassword = (EditText) findViewById(R.id.new_repassword);
		change_password_submit = (Button) findViewById(R.id.change_password_submit);

		// 取出登录时本地保存的用户ID、用户名和密码
		sp_userId = SharePreferencesUtils.getSharedlongData("userId");
		sp_username = SharePreferencesUtils.getSharedStringData("userName");
		sp_password = SharePreferencesUtils.getSharedStringData("password");
//		sp_password = "123456";
	}

	@Override
	protected void reigesterAllEvent() {

		// 修改密码
		change_password_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				currentPassword = current_password.getText().toString();
				newPassword = new_password.getText().toString();
				newRePassword = new_repassword.getText().toString();

				if (StringUtil.isBlank(currentPassword)) {
					Toast.makeText(getApplicationContext(), "请输入当前密码",
							Toast.LENGTH_LONG).show();
				} else if (!currentPassword.equals(sp_password)) {
					Toast.makeText(getApplicationContext(), "当前密码输入有误，请重新输入",
							Toast.LENGTH_LONG).show();
				} else if (StringUtil.isBlank(newPassword)) {
					Toast.makeText(getApplicationContext(), "请输入新密码",
							Toast.LENGTH_LONG).show();
				} else if (newPassword.length() < 6
						|| newPassword.length() > 16) {
					Toast.makeText(getApplicationContext(), "密码长度为6~16位",
							Toast.LENGTH_LONG).show();
				} else if (StringUtil.isBlank(newRePassword)) {
					Toast.makeText(getApplicationContext(), "请确认密码",
							Toast.LENGTH_LONG).show();
				} else if (newRePassword.length() < 6
						|| newRePassword.length() > 16) {
					Toast.makeText(getApplicationContext(), "密码长度为6~16位",
							Toast.LENGTH_LONG).show();
				} else if (!newPassword.equals(newRePassword)) {
					Toast.makeText(getApplicationContext(), "两次输入的密码不一致，请重新输入",
							Toast.LENGTH_LONG).show();
				} else {// 终于可以提交了
					newMD5Password = Md5Builder.getMd5(newPassword);
					changePasswordThread();
				}

			}

		});

	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void changePasswordThread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						changePassword();
					}
				});
		mThread.start();
	}

	/**
	 * 请求数据，并将返回结果显示在界面上
	 */
	private void changePassword() {

		String url = "appPersonCenter!modifyPwd.app";

		Message msg = new Message();
		try {
			// Userid;userPwd;userTxtPwd
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid",sp_userId+""));
			params.add(new BasicNameValuePair("userPwd", newPassword));
			params.add(new BasicNameValuePair("userTxtPwd", newMD5Password));
			response = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(response)) {
				ChangePasswordActivity.this.sendExceptionMsg(response);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(response);
			if ("0".equals(responseJsonObject.getString("resultCode"))) {
				msg.what = 0;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
		}
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if(msg.what == 0){
				SharePreferencesUtils.setSharedStringData("password", newPassword);
				Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
				finish();
			}
			closeProcessDialog();
		}
	};

}
