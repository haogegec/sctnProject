package com.sctn.sctnet.Utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sctn.sctnet.component.MultiMemberGZIPInputStream;

public class StringUtil {

	/**
	 * 删除str指定的后缀
	 * 
	 * @param str
	 * @param suffix
	 * @return
	 */
	public static String removeSuffix(String str, String suffix) {
		if (null == str)
			return null;
		if ("".equals(str.trim()))
			return "";

		if (null == suffix || "".equals(suffix))
			return str;

		if (str.endsWith(suffix)) {
			return str.substring(0, str.length() - suffix.length());
		}

		return "";
	}

	
	/**
	 * 按长度截取字符串
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String subString(String str, int length) {
		if (isBlank(str)) {
			return "";
		} else if (str.length() > length) {
			return str.substring(0, length);
		} else {
			return str;
		}
	}

	/**
	 * 是否是空的（包括null、""、空格）
	 * 
	 * @param str
	 * @return
	 * @throws UtilException
	 */
	public static boolean isBlank(String str) {
		if (null == str)
			return true;
		if ("".equals(str.trim()))
			return true;

		return false;
	}

	public static boolean isBlank(Long str) {
		if (null == str)
			return true;
		return false;
	}

	/**
	 * 将对象转成String
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString().trim();
	}
	
	
	public static String add(String str,int num){
		int i = num;
		if(!isBlank(str)){
			int intStr = Integer.parseInt(str);
			i = i + intStr;
		}
		
		return Integer.toString(i) ;
	}
	
	/**
	 * 判断一个字符串是否都为数字
	 */
	public  static boolean isDigit(String strNum) {  
	    Pattern pattern = Pattern.compile("[0-9]{1,}");  
	    Matcher matcher = pattern.matcher((CharSequence) strNum);  
	    return matcher.matches();  
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String getString(String str) {
		if (null == str)
			return "";
		return str;

	}

	/**
	 * 计算两个数字字符串的和
	 * 
	 * @param augend
	 * @param addend
	 * @return
	 * @throws UtilException
	 */
	public static String getSum(String augend, String second, String addend) {
		if (augend == null)
			augend = "0";
		if (second == null)
			second = "0";
		if (addend == null)
			addend = "0";
		int sum = Integer.parseInt(augend) + Integer.parseInt(second) + Integer.parseInt(addend);
		return new Integer(sum).toString();
	}

	public static String change(String str, int n, boolean isLeft) {
		if (str == null || str.length() >= n)
			return str;
		String s = "";
		for (int i = str.length(); i < n; i++)
			s += "0";
		if (isLeft)
			return s + str;
		else
			return str + s;
	}

	public static String getInString(String str) {
		if (str == null)
			return null;
		StringBuffer sb = new StringBuffer("");
		String s[] = str.split(",");
		if (s != null && s.length > 0) {
			for (int i = 0; i < s.length; i++) {
				if (i != 0)
					sb.append(",");
				sb.append("'").append(s[i]).append("'");
			}
		}
		return sb.toString();
	}
	
	public static String subInStringByFlag(String str,String flag) {
		if (isBlank(str))
			return null;
		StringBuffer sb = new StringBuffer(str);
		int index = str.lastIndexOf(flag);
		if (index < 0) {
			return str;
		} else {
			str = sb.delete(sb.length()-flag.length(), sb.length()).toString();
			index = str.indexOf(flag);
			if (index < 0) {
				return str;
			} else {
				return sb.deleteCharAt(0).toString();
			}
		}
	}
	/**
	 * 根据标识获取str中最后一个flag后的内容
	 * 
	 * @param str
	 * @param flag
	 * @return
	 */
	public static String getLastStr(String str, String flag) {
		if (isBlank(str))
			return null;
		int index = str.lastIndexOf(flag);
		if (index < 0) {
			return str;
		} else {
			return str.substring(index + flag.length());
		}

	}

	/**
	 * 获取正则表达式匹配的字符串，将$符处理一下，不然匹配时会认作分组
	 * 
	 * @param str
	 * @return
	 */
	public static String getRegexStr(String str) {
		String ret = "";
		if (isBlank(str))
			return "";
		if (str.indexOf('$', 0) > -1) {
			while (str.length() > 0) {
				if (str.indexOf('$', 0) > -1) {
					ret += str.subSequence(0, str.indexOf('$', 0));
					ret += "\\$";
					str = str.substring(str.indexOf('$', 0) + 1, str.length());
				} else {
					ret += str;
					str = "";
				}
			}

		} else {

			ret = str;
		}

		return ret;

	}
	
	
	/**
	 * 根据正则表达式截取匹配的字符串列表
	 * 从指定字符串中，按正则表达式要求，找出其中能匹配上的字符串列表
	 * @param str
	 * @param regx
	 * @return
	 */
	public static List catchStr(String str,String regx){
		if (isBlank(str))
			return null;
		
		List ret = new ArrayList();
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(str);
		int start = 0;
		int end = 0;
		while(matcher.find()){
			start = matcher.start();
			end = matcher.end();
			ret.add(str.substring(start,end));
		}
		return ret;
		
	}

	/**
	 * 过滤换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String filterNextLine(String str) {
		if (isBlank(str))
			return "";
		return str.replaceAll("[\n\r\u0085\u2028\u2029]", "");
	}
	public static String lpad(String str ,int length) {  
		if (isBlank(str))
			return "";
		int  j= str.length();
		for(int i=j;i<length;i++){
        	str ="0"+str;
        }
        return str;
    }
	
	
	public static boolean isExcetionInfo(String str) {
		if(isBlank(str))return false;
		Document doc=Jsoup.parseBodyFragment(str);
		Element  text=doc.getElementById("errinfospan");
		if(str.startsWith("<!DOCTYPE html")||str.startsWith("<!DOCTYPE HTML") || text!=null ) {
			return true;
		}
		return false;
	}
	
	/**
	 * 创建异常页面源码字符串，用于MOS Native替代抛出的异常页面
	 * @param errMsg
	 * @return
	 */
	public static String getAppException4MOS(String errMsg) {
		String str="<!DOCTYPE html><html><head></head><body><span id=\"errinfospan\">"+errMsg+" </span></body></html>";
		return str;
	}
	
	/**
	 * 获取服务器端返回的错误信息描述。当服务器端出现Exception时，客户端得到的结果是一个html 错误界面源码，该方法通过解析html，获得错误描述信息
	 * @param result
	 * @return
	 */
	public static String getExceptionDesc(String result) {
		Document doc=Jsoup.parseBodyFragment(result);
		Element  ele= doc.getElementById("errinfospan");
		String text="";
        if( ele!=null) {
        	text=ele.text();
        }else {
        	text="服务器端出现异常！";
        }
        return text;
	}
	
	/**
	 * 获取http://search.anccnet.com返回的条码查询结果。解析Html获得相应字段
	 * 查询url为http://search.anccnet.com/searchResult.aspx?keyword=对应的EAN码
	 * @param result
	 * @return
	 */
	public static Map<String,String> getEanResult(String result) {
		Map<String,String> resultMap=new HashMap<String,String>();
		Document doc=Jsoup.parseBodyFragment(result);
	   
		String txtTccode=doc.getElementById("txt_tccode").text();
		String firmName=doc.getElementById("firm_name").text();
		String firmNameEn=doc.getElementById("firm_name_en").text();
		String registerAddress=doc.getElementById("register_address").text();
		String registerAddressEn=doc.getElementById("register_address_en").text();
		String registerPostalcode=doc.getElementById("register_postalcode").text();
		resultMap.put("txtTccode", txtTccode);
		resultMap.put("firmName", firmName);
		resultMap.put("firmNameEn", firmNameEn);
		resultMap.put("registerAddress", registerAddress);
		resultMap.put("registerAddressEn", registerAddressEn);
		resultMap.put("registerPostalcode", registerPostalcode);
		if(resultMap==null||resultMap.isEmpty()) {
			return null;
		}
		return resultMap;
	}

	public static  List<Map<String, String>> getScanEanResult(String result) {
		Map<String,String> resultMap=new HashMap<String,String>();
		Document doc=Jsoup.parseBodyFragment(result);
	             
	   Element element =doc.getElementsByClass("p-info").first();
	   Elements titleElements = element.getElementsByTag("dt");
	   Elements contentElements = element.getElementsByTag("dd");
	   List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	   for (int i=0;i<titleElements.size();i++){
		   Map<String, String>  map = new HashMap<String, String>();
		   map.put("title", titleElements.get(i).getElementsByTag("dt").text());
		   map.put("content", contentElements.get(i).getElementsByTag("dd").text());
		   list.add(map);
	   }

	   
		return list;
	}
	
	
	public static void main(String[] args)  {
		//String str = "kkjlj l.class";
		String str = "where instr($GET_PHONE_GROUP_TYPE,'[31]')>0 and nvl(replace($GET_PHONE_GROUP_SPEC_STR,'[31]',''),'N')<>'N' and $GET_PHONE_GROUP_TYPE=1";
		//log.debug(StringUtil.removeSuffix(str, ".class"));

		//System.out.println(StringUtil.getLastStr("com.cattsoft.sp.component.domain.SoManagerDOM","."));

		//String a = "com.cattsoft.sp. \r com \n ponent.domain.SoManagerDOM";
		//System.out.println(StringUtil.filterNextLine(a));
		//System.out.println(StringUtil.subString("aa",10));
		
		//System.out.println(StringUtil.add("1", 11));
		
		System.out.println(StringUtil.catchStr(str,"\\$\\w*\\b").get(0));
		System.out.println(StringUtil.catchStr(str,"\\$\\w*\\b").get(1)); 
		System.out.println(StringUtil.catchStr(str,"\\$\\w*\\b").get(2)); 
	}

	public static String unCompress(byte[] bytCompressed) throws SysException {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytCompressed);
		String res = "";
		try {
			GZIPInputStream gunzip = new MultiMemberGZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			res = out.toString("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			throw new SysException("", "解压过程中出现异常！ ", e);
		}

		return res;

	}
}
