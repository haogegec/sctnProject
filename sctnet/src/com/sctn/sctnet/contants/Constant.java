package com.sctn.sctnet.contants;

/**
 * 常量值
 * @author xueweiwei
 *
 */
public class Constant {

	public static String ServerURL="http://www.weeqii.com/booking/";//测试环境
	public static String ServerImageURL="http://www.weeqii.com/booking/main!getImage";//图片地址
	
	public static String packageName = "com.sctn.sctnet";//工程的包名
	public static String jobSeekerType = "1";//求职者类型
	
	//薪酬调查模块中用到的请求码
	public static final int FOREIGNLANGUAGE_REQUEST_CODE = 1;// 选择外语能力请求码
	public static final int JOBEXP_REQUEST_CODE = 2;// 职场经历请求码
	public static final int WORKINGAREA_REQUEST_CODE = 3;// 选择工作地区请求码
	// 职位搜索模块中用到的请求吗
	public static final int RELEASETIME_REQUEST_CODE = 4;// 选择发布日期请求码
	public static final int INDUSTRY_REQUEST_CODE = 5;// 选择行业请求码
	// 系统图片存放字段
	public static final String SYS_IMAGE_DATA_STORE = "/mnt/sdcard/sctnet/"; // 图片存放地址

}
