package com.twm.mgmt.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateUtilsEx extends DateUtils {

	public static final String HTML_DATE_PATTERN = "yyyy-MM-dd";

	public static final String HTML_TIME_PATTERN = "HH:mm";

	public static final String DATETIME_PATTERN = "yyyy/MM/dd HH:mm:ss";

	public static final String DATE_PATTERN = "yyyy/MM/dd";

	public static final String DATE_TIME_PATTERN = "yyyy/MM/dd HH:mm:ss";

	public static final String DATE_TIME_PATTERN_LONG = "yyyy/MM/dd HH:mm:ss SSS";

	public static final String DATE_TIME_PATTERN_WITHOUT_SEC = "yyyy/MM/dd HH:mm";

	public static final String TIME_PATTERN = "HH:mm:ss";

	public static final String TIME_PATTERN_WITHOUT_SEC = "HH:mm";

	public static final String DATE_TIME_PATTERN_24 = "yyyyMMddHHmmss";
	
	public static final String DATE_TIME_PATTERN_24_LONG = "yyyyMMddHHmmssSSS";

	public static final String DATE_PATTERN_24 = "yyyyMMdd";
	
	public static final String DATE_PATTERN_YEAR_MONTH = "yyyyMM";

	private static final ZoneId ZONE_ID = ZoneId.systemDefault();

	/**
	 * date with start(default time 00:00:00.000)
	 * 
	 * @param date
	 * @return
	 */
	public static Date startDate(String date) {
		if (StringUtils.isBlank(date)) {

			return null;
		}

		return startDate(parseDate(date));
	}

	/**
	 * date with start(default time 00:00:00.000)
	 * 
	 * @param date
	 * @return
	 */
	public static Date startDate(Date date) {
		if (date == null) {

			return null;
		}

		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		zonedDateTime = ZonedDateTime.of(zonedDateTime.toLocalDate(), LocalTime.MIN, zonedDateTime.getZone());

		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * date with end(default time 23:59:59.999)
	 * 
	 * @param date
	 * @return
	 */
	public static Date endDate(String date) {
		if (StringUtils.isBlank(date)) {

			return null;
		}

		return endDate(parseDate(date));
	}

	/**
	 * date with end(default time 23:59:59.999)
	 * 
	 * @param date
	 * @return
	 */
	public static Date endDate(Date date) {
		if (date == null) {

			return null;
		}

		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		zonedDateTime = ZonedDateTime.of(zonedDateTime.toLocalDate(), LocalTime.MAX, zonedDateTime.getZone());

		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * date plus
	 * 
	 * @param date
	 * @param amount
	 * @param unit
	 * @return
	 */
	public static Date plus(Date date, long amount, ChronoUnit unit) {
		if (date == null) {

			return null;
		}

		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		zonedDateTime = zonedDateTime.plus(amount, unit);

		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * set new value with specified field
	 * 
	 * @param date
	 * @param amount
	 * @param field
	 * @return
	 */
	public static Date with(Date date, long value, ChronoField field) {
		if (date == null) {

			return null;
		}

		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		// check if invalid
		int lastDay = zonedDateTime.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();

		if (value > lastDay) {
			value = lastDay;
		}

		zonedDateTime = zonedDateTime.with(field, value);

		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * an adjusted copy of this date-time
	 * 
	 * @param date
	 * @param adjuster
	 * @return
	 */
	public static ZonedDateTime adjuster(Date date, TemporalAdjuster adjuster) {
		if (date == null) {

			return null;
		}

		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		return zonedDateTime.with(adjuster);
	}

	/**
	 * date1 is after than date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Boolean isAfter(Date date1, Date date2) {
		if (date1 == null || date2 == null) {

			return false;
		}

		return date1.after(date2);
	}

	/**
	 * 相差
	 * 
	 * @param startDate
	 * @param endDate
	 * @param unit
	 * @return
	 */
	public static long between(Date startDate, Date endDate, TemporalUnit unit) {
		if (startDate == null || endDate == null) {

			return -1;
		}

		ZoneId zoneId = ZoneId.systemDefault();

		ZonedDateTime startDateTime = ZonedDateTime.ofInstant(startDate.toInstant(), zoneId);

		ZonedDateTime endDateTime = ZonedDateTime.ofInstant(endDate.toInstant(), zoneId);

		return unit.between(startDateTime, endDateTime);
	}

	/**
	 * date to Date
	 * 
	 * @param date yyyy-MM-dd
	 * @return
	 */
	public static Date parseDate(String date) {

		return parseDate(date, HTML_DATE_PATTERN);
	}

	/**
	 * date to LocalDate
	 * 
	 * @param date
	 * @return
	 */
	public static LocalDate parseLocalDate(String date) {

		return parseLocalDate(date, HTML_DATE_PATTERN);
	}

	/**
	 * date to LocalDate
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static LocalDate parseLocalDate(String date, String pattern) {
		if (StringUtils.isBlank(date) || StringUtils.isBlank(pattern)) {

			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		LocalDate localDate = LocalDate.parse(date, formatter);

		return localDate;
	}

	/**
	 * to Date
	 * 
	 * @param date yyyy-MM-dd
	 * @param time HH:mm
	 * @return
	 */
	public static Date parseDateTime(String date, String time) {
		if (StringUtils.isBlank(date) || StringUtils.isBlank(time)) {

			return null;
		}

		return parseDate(String.format("%s %s", date, time),
				String.format("%s %s", HTML_DATE_PATTERN, HTML_TIME_PATTERN));
	}

	/**
	 * date to Date
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String date, String pattern) {
		if (StringUtils.isBlank(date) || StringUtils.isBlank(pattern)) {

			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		LocalDateTime localDateTime;

		if (pattern.indexOf("H") > 0) {
			localDateTime = LocalDateTime.parse(date, formatter);
		} else {
			localDateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN);
		}

		ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());

		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * date to pattern formatter
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {

		return formatDate(date, DATETIME_PATTERN);
	}

	/**
	 * date to pattern formatter
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {

			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		return zonedDateTime.format(formatter);
	}

	/**
	 * 取得現在時間
	 */
	public static LocalTime getNowLocalTime() {
		return LocalTime.now(ZONE_ID);
	}

	/**
	 * 取得現在日期
	 */
	public static LocalDate getNowLocalDate() {
		return LocalDate.now(ZONE_ID);
	}

	/**
	 * 取得現在日期時間
	 */
	public static LocalDateTime getNowLocalDateTime() {
		return LocalDateTime.now(ZONE_ID);
	}

	/**
	 * 取得現在日期格式時間
	 */
	public static String getNowTimeStr(String pattern) {
		LocalDateTime now = LocalDateTime.now(ZONE_ID);
		DateTimeFormatter f = DateTimeFormatter.ofPattern(pattern, Locale.TAIWAN);
		return f.format(now);
	}

	/**
	 * date轉LocalTime
	 */
	public static LocalTime date2LocalTime(Date date) {
		LocalDateTime localDateTime = date2LocalDateTime(date);
		return localDateTime.toLocalTime();
	}

	/**
	 * date轉LocalDate
	 */
	public static LocalDate date2LocalDate(Date date) {
		LocalDateTime localDateTime = date2LocalDateTime(date);
		return localDateTime.toLocalDate();
	}

	/**
	 * date轉LocalDateTime
	 */
	public static LocalDateTime date2LocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZONE_ID);
	}

	/**
	 * localDate轉Date
	 */
	public static Date localDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZONE_ID).toInstant());
	}

	/**
	 * localDateTime轉Date
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		Instant instant = localDateTime.atZone(ZONE_ID).toInstant();
		return Date.from(instant);
	}

	/**
	 * LocalDate轉LocalDateTime
	 */
	public static LocalDateTime localDate2LocalDateTime(LocalDate localDate) {
		return LocalDateTime.of(localDate, LocalTime.MIN);
	}

	/**
	 * 字串日期轉為LocalDateTime
	 */
	public static LocalDateTime parseLocalDateTime(String date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(date, formatter);
	}

	/**
	 * 日期轉字串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String parseDateToString(Date date, String pattern) {
		SimpleDateFormat desiredFormat = new SimpleDateFormat(pattern);
		return desiredFormat.format(date);
	}

	/**
	 * 取得一日最早時間 00:00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartOfDay(Date date) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
				ZoneId.systemDefault());
		LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 取得一日最晚時間 23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndOfDay(Date date) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
				ZoneId.systemDefault());
		LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static boolean isThisDateValid(String dateToValidate, String dateFromat) {

		if (dateToValidate == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat,Locale.US);
		sdf.setLenient(false);

		try {

			sdf.parse(dateToValidate);

		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	
	/**
	 * 比較年月是否在當前之前:
	 * 
	 * @param ym1
	 * @param now
	 * @return 
	 * @return
	 */
	public static boolean compareYearsAndMonthsIsBefore(YearMonth ym1,YearMonth now) {
			return ym1.isBefore(now);
	}
	
	
	/**
	 * 比較年月是否在當前之後或是等於當月:
	 * 
	 * @param ym1
	 * @param now
	 * @return boolean
	 */
	public static boolean compareYearsAndMonthsIsAfter(YearMonth ym1,YearMonth now) {
			return ym1.isAfter(now) || ym1.equals(now);
	}
	
	
	
	/**
	 * 獲取當前的年月
	 * 
	 * @return YearMonth
	 */
	public static YearMonth  getYearMonthNow() {
			return YearMonth.now();
	}
	
	
	
	
	
	/**
	 * 根據年月的值獲取
	 * 
	 * @param year
	 * @param month
	 * @return YearMonth
	 */
	public static YearMonth  getYearMonthGet(int year,int month) {
		return YearMonth.of(year, month);
	}
	
	
	
	
	/**
	 * 是否可編輯活動起日
	 * 
	 * @param campaignSdateDate
	 * @return boolean
	 */
	public static boolean isEditStartDate(Date date) {
		
		ZoneId timeZone = ZoneId.systemDefault();
		LocalDate getLocalDate = date.toInstant().atZone(timeZone).toLocalDate();
				
		return compareYearsAndMonthsIsAfter(getYearMonthGet(getLocalDate.getYear(),getLocalDate.getMonthValue()),getYearMonthNow());
	}
	
	
	/**
	 * 取出現在日期 "yyyyMMddHHmmss"
	 * @return
	 */
	public static String getNowDateNum() throws Exception{
		java.text.SimpleDateFormat sdf;
		sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		return (sdf.format(new java.util.Date()));
	}
	
	public static SimpleDateFormat dateFormat() {
		return new SimpleDateFormat("yyyyMMdd");
	}
	
	/**
     * 是否是 yyyy/MM/dd 日期格式 校验
     * @param date 日期
     * @return boolean
     */
    public static boolean isDateFormat(String date) {
        if (StringUtils.isBlank(date)) {
            return false;
        }
        String regexp = "[0-9]{4}/(1[0-2]|[1-9])/([1-9]|[12]\\d|3[01])";
        return Pattern.compile(regexp).matcher(date).matches();
    }

}
