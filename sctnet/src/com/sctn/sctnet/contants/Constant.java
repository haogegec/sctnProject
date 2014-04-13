package com.sctn.sctnet.contants;

/**
 * 常量值
 * 
 * @author xueweiwei
 * 
 */
public class Constant {

	public static String ServerURL = "http://60.172.228.219:9099/scgenuis/";// 测试环境
//	public static String ServerImageURL = "http://www.scrc168.com/";// 图片地址
	public static String ServerImageURL = "http://60.172.228.219:9099/scgenuis/uploadPath/";// 图片地址
	public static String DocUrl = "http://www.scrc168.com/";// 下载文档的地址

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
	public static final int LOGIN_SHARE_JOB_ACTIVITY = 40;

	public static final int PageSize = 15;// 每个列表页面一页显示的个数

	public static final int REQUEST_TIMEOUT = 50 * 1000;// 设置请求超时50秒钟
	public static final int SO_TIMEOUT = 50 * 1000; // 设置等待数据超时时间50秒钟

	public static final int PROVINCE_TYPE = 1;// 请求省份类型id
	public static final int CITY_TYPE = 2;// 请求市类型id
	public static final int INDUSTRY_TYPE = 22;// 请求行业类型id
	public static final int POSITION_TYPE = 4;// 请求职业类型id
	public static final int POSITION_DETAIL_TYPE = 5;// 请求详细职位类型id
	
	public static final int CURRENT_POSITION_REQUEST_CODE = 8;

	// 薪酬调查页面获取后台数据时候用到的常量
	public static final int DEGREE = 11;// 学位
	public static final int FIRST_FOREIGN_LANGUAGE = 12;// 外语能力
	public static final int LANGUAGE_LEVEL = 13;// 外语能力
	public static final int CURRENT_INDUSTRY = 14;
	public static final int JOB = 15;
	public static final int JOB_UPDATE = 49;
	public static final int DETAIL_JOB = 16;
	public static final int DETAIL_JOB_UPDATE = 50;
	public static final int AREA = 17;
	public static final int SCALE = 18;
	public static final int PROPERTY = 19;
	public static final int SALARY_SURVEY_SUBMIT = 20;
	public static final int WORKING_YEARS = 44;
	public static final int WORK_EXP = 45;
	public static final int TOTAL_WORKING_TIME = 46;
	public static final int COMPANY_SCALE = 47;// 现单位规模
	public static final int COMPANY_PROPERTY = 48;// 现单位规模
	

	// 消息推送用到的常量
	public static final String MESSAGE_RECEIVED_ACTION = "com.sctn.sctnet.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	// 登录页注册请求码
	public static final int REGISTER_REQUEST_CODE = 0;
	public static final int FORGET_PASSWORD_REQUEST_CODE = 1;

	// 职位申请、收藏成功时的msg.what的值
	public static final int APPLY_SUCCESS = 101;
	public static final int COLLECT_SUCCESS = 102;

	// 简历管理模块。选择籍贯。选择城市。户口所在地。居住地请求码
	public static final int SELECT_NATIVE_PLACE_REQUEST_CODE = 25;
	public static final int SELECT_CITY_REQUEST_CODE = 26;
	public static final int SELECT_RESIDENCE_REQUEST_CODE = 27;
	public static final int SELECT_HABITAT_REQUEST_CODE = 28;
	public static final int WORKPERFORMANCE_REQUEST_CODE = 29;// 选择目前担任的职务请求码

	// 剪切图片的比例 及 长宽
	public static final int IMAGE_CROP_WIDTH_SCALE = 1;
	public static final int IMAGE_CROP_HIGHT_SCALE = 1;
	public static final int IMAGE_CROP_WIDTH = 180;
	public static final int IMAGE_CROP_HIGHT = 180;
	public static final int IMAGE_REQUEST_CODE = 0;// 本地图片请求码
	public static final int CAMERA_REQUEST_CODE = 1;// 调用照相机
	public static final int CAOP_RESULT_REQUEST_CODE = 2;// 剪辑结果码
	public static final int UPLAOD_RESULT_REQUEST_CODE = 3;// 上传结果码

	// 简历管理中
	public static final int PEOPLE = 30;
	public static final int POLITICAL = 31;
	public static final int MARITAL = 32;
	public static final int HEALTH = 33;
	public static final int COMPUTER_LEVEL = 34;
	public static final int WORK_STATE = 35;
	public static final int WAGE_RANGE = 36;
	public static final int FIRST_LANGUAGE = 51;
	public static final int FIRST_LANGUAGE_LEVEL = 52;
	public static final int SECOND_LANGUAGE = 53;
	public static final int SECOND_LANGUAGE_LEVEL = 54;

	// 职位搜索按照类型来查询
	public static final String TYPE_JOB_NAME = "1";// 按照职位名称搜索
	public static final String TYPE_COMPANY_NAME = "2";// 按照公司名称搜索
	public static final String TYPE_FULL_TEXT = "3";// 按照全文搜索

	public static final int JOB_LIST = 11;// 职位列表返回到职位搜索界面
	// 分享后的返回码
	public static final int SHARE_ERROR = 200;// 分享失败
	public static final int SHARE_CANCEL = 201;// 分享取消
	public static final int SHARE_COMPLETE = 202;// 分享成功

	// 职业信息自动推送设置
	public static final int AUTO_PUSH_SUCCESS = 210;
	public static final int AUTO_PUSH_SUCCESS_MODIFY_WIDGET = 212;
	public static final int AUTO_PUSH_ERROR = 216;
	
	// 自定义推送内容设置
	public static final int USER_DEFINED_PUSH_SUCCESS = 211;
	public static final int USER_DEFINED_PUSH_SUCCESS_MODIFY_WIDGET = 213;
	public static final int USER_DEFINED_PUSH_ERROR = 215;
	
	public static final int SUBSCRIBE_REQUEST_CODE = 214;
	
	public static final String PUSH_BY_ALIAS = "A";// 自动推送
	public static final String PUSH_BY_TAGS = "T";// 订阅推送
	
	
}
