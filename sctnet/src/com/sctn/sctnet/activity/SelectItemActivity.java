package com.sctn.sctnet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sctn.sctnet.R;
import com.sctn.sctnet.Utils.StringUtil;
import com.sctn.sctnet.activity.JobListActivity.MyAdapter;
import com.sctn.sctnet.contants.Constant;

public class SelectItemActivity extends BaicActivity {

	private ListView listview;

	private List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
	private MyAdapter myAdapter;
	private String result;// 服务端返回的结果

	private String which;

	private String id;// 从SelectJobActivity里传过来的大职位的key
	private String value;// 从SelectJobActivity里传过来的大职位的value

	private String profession;// 选择专业时候用到
	private String professionId;// 选择专业时候用到
	
	// 分页
	private int page = 1;
	private int pageSize = Constant.PageSize;
	private int total = 0;
	private int itemCount; // 当前窗口可见项总数
	private int visibleLastIndex = 0;// 最后的可视项索引
	private View footViewBar;// 下滑加载条
	private int count;// 一次可以显示的条数（=pageSize或者小于）

	// 外语能力
	private String[] languageIds = { "50003600", "50004600", "50003100", "50006900", "50003800", "50012700", "50013200", "50007100", "50002300", "50000600", "00000000" };
	private String[] languages = { "英语", "法语", "德语", "意大利语", "西班牙语", "葡萄牙语", "俄语", "日语", "韩语", "阿拉伯语", "无" };
	private String[] languageLevelIds = { "60001100", "60000600", "60000500", "60001000", "60000900", "60000100", "60000200", "60000300", "60000400", "60001200" };
	private String[] languageLevels = { "国家专业八级", "国家专业六级", "国家专业四级", "大学英语六级", "大学英语四级", "精通", "熟练", "良好", "一般", "差" };

	// 学历
	private String[] educationIds = { "14000900", "14000800", "14000700", "14000600", "14000500", "14000400", "14000300", "14000200", "14001100" };
	private String[] educations = { "博士", "研究生", "本科", "大专", "中专", "技工", "中学", "小学", "其他" };

	// 学位
	private String[] degreeIds = {"21000100","21000200","21000300","21000400"};
	private String[] degrees = {"博士后","博士","硕士","学士"};
	
	// 专业
//	private String[] professionIds = {"13010000","13020000","13030000","13040000","13050000","13060000","13070000","13080000","13090000","13100000","13110000","13140000","13120000","13130000"};
//	private String[] professions = {"哲学","经济学","法学","教育学","文学","历史学","理学","工学","农学","医学","军事学","管理学","其他学科","无"};
	private String[] professionIds = {"30010000","30020000","30020000","30040000","30050000","30060000","30070000","30080000","30090000","30100000","30110000","30120000","30990000","30000000"};
	private String[] professions = {"哲学","经济学","法学","教育学","文学","历史学","理学","工学","农学","医学","军事学","管理学","其他学科","无"};
	
	// 工龄、该行业累计工作时间、担任现职务时间
	private String[] workingYears = { "6个月以下", "6~12个月", "1年", "2年", "3年", "4年", "5年", "6~9年", "10~15年", "16年以上" };
	private String[] workingYearIds = { "1", "6", "12", "24", "36", "48", "60", "72", "120", "192" };

	// 公司规模
	private String[] companyScaleIds = { "10000600", "10000500", "10000400", "10000300", "10000200", "10000100", "10000700" };
	private String[] companyScales = { "10人以下", "10--99人", "100--499人", "500--999人", "1000--2999人", "3000人以上", "其他" };

	// 公司性质
	private String[] companyPropertyIds = { "22000100", "22000200", "22000300", "22000400", "22000500", "22000600", "22000700", "22000800", "22000900", "22001000", "22001200", "22001300", "22001400","22001100" };
	private String[] companyProperties = { "国家机关", "事业单位", "国营企业", "三资企业", "集体企业", "民营企业", "私营企业", "乡镇企业", "有限责任", "股份制企业", "分支", "外商企业", "合资企业", "其他" };

	// 政治面貌
	private String[] politicalIds = {"12000100","12000200","12000300","12000400","12000500","12000600","12000700","12000800","12001100","12001200","12001300","12001400","12001500",};
	private String[] politicals = {"中共党员","中共预备党员","共青团员","民革会员","民盟盟员","民建会员","民进会员","农工党党员","台盟盟员","无党派民主人士","群众","其他党派","不限",};
	
	// 婚姻状况
	private String[] maritalIds = {"20000100","20000200","20000300","20000400","20000500","20000600"};
	private String[] maritals = {"未婚","已婚","丧偶","离婚","其他","不详"};
	
	// 健康状况
	private String[] healthIds = {"18000100","18000200","18000300","18000400","18000500","18000600","18000700","18000800","18000900","18001000","18001100","18001200","18001300","18001400","18001500","18001600","18001700","18001800","18001900","18002000","18002100","18002200","18002300","18002400","18002500","18002600",};
	private String[] healths = {"健康或良好","一般或较弱","有慢性病","心血管病","脑血管病","慢性呼吸系","慢性消化系","慢性肾炎","结核病","其他慢性病","有生理缺陷","聋哑","盲人","高度近视","其他缺陷","残废","特等残废","一等残废","二等甲级残","二等乙级残","三等甲级残","三等乙级残","其他残废","良好","神经或精神","糖尿病",};
	
	// 工作性质
	private String[] workStateIds = {"10000100","10000200","10000300","10000400","10000500","10000600","10000700"};
	private String[] workStates = {"全职","兼职","实习","临时","小时工","不限","其他"};
	
	// 月薪要求
	private String[] wageIds = {"70000100","70000200","70000300","70000400","70000500","70000600","70000700","70000800","70000900","70001000"};
	private String[] wages = {"1000元以下","1000-1999元","2000-2999元","3000-3999元","4000-4999元","5000-7999元","8000-9999元","10000-19999元","20000元及以上","不限"};
	
	// 微机水平
	private String[] computerLevelIds = {"11010000","11020000","11030000","11040000","11050000","11060000","11070000","11080000","11090000","11100000","11110000"};
	private String[] computerLevels = {"计算机水平一级","计算机水平二级","计算机水平三级","精通","较好","一般","差","微软认证工程师","思科认证工程师","SUN认证工程师","IBM认证工程师"};
	
	// 民族
	private String[] peopleIds = {"17000100","17000200","17000300","17000400","17000500","17000600","17000700","17000800","17000900","17001100","17001200","17001300","17001400","17001500","17001600","17001700","17001800","17001900","17002000",
			"17002200","17002300","17002400","17002500","17002600","17002700","17002800","17002900","17003000","17003100","17003200","17003300","17003400","17003500","17003600","17003700","17003800","17003900","17004000",
			"17004100","17004200","17004300","17004400","17004500","17004600","17004700","17004800","17004900","17005000","17005100","17005200","17005300","17005400","17005500","17005600","17005700","17005800"};
	private String[] peoples = {"汉族","蒙古族","回族","藏族","维吾尔族","苗族","彝族","壮族","布依族","朝鲜族","满族","侗族","瑶族","白族","土家族","哈尼族","哈萨克族","傣族","黎族",
			"傈僳族","佤族","畲族","高山族","拉祜族","水族","东乡族","纳西族","景颇族","柯尔克孜族","土族","达斡尔族","仫佬族","羌族","布朗族","撒拉族","毛南族","仡佬族","锡伯族",
			"阿昌族","普米族","塔吉克族","怒族","乌孜别克族","俄罗斯族","鄂温克族","德昂族","保安族","裕固族","京族","塔塔尔族","独龙族","鄂伦春族","赫哲族","门巴族","珞巴族","基诺族"};
	
	private String[] houseWhereIds = {"有","否"};
	private String[] houseWheres = {"要提供","无所谓"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_area_listview);

		initIntent();
		initAllView();
		reigesterAllEvent();

	}

	protected void initIntent() {
		which = getIntent().getStringExtra("which");

		if ("SelectFirstLanguage".equals(which)) {
			super.setTitleBar("选择第一外语", View.VISIBLE, View.GONE);
			for (int i = 0; i < languageIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", languageIds[i]);
				map.put("value", languages[i]);
				listItems.add(map);
			}
		} else if ("SelectLanguageLevel".equals(which)) {
			super.setTitleBar("选择外语等级", View.VISIBLE, View.GONE);
			for (int i = 0; i < languageLevelIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", languageLevelIds[i]);
				map.put("value", languageLevels[i]);
				listItems.add(map);
			}
		} else if ("Education".equals(which)) {
			super.setTitleBar("选择学历", View.VISIBLE, View.GONE);
			for (int i = 0; i < educationIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", educationIds[i]);
				map.put("value", educations[i]);
				listItems.add(map);
			}
		} else if ("WorkingYears".equals(which)) {
			super.setTitleBar("选择您的工作年限", View.VISIBLE, View.GONE);
			for (int i = 0; i < workingYearIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", workingYearIds[i]);
				map.put("value", workingYears[i]);
				listItems.add(map);
			}
		} else if ("WorkExp".equals(which)) {
			super.setTitleBar("担任现职务时间", View.VISIBLE, View.GONE);
			for (int i = 0; i < workingYearIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", workingYearIds[i]);
				map.put("value", workingYears[i]);
				listItems.add(map);
			}
		} else if ("TotalWorkingTime".equals(which)) {
			super.setTitleBar("累计工作时间", View.VISIBLE, View.GONE);
			for (int i = 0; i < workingYearIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", workingYearIds[i]);
				map.put("value", workingYears[i]);
				listItems.add(map);
			}
		} else if ("CompanyScale".equals(which)) {
			super.setTitleBar("选择公司规模", View.VISIBLE, View.GONE);
			for (int i = 0; i < companyScaleIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", companyScaleIds[i]);
				map.put("value", companyScales[i]);
				listItems.add(map);
			}
		} else if ("CompanyProperty".equals(which)) {
			super.setTitleBar("选择公司性质", View.VISIBLE, View.GONE);
			for (int i = 0; i < companyPropertyIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", companyPropertyIds[i]);
				map.put("value", companyProperties[i]);
				listItems.add(map);
			}
		} else if ("SelectJob".equals(which)) {
			id = getIntent().getStringExtra("id");
			value = getIntent().getStringExtra("value");
			super.setTitleBar(value, View.VISIBLE, View.GONE);
			initJobDataThread(0);

		} else if ("Political".equals(which)) {
			super.setTitleBar("选择政治面貌", View.VISIBLE, View.GONE);
			for (int i = 0; i < politicalIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", politicalIds[i]);
				map.put("value", politicals[i]);
				listItems.add(map);
			}
		} else if ("Marital".equals(which)) {
			super.setTitleBar("选择婚姻状况", View.VISIBLE, View.GONE);
			for (int i = 0; i < maritalIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", maritalIds[i]);
				map.put("value", maritals[i]);
				listItems.add(map);
			}
		} else if ("Health".equals(which)) {
			super.setTitleBar("选择健康状况", View.VISIBLE, View.GONE);
			for (int i = 0; i < healthIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", healthIds[i]);
				map.put("value", healths[i]);
				listItems.add(map);
			}
		} else if ("WorkState".equals(which)) {
			super.setTitleBar("选择工作性质", View.VISIBLE, View.GONE);
			for (int i = 0; i < workStateIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", workStateIds[i]);
				map.put("value", workStates[i]);
				listItems.add(map);
			}
		} else if ("Wage".equals(which)) {
			super.setTitleBar("选择月薪要求", View.VISIBLE, View.GONE);
			for (int i = 0; i < wageIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", wageIds[i]);
				map.put("value", wages[i]);
				listItems.add(map);
			}
		} else if ("ComputerLevel".equals(which)) {
			super.setTitleBar("选择微机水平", View.VISIBLE, View.GONE);
			for (int i = 0; i < computerLevelIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", computerLevelIds[i]);
				map.put("value", computerLevels[i]);
				listItems.add(map);
			}
		} else if ("People".equals(which)) {
			super.setTitleBar("选择民族", View.VISIBLE, View.GONE);
			for (int i = 0; i < peopleIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", peopleIds[i]);
				map.put("value", peoples[i]);
				listItems.add(map);
			}
		} else if ("HouseWhere".equals(which)) {
			super.setTitleBar("选择住房要求", View.VISIBLE, View.GONE);
			for (int i = 0; i < houseWhereIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", houseWhereIds[i]);
				map.put("value", houseWheres[i]);
				listItems.add(map);
			}
		} else if ("Degree".equals(which)) {
			super.setTitleBar("选择学位", View.VISIBLE, View.GONE);
			for (int i = 0; i < degreeIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", degreeIds[i]);
				map.put("value", degrees[i]);
				listItems.add(map);
			}
		} else if ("Profession".equals(which)) {
			super.setTitleBar("选择专业", View.VISIBLE, View.GONE);
			for (int i = 0; i < professionIds.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", professionIds[i]);
				map.put("value", professions[i]);
				listItems.add(map);
			}
		}
		
	}

	private void initJobDataThread(final int i) {

		if (i == 0) {
			showProcessDialog(false);
		}
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						initJobData(i);
					}
				});
		mThread.start();
	}

	@Override
	protected void initAllView() {
		listview = (ListView) findViewById(R.id.lv_area);
		footViewBar = View.inflate(SelectItemActivity.this, R.layout.foot_view_loading, null);
		myAdapter = new MyAdapter(this, listItems, R.layout.select_area_item);
		listview.setAdapter(myAdapter);
		listview.setOnScrollListener(listener);
	}

	@Override
	protected void reigesterAllEvent() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if ("SelectFirstLanguage".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("language", languages[position]);
					intent.putExtra("languageId", languageIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("SelectLanguageLevel".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("languageLevel", languageLevels[position]);
					intent.putExtra("languageLevelId", languageLevelIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("Education".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("education", educations[position]);
					intent.putExtra("educationId", educationIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("WorkingYears".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("workingYear", workingYears[position]);
					intent.putExtra("workingYearId", workingYearIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("WorkExp".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("workExp", workingYears[position]);
					intent.putExtra("workExpId", workingYearIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("TotalWorkingTime".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("totalWorkingTime", workingYears[position]);
					intent.putExtra("totalWorkingTimeId", workingYearIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("CompanyScale".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("companyScale", companyScales[position]);
					intent.putExtra("companyScaleId", companyScaleIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("CompanyProperty".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("companyProperty", companyProperties[position]);
					intent.putExtra("companyPropertyId", companyPropertyIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("SelectJob".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("currentJob", listItems.get(position).get("value"));
					intent.putExtra("currentJobId", listItems.get(position).get("id"));
					setResult(RESULT_OK, intent);
					finish();
				} else if ("Political".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("political", politicals[position]);
					intent.putExtra("politicalId", politicalIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("Marital".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("marital", maritals[position]);
					intent.putExtra("maritalId", maritalIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("Health".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("health", healths[position]);
					intent.putExtra("healthId", healthIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("WorkState".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("workState", workStates[position]);
					intent.putExtra("workStateId", workStateIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("Wage".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("wage", wages[position]);
					intent.putExtra("wageId", wageIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("ComputerLevel".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("computerLevel", computerLevels[position]);
					intent.putExtra("computerLevelId", computerLevelIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("People".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("people", peoples[position]);
					intent.putExtra("peopleId", peopleIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("HouseWhere".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("houseWhere", houseWheres[position]);
					intent.putExtra("houseWhereId", houseWhereIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("Degree".equals(which)) {
					Intent intent = getIntent();
					intent.putExtra("degree", degrees[position]);
					intent.putExtra("degreeId", degreeIds[position]);
					setResult(RESULT_OK, intent);
					finish();
				} else if ("Profession".equals(which)) {
//					Intent intent = getIntent();
//					intent.putExtra("profession", professions[position]);
//					intent.putExtra("professionId", professionIds[position]);
//					setResult(RESULT_OK, intent);
//					finish();
					
					Intent intent = new Intent(SelectItemActivity.this,SelectProfessionActivity.class);
					intent.putExtra("professionId", professionIds[position]);
					startActivityForResult(intent, Constant.PROFESSION_TYPE);
					
					
				}
				
			}

		});
	}

	private void initJobData(int j) {

		String url = "appCmbShow.app";

		Message msg = new Message();
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		// if ("WorkExperienceEditActivity".equals(flag)) {
		// params.add(new BasicNameValuePair("type", "5"));
		// } else {
		params.add(new BasicNameValuePair("type", "13"));
		// }
		params.add(new BasicNameValuePair("key", id));
		params.add(new BasicNameValuePair("page", page + ""));
		result = getPostHttpContent(url, params);

		if (StringUtil.isExcetionInfo(result)) {
			sendExceptionMsg(result);
			return;
		}

		if (StringUtil.isBlank(result)) {
			result = StringUtil.getAppException4MOS("未获得服务器响应结果！");
			sendExceptionMsg(result);
		}
		JSONObject responseJsonObject = JSONObject.parseObject(result);
		if (responseJsonObject.get("resultcode").toString().equals("0")) {
			total = responseJsonObject.getInteger("resultCount");
			JSONArray jArray = responseJsonObject.getJSONArray("result");
			if (jArray.size() > 15) {
				count = 15;
			} else {
				count = jArray.size();
			}
			for (int i = 0; i < jArray.size(); i++) {
				JSONObject jObject = jArray.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", jObject.getString("key"));
				map.put("value", jObject.getString("value"));
				listItems.add(map);
			}

			msg.what = j == 0 ? Constant.DETAIL_JOB : Constant.DETAIL_JOB_UPDATE;
			handler.sendMessage(msg);
		} else {
			String errorResult = (String) responseJsonObject.get("result");
			String err = StringUtil.getAppException4MOS(errorResult);
			sendExceptionMsg(err);
		}

	}

	// 处理线程发送的消息
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.DETAIL_JOB:
				initUI();
				closeProcessDialog();
				break;

			case Constant.DETAIL_JOB_UPDATE:

				updateUI();
				break;
			}
		}
	};

	// 初始化列表
	protected void initUI() {
		if (total > pageSize * page) {
			listview.addFooterView(footViewBar);// 添加list底部更多按钮
		}
		listview.setAdapter(myAdapter);
	}

	private void updateUI() {

		if (total <= pageSize * page) {
			listview.removeFooterView(footViewBar);
		}
		myAdapter.notifyDataSetChanged();
	}

	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			itemCount = visibleItemCount;
			visibleLastIndex = firstVisibleItem + visibleItemCount - 1;

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				if (view.getLastVisiblePosition() == myAdapter.getCount()) {
					page++;
					initJobDataThread(1);// 滑动list请求数据
				}
			}

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.PROFESSION_TYPE:
				professionId = data.getStringExtra("professionId");
				profession = data.getStringExtra("profession");

				Intent intent = getIntent();
				intent.putExtra("professionId", professionId);
				intent.putExtra("profession", profession);
				setResult(RESULT_OK, intent);
				finish();
				break;
			}
			
		}
		
	}
	
	// 自定义适配器
	class MyAdapter extends BaseAdapter {
		private Context mContext;// 上下文对象
		List<Map<String, String>> list;// 这是要绑定的数据
		private int resource;// 这是要绑定的 item 布局文件
		private LayoutInflater inflater;// 布局填充器，Android系统内置的

		public MyAdapter(Context context, List<Map<String, String>> list, int resource) {
			this.mContext = context;
			this.list = list;
			this.resource = resource;
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 布局填充服务
		}

		@Override
		public int getCount() {// 数据的总数量
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView item = null;

			if (convertView == null) {// 目前显示的是第一页，为这些条目数据new一个view出来，如果不是第一页，则继续用缓存的view
				convertView = inflater.inflate(resource, null);

				// 初始化控件
				item = (TextView) convertView.findViewById(R.id.area);
				ViewCache viewCache = new ViewCache();
				viewCache.item = item;
				convertView.setTag(viewCache);
			} else {
				// 初始化控件
				ViewCache viewCache = (ViewCache) convertView.getTag();
				item = viewCache.item;
			}

			item.setText(list.get(position).get("value"));

			return convertView;
		}

	}

	private final class ViewCache {
		public TextView item;
	}

}
