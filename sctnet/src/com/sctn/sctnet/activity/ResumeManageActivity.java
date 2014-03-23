package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * 简历管理界面
 * 
 * @author xueweiwei
 * 
 */
public class ResumeManageActivity extends BaicActivity {

	private ImageView resumePreviewImg;
	private ImageView modifyImg;
	private Button modifyBtn;
	private ImageView deleteImg;
	private Button deleteBtn;
	private ImageView refreshImg;
	private Button refreshBtn;
	private ImageView isPublicImg;
	private Button isPublicBtn;

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
	private float i=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_manager_activity);
		setTitleBar(getString(R.string.resumeManageActivityTitle), View.VISIBLE, View.VISIBLE);
		super.setTitleRightButtonImg(R.drawable.log_off_bg);
		cacheProcess = new CacheProcess();
		userId = cacheProcess.getLongCacheValueInSharedPreferences(this, "userId");
		initAllView();
		reigesterAllEvent();
		initDataTread();

	}

	@Override
	protected void initAllView() {
		
		myPhoto = (ImageView) findViewById(R.id.myPhoto);

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
		resumePublicValue = (TextView) findViewById(R.id.resumeIsPublicText);
		userId = SharePreferencesUtils.getSharedlongData("userId");
		Bitmap bitmap = asyncBitmapLoader.loadBitmap(myPhoto, userId+"", userId+"", true, 120, 120, new ImageCallBack() {
			@Override
			public void imageLoad(ImageView imageView, Bitmap bitmap) {
				myPhoto.setImageBitmap(bitmap);
//				if (bitmap != null) {
//					user.setAvatarBitmap(bitmap);
//				}
			}
		});
		if (bitmap != null) {
			myPhoto.setImageBitmap(bitmap);
			
		}
	}

	@Override
	protected void reigesterAllEvent() {

		resumePreviewImg.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ResumeManageActivity.this, ResumePreviewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("resumeInfo", dataList);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		modifyImg.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				toResumeEditActivity();
			}

		});
		modifyBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				modifyImg.setPressed(true);
				toResumeEditActivity();
			}

		});

		deleteImg.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				deleteDialog();
			}

		});

		deleteBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteImg.setPressed(true);
				deleteDialog();
			}

		});

		refreshImg.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 刷新简历thread
				refreshTread();

			}

		});

		refreshBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				refreshImg.setPressed(true);
				// 刷新简历thread
				refreshTread();
			}

		});

		isPublicImg.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				displayOrNotTread();

			}

		});

		isPublicBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				isPublicImg.setPressed(true);

				displayOrNotTread();

			}

		});
		
		myPhoto.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				cameraUtil = new CameraUtil(ResumeManageActivity.this, new CameraCallBack() {
					@Override
					public void cropResult(Context context, Intent data) {
						Bundle extras = data.getExtras();
						if (extras != null) {
							myPhotoBitmap = extras.getParcelable("data");
							myPhoto.setImageBitmap(myPhotoBitmap);
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
				// 将本地保存的登录信息清空
				LoginInfo.logOut();
				// ->直接跳转到HomeActivity 同时清空栈中 HomeActivity 之前的 Activity
				Toast.makeText(ResumeManageActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ResumeManageActivity.this, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 利用ClearTop标志
				startActivity(intent);
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
	
	private void updateData(){
		String url = "appPersonCenter!saveFileImage.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid",userId+""));
			params.add(new BasicNameValuePair("byteimage",BitMapUtil.bitMap2Base64Encode(myPhotoBitmap)));

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
		}catch (JSONException e) {
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
			params.add(new BasicNameValuePair("Userid","100020"));

			result = getPostHttpContent(url, params);

			if (StringUtil.isExcetionInfo(result)) {
				ResumeManageActivity.this.sendExceptionMsg(result);
				return;
			}

			if (StringUtil.isBlank(result)) {// 说明该用户没有创建简历

				msg.what = 1;
				handler.sendMessage(msg);

				return;
			}

			JSONObject responseJsonObject = null;// 返回结果存放在该json对象中

			// JSON的解析过程
			responseJsonObject = new JSONObject(result);
			if (responseJsonObject.getInt("resultCode") == 0) {// 获得响应结果

				JSONObject resultJsonObject = responseJsonObject.getJSONObject("result");

				String accountCity = resultJsonObject.getString("accountcity");
				String address = resultJsonObject.getString("address");
				String adminpost = resultJsonObject.getString("adminpost");
				String aidprofession = resultJsonObject.getString("aidprofession");
				String birthday = resultJsonObject.getString("birthday");
				String birthplace = resultJsonObject.getString("birthplace");
				String cardid = resultJsonObject.getString("cardid");
				String companyname = resultJsonObject.getString("companyname");
				String computerlevel = resultJsonObject.getString("computerlevel");
				String contactsname = resultJsonObject.getString("contactsname");
				String contactsphone = resultJsonObject.getString("contactsphone");
				String currentcity = resultJsonObject.getString("currentcity");
				String currentprofessional = resultJsonObject.getString("currentprofessional");
				String degree = resultJsonObject.getString("degree");
				String degreecert = resultJsonObject.getString("degreecert");
				String drivecode = resultJsonObject.getString("drivecode");
				String education = resultJsonObject.getString("education");
				String email = resultJsonObject.getString("email");
				String graduatedcode = resultJsonObject.getString("graduatedcode");
				String graduateddate = resultJsonObject.getString("graduateddate");
				String graduatedschool = resultJsonObject.getString("graduatedschool");
				String healthstate = resultJsonObject.getString("healthstate");
				String marriagestate = resultJsonObject.getString("marriagestate");
				String oneenglish = resultJsonObject.getString("oneenglish");
				String onelevel = resultJsonObject.getString("onelevel");
				String people = resultJsonObject.getString("people");
				String political = resultJsonObject.getString("political");
				String postalcode = resultJsonObject.getString("postalcode");
				String profession = resultJsonObject.getString("profession");
				String qqmsn = resultJsonObject.getString("qqmsn");
				String reccontent = resultJsonObject.getString("reccontent");
				String resume = resultJsonObject.getString("resume");
				String sex = resultJsonObject.getString("sex");
				String specialtycontent = resultJsonObject.getString("specialtycontent");
				String technology = resultJsonObject.getString("technology");
				String truename = resultJsonObject.getString("truename");
				String twoenglish = resultJsonObject.getString("twoenglish");
				String twolevel = resultJsonObject.getString("twolevel");
				long useheight = resultJsonObject.getLong("useheight");
				String usephone = resultJsonObject.getString("usephone");
				long workexperience = resultJsonObject.getLong("workexperience");
				String workperformance = resultJsonObject.getString("workperformance");

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
					i++;
				}
				 if(StringUtil.isBlank(resume)){
				    personalExperienceMap.put("个人经历", resume);
				 }

				if (!StringUtil.isBlank(sex)) {
					basicInfoMap.put("性别", sex);
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
					basicInfoMap.put("姓名", truename);
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
				if (!StringUtil.isBlank(useheight)) {
					basicInfoMap.put("身高", Long.toString(useheight));
					i++;
				}
				if (!StringUtil.isBlank(usephone)) {
					contactMap.put("本人手机号", usephone);
					i++;
				}
				if (!StringUtil.isBlank(workexperience)) {
					workExperienceMap.put("工作年限", Long.toString(workexperience));
					i++;
				}
				if (!StringUtil.isBlank(workperformance)) {
					workExperienceMap.put("工作业绩", workperformance);
					i++;
				}

				basicInfoList.add(basicInfoMap);
				personalExperienceList.add(personalExperienceMap);
				workExperienceList.add(workExperienceMap);
				educationExperienceList.add(educationExperienceMap);
				contactList.add(contactMap);
				jobIntentionList.add(jobIntentionMap);

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
				Intent intent = new Intent(ResumeManageActivity.this, ResumeCreateActivity.class);
				startActivity(intent);
				finish();
			}
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "上传成功",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:{
				Toast.makeText(getApplicationContext(), "删除成功",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ResumeManageActivity.this, ResumeCreateActivity.class);
				startActivity(intent);
				finish();
			}break;
			case 4:
				Toast.makeText(getApplicationContext(), "删除失败",
						Toast.LENGTH_SHORT).show();
				break;
			case 5:{
				if("隐藏".equals(isPublicBtn.getText().toString())){
					isPublicImg.setImageResource(R.drawable.resume_is_public_bg);
					isPublicBtn.setText("公开");
				}else{
					isPublicImg.setImageResource(R.drawable.resume_is_secret_bg);
					isPublicBtn.setText("隐藏");
				}
			}break;
			case 6:
				Toast.makeText(getApplicationContext(), "操作失败",
						Toast.LENGTH_SHORT).show();
				break;
			case 7:
				Toast.makeText(getApplicationContext(), "刷新成功，您的简历会更好的被猎头发现了！",
						Toast.LENGTH_SHORT).show();
				break;
			case 8:
				Toast.makeText(getApplicationContext(), "刷新失败，再试试吧",
						Toast.LENGTH_SHORT).show();
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
		resumeUpdateValue.setText(resumeInfo.getUpresumetime());
		finishStatus = (i/41)*100+"%";
		resumeFinishStatusValue.setText(finishStatus);
		resumePublicValue.setText(resumeInfo.getIshide() + "");

		if (resumeInfo.getIshide() == 0) {
			isPublicImg.setImageResource(R.drawable.resume_is_secret_bg);
			isPublicBtn.setText("隐藏");
		} else {
			isPublicImg.setImageResource(R.drawable.resume_is_public_bg);
			isPublicBtn.setText("公开");
		}

	}

	// 跳转到简历修改界面
	private void toResumeEditActivity() {
		Intent intent = new Intent(ResumeManageActivity.this, ResumeEditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("resumeInfo", dataList);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	// 删除提示框
	public void deleteDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确定要删除吗?");
		builder.setTitle("提示");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 删除线程
				dialog.dismiss();
				deleteTread();
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
	
	private void deleteData(){
		String url = "appRegister!deleteResume.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid",userId+""));

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
				msg.what = 3;
				handler.sendMessage(msg);
			}else{
				msg.what = 4;
				handler.sendMessage(msg);
			}
		}catch (JSONException e) {
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
	
	private void refreshData(){
		
		String url = "appPersonInfo!topResume.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid",userId+""));

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
			}else{
				msg.what = 8;
				handler.sendMessage(msg);
			}
		}catch (JSONException e) {
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
	
	private void displayOrNotData(){
		
		String url = "appRegister!displayorNotResume.app";

		Message msg = new Message();
		try {

			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Userid",userId+""));

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
				msg.what = 5;
				handler.sendMessage(msg);
			}else{
				msg.what = 6;
				handler.sendMessage(msg);
			}
		}catch (JSONException e) {
			String err = StringUtil.getAppException4MOS("解析json出错！");
			this.sendExceptionMsg(err);
		}		

	}
}
