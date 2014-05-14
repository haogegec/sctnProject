package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.BitMapUtil;
import com.sctn.sctnet.Utils.CameraCallBack;
import com.sctn.sctnet.Utils.CameraUtil;
import com.sctn.sctnet.Utils.SharePreferencesUtils;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.cache.CacheProcess;
import com.sctn.sctnet.entity.LoginInfo;
import com.sctn.sctnet.entity.ResumeInfo;
import com.sctn.sctnet.httpConnect.AsyncBitmapLoader;
import com.sctn.sctnet.httpConnect.AsyncBitmapLoader.ImageCallBack;
import com.sctn.sctnet.view.CustomDialog;

/**
 * 简历管理界面
 * 
 * @author xueweiwei
 * 
 */
public class ResumeManageActivity extends BaicActivity {

	private RelativeLayout layout;
	private RelativeLayout layout1;

	private ImageView resumePreviewImg;
	private ImageView modifyImg;
	private Button modifyBtn;
	private ImageView deleteImg;
	private Button deleteBtn;
	private ImageView refreshImg;
	private Button refreshBtn;
	private ImageView isPublicImg;
	private Button isPublicBtn;

	private ImageView myHeadPhoto;
	private ImageView myPhoto;
	private TextView resumeNameValue;
	private TextView resumeUpdateValue;
	private TextView resumeFinishStatusValue;
	private TextView resumePublicValue;

	private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();// 异步加载图片

	private long userId;// 用户唯一标识

	private CacheProcess cacheProcess;// 缓存数据

	private String result;// 服务端返回结果数据

	private ResumeInfo resumeInfo;// 简历表所对应的类

	private String finishStatus;// 简历完成度

	private CameraUtil cameraUtil;
	private Bitmap myPhotoBitmap;

	private HashMap<String, String> basicInfoMap = new HashMap<String, String>();// 基本信息
	private HashMap<String, String> personalExperienceMap = new HashMap<String, String>();// 个人简介
	private HashMap<String, String> educationExperienceMap = new HashMap<String, String>();// 教育情况
	private HashMap<String, String> workExperienceMap = new HashMap<String, String>();// 职业生涯
	private HashMap<String, String> jobIntentionMap = new HashMap<String, String>();// 求职意向
	private HashMap<String, String> contactMap = new HashMap<String, String>();// 联系方式

	private ArrayList<HashMap<String, String>> basicInfoList = new ArrayList<HashMap<String, String>>();// 基本信息
	private ArrayList<HashMap<String, String>> personalExperienceList = new ArrayList<HashMap<String, String>>();// 个人简介
	private ArrayList<HashMap<String, String>> educationExperienceList = new ArrayList<HashMap<String, String>>();// 教育情况
	private ArrayList<HashMap<String, String>> workExperienceList = new ArrayList<HashMap<String, String>>();// 职业生涯
	private ArrayList<HashMap<String, String>> jobIntentionList = new ArrayList<HashMap<String, String>>();// 求职意向
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();// 联系方式

	private ArrayList<ArrayList<HashMap<String, String>>> dataList = new ArrayList<ArrayList<HashMap<String, String>>>();
	private float i = 0;

	private SharedPreferences sharedPreferences;

	private String isOpen;
	
	private ArrayList<String> flagIdList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_manager_activity);
		setTitleBar(getString(R.string.resumeManageActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		cacheProcess = new CacheProcess();
		userId = cacheProcess.getLongCacheValueInSharedPreferences(this, "userId");
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		initAllView();
		reigesterAllEvent();
		initDataTread();

	}

	@Override
	protected void initAllView() {
		layout = (RelativeLayout) findViewById(R.id.relativeLayout);
		layout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);

		myPhoto = (ImageView) findViewById(R.id.myPhoto);
		myHeadPhoto = (ImageView) findViewById(R.id.myPhoto1);

		resumePreviewImg = (ImageView) findViewById(R.id.resumePreview);

		modifyImg = (ImageView) findViewById(R.id.resumeModifyImg);
		modifyBtn = (Button) findViewById(R.id.resumeModifyText);

		deleteImg = (ImageView) findViewById(R.id.resumeDeleteImg);
		deleteBtn = (Button) findViewById(R.id.resumeDeleteText);

		refreshImg = (ImageView) findViewById(R.id.resumeRefreshImg);
		refreshBtn = (Button) findViewById(R.id.resumeFefreshText);

		isPublicImg = (ImageView) findViewById(R.id.resumeIsPublicImg);
		isPublicBtn = (Button) findViewById(R.id.resumeIsPublicText);

		// 要更新的值
		resumeNameValue = (TextView) findViewById(R.id.resumeName);
		resumeUpdateValue = (TextView) findViewById(R.id.resumeUpdateValue);
		resumeFinishStatusValue = (TextView) findViewById(R.id.resumeFinishStatusValue);
		resumePublicValue = (TextView) findViewById(R.id.resumePublicValue);
		userId = SharePreferencesUtils.getSharedlongData("userId");
		Bitmap bitmap = asyncBitmapLoader.loadBitmap(myHeadPhoto, userId + ".jpg", userId + "", true, 180, 180, new ImageCallBack() {

			@Override
			public void imageLoad(ImageView imageView, Bitmap bitmap) {
				myHeadPhoto.setImageBitmap(bitmap);
				// if (bitmap != null) {
				// user.setAvatarBitmap(bitmap);
				// }
			}
		});
		if (bitmap != null) {
			myHeadPhoto.setImageBitmap(bitmap);

		}
	}

	@Override
	protected void reigesterAllEvent() {

		// 简历预览
		resumePreviewImg.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dataList.size() == 0) {
					resumePreviewImg.setClickable(false);
				} else {
					Intent intent = new Intent(ResumeManageActivity.this, ResumePreviewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("resumeInfo", dataList);
					intent.putExtras(bundle);
					startActivity(intent);
				}

			}

		});

		// 修改
		modifyImg.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				toResumeEditActivity();
			}

		});
		// 修改
		modifyBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				modifyImg.setPressed(true);
				toResumeEditActivity();
			}

		});

		// 删除
		deleteImg.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				deleteDialog();
			}

		});

		// 删除
		deleteBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteImg.setPressed(true);
				deleteDialog();
			}

		});

		// 刷新
		refreshImg.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 刷新简历thread
				refreshTread();

			}

		});

		// 刷新
		refreshBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				refreshImg.setPressed(true);
				// 刷新简历thread
				refreshTread();
			}

		});

		// 公开
		isPublicImg.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (isOpen.equals("0")) {
					isOpen = "1";
				} else {
					isOpen = "0";
				}
				displayOrNotTread();

			}

		});

		// 公开
		isPublicBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				isPublicImg.setPressed(true);

				if (isOpen.equals("0")) {
					isOpen = "1";
				} else {
					isOpen = "0";
				}
				displayOrNotTread();

			}

		});

		// 头像
		myPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				cameraUtil = new CameraUtil(ResumeManageActivity.this, new CameraCallBack() {
					@Override
					public void cropResult(Context context, Intent data) {
						Bundle extras = data.getExtras();
						if (extras != null) {
							myPhotoBitmap = extras.getParcelable("data");
							myHeadPhoto.setImageBitmap(myPhotoBitmap);
							updateUserInfo();
						}

					}

					@Override
					public void upLoadImageResult(Context context, Intent data) {
						// 不需要重写
					}

				});
				cameraUtil.showUplaodImageDialog();
			}

		});

		// 注销
		super.titleRightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//				new AlertDialog.Builder(ResumeManageActivity.this).setTitle("提示").setMessage("确定要注销吗？").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
				//					@Override
				//					public void onClick(DialogInterface dialog, int which) {
				//						// 将本地保存的登录信息清空
				//						LoginInfo.logOut();
				//						// ->直接跳转到 HomeActivity(设置成单例) 同时清空栈中 HomeActivity 之前的
				//						// Activity
				//						Toast.makeText(ResumeManageActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
				//						Intent intent = new Intent(ResumeManageActivity.this, HomeActivity.class);
				//		//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 利用ClearTop标志
				//						startActivity(intent);
				//						finish();
				//					}
				//				}).setNegativeButton("取消", null).show();
				//
				//			}
				//		});

				final CustomDialog dialog = new CustomDialog(ResumeManageActivity.this, R.style.CustomDialog);
				//				dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
				dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
				dialog.setTitle("友情提示");
				dialog.setMessage("确定要注销吗？");
				dialog.setOnPositiveListener("确定", new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 将本地保存的登录信息清空
						LoginInfo.logOut();
						Toast.makeText(ResumeManageActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(ResumeManageActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
					}

				});
				dialog.setOnNegativeListener("取消", new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}

				});
				dialog.show();

			}
		});

	}

	/**
	 * 更新头像
	 */
	private void updateUserInfo() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						updateData();
					}
				});
		mThread.start();
	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void initDataTread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						initData();
					}
				});
		mThread.start();
	}

	private void updateData() {
		String url = "appPersonCenter!saveFileImage.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid", userId + ""));
			params.add(new BasicNameValuePair("byteimage", BitMapUtil.bitMap2Base64Encode(myPhotoBitmap)));

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				ResumeManageActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {// 说明该用户没有创建简历

				String err = StringUtil.getAppException4MOS("未获取服务端响应！");
				ResumeManageActivity.this.sendExceptionMsg(err);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果
				msg.what = 2;
				handler.sendMessage(msg);
			}
		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			ResumeManageActivity.this.sendExceptionMsg(err);
		}
	}

	/**
	 * 请求数据，并将返回结果显示在界面上
	 */
	private void initData() {

		String url = "appPersonInfo.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid", userId + ""));
			// params.add(new BasicNameValuePair("Userid",100020+""));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				ResumeManageActivity.this.sendExceptionMsg(result);
				return;
			}

			boolean isResumeNull = false;// 标识是否有简历，true表示有，false表示没有
			// JSON的解析过程
			JSONObject responseJsonObject = new JSONObject(result);
			JSONArray responseJsonArray = responseJsonObject.getJSONArray("result");

//			// 循环保存flagid（取出五个求职意向的id，存到本地，更改求职意向的时候用到）
//			for(int i = 0; i < responseJsonArray.length(); i++){
//				JSONObject jObject = (JSONObject) responseJsonArray.get(i);
//				String flagId = jObject.getString("flagid");
//				if (StringUtil.isBlank(jObject.getString("reccontent"))) {
//					if(!SharePreferencesUtils.getSharedBooleanData(flagId)){
//						SharePreferencesUtils.setSharedStringData(i+"", flagId);
//						SharePreferencesUtils.setSharedBooleanData(flagId, false);// false表示当前的求职意向是空。
//						
////						HashMap<String, String> map = new HashMap<String, String>();// 求职意向
////						map.put("flagId", flagId);
////						jobIntentionList.add(map);
//					}
//					
//				}
//			}
			
//			for (int i = 0; i < responseJsonArray.length(); i++) {
//				JSONObject jObject = (JSONObject) responseJsonArray.get(i);
//				if (!StringUtil.isBlank(jObject.getString("reccontent"))) {
//					isResumeNull = true;
//					break;
//				}
//			}

//			if (!isResumeNull) {// 说明该用户没有创建简历
//				msg.what = 1;
//				handler.sendMessage(msg);
//				return;
//			}

			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONArray resultJsonArray = responseJsonObject.getJSONArray("result");

				JSONObject resultJsonObject = resultJsonArray.getJSONObject(0);
				
//				if (resultJsonObject.getString("reccontent").equals("") && resultJsonObject.getString("housesubsidy").equals("")) {
//					msg.what = 1;
//					handler.sendMessage(msg);
//					return;
//				}
				
				
				for(int i=0; i<resultJsonArray.length(); i++){
					JSONObject jObject = resultJsonArray.getJSONObject(i);
					String flagId = jObject.getString("flagid");
					flagIdList.add(flagId);
				}
				Editor editor = sharedPreferences.edit();

				editor.putBoolean(userId + "", true);
				editor.commit();

				String accountCity = resultJsonObject.getString("accountcityname");// 户口所在地，accountcity是编号
				String address = resultJsonObject.getString("address");
				String adminpost = resultJsonObject.getString("adminpostname");// 当前从事职位
				String aidprofession = resultJsonObject.getString("aidprofessionname");// 辅助专业,aidprofession是编号
				String birthday = resultJsonObject.getString("birthday");
				String birthplace = resultJsonObject.getString("birthplacename");// 籍贯
				String cardid = resultJsonObject.getString("cardid");// 身份证号
				String companyname = resultJsonObject.getString("companyname");
				String computerlevel = resultJsonObject.getString("computerlevelname");// 微机水平，computerlevel是编号
				String contactsname = resultJsonObject.getString("contactsname");
				String contactsphone = resultJsonObject.getString("contactsphone");
				String currentcity = resultJsonObject.getString("currentcity");
				String currentprofessional = resultJsonObject.getString("currentprofessionalname");// 当前从事行业，currentprofessional是编号
				String degree = resultJsonObject.getString("degree");
				String degreecert = resultJsonObject.getString("degreecert");
				String drivecode = resultJsonObject.getString("drivecode");
				String education = resultJsonObject.getString("educationname");// 学历，education是编号
				String email = resultJsonObject.getString("email");
				String graduatedcode = resultJsonObject.getString("graduatedcode");
				String graduateddate = resultJsonObject.getString("graduateddate");// 需要格式化
				String graduatedschool = resultJsonObject.getString("graduatedschool");
				String healthstate = resultJsonObject.getString("healthstatename");// healthstate是编号
				String marriagestate = resultJsonObject.getString("marriagestatename");// 婚姻状况，marriagestate是编号
				String oneenglish = resultJsonObject.getString("oneenglishname");// oneenglish是编号
				String onelevel = resultJsonObject.getString("onelevelname");// onelevel是编号
				String people = resultJsonObject.getString("peoplename");// 民族,people是编号
				String political = resultJsonObject.getString("politicalname");// 政治面貌，political是编号
				String postalcode = resultJsonObject.getString("postalcode");
				String profession = resultJsonObject.getString("professionname");// 专业，profession是编号
				String qqmsn = resultJsonObject.getString("qqmsn");
				String reccontent = resultJsonObject.getString("reccontent");
				String resume = resultJsonObject.getString("resume");
				String sex = resultJsonObject.getString("sex");
				String specialtycontent = resultJsonObject.getString("specialtycontent");
				String technology = resultJsonObject.getString("technology");
				String truename = resultJsonObject.getString("truename");
				String twoenglish = resultJsonObject.getString("twoenglishname");// twoengligh是编号
				String twolevel = resultJsonObject.getString("twolevelname");// twolevel是编号
				long useheight = resultJsonObject.getLong("useheight");
				String usephone = resultJsonObject.getString("usephone");
				long workexperience = resultJsonObject.getLong("workexperience");
				String workperformance = resultJsonObject.getString("workperformance");
				
				
				for(int i=0; i<resultJsonArray.length(); i++){
					
					JSONObject object = resultJsonArray.getJSONObject(i);
					
					String housesubsidy = object.getString("housesubsidy");
					String jobsstate = object.getString("jobsstate");
					String companytype = object.getString("companytype");
					String wagename = object.getString("wagename");
					String workmannername = object.getString("workmannername");
					String workregionname = object.getString("workregionname");
					String post = object.getString("postcodename");
					String flagid = object.getString("flagid");
					
					HashMap<String, String> map = new HashMap<String, String>();
					
					if (!StringUtil.isBlank(housesubsidy)) {
						map.put("住房要求", housesubsidy);
					}
					if (!StringUtil.isBlank(jobsstate)) {
						map.put("工作性质", jobsstate);
					}
					if (!StringUtil.isBlank(companytype)) {
						map.put("企业性质", companytype);
					}
					if (!StringUtil.isBlank(wagename)) {
						map.put("月薪要求", wagename);
					}
					if (!StringUtil.isBlank(workmannername)) {
						map.put("欲从事行业", workmannername);
					}
					if (!StringUtil.isBlank(post)) {
						map.put("欲从事岗位", post);
					}
					if (!StringUtil.isBlank(workregionname)) {
						map.put("工作地区", workregionname);
					}
					if (!StringUtil.isBlank(flagid)) {
						map.put("flagId", flagid);
					}
					jobIntentionList.add(map);
				}
				
				String housesubsidy = resultJsonObject.getString("housesubsidy");
				String jobsstate = resultJsonObject.getString("jobsstate");
				String companytype = resultJsonObject.getString("companytype");
				String wagename = resultJsonObject.getString("wagename");
				String workmannername = resultJsonObject.getString("workmannername");
				String workregionname = resultJsonObject.getString("workregionname");
				String post = resultJsonObject.getString("postcodename");

				if (!StringUtil.isBlank(housesubsidy)) {
					jobIntentionMap.put("住房要求", housesubsidy);
					i++;
				}
				if (!StringUtil.isBlank(jobsstate)) {
					jobIntentionMap.put("工作性质", jobsstate);
					i++;
				}
				if (!StringUtil.isBlank(companytype)) {
					jobIntentionMap.put("企业性质", companytype);
					i++;
				}
				if (!StringUtil.isBlank(wagename)) {
					jobIntentionMap.put("月薪要求", wagename);
					i++;
				}
				if (!StringUtil.isBlank(workmannername)) {
					jobIntentionMap.put("欲从事行业", workmannername);
					i++;
				}
				if (!StringUtil.isBlank(post)) {
					jobIntentionMap.put("欲从事岗位", post);
					i++;
				}
				if (!StringUtil.isBlank(workregionname)) {
					jobIntentionMap.put("工作地区", workregionname);
					i++;
				}
				if (!StringUtil.isBlank(birthplace)) {
					basicInfoMap.put("籍贯", birthplace);
					i++;
				}
				if (!StringUtil.isBlank(accountCity)) {
					basicInfoMap.put("户口所在地", accountCity);
					i++;
				}
				if (!StringUtil.isBlank(address)) {
					basicInfoMap.put("地址", address);
					i++;
				}
				if (!StringUtil.isBlank(birthday)) {
					basicInfoMap.put("出生日期", birthday);
					i++;
				}

				if (!StringUtil.isBlank(cardid)) {
					basicInfoMap.put("身份证号", cardid);
					i++;
				}

				if (!StringUtil.isBlank(adminpost)) {
					workExperienceMap.put("当前从事职业", adminpost);
					i++;
				}
				if (!StringUtil.isBlank(aidprofession)) {
					educationExperienceMap.put("辅助专业", aidprofession);
					i++;
				}

				if (!StringUtil.isBlank(companyname)) {
					workExperienceMap.put("当前公司", companyname);
					i++;
				}
				if (!StringUtil.isBlank(computerlevel)) {
					educationExperienceMap.put("微机水平", computerlevel);
					i++;
				}
				if (!StringUtil.isBlank(contactsname)) {
					contactMap.put("联系人", contactsname);
					i++;
				}
				if (!StringUtil.isBlank(contactsphone)) {
					contactMap.put("联系人电话", contactsphone);
					i++;
				}
				if (!StringUtil.isBlank(currentcity)) {
					basicInfoMap.put("当前城市", currentcity);
					i++;
				}
				if (!StringUtil.isBlank(currentprofessional)) {
					workExperienceMap.put("当前从事行业", currentprofessional);
					i++;
				}
				if (!StringUtil.isBlank(degree)) {
					educationExperienceMap.put("学位", degree);
					i++;
				}
				if (!StringUtil.isBlank(degreecert)) {
					educationExperienceMap.put("学位证号", degreecert);
					i++;
				}
				if (!StringUtil.isBlank(drivecode)) {
					basicInfoMap.put("驾驶证号", drivecode);
					i++;
				}
				if (!StringUtil.isBlank(education)) {
					educationExperienceMap.put("学历", education);
					i++;
				}
				if (!StringUtil.isBlank(email)) {
					contactMap.put("邮箱", email);
					i++;
				}
				if (!StringUtil.isBlank(graduatedcode)) {
					educationExperienceMap.put("毕业证号", graduatedcode);
					i++;
				}
				if (!StringUtil.isBlank(graduateddate)) {
					educationExperienceMap.put("毕业日期", graduateddate);
					i++;
				}
				if (!StringUtil.isBlank(graduatedschool)) {
					educationExperienceMap.put("毕业学校", graduatedschool);
					i++;
				}
				if (!StringUtil.isBlank(healthstate)) {
					basicInfoMap.put("健康状况", healthstate);
					i++;
				}
				if (!StringUtil.isBlank(marriagestate)) {
					basicInfoMap.put("婚姻状况", marriagestate);
					i++;
				}
				if (!StringUtil.isBlank(oneenglish)) {
					educationExperienceMap.put("第一外语", oneenglish);
					i++;
				}
				if (!StringUtil.isBlank(onelevel)) {
					educationExperienceMap.put("第一外语水平", onelevel);
					i++;
				}
				if (!StringUtil.isBlank(people)) {
					basicInfoMap.put("民族", people);
					i++;
				}
				if (!StringUtil.isBlank(political)) {
					basicInfoMap.put("政治面貌", political);
					i++;
				}
				if (!StringUtil.isBlank(postalcode)) {
					contactMap.put("邮政编码", postalcode);
					i++;
				}
				if (!StringUtil.isBlank(profession)) {
					educationExperienceMap.put("专业", profession);
					i++;
				}
				if (!StringUtil.isBlank(qqmsn)) {
					contactMap.put("QQ", qqmsn);
					i++;
				}
				if (!StringUtil.isBlank(reccontent)) {
					personalExperienceMap.put("推荐自己", reccontent);
					basicInfoMap.put(" ", "");
					workExperienceMap.put(" ", "");
					educationExperienceMap.put(" ", "");
					contactMap.put(" ", "");
					i++;
				}
				if (!StringUtil.isBlank(resume)) {
					personalExperienceMap.put("个人经历", resume);
					i++;
				}

				if (!StringUtil.isBlank(sex)) {
					if (sex.equals("0")) {
						basicInfoMap.put("性别", "女");
					} else {
						basicInfoMap.put("性别", "男");
					}

					i++;
				}
				if (!StringUtil.isBlank(specialtycontent)) {
					personalExperienceMap.put("特长", specialtycontent);
					i++;
				}
				if (!StringUtil.isBlank(technology)) {
					educationExperienceMap.put("专业职称", technology);
					i++;
				}
				if (!StringUtil.isBlank(truename)) {
					String name = truename.replace("\\s", "");
					name = name.replace("\n", "");
					basicInfoMap.put("姓名", name);
					i++;
				}
				if (!StringUtil.isBlank(twoenglish)) {
					educationExperienceMap.put("第二外语", twoenglish);
					i++;
				}
				if (!StringUtil.isBlank(twolevel)) {
					educationExperienceMap.put("第二外语水平", twolevel);
					i++;
				}
				if (!StringUtil.isBlank(useheight) && useheight != 0) {
					basicInfoMap.put("身高", Long.toString(useheight));
					i++;
				}
				if (!StringUtil.isBlank(usephone)) {
					contactMap.put("本人手机号", usephone);
					i++;
				}
				if (!StringUtil.isBlank(workexperience) && workexperience != 0) {
					workExperienceMap.put("工作年限", Long.toString(workexperience));
					i++;
				}
				if (!StringUtil.isBlank(workperformance)) {
					workExperienceMap.put("工作业绩", workperformance);
					i++;
				}

				float temp = i * 100 % 48;
				if(i <= 2 && temp <= 8){// 该用户还没有简历(刚创建账号时  i=2,temp = 8)
					msg.what = 1;
					handler.sendMessage(msg);
					return;
				}
				
				
				basicInfoList.add(basicInfoMap);
				personalExperienceList.add(personalExperienceMap);
				workExperienceList.add(workExperienceMap);
				educationExperienceList.add(educationExperienceMap);
				contactList.add(contactMap);
				

				dataList.add(basicInfoList);
				dataList.add(personalExperienceList);
				dataList.add(workExperienceList);
				dataList.add(educationExperienceList);
				dataList.add(contactList);
				dataList.add(jobIntentionList);

				resumeInfo = new ResumeInfo();

				resumeInfo.setAccountcity(accountCity);
				resumeInfo.setAdapplicationtstate(resultJsonObject.getLong("adapplicationtstate"));
				resumeInfo.setAdapplicationttme(resultJsonObject.getString("adapplicationttme"));
				resumeInfo.setAddress(address);
				resumeInfo.setAdminpost(adminpost);
				resumeInfo.setAdviewendtime(resultJsonObject.getString("adviewendtime"));
				resumeInfo.setAdviewplaytime(resultJsonObject.getString("adviewplaytime"));
				resumeInfo.setAidprofession(aidprofession);

				resumeInfo.setBirthday(birthday);
				resumeInfo.setBirthplace(birthplace);

				resumeInfo.setCardid(cardid);
				resumeInfo.setClicknuum(resultJsonObject.getLong("clicknuum"));
				resumeInfo.setCompanyid(resultJsonObject.getLong("companyid"));
				resumeInfo.setCompanyname(companyname);
				resumeInfo.setComputerlevel(computerlevel);
				resumeInfo.setContactsname(contactsname);
				resumeInfo.setContactsphone(contactsphone);
				resumeInfo.setCurrentcity(currentcity);
				resumeInfo.setCurrentprofessional(currentprofessional);

				resumeInfo.setDegree(degree);
				resumeInfo.setDegreecert(degreecert);
				resumeInfo.setDrivecode(drivecode);

				resumeInfo.setEducation(education);
				resumeInfo.setEmail(email);
				resumeInfo.setEndtime(resultJsonObject.getString("endtime"));

				resumeInfo.setGraduatedcode(graduatedcode);
				resumeInfo.setGraduateddate(graduateddate);
				resumeInfo.setGraduatedschool(graduatedschool);

				resumeInfo.setHealthstate(healthstate);

				resumeInfo.setIshide(resultJsonObject.getInt("ishide"));
				resumeInfo.setIsresumehide(resultJsonObject.getLong("isresumehide"));

				resumeInfo.setJobsid(resultJsonObject.getLong("jobsid"));
				resumeInfo.setJobsname(resultJsonObject.getString("jobsname"));

				resumeInfo.setMarriagestate(marriagestate);

				resumeInfo.setOneenglish(oneenglish);
				resumeInfo.setOnelevel(onelevel);

				resumeInfo.setPeople(people);
				resumeInfo.setPolitical(political);
				resumeInfo.setPostalcode(postalcode);
				resumeInfo.setProfession(profession);

				resumeInfo.setQqmsn(qqmsn);

				resumeInfo.setReccontent(reccontent);
				resumeInfo.setResume(resume);

				resumeInfo.setSex(sex);
				resumeInfo.setSpecialtycontent(specialtycontent);

				resumeInfo.setTechnology(technology);
				resumeInfo.setTruename(truename);
				resumeInfo.setTwoenglish(twoenglish);
				resumeInfo.setTwolevel(twolevel);
				resumeInfo.setType(resultJsonObject.getString("type"));

				resumeInfo.setUpresumetime(resultJsonObject.getString("upresumetime"));
				resumeInfo.setUseheight(useheight);
				resumeInfo.setUsephone(usephone);
				resumeInfo.setUserhead(resultJsonObject.getString("userhead"));
				resumeInfo.setUserid(resultJsonObject.getLong("userid"));

				resumeInfo.setWorkexperience(workexperience);
				resumeInfo.setWorkperformance(workperformance);

				msg.what = 0;
				handler.sendMessage(msg);
			} else {
				String errorResult = (String) responseJsonObject.get("result");
				String err = StringUtil.getAppException4MOS(errorResult);
				ResumeManageActivity.this.sendExceptionMsg(err);
			}

		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			ResumeManageActivity.this.sendExceptionMsg(err);
		}
	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				updateUI();
				break;
			case 1: {
				Editor editor = sharedPreferences.edit();

				editor.putBoolean("hasResume", false);
				editor.commit();
				Intent intent = new Intent(ResumeManageActivity.this, ResumeCreateActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("jobIntentionList", jobIntentionList);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
				break;
			case 3: {
				Editor editor = sharedPreferences.edit();

				editor.putBoolean(userId + "", false);
				editor.commit();
				Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ResumeManageActivity.this, ResumeCreateActivity.class);
				startActivity(intent);
				finish();
			}
				break;
			case 4:
				Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
				break;
			case 5: {
				if ("隐藏".equals(isPublicBtn.getText().toString())) {
					isPublicImg.setImageResource(R.drawable.resume_is_public_bg);
					isPublicBtn.setText("公开");
					Toast.makeText(getApplicationContext(), "现在您的简历就只有您自己可以看到了~~", Toast.LENGTH_SHORT).show();
				} else {
					isPublicImg.setImageResource(R.drawable.resume_is_secret_bg);
					isPublicBtn.setText("隐藏");
					Toast.makeText(getApplicationContext(), "您的简历已公开，所有的猎头都可以看到哟~~", Toast.LENGTH_SHORT).show();
				}
			}
				break;
			case 6:
				Toast.makeText(getApplicationContext(), "操作失败", Toast.LENGTH_SHORT).show();
				break;
			case 7:
				Toast.makeText(getApplicationContext(), "刷新成功，您的简历会更好的被猎头发现了！", Toast.LENGTH_SHORT).show();
				break;
			case 8:
				Toast.makeText(getApplicationContext(), "刷新失败，再试试吧", Toast.LENGTH_SHORT).show();
				break;

			}
			closeProcessDialog();
		}
	};

	/**
	 * 请求完数据，更新界面的数据
	 */
	private void updateUI() {

		resumeNameValue.setText("我的简历");
		if (resumeInfo != null && !StringUtil.isBlank(resumeInfo.getUpresumetime())) {
			resumeUpdateValue.setText(resumeInfo.getUpresumetime().substring(0, 10));
		}

		finishStatus = (int) Math.round(i * 100 / 48) + "%";
		resumeFinishStatusValue.setText(finishStatus);
		// resumePublicValue.setText(resumeInfo.getIshide() + "");

		if (resumeInfo.getIsresumehide() == 1) {
			isPublicImg.setImageResource(R.drawable.resume_is_secret_bg);
			resumePublicValue.setText("公开");
			isPublicBtn.setText("隐藏");
			isOpen = "1";
		} else {
			isPublicImg.setImageResource(R.drawable.resume_is_public_bg);
			resumePublicValue.setText("隐藏");
			isPublicBtn.setText("公开");
			isOpen = "0";

		}

		layout.setVisibility(View.VISIBLE);
		layout1.setVisibility(View.VISIBLE);
	}

	// 跳转到简历修改界面
	private void toResumeEditActivity() {
		Intent intent = new Intent(ResumeManageActivity.this, ResumeEditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("resumeInfo", dataList);
		bundle.putSerializable("flagIdList", flagIdList);
		bundle.putSerializable("jobIntentionList", jobIntentionList);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	// 删除提示框
	public void deleteDialog() {

		final CustomDialog dialog = new CustomDialog(this, R.style.CustomDialog);
		//		dialog.setCanceledOnTouchOutside(false);// 点击dialog外边，对话框不会消失，按返回键对话框消失
		//	dialog.setCancelable(false);// 点击dialog外边、按返回键 对话框都不会消失
		dialog.setTitle("友情提示");
		dialog.setMessage("确定要删除吗?");
		dialog.setOnPositiveListener("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 删除线程
				dialog.dismiss();
				deleteTread();
			}

		});
		dialog.setOnNegativeListener("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		dialog.show();
	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void deleteTread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						deleteData();
					}
				});
		mThread.start();
	}

	private void deleteData() {
		String url = "appRegister!deleteResume.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid", userId + ""));

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				ResumeManageActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {

				String err = StringUtil.getAppException4MOS("未获得服务端反应");
				ResumeManageActivity.this.sendExceptionMsg(err);

				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果
				msg.what = 3;
				handler.sendMessage(msg);
			} else {
				msg.what = 4;
				handler.sendMessage(msg);
			}
		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			this.sendExceptionMsg(err);
		}

	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void refreshTread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						refreshData();
					}
				});
		mThread.start();
	}

	private void refreshData() {

		String url = "appPersonInfo!topResume.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid", userId + ""));
			// params.add(new BasicNameValuePair("Userid",100020+""));
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				ResumeManageActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {

				String err = StringUtil.getAppException4MOS("未获得服务端反应");
				ResumeManageActivity.this.sendExceptionMsg(err);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果
				msg.what = 7;
				handler.sendMessage(msg);
			} else {
				msg.what = 8;
				handler.sendMessage(msg);
			}
		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			this.sendExceptionMsg(err);
		}

	}

	/**
	 * 在子线程与远端服务器交互，请求数据
	 */
	private void displayOrNotTread() {
		showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						displayOrNotData();
					}
				});
		mThread.start();
	}

	private void displayOrNotData() {

		String url = "appRegister!displayorNotResume.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid", userId + ""));
			// params.add(new BasicNameValuePair("Userid",100020+""));
			params.add(new BasicNameValuePair("IsResumeHide", isOpen));// 1是显示，0是隐藏
			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				ResumeManageActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {

				String err = StringUtil.getAppException4MOS("未获得服务端反应");
				ResumeManageActivity.this.sendExceptionMsg(err);
				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultcode") == 0) {// 获得响应结果
				msg.what = 5;
				handler.sendMessage(msg);
			} else {
				msg.what = 6;
				handler.sendMessage(msg);
			}
		} catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			this.sendExceptionMsg(err);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		cameraUtil.onActivityResult(requestCode, resultCode, data);
	}
}
