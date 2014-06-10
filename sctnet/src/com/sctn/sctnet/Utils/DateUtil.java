package com.sctn.sctnet.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO 时间工具
 * 
 * @author 姜勇男
 * 
 */
public class DateUtil {

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_FORMAT = "hh:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm";
	/**
	 * 获取当前系统时间
	 * 
	 * @throws ParseException
	 */
	public static String getCurrentDate() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		String currentDate = sdf.format(dt);

		return currentDate;
	}

	/**
	 * 比较当前时间和预约时间，如果当前时间已经过了预约时间（不能预约），则返回负数，否则返回正数
	 * 
	 * @param 当前系统时间
	 *            （yyyy-MM-dd hh:mm:ss）
	 * @throws ParseException
	 */
	public int compareDate(String orderDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		Date d1 = sdf.parse(orderDate);// 预约时间
		Date d2 = sdf.parse(getCurrentDate());// 当前系统时间
		return (int) (d1.getTime() - d2.getTime());
	}

	/**
	 * */
	public static boolean compareWithCurrentTime(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MONTH_DAY_HOUR_MINUTE);
		Date currentDate = null;
		Date date = null;
		try {
			currentDate = sdf.parse(getFormatCurrentTime(YEAR_MONTH_DAY_HOUR_MINUTE));
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (currentDate.getTime() - date.getTime()) > 0 ? true : false;
	}

	/**
	 *  格式为 yyyy-MM-dd
	 * @param strDate
	 * @return
	 */
	public static boolean compareWithCurrentTimeByYYYYMMDD(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date currentDate = null;
		Date date = null;
		try {
			currentDate = sdf.parse(getFormatCurrentTime(DATE_FORMAT));
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (currentDate.getTime() - date.getTime()) > 0 ? true : false;
	}
	/**
	 * 比较时间 时间的格式为 hh:mm 即只判断 小时和分钟
	 * */
	public static boolean compareTime(String time1, String time2) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			Date date1 = sdf.parse(time1);
			Date date2 = sdf.parse(time2);
			return date1.getTime() - date2.getTime() > 0 ? true : false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean compareTime(String time1, String time2,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date1 = sdf.parse(time1);
			Date date2 = sdf.parse(time2);
			return date1.getTime() - date2.getTime() >= 0 ? true : false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 比较时间 时间的格式为 DATE_FORMAT 格式为：yyyy-MM-dd 
	 * */
	public static boolean compareDateByYYYYMMDD(String time1, String time2) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		try {
			Date date1 = sdf.parse(time1);
			Date date2 = sdf.parse(time2);
			return date1.getTime() - date2.getTime() > 0 ? true : false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static String getCurrentYear() {
		return getFormatCurrentTime("yyyy");
	}
	public static String getCurrentMonth() {
		return getFormatCurrentTime("MM");
	}

	public static String getCurrentDay() {
		return getFormatCurrentTime("dd");
	}
	public static String getCurrentHour() {
		return getFormatCurrentTime("HH");
	}

	public static String getCurrentMinute() {
		return getFormatCurrentTime("mm");
	}

	public static int getCurrentIntYear() {
		return Integer.parseInt(getFormatCurrentTime("yyyy"));
	}
	public static int getCurrentIntMonth() {
		return Integer.parseInt(getFormatCurrentTime("MM"));
	}

	public static int getCurrentIntDay() {
		return Integer.parseInt(getFormatCurrentTime("dd"));
	}

	public static int getCurrentIntHour() {
		return Integer.parseInt(getFormatCurrentTime("hh"));
	}

	public static int getCurrentIntMinute() {
		return Integer.parseInt(getFormatCurrentTime("mm"));
	}
	public static String getCurrentYearMonthDay() {
		return getFormatDateTime(new Date(), DATE_FORMAT);
	}

	public static String getCurrentHourMinuteSecond() {
		return getFormatDateTime(new Date(), TIME_FORMAT);
	}
	public static String getCurrentHourMinute() {
		return getFormatDateTime(new Date(), "hh:mm");
	}
	
	
	public static String getFormatDateTime(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	public static String getFormatCurrentTime(String format) {
		return getFormatDateTime(new Date(), format);
	}

	public static Date getTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
