package cn.emay.sdk.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final long ONE_HOUR_TIME_LONG = 3600000;

	public static String toString(Date date, String format) {
		String dateStr = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			dateStr = sdf.format(date);
		} catch (Exception e) {
		}
		return dateStr;
	}

	public static Date parseDate(String dateStr, String format) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(dateStr);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 获取日期当天的最小时间日期,0点
	 */
	public static Date getMinTimeDateByDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		String datestr = toString(date, "yyyyMMdd");
		return parseDate(datestr, "yyyyMMdd");
	}

	/**
	 * 获取日期当天的最大时间日期,12点整
	 */
	public static Date getMaxTimeDateByDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		String datestr = toString(date, "yyyyMMdd");
		Date d = parseDate(datestr, "yyyyMMdd");
		return new Date(d.getTime() + 24l * 60l * 60l * 1000l - 1l);
	}

	public static long subTime(Date startDate, Date endDate) {
		return endDate.getTime() - startDate.getTime();
	}

	/**
	 * 获取上月第一天最早时间
	 *
	 * @return Date
	 */
	public static Date getLastMonthFirstDay() {
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		cal_1.setTime(getMinTimeDateByDate(new Date()));
		cal_1.add(Calendar.MONTH, -1);
		cal_1.set(Calendar.DAY_OF_MONTH, 1);
		return cal_1.getTime();
	}

	/**
	 * 获取上月最后一天最晚时间
	 *
	 * @return Date
	 */
	public static Date getLastMonthLastDay() {
		Calendar cale = Calendar.getInstance();
		cale.setTime(getMinTimeDateByDate(new Date()));
		cale.add(Calendar.MONTH, -1);
		cale.set(Calendar.DAY_OF_MONTH, cale.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new Date(cale.getTime().getTime() + 1000l * 60l * 60l * 24l - 1l);
	}

	/**
	 * 获取本月第一天最早时间
	 *
	 * @return Date
	 */
	public static Date getNowMonthFirstDay() {
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		cal_1.setTime(getMinTimeDateByDate(new Date()));
		cal_1.add(Calendar.MONTH, 0);
		cal_1.set(Calendar.DAY_OF_MONTH, 1);
		return cal_1.getTime();
	}

	/**
	 * 获取本月最后一天最晚时间
	 *
	 * @return Date
	 */
	public static Date getNowMonthLastDay() {
		Calendar cale = Calendar.getInstance();
		cale.setTime(getMinTimeDateByDate(new Date()));
		cale.set(Calendar.DAY_OF_MONTH, cale.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new Date(cale.getTime().getTime() + 1000l * 60l * 60l * 24l - 1l);
	}

	/**
	 * 获取当前时间变量内月份的第一天的日期
	 * 
	 * @param changeMonthNum
	 *            变动月份数。例如 -1：前一个月的最小日期 1 ：后一个月的最小日期
	 * @return Date
	 */
	public static Date getOtherMonthFirstDay(int changeMonthNum) {
		Calendar cale = Calendar.getInstance();
		Date nowMonthFirstDay = getNowMonthFirstDay();
		cale.setTime(nowMonthFirstDay);
		cale.add(Calendar.MONTH, changeMonthNum);
		return cale.getTime();
	}

	/**
	 * 获取本月最后一天
	 *
	 * @return Date
	 */
	public static Date getTheMonthLastDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.set(Calendar.DAY_OF_MONTH, cale.getActualMaximum(Calendar.DAY_OF_MONTH));
		cale.set(Calendar.HOUR, 0);
		cale.set(Calendar.HOUR_OF_DAY, 0);
		cale.set(Calendar.MINUTE, 0);
		cale.set(Calendar.SECOND, 0);
		cale.set(Calendar.MILLISECOND, 0);
		return cale.getTime();
	}

	/**
	 * 返回之前某之后的某天开始日期、结束或者当前时分秒
	 *
	 * @param date
	 *            日期
	 * @param n
	 *            正数之后的某天，负数之前某天
	 * @param type
	 *            返回数据类型，开始00:00:00 结束 23:59:59或当
	 *
	 *            ex :
	 *
	 *            getOtherDay(new Date(), -1, ""); 前一天当前时分秒
	 *
	 *            getOtherDay(new Date(), -1, "start"); 前一天开始时间00:00:00
	 *
	 *            getOtherDay(new Date(), -1, "end"); 前一天结束时间23:59:59
	 * @return
	 */
	public static Date getOtherDay(Date date, int n, String type) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if ("start".equals(type)) {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
		} else if ("end".equals(type)) {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MINUTE, 59);
		}
		calendar.add(Calendar.DAY_OF_MONTH, n);
		date = calendar.getTime();
		return date;
	}

	public static void main(String[] args) {
		Date otherDay = getOtherDay(new Date(), -1, "");
		System.out.println(otherDay);
		// System.out.println(toString(getTheMonthLastDay(new Date()),
		// "yyyy-MM-dd HH:mm:ss"));
		// System.out.println(toString(getLastMonthLastDay(), "yyyy-MM-dd
		// HH:mm:ss"));
		// System.out.println(toString(getNowMonthFirstDay(), "yyyy-MM-dd
		// HH:mm:ss"));
		// System.out.println(toString(getNowMonthLastDay(), "yyyy-MM-dd
		// HH:mm:ss"));
	}

}
