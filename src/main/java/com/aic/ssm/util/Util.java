/*
 * @(#)Util.java	1.0 2004-8-18
 *
 * Copyright 2004 Client Server International, Inc. All rights reserved.
 * CSII PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.aic.ssm.util;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

import org.springframework.ui.Model;

/**
 * Util类。
 * <p>
 * Created on 2004-8-18 Modification history
 * <p>
 *
 * @author LF Expert Group, LFFF
 * @version 1.0
 * @since 1.0
 */
public class Util {

	private final static java.lang.String PWD_SEED = "_UIBSForManagementDigest200805131104";
	private final static java.lang.String PWD_SEED_ENT = "_UIBSForEnterpriseDigest200805131104";
	private final static java.lang.String PWD_SEED_PER = "_UIBSForEnterpriseDigest200805131104";

	public static final String[] Money = { "AUD", "CAD", "CHF", "CNY", "EUR",
			"GBP", "HKD", "JPY", "NZD", "SEK", "SGD", "USD" };
	public static final String[] MoneyName = { "澳大利亚元", "加拿大元 ", "瑞士法郎", "人民币",
			"欧元", "英镑", "港币", "日本元", "新西兰元", "瑞典克朗", "新加坡元", "美元" };

	private static String DES_KEY = "";
	private static String MSG_SYSFLAG;
	private static String MSG_URL;
	private static String WEB_URL_ROOT = "http://202.108.90.233:81/pwxweb/";
	private static String RULEID = "transTimeOut";

	public static String getMSG_URL() {
		return MSG_URL;
	}

	public void setMSG_URL(String mSG_URL) {
		Util.MSG_URL = mSG_URL;
	}

	public static String getMSG_SYSFLAG() {
		return MSG_SYSFLAG;
	}

	public void setMSG_SYSFLAG(String mSG_SYSFLAG) {
		Util.MSG_SYSFLAG = mSG_SYSFLAG;
	}

	/**
	 *
	 */
	public Util() {
		super();
	}

	public final static String DEFAULT_PASSWORD = "123456";

	public static void main(String[] args)// throws java.io.IOException
	{
		// LogFactory.getLog(getClass()).info(Util.digest("sysadmin" +
		// "88888888"));
		// LogFactory.getLog(getClass()).info(Util.digest("authadmin" +
		// "88888888"));
		// LogFactory.getLog(getClass()).info(Util.digest("csysadmin" +
		// "88888888"));
		// LogFactory.getLog(getClass()).info(Util.digest("cauthadmin" +
		// "88888888"));
		// LogFactory.getLog(getClass()).info(Util.digest("88888888" +
		// "88888888"));
		// LogFactory.getLog(getClass()).info(Util.digest("77777777" +
		// "77777777"));
		// LogFactory.getLog(getClass()).info(Util.digest("66666666" +
		// "66666666"));
	}

	public static boolean isDateEqual(java.util.Date date1, java.util.Date date2) {

		java.util.Calendar cal1 = java.util.Calendar.getInstance();
		cal1.setTime(date1);
		java.util.Calendar cal2 = java.util.Calendar.getInstance();
		cal2.setTime(date2);

		return (cal1.get(java.util.Calendar.YEAR) == cal2
				.get(java.util.Calendar.YEAR)
				&& cal1.get(java.util.Calendar.MONTH) == cal2
						.get(java.util.Calendar.MONTH) && cal1
					.get(java.util.Calendar.DAY_OF_MONTH) == cal2
				.get(java.util.Calendar.DAY_OF_MONTH));

	}

	public static int compareDate(java.util.Date date1, java.util.Date date2) {

		java.util.Calendar cal1 = java.util.Calendar.getInstance();
		cal1.setTime(date1);
		java.util.Calendar cal2 = java.util.Calendar.getInstance();
		cal2.setTime(date2);

		if (cal1.get(java.util.Calendar.YEAR) > cal2
				.get(java.util.Calendar.YEAR))
			return 1;

		if (cal1.get(java.util.Calendar.YEAR) < cal2
				.get(java.util.Calendar.YEAR))
			return -1;

		if (cal1.get(java.util.Calendar.MONTH) > cal2
				.get(java.util.Calendar.MONTH))
			return 1;

		if (cal1.get(java.util.Calendar.MONTH) < cal2
				.get(java.util.Calendar.MONTH))
			return -1;

		if (cal1.get(java.util.Calendar.DAY_OF_MONTH) > cal2
				.get(java.util.Calendar.DAY_OF_MONTH))
			return 1;

		if (cal1.get(java.util.Calendar.DAY_OF_MONTH) < cal2
				.get(java.util.Calendar.DAY_OF_MONTH))
			return -1;

		return 0;

	}

	/**
	 * getCurrentDateString
	 * 
	 * @return yyyymmdd
	 */
	public static String getCurrentDateString() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DATE);
		return "" + calendar.get(Calendar.YEAR)
				+ (month < 10 ? "0" + month : "" + month)
				+ (date < 10 ? "0" + date : "" + date);
	}

	public static boolean isNullOrEmpty(String orgStr) {
		return (orgStr == null || orgStr.trim().length() == 0);
	}

	public static boolean isNullOrEmpty(Object obj) {
		if (obj instanceof Object[]) {
			Object[] o = (Object[]) obj;
			for (int i = 0; i < o.length; i++) {
				Object object = o[i];
				if ((object == null) || (("").equals(object))) {
					return true;
				}
			}
		} else {
			if ((obj == null) || (("").equals(obj))) {
				return true;
			}
		}

		return false;
	}

	// 去掉字符串中的所有空格
	public static String filterSBCCase(String str) {
		char[] ch = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ch.length; i++) {
			if (!Character.isWhitespace(ch[i])) {
				sb.append(String.valueOf(ch[i]));
			}
		}
		return sb.toString();
	}

	/**
	 *
	 * @param str
	 * @return
	 */
	public static boolean isDigit(String str) {
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (!Character.isDigit(ch[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	public static boolean isOverLong(String str, int length) {
		if (filterSBCCase(str).length() > length) {
			return true;
		}
		return false;
	}

	// 判断是否为全角
	public static boolean isChinese(String str) {
		byte[] bt = str.getBytes();
		for (int i = 0; i < bt.length; i++) {
			if (!(new Byte(bt[i]).toString()).startsWith("-")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * get substring in bytes length
	 * 
	 * @param orgString
	 *            original string
	 * @param lengthInBytes
	 *            bytes length
	 * @return substring
	 */
	public static final String subStringInBytes(String orgString, int startPos,
			int lengthInBytes) {

		if (orgString == null)
			return null;

		byte[] orgBytes = orgString.getBytes();
		if (startPos < 0 || startPos > orgBytes.length)
			return null;
		else if (lengthInBytes < startPos)
			return null;

		byte[] newBytes;
		int newLength = orgBytes.length - startPos;
		if (lengthInBytes < newLength)
			newLength = lengthInBytes;

		newBytes = new byte[newLength];
		System.arraycopy(orgBytes, startPos, newBytes, 0, newLength);

		return new String(newBytes);
	}

	public static java.util.Date getToDate(String s) {
		StringBuffer sb = new StringBuffer();
		sb.append(s.substring(0, 4)).append("-").append(s.substring(4, 6))
				.append("-").append(s.substring(6, 8));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date obj = null;
		try {
			obj = sdf.parse(sb.toString());
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return obj;
	}

	/**
	 * 将日期的字符串转化为日期类型 格式为"2004-10-10" --> Date
	 * <p>
	 * <code> checkStartEndDate </code>
	 * </p>
	 * 
	 * @param context
	 * @throws ValidationException
	 * @author Songyi 2004-12-15
	 * @since 1.1
	 */
	public static java.util.Date getString2Date(String sDate) {
		if(!Util.isNullOrEmpty(sDate)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date obj = null;
			try {
				obj = sdf.parse(sDate);
			} catch (ParseException e) {
				e.printStackTrace();
	
			}
			return obj;
		}else{
			return null;
		}
	}

	public static java.util.Date getString2Date2(String sDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		java.util.Date obj = null;

		try {
			obj = sdf.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();

		}

		return obj;
	}

	public static String getDate2ChnString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateString = sdf.format(date);
		StringBuffer sb = new StringBuffer();
		sb.append(dateString.substring(0, 4)).append("年")
				.append(dateString.substring(4, 6)).append("月")
				.append(dateString.substring(6, 8)).append("日");
		return sb.toString();
	}

	public static String getDate2String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	public static String getDate3String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static String getCurrentDate3String() {
		return getDate3String(new Date());
	}

	public static String getDate4String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(date);
	}

	public static String getTranTd(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
		return sdf.format(date);
	}

	public static String getDate5String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}

	public static String getDate6String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}

	public static String getDate8String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static Date getDate7String(String sDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		java.util.Date obj = null;

		try {
			obj = sdf.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();

		}

		return obj;

	}

	/**
	 * 取系统当前日期，格式为： yyyy-mm-dd
	 * 
	 * @author zongyanfeng
	 */
	public static String getNowDateString() {
		return getDate2String(new Date());
	}

	/**
	 * 取系统当前日期，格式为： yyyy-mm-dd
	 * 
	 * @author zongyanfeng
	 */
	public static Date getNowDate() {
		return getString2Date(getNowDateString());
	}

	/**
	 * 开始日期应大于等于结束日期
	 * <p>
	 * <code> checkStartEndDate </code>
	 * </p>
	 * 
	 * @param context
	 * @throws ValidationException
	 * @author Songyi 2004-12-15
	 * @since 1.1
	 */
	public static void checkStartEndDate2(Date startDate, Date endDate)
			throws ValidationException {
		if ((startDate.toString().length() > 0)
				&& (endDate.toString().length() > 0)) {
			if (startDate.getTime() > endDate.getTime()) {
				throw new ValidationException("开始日期不能大于结束日期");
			}
		}

	}

	/**
	 * 比较两个XXXX-XX-XX格式的日期
	 * <p>
	 * <code> compareDate2 </code>
	 * </p>
	 * 
	 * @param context
	 * @throws
	 * @author tanzs 20081028
	 * @since 1.1
	 */
	public static boolean date1LargeDate2(String beginDate, String endDate) {

		String s1 = "";
		String s2 = "";

		String[] bd = beginDate.split("-");
		String[] ed = endDate.split("-");

		for (int i = 0; i < bd.length; i++) {
			s1 += bd[i];
		}

		for (int i = 0; i < ed.length; i++) {
			s2 += ed[i];
		}

		if (Integer.parseInt(s1) > Integer.parseInt(s2)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkStartEndDate3(Date startDate, Date endDate)
			throws ValidationException {
		if ((startDate.toString().length() > 0)
				&& (endDate.toString().length() > 0)) {
			if (startDate.getTime() >= endDate.getTime()) {
				return true;
				// throw new ValidationException("validation.startEndDate");
			}

		}
		return false;

	}

	public static String parseStringPattern(Object v, int scale) {
		String temp = "###,###,###,###,###,###,###,##0.";
		for (int i = 0; i < scale; i++) {
			temp += "0";
		}
		DecimalFormat format = new DecimalFormat(temp);
		return format.format(v).toString();
	}

	public static String parseStringPattern(double v, int scale) {
		String temp = "###,###,###,###,###,###,###,##0.";
		for (int i = 0; i < scale; i++) {
			temp += "0";
		}
		DecimalFormat format = new DecimalFormat(temp);
		return format.format(v).toString();
	}

	//
	public static boolean isCurrentDate(String strDate) {
		return strDate == null || !strDate.equals(Util.getCurrentDateString()) ? false
				: true;
	}

	public static String rollDate(String strDate, boolean up) {
		return Util.rollDate(Util.getToDate(strDate), up);
	}

	public static String rollDate(Date date, boolean up) {

		if (date == null)
			return null;
		String returnString = null;

		GregorianCalendar beforeCalendar = new GregorianCalendar();
		beforeCalendar.setTime(date);
		beforeCalendar.add(Calendar.DATE, up ? +1 : -1);
		int beforeDate = beforeCalendar.get(Calendar.DATE);
		int beforeMonth = beforeCalendar.get(Calendar.MONTH) + 1;
		int beforeYear = beforeCalendar.get(Calendar.YEAR);

		return "" + beforeYear
				+ (beforeMonth < 10 ? "0" + beforeMonth : "" + beforeMonth)
				+ (beforeDate < 10 ? "0" + beforeDate : "" + beforeDate);

	}

	public static String rollDate(Date date, boolean up, String pattern) {
		if (date == null)
			return null;
		GregorianCalendar beforeCalendar = new GregorianCalendar();
		beforeCalendar.setTime(date);
		beforeCalendar.add(Calendar.DATE, up ? +1 : -1);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(beforeCalendar.getTime());

	}

	public static String getDateString(String dateStr) {
		StringBuffer sb = new StringBuffer();
		dateStr = dateStr.trim();
		return sb.append(dateStr.substring(0, 4))
				.append(dateStr.substring(5, 7))
				.append(dateStr.substring(8, 10)).toString();
	}

	public static String getTimeString(String timeStr) {
		StringBuffer sb = new StringBuffer();
		timeStr = timeStr.trim();
		return sb.append(timeStr.substring(0, 2))
				.append(timeStr.substring(3, 5))
				.append(timeStr.substring(6, 8)).toString();
	}

	public static String formatDateString(String strDate, char splitChar) {
		if (strDate == null || strDate.length() < 8)
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append(strDate.substring(0, 4)).append(splitChar)
				.append(strDate.substring(4, 6)).append(splitChar)
				.append(strDate.substring(6, 8));
		return sb.toString();
	}

	public static java.util.Date getDate(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		java.util.Date obj = null;
		try {
			obj = sdf.parse(s);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return obj;
	}

	public static java.util.Date getDate2(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date obj = null;
		try {
			obj = sdf.parse(s);
		} catch (ParseException e) {

			return null;
		}
		return obj;
	}

	public static String[] getStringArray(Object object) {
		if (object == null)
			return null;
		if (object.getClass().isArray()) {
			return (String[]) object;
		} else {
			String tmpStrs[] = new String[1];
			tmpStrs[0] = (String) object;
			return tmpStrs;
		}
	}

	public static BigDecimal[] getBigDecimalArray(Object object) {
		if (object == null)
			return null;
		if (object.getClass().isArray()) {
			return (BigDecimal[]) object;
		} else {
			BigDecimal tmpStrs[] = new BigDecimal[1];
			tmpStrs[0] = (BigDecimal) object;
			return tmpStrs;
		}
	}

	// A value of Map is Null or Empty?
	public static boolean JudgeNullorEmpty(Map map, String field) {
		Object obj = map.get(field);
		if (obj == null)
			return true;
		else if (obj.equals(""))
			return true;
		else
			return false;
	}

	public static BigDecimal getBigDecimal(Object object) {
		if (object == null)
			return null;

		BigDecimal result;

		if (object instanceof BigDecimal)
			result = (BigDecimal) object;
		else if (object instanceof String) {
			result = new BigDecimal((String) object);
		} else if (object instanceof Number) {
			result = new BigDecimal(((Number) object).doubleValue());
		} else
			throw new RuntimeException("unsupported_bigdecimal_convert");

		return result;

	}

	public static BigDecimal str2BigDecimal(String input) {
		return new BigDecimal(input);
	}

	public static BigDecimal round(BigDecimal v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal one = new BigDecimal("1");
		return v.divide(one, scale, BigDecimal.ROUND_HALF_UP);

	}

	public static String mixString(String string) {
		char[] letter = new char[] { '6', 'b', '1', 'd', '3', 'f', '2', 'h',
				'8', 'j', 'k', 'm', 'z', 'n', '0', 'q', 'r', '9', 'y', '5' };
		String mixString = "";
		Random ran = new Random();
		for (int i = 0; i < string.length(); i++) {

			int postion = ran.nextInt(20);
			mixString += String.valueOf(string.charAt(i))
					+ String.valueOf(letter[postion]);
		}
		return mixString;
	}

	public static String unMixString(String string) {
		String realString = "";
		for (int i = 0; i < string.length(); i++) {
			realString += string.substring(i, i + 1);
			i++;
		}
		return realString;
	}

	// 产生N位随机数
	public static String randomPassword(int n) {
		Random random = new Random();

		String randomStr = "";
		for (int i = 0; i < n; i++) {
			int number = random.nextInt(10);
			randomStr = randomStr.concat(new Integer(number).toString());
		}
		return randomStr;
	}


	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	// 老网银用户密码加解密算法
	public static String oldDbEncrypt(String strIn) {
		// 密钥
		String strEncrypt = new String("asdf023p489ilsd;fnm9u2345rxc,a/234r0");
		char[] cKey = strEncrypt.toCharArray();
		char c[] = strIn.toCharArray();
		char d[] = new char[c.length];
		int iIndex = c.length % cKey.length;
		for (int i = 0; i < c.length; i++) {
			d[i] = (char) (c[i] ^ cKey[iIndex]);
		}
		String strOut = new String(d);
		return strOut;
	}

	/**
	 * Get current date string，in the format： yyyy-mm-dd
	 * 
	 * @return String
	 * @author Richard
	 */
	public static String getCurrentDateStr() {
		return getDate3String(new Date());
	}

	/**
	 * Get current date, in the format: yyyy-mm-dd
	 * 
	 * @return Date
	 * @author Richard
	 */
	public static Date getCurrentDate() {
		return getString2Date(getCurrentDateStr());
	}

	public static Date getCurrentDateWithHMS() {
		return getDate7String(getCurrentSystemTimeStr());
	}

	public static String getCurrentSystemTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * Get current time, in the format:hhmmss
	 * 
	 * @return String
	 * @author Justlin
	 */
	public static String getCurrentTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 获得从now开始iMonth个月前的一天
	 * 
	 * @param now
	 * @param iMonth
	 * @return
	 */
	public static Date getDateBefore(Date now, int iMonth) {
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.MONTH, 0 - iMonth);
		return c.getTime();
	}

	/**
	 * 得到日期的月份的第一天（如：param 2008-02-23 return 2008-02-01）
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthFirstDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	 * 得到日期的年份的第一天（如：param 2008-02-23 return 2008-01-01）
	 * 
	 * @param date
	 * @return
	 */
	public static Date getYearFirstDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, 0);
		return c.getTime();
	}

	public static Date getDateAfter(Date now, int iDay) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(now);
		calendar.add(Calendar.DATE, iDay);
		return calendar.getTime();

	}

	/**
	 * 比较2个日期
	 * 
	 * @param StartDate
	 * @param EndDate
	 * @return
	 */
	public static int dateCompare(String startDate, String endDate) {

		Date start = getToDate(startDate);
		Date end = getToDate(endDate);

		if (start.before(end)) {
			return -1;
		} else if (start.after(end)) {
			return 1;
		} else
			return 0;

	}

	// get divided string
	public static String[] getStrDivValues(String str, String reg) {

		if (reg.equals("|")) {
			reg = "[|]";
		}

		String tmp = str.replaceAll(reg, ":");
		String[] result = tmp.split(":");

		return result;
	}

	/**
	 *
	 * 比较两个对象是否相等
	 *
	 * @param firstStr
	 *
	 * @param secondStr
	 *
	 * @return
	 *
	 */

	public static boolean trimAndEquals(Object firstStr, Object secondStr) {
		if (firstStr == null && secondStr == null) {
			return true;
		} else if (firstStr == null || secondStr == null) {
			return false;
		} else {
			return toStringAndTrim(firstStr).equals(toStringAndTrim(secondStr));
		}

	}

	/**
	 *
	 * 获取一个对象的string形式并去除空格
	 *
	 * @param object
	 * @return
	 *
	 * @version 1.0
	 * @since 1.0
	 */
	public static String toStringAndTrim(Object object) {
		if (object == null) {
			return "";
		} else {
			return object.toString().trim();
		}

	}

	public static boolean isContainByArray(String[] array, String keyName) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i].equals(keyName)) {
					return true;
				}
			}
		}
		return false;
	}

	public final static String DATEUNITTYPE_D = "D";
	public final static String DATEUNITTYPE_W = "W";
	public final static String DATEUNITTYPE_M = "M";
	public final static String DATEUNITTYPE_Y = "Y";

	public static int resolveDateRoleUnit(String dateunitType) {
		if (DATEUNITTYPE_D.equals(dateunitType)) {
			return Calendar.DATE;
		} else if (DATEUNITTYPE_W.equals(dateunitType)) {
			return Calendar.WEDNESDAY;
		} else if (DATEUNITTYPE_M.equals(dateunitType)) {
			return Calendar.MONTH;
		} else if (DATEUNITTYPE_Y.equals(dateunitType)) {
			return Calendar.YEAR;
		} else {
			throw new IllegalArgumentException(
					"the date type is invalid,valid date type is D or W or M or Y");
		}
	}

	/**
	 * 根据当前日期计算出与当前日期间隔时间单位的日期
	 * 
	 * @param currentDate
	 *            当前日期
	 * @param dateUnit
	 *            时间单位
	 * @param prev
	 *            前滚标志
	 * @param dateUnitType
	 *            滚动日期单位的类型
	 * @return
	 */
	public static java.sql.Date rollDateByDateUnit(Date currentDate,
			int dateUnit, boolean prev, int dateUnitType) {
		Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
		if (currentDate != null) {
			calendar.setTime(currentDate);
		}
		calendar.add(dateUnitType, prev ? -dateUnit : dateUnit);
		return new java.sql.Date(calendar.getTime().getTime());
	}

	/**
	 * 根据当前日期计算出与当前日期间隔天数的日期
	 * 
	 * @param dateTime
	 * @param value
	 * @return
	 */
	public static java.sql.Date rollDateByDay(Date currentDate, int day,
			boolean prev) {
		Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
		if (currentDate != null) {
			calendar.setTime(currentDate);
		}
		calendar.add(Calendar.DATE, prev ? -day : day);
		return new java.sql.Date(calendar.getTime().getTime());
	}

	public static java.util.Date rollDateByWeek(Date currentDate, int week,
			boolean prev) {
		Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
		if (currentDate != null) {
			calendar.setTime(currentDate);
		}
		calendar.add(Calendar.WEDNESDAY, prev ? -week : week);
		return new java.util.Date(calendar.getTime().getTime());
	}

	public static java.sql.Date rollDateByMonth(Date currentDate, int month,
			boolean prev) {
		Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
		if (currentDate != null) {
			calendar.setTime(currentDate);
		}
		calendar.add(Calendar.MONTH, prev ? -month : month);
		return new java.sql.Date(calendar.getTime().getTime());
	}

	public static java.sql.Date rollDateByYear(Date currentDate, int year,
			boolean prev) {
		Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
		if (currentDate != null) {
			calendar.setTime(currentDate);
		}
		calendar.add(Calendar.YEAR, prev ? -year : year);
		return new java.sql.Date(calendar.getTime().getTime());
	}

	/**
	 *
	 * @param listMap
	 * @param key
	 * @return
	 */
	public static Object[] listToArray(List listMap, String key) {
		if (listMap != null) {
			List array = new ArrayList();
			for (Iterator it = listMap.iterator(); it.hasNext();) {
				Map tmp = (Map) it.next();
				array.add(tmp.get(key));
			}
			return array.toArray();
		}
		return null;
	}

	public static Map listToMapByKey(List list, String key) {
		Map result = new HashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map tmp = (Map) it.next();
			Object keyValue = tmp.get(key);
			if (!result.keySet().contains(keyValue)) {// new key
				List tmpList = new ArrayList();
				tmpList.add(tmp);
				result.put(keyValue, tmpList);
			} else {// append key value list
				List tmpList = (List) result.get(keyValue);
				tmpList.add(tmp);
			}
		}
		return result;
	}

	public final static boolean isContain(List list, String key, Object value) {
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map tmp = (Map) list.get(i);
				if (value.equals(tmp.get(key))) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getMoneyName(String money) {
		for (int k = 0; k < Money.length; k++) {
			if (money.equalsIgnoreCase(Money[k])) {
				return MoneyName[k];
			}
		}
		return "";
	}


	public static String getWEB_URL_ROOT() {
		return WEB_URL_ROOT;
	}

	public void setWEB_URL_ROOT(String wEB_URL_ROOT) {
		WEB_URL_ROOT = wEB_URL_ROOT;
	}


	public static Map str2map(String plain) throws Exception {
		Map map = new HashMap();
		Pattern p = Pattern.compile("([^=;]+)=([^=;]+);");
		Matcher m = p.matcher(plain);
		while (m.find()) {
			map.put(m.group(1), m.group(2));
		}
		// timeOut((String)map.get("Timestamp"));
		// Timestamp=1390440084539
		return map;
	}

	public static String getDES_KEY() {
		return DES_KEY;
	}

	public void setDES_KEY(String dES_KEY) {
		DES_KEY = dES_KEY;
	}

	public static String trimString(Object object) {
		return object != null ? String.valueOf(object).trim() : "";
	}

	/**
	 * 验证腾讯发送是否成功
	 * 
	 * {method specification, must edit}
	 *
	 * @return
	 *
	 * @version 1.0
	 * @since 1.0
	 */
	public static boolean IsSendToTX(Map item) {
		if ("0".equalsIgnoreCase(String.valueOf(item.get("errcode")))
				|| "ok".equalsIgnoreCase(String.valueOf(item.get("errmsg")))
				|| item.get("errcode") == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 组装部门渠道
	 */
	public static List getChennelList(String Channel) {
		String[] ch = Channel.split(",");
		List chList = new ArrayList();
		for (int i = 0; i < ch.length; i++) {
			Map chMap = new HashMap();
			chMap.put("channel", ch[i]);
			chMap.put("flag", "Y");
			chList.add(chMap);
		}
		return chList;
	}

	/**
	 * 判断返回码
	 */
	public static String checkErrCodeMsg(String errcode) {
		String errmsg = "validation.weixinnews.senderror";// 其他错误微信群发失败
		if ("40004".equals(errcode)) {
			errmsg = "validation.weixinnews.senderror" + 40004;// 不合法的媒体类型
		} else if ("40007".equals(errcode)) {
			errmsg = "validation.weixinnews.senderror" + 40007;// 不合法的媒体id
																// ：media_id已经失效
		} else if ("40009".equals(errcode)) {
			errmsg = "validation.weixinnews.senderror" + 40009;// 不合法的图片文件大小
		} else if ("45009".equals(errcode)) {
			errmsg = "validation.weixinnews.senderror" + 45009;// 接口调用超过限制
		} else if ("48001".equals(errcode)) {
			errmsg = "validation.weixinnews.senderror" + 48001;// 没有权限调用该接口
		}
		if ("45028".equals(errcode)) {
			errmsg = "validation.weixinnews.senderror" + 45028;// 没有群发图文消息的权限
		}
		return errmsg;
	}

	/**
	 * 将string转换为java.sql.Date
	 * 
	 * @param dateTime
	 * @param value
	 * @return
	 */
	public static java.sql.Date getString2Date3(String date) {
		String dateStr = Util.toStringAndTrim(date);
		if (Util.isNullOrEmpty(dateStr))
			return null;
		dateStr = dateStr.replaceAll("-", "");
		try {
			SimpleDateFormat DATE_FORMATE = new SimpleDateFormat("yyyyMMdd");
			return new java.sql.Date(DATE_FORMATE.parse(dateStr).getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据当前日期计算出与当前日期间隔天数的日期
	 * 
	 * @param dateTime
	 * @param value
	 * @return
	 */
	public static java.sql.Date rollDateByDay2(java.sql.Date currentDate,
			int day, boolean prev) {
		Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
		if (currentDate != null) {
			calendar.setTime(currentDate);
		}
		calendar.add(Calendar.DATE, prev ? -day : day);
		return new java.sql.Date(calendar.getTime().getTime());
	}

	public static String getTimeByHms(int time) {
		String formatTime = "";
		int length = (time + "").length();
		if (length == 1) {
			formatTime = "00000";
		} else if (length == 2) {
			formatTime = "0000";
		} else if (length == 3) {
			formatTime = "000";
		} else if (length == 4) {
			formatTime = "00";
		} else if (length == 5) {
			formatTime = "0";
		}
		return formatTime + time;
	}

	public static String formatString2(Object date, String pattern) {
		try {
			SimpleDateFormat DATE_FORMATE = new SimpleDateFormat(pattern);
			return DATE_FORMATE.format(date);

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public static Date getDate9String(String sDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmss");

		java.util.Date obj = null;

		try {
			obj = sdf.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();

		}

		return obj;

	}

	public static Date getDate9StringyyyyMMdd(String sDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		java.util.Date obj = null;

		try {
			obj = sdf.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();

		}

		return obj;

	}
	
	// 截取list
	public static List modList(List list, int begin, int end) {
		List newList = new ArrayList();
		for (int i = begin; i <= end; i++) {
			if(i<list.size()){
				newList.add(list.get(i));
			}
		}
		return newList;
	}

	// 分页
	public static Model getPage(Model model,List list, HttpServletRequest request) {

		int pageSize = 10;
		int allPage = list.size() / pageSize;
		int curPage = 1;
		if (!Util.isNullOrEmpty(request.getParameter("curPage")))
			curPage = Integer.valueOf(request.getParameter("curPage")
					.toString());
		if(!Util.isNullOrEmpty(request.getParameter("opera"))){
			String opera = request.getParameter("opera").toString();
			switch (opera) {
			case "S":
				curPage = 1;
				break;
			case "W":
				curPage = allPage;
				break;
			case "Q":
				curPage = curPage-1;
				break;
			case "H":
				curPage = curPage+1;
				break;
			}
		}
		if (list.size() % pageSize != 0) {
			allPage += 1;
		}
		list = Util.modList(list, (curPage - 1) * pageSize, curPage * pageSize - 1);

		model.addAttribute("allPage", allPage);
		model.addAttribute("curPage", curPage);
		model.addAttribute("resultList", list);
		return model;
	}

	//list2Model
	public static Model list2Model(Model model,List list, HttpServletRequest request) {
		for(int i = 0; i<list.size();i++){
			model.addAttribute(list.get(i).toString(), request.getParameter(list.get(i).toString()));
		}
		return model;
	}
	
	//计算时间。initDate：开始时间；doway： 计算单位；count：计算数量；
	public static String countDate(String initDateString, int count , String doway) {
		Date initDate = getDate9StringyyyyMMdd(initDateString);
		 Format f = new SimpleDateFormat("yyyy-MM-dd");
		 Calendar date = Calendar.getInstance();
			 date.setTime(initDate);
			 switch (doway ) {
				case "1":
					date.add(Calendar.DATE,count);
						break;
				case "2":
					date.add(Calendar.WEEK_OF_YEAR, count);
						break;
				case "3":
					date.add(Calendar.MONTH, count);
					break;
				case "4":
					date.add(Calendar.YEAR, count);
					break;
			}
		return f.format(date.getTime());
	}
	//数组排序
	public static double[] quickArraySort(double array[], int low, int high) {// 传入low=0，high=array.length-1;
        int p_pos, i;// pivot->位索引;p_pos->轴值。
        double pivot, t;// pivot->位索引;p_pos->轴值。
        if (low < high) {
            p_pos = low;
            pivot = array[p_pos];
            for (i = low + 1; i <= high; i++)
                if (array[i] < pivot) {
                    p_pos++;
                    t = array[p_pos];
                    array[p_pos] = array[i];
                    array[i] = t;
                }
            t = array[low];
            array[low] = array[p_pos];
            array[p_pos] = t;
            // 分而治之
            quickArraySort(array, low, p_pos - 1);// 排序左半部分
            quickArraySort(array, p_pos + 1, high);// 排序右半部分
        }
		return array;
	}
	
	//list排序double
	public static List quickListSort(List list, int low, int high,String sortName) {// 传入low=0，high=array.length-1;sortName:需要排序的列
        int  p_pos, i;// pivot->位索引;p_pos->轴值。
        double pivot;
        Map t = new HashMap();
        if (low < high) {
            p_pos = low;
            Map one = (Map)list.get(p_pos);
            pivot = (double) one.get(sortName);
            for (i = low + 1; i <= high; i++)
                if (((double) ((Map)list.get(i)).get(sortName)) > pivot) {
                    p_pos++;
                    t = (Map)list.get(p_pos);
                    list.set(p_pos,list.get(i));
                    list.set(i,t );
                }
            t =(Map)list.get(low);
            list.set(low, (Map)list.get(p_pos));
            list.set(p_pos, t);
            // 分而治之
            quickListSort(list, low, p_pos - 1, sortName);// 排序左半部分
            quickListSort(list, p_pos + 1, high, sortName);// 排序右半部分
        }
        return list;
	}
	//list排序int
		public static List quickListIntSort(List list, int low, int high,String sortName) {// 传入low=0，high=array.length-1;sortName:需要排序的列
	        int  p_pos, i;// pivot->位索引;p_pos->轴值。
	        int pivot;
	        Map t = new HashMap();
	        if (low < high) {
	            p_pos = low;
	            Map one = (Map)list.get(p_pos);
	            pivot = (int) one.get(sortName);
	            for (i = low + 1; i <= high; i++)
	                if (((int) ((Map)list.get(i)).get(sortName)) > pivot) {
	                    p_pos++;
	                    t = (Map)list.get(p_pos);
	                    list.set(p_pos,list.get(i));
	                    list.set(i,t );
	                }
	            t =(Map)list.get(low);
	            list.set(low, (Map)list.get(p_pos));
	            list.set(p_pos, t);
	            // 分而治之
	            quickListIntSort(list, low, p_pos - 1, sortName);// 排序左半部分
	            quickListIntSort(list, p_pos + 1, high, sortName);// 排序右半部分
	        }
	        return list;
		}
		
		public static double mindoubleArr(double[] initP,boolean bool) {
			double result = 0;
			if(initP.length>0){
				for (int i = 0; i < initP.length; i++) {
					if(bool){
						if(result > initP[i]){
							result = initP[i];
						}
					}else{
						if(result < initP[i]){
							result = initP[i];
						}
					}
				}
			}
			return result;
		}
		
		public static double mindoubleList(List<Double> initP,boolean bool) {
			double result = 0;
			if(initP.size()>0){
				for (int i = 0; i < initP.size(); i++) {
					if(bool){
						if(result > initP.get(i)){
							result = initP.get(i);
						}
					}else{
						if(result < initP.get(i)){
							result = initP.get(i);
						}
					}
				}
			}
			return result;
		}		    
}
