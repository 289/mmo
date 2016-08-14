package mmo.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2011 by TianShen
 *
 * @author Yan XiaoPing (xiaopingfriend@sina.com) Modify on 2011-8-16 下午08:03:33
 * @version 1.0
 */
public class DateUtil {

	/**
	 * 毫秒时间(单位毫秒)；
	 */
	public final static long ONE_MILLISECOND = TimeUnit.MILLISECONDS.toMillis(1);

	/**
	 * 秒(单位毫秒)；
	 */
	public final static long ONE_SECONDE = TimeUnit.SECONDS.toMillis(1);

	/**
	 * 分钟(单位毫秒)；
	 */
	public final static long ONE_MINUTE = TimeUnit.MINUTES.toMillis(1);

	/**
	 * 小时(单位毫秒)；
	 */
	public final static long ONE_HOUR = TimeUnit.HOURS.toMillis(1);

	/**
	 * 天(单位毫秒)
	 */
	public final static long ONE_DAY = TimeUnit.DAYS.toMillis(1);

	private static ThreadLocal<DateFormat> sdf = new ThreadLocal<DateFormat>() {

		@Override
		protected synchronized DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private static ThreadLocal<DateFormat> sdfcn = new ThreadLocal<DateFormat>() {

		@Override
		protected synchronized DateFormat initialValue() {
			return new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		}
	};

	private static ThreadLocal<DateFormat> daySDF = new ThreadLocal<DateFormat>() {

		@Override
		protected synchronized DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	private static ThreadLocal<DateFormat> timeSDF = new ThreadLocal<DateFormat>() {

		@Override
		protected synchronized DateFormat initialValue() {
			return new SimpleDateFormat("HH:mm:ss");
		}
	};

	private static ThreadLocal<DateFormat> marriageTimeSDF = new ThreadLocal<DateFormat>() {

		@Override
		protected synchronized DateFormat initialValue() {
			return new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		}
	};

	private static ThreadLocal<DateFormat> hAndMSDF = new ThreadLocal<DateFormat>() {

		@Override
		protected synchronized DateFormat initialValue() {
			return new SimpleDateFormat("mm分ss秒");
		}
	};

	private static ThreadLocal<DateFormat> hourAndMinute = new ThreadLocal<DateFormat>() {

		@Override
		protected synchronized DateFormat initialValue() {
			return new SimpleDateFormat("HH:mm");
		}
	};

	public static String getStringDate(Date date) {
		if (date == null) {
			return "";
		}
		return sdf.get().format(date);
	}

	/**
	 * 获取短日期字符串
	 *
	 * @param date
	 * @return
	 */
	public static String getShortDate2String(Date date) {
		return daySDF.get().format(date);
	}

	public static String getStringDate(long time) {
		Date date = new Date(time);
		return sdf.get().format(date);
	}

	public static String getStringDateCN(long time) {
		Date date = new Date(time);
		return sdfcn.get().format(date);
	}

	public static String getNowStringDate() {
		return sdf.get().format(new Date());
	}

	public static Date getDateByString(String date) {
		try {
			return sdf.get().parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date getDateByStringOnlyDay(String date) {
		try {
			return daySDF.get().parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date getDateByStringOnlyTimes(String times) {
		try {
			return timeSDF.get().parse(times);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getDateForLongToString(long times) {
		try {
			return timeSDF.get().format(times);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getMarriageDateForLongToString(long times) {
		try {
			return marriageTimeSDF.get().format(times);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getHourAndMinuteForDateToString(Date date) {
		try {
			return hourAndMinute.get().format(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDateByLong(Date date) {
		try {
			return hAndMSDF.get().format(date);
		} catch (Exception e) {
		}
		return null;
	}


	/**
	 * 判断时间 是不是同一天 true：是今天 false：不是今天
	 */
	public static boolean isToday(long time) {
		return DateUtils.isSameDay(new Date(), new Date(time));
	}

	/**
	 * 判断时间 是不是同一天 true：是今天 false：不是今天
	 */
	public static boolean isSameDay(long time1, long time2) {
		return DateUtils.isSameDay(new Date(time1), new Date(time2));
	}

	public static boolean isTomorrow5(Date date) {
		if (date == null) {
			return false;
		}
		long time = date.getTime();
		return isTomorrow5(time);
	}

	/**
	 * 判断时间是否到了第二天的凌晨5点，如果是的话，返回true，进行更新数据操作；如果不是，返回false。
	 */
	public static boolean isTomorrow5(long time) {
		Calendar c1 = getCalendar();
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(time);
		if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {
			if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR)) {
				return false;
			} else if (c1.get(Calendar.YEAR) > c2.get(Calendar.YEAR)) {
				return true;
			}
		}
		int day1 = c1.get(Calendar.DAY_OF_YEAR);
		int day2 = c2.get(Calendar.DAY_OF_YEAR);
		if (day1 - day2 >= 2) {
			//时隔两天以上
			return true;
		} else if (day1 - day2 == 1) {
			//时隔一天
			if (before(c2, 5)) {
				return true;
			} else {
				if (after(c1, 5)) {
					return true;
				}
			}
		} else if (day1 == day2) {
			//同一天
			if (before(c2, 5) && after(c1, 5)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 在c1这一天的hour之后
	 */
	private static boolean after(Calendar c1, int hour) {
		Calendar c3 = getCalendar();
		c3.set(Calendar.DAY_OF_YEAR, c1.get(Calendar.DAY_OF_YEAR));
		c3.set(Calendar.HOUR_OF_DAY, hour);
		c3.set(Calendar.MINUTE, 0);
		c3.set(Calendar.SECOND, 0);
		if (c1.after(c3)) {
			return true;
		}
		return false;
	}

	/**
	 * 在c2这一天的hour之前
	 */
	private static boolean before(Calendar c2, int hour) {
		Calendar c3 = getCalendar();
		c3.set(Calendar.DAY_OF_YEAR, c2.get(Calendar.DAY_OF_YEAR));
		c3.set(Calendar.HOUR_OF_DAY, hour);
		c3.set(Calendar.MINUTE, 0);
		c3.set(Calendar.SECOND, 0);
		if (c2.before(c3)) {
			return true;
		}
		return false;
	}

	/**
	 * 是不是同一天
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isTheSameDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) && (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 判断时间是不是在一个时间范围内
	 *
	 * @param d1
	 *            需要判断的时间
	 * @param d2
	 *            范围的开始时间
	 * @param d3
	 *            范围的结束时间
	 * @return
	 */
	public static boolean isBetween(Date d1, Date d2, Date d3) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Calendar c3 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		c3.setTime(d3);
		return c1.before(c3) && c1.after(c2);
	}

	/**
	 * 判断时间 是不是同一个月 true：是同一个月 false：不是同一个月
	 */
	public static boolean isThatSameMonth(long time) {
		Calendar c1 = getCalendar();
		Calendar c2 = Calendar.getInstance();
		// 修改时间为数据库记录时间
		c2.setTimeInMillis(time);
		if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {
			return false;
		}
		int month1 = c1.get(Calendar.MONTH);
		int month2 = c2.get(Calendar.MONTH);

		return month1 == month2;
	}

	/**
	 * 判断时间 是不是本年的同一周：周一是每周的第一天
	 */
	public static boolean isThatSameWeek(int dayOfYear) {
		Calendar c1 = getCalendar();
		Calendar c2 = Calendar.getInstance();
		// 修改时间为数据库记录时间
		c2.set(Calendar.DAY_OF_YEAR, dayOfYear);

		c2.setFirstDayOfWeek(Calendar.MONDAY);
		c1.setFirstDayOfWeek(Calendar.MONDAY);

		if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {
			return false;
		}
		int week1 = c1.get(Calendar.WEEK_OF_YEAR);
		int week2 = c2.get(Calendar.WEEK_OF_YEAR);

		return week1 == week2;
	}

	/**
	 * 判断时间 是不是本年的同一周：周一是每周的第一天
	 */
	public static boolean isThatSameWeek(long time) {
		Calendar c1 = getCalendar();
		Calendar c2 = Calendar.getInstance();
		// 修改时间为数据库记录时间
		c2.setTimeInMillis(time);

		c2.setFirstDayOfWeek(Calendar.MONDAY);
		c1.setFirstDayOfWeek(Calendar.MONDAY);

		if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {
			return false;
		}
		int week1 = c1.get(Calendar.WEEK_OF_YEAR);
		int week2 = c2.get(Calendar.WEEK_OF_YEAR);

		return week1 == week2;
	}

	/**
	 * 判断时间 相差几天
	 */
	public static int differOfDayByABS(long time) {
		return Math.abs(differOfDay(time));
	}

	/**
	 * 判断时间 相差几天,如果目标时间是未来的话，返回负数
	 */
	public static int differOfDay(long time) {
		int dayNum = 0;
		Calendar now = getCalendar();
		Calendar targettime = Calendar.getInstance();
		targettime.setTimeInMillis(time);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		targettime.set(Calendar.HOUR_OF_DAY, 0);
		targettime.set(Calendar.MINUTE, 0);
		targettime.set(Calendar.SECOND, 0);
		dayNum = (int) ((now.getTimeInMillis() / 1000 - targettime.getTimeInMillis() / 1000) * 1000 / ONE_DAY);
		return dayNum;
	}

	/**
	 * 判断给定年是否为润年
	 */
	public static boolean isLeapYear(int year) {
		if (year % 4 == 0 && year % 100 != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 返回给定年数的最大天数
	 */
	public static int getMaxDayOfYear(int year) {
		if (isLeapYear(year)) {
			return 366;
		}

		return 365;
	}

	/**
	 * 判断时间 距离当月最后一个24点相差多少 long
	 */
	public static long nowCut24(long time) {
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(time);
		c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));
		c2.set(Calendar.HOUR_OF_DAY, c2.getActualMaximum(Calendar.HOUR_OF_DAY));
		c2.set(Calendar.MINUTE, c2.getActualMaximum(Calendar.MINUTE));
		c2.set(Calendar.SECOND, c2.getActualMaximum(Calendar.SECOND));
		long longTime24 = c2.getTimeInMillis();
		longTime24 = longTime24 - time;

		return longTime24;
	}

	/**
	 * 判断时间距离当月第一个凌晨0点相差多少long
	 */
	public static long nowCut0(long time) {
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(time);
		c2.set(Calendar.DAY_OF_MONTH, c2.getActualMinimum(Calendar.DAY_OF_MONTH));
		c2.set(Calendar.HOUR_OF_DAY, c2.getActualMinimum(Calendar.HOUR_OF_DAY));
		c2.set(Calendar.MINUTE, c2.getActualMinimum(Calendar.MINUTE));
		c2.set(Calendar.SECOND, c2.getActualMinimum(Calendar.SECOND));
		long longTime0 = c2.getTimeInMillis();
		longTime0 = time - longTime0;

		return longTime0;
	}

	/**
	 * 判断时间是这个月第几天
	 */
	public static int someDayOnThisMonth(long time) {
		Calendar c1 = getCalendar();
		c1.setTimeInMillis(time);
		return c1.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取当前时间的共计多少小时
	 *
	 * @param time
	 *            (单位毫秒)
	 * @return
	 */
	public static int hasSomeHours(long time) {
		int i = (int) (time / 60 / 60 / 1000);
		return i;
	}

	/**
	 * 取当前时间不到1小时的毫秒数
	 */
	public static long hasSomeMillisecond(long time) {
		long i = time % (60 * 60 * 1000);
		return i;
	}

	/**
	 * 将天转换成秒
	 */
	public static long getMillsByDay(int day) {
		long i = day * 24 * 60 * 60 * 1000;
		return i;
	}

	/**
	 * 把格式为HH:mm:ss的字符串转换成HHmmss以int形式返回。
	 *
	 * @param time
	 *            时间。
	 * @return 转换成HHmmss以int形式返回。
	 */
	public static int timeToInt(String time) {
		if (StringUtils.isNotBlank(time)) {
			try {
				time = time.replaceAll(":", "");
				int result = Integer.parseInt(time, 10);
				return result;
			} catch (NumberFormatException ex) {
				// ignore
				ex.printStackTrace();
			}
		}
		return 0;
	}

	public static String timeShow(long millisecond) {
		long v = millisecond / 1000;
		long s = v % 60;
		long m = v / 60 % 60;
		long h = v / 3600 % 86400;
		return h + "小时" + m + "分" + s + "秒";
	}

	public static String timeShow2(long millisecond) {
		long v = millisecond / 1000;
		long s = v % 60;
		long m = v / 60 % 60;
		long h = v / 3600 % 24;
		long d = v / (3600 * 24);
		return d + "天" + h + "小时" + m + "分" + s + "秒";
	}

	/**
	 * 查询当前是几点
	 *
	 * @return
	 */
	public static int getCurrentHour() {
		return getCalendar().get(Calendar.HOUR_OF_DAY);
	}

	public static Calendar getCalendar(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar;
	}

	/**
	 * 查询当前年中的第几天
	 *
	 * @return
	 */
	public static int getCurrentDayOfYear() {
		return getCalendar().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 查询当前年中的第几天
	 *
	 * @param time
	 * @return
	 */
	public static int getDayofYear(long time) {
		return getCalendar().get(Calendar.DAY_OF_YEAR);
	}

	public static int getCurrentWeekOfYear() {
		Calendar calendar = getCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取当前天是当月的第几天
	 * @return
	 */
	public static int getCurrentDayOfMonth() {
		return getCalendar().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取上周 是当前年中的第几周
	 *
	 * @return
	 */
	public static int getPreWeekOfYear() {
		Calendar calendar = getCalendar();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 是否为当前周
	 *
	 * @param currentWeekOfYear
	 * @param weekOfYear
	 * @return
	 */
	public static boolean isCurWeekOfYear(int currentWeekOfYear, int weekOfYear) {
		return currentWeekOfYear == weekOfYear;
	}

	/**
	 * 获取当日零点的毫秒表示
	 */
	public static long getZeroTodayInMillis() {
		Calendar calendar = getCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取次日零点的毫秒表示
	 */
	public static long getZeroNextDayInMillis() {
		Calendar calendar = getCalendar();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取指定日期0点整的毫秒表示
	 */
	public static long getZeroInMillis(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取给定时间所在周的周一的0点时间
	 * <p>！！！这个返回的是周日的0点时间，不知道有没有其他错误，是个坑！！！by naijiang.wang
	 * @param args
	 * @throws Exception
	 */
	public static long getThisWeekOfMondayZeroClockTime(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		int day = c.get(Calendar.DAY_OF_WEEK);
		long time = c.getTimeInMillis();
		switch (day) {
		case Calendar.MONDAY:
			break;
		case Calendar.TUESDAY:
			time = time - DateUtil.ONE_DAY * 1;
			break;
		case Calendar.WEDNESDAY:
			time = time - DateUtil.ONE_DAY * 2;
			break;
		case Calendar.THURSDAY:
			time = time - DateUtil.ONE_DAY * 3;
			break;
		case Calendar.FRIDAY:
			time = time - DateUtil.ONE_DAY * 4;
			break;
		case Calendar.SATURDAY:
			time = time - DateUtil.ONE_DAY * 5;
			break;
		case Calendar.SUNDAY:
			time = time - DateUtil.ONE_DAY * 6;
			break;
		}

		return time;
	}

	/**
	 * 获取开服N天活动的开始时间
	 */
	public static Date getActStartTimeByNDay(String s, int nowDay) {
		String[] args = s.split("-");
		if (args[0].equals("")) {
			return null;
		}
		int startDay = Integer.valueOf(args[0]);
		int endDay = Integer.valueOf(args[1]);
		long zeroToDayInMillis = getZeroTodayInMillis();
		if (nowDay == 0 && startDay == 1) {
			return new Date(zeroToDayInMillis);
		} else if (nowDay == startDay) {
			return new Date(zeroToDayInMillis);
		} else if (nowDay > startDay && nowDay <= endDay) {
			int day = nowDay - startDay;
			zeroToDayInMillis -= TimeUnit.DAYS.toMillis(day);
			return new Date(zeroToDayInMillis);
		} else {
			return null;
		}
	}

	/**
	 * 获取开服N天活动的结束时间
	 */
	public static Date getActEndTimeByNDay(String s, int nowDay) {
		String[] args = s.split("-");
		int startDay = Integer.valueOf(args[0]);
		int endDay = Integer.valueOf(args[1]);
		long zeroToDayInMillis = getZeroTodayInMillis();
		if (nowDay == 0 && startDay == 1) {
			zeroToDayInMillis = zeroToDayInMillis + TimeUnit.DAYS.toMillis(endDay);
			return new Date(zeroToDayInMillis);
		} else if (nowDay == startDay) {
			zeroToDayInMillis = zeroToDayInMillis + TimeUnit.DAYS.toMillis(endDay - startDay + 1);
			return new Date(zeroToDayInMillis);
		} else if (nowDay > startDay && nowDay <= endDay) {
			zeroToDayInMillis = zeroToDayInMillis + TimeUnit.DAYS.toMillis(endDay - nowDay + 1);
			return new Date(zeroToDayInMillis);
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		System.err.println(getStringDateCN(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2) - TimeUnit.HOURS.toMillis(10)));
		System.err.println(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2) - TimeUnit.HOURS.toMillis(10));
	}

	/**
	 * 根据week,hour,minute,获取下一次的倒计时，单位毫秒,精确到秒
	 * @param c
	 * @param week 美国【星期天，星期一】=[1,7]
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long getCountDownByWeek(Calendar c, int openWeekDay, int openHour, int openMinute){
		int week = c.get(Calendar.DAY_OF_WEEK);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		int _week = (week + 5)%7 + 1; //变为中国【星期一，星期天】=[1,7]
		int _openWeekDay = (openWeekDay + 5)%7 + 1; //变为中国【星期一，星期天】=[1,7]
		long countDown = (_openWeekDay - _week) * DateUtil.ONE_DAY
				+ (openHour - hour) * DateUtil.ONE_HOUR
				+ (openMinute - minute)*DateUtil.ONE_MINUTE
				- second * DateUtil.ONE_SECONDE;
		if(countDown < 0){
			countDown += 7 * DateUtil.ONE_DAY;
		}
		return countDown;
	}
	
}
