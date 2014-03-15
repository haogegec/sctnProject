package com.sctn.sctnet.contants;

/**
 * 常量值
 * 
 * @author xueweiwei
 * 
 */
public class Constant {

	public static String ServerURL = "http://60.172.228.219:9099/scgenuis/";// 测试环境
	public static String ServerImageURL = "http://www.weeqii.com/booking/main!getImage";// 图片地址

	public static String packageName = "com.sctn.sctnet";// 工程的包名
	public static String jobSeekerType = "1";// 求职者类型

	// 薪酬调查模块中用到的请求码
	public static final int FOREIGNLANGUAGE_REQUEST_CODE = 1;// 选择外语能力请求码
	public static final int JOBEXP_REQUEST_CODE = 2;// 职场经历请求码
	public static final int WORKINGAREA_REQUEST_CODE = 3;// 选择工作地区请求码
	public static final int CURRENT_INDUSTRY_REQUEST_CODE = 6;// 选择目前就职的行业请求码
	public static final int JOB_REQUEST_CODE = 7;// 选择目前担任的职务请求码

	// 职位搜索模块中用到的请求吗
	public static final int RELEASETIME_REQUEST_CODE = 4;// 选择发布日期请求码
	public static final int INDUSTRY_REQUEST_CODE = 5;// 选择行业请求码
	// 系统图片存放字段
	public static final String SYS_IMAGE_DATA_STORE = "/mnt/sdcard/sctnet/"; // 图片存放地址


	// 首页中的登录功能用到的请求码
	public static final int LOGIN_REQUEST_CONDE = 1;
	public static final int LOGIN_PERSONAL_CENTER_ACTIVITY = 10;
	public static final int LOGIN_RESUME_MANAGE_ACTIVITY = 21;
	public static final int LOGIN_SALARY_SURVEY_ACTIVITY = 22;
	
	// 职位申请时用到的请求码
	public static final int LOGIN_APPLY_JOB_ACTIVITY = 23;
	public static final int LOGIN_COLLECT_JOB_ACTIVITY = 24;
	
	public static final int PageSize = 15;// 每个列表页面一页显示的个数

	public static final int REQUEST_TIMEOUT = 50 * 1000;// 设置请求超时50秒钟
	public static final int SO_TIMEOUT = 50 * 1000; // 设置等待数据超时时间50秒钟

	public static final int PROVINCE_TYPE = 1;// 请求省份类型id
	public static final int CITY_TYPE = 2;// 请求市类型id
	public static final int INDUSTRY_TYPE = 3;// 请求行业类型id
	public static final int POSITION_TYPE = 4;// 请求职业类型id
	public static final int POSITION_DETAIL_TYPE = 5;//请求详细职位类型id

	// 薪酬调查页面获取后台数据时候用到的常量
	public static final int DEGREE = 11;// 学位
	public static final int FIRST_FOREIGN_LANGUAGE = 12;// 外语能力
	public static final int LANGUAGE_LEVEL = 13;// 外语能力
	public static final int CURRENT_INDUSTRY = 14;
	public static final int JOB = 15;
	public static final int DETAIL_JOB = 16;
	public static final int AREA = 17;
	public static final int SCALE = 18;
	public static final int PROPERTY = 19;
	public static final int SALARY_SURVEY_SUBMIT = 20;

	// 消息推送用到的常量
	public static final String MESSAGE_RECEIVED_ACTION = "com.sctn.sctnet.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	// 登录页注册请求码
	public static final int REGISTER_REQUEST_CODE = 0;
	
	// 职位申请、收藏成功时的msg.what的值
	public static final int APPLY_SUCCESS = 101;
	public static final int COLLECT_SUCCESS = 102;
	
	
}
