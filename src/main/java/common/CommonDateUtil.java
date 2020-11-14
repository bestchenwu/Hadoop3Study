/*
 * Copyright 1999-2004 yihaodian.com All right reserved. This software is the confidential and proprietary information
 * of yihaodian.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you entered into with yihaodian.com.
 */
package common;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 通用的日期工具类
 *
 * @author chenwu@yihaodian.com 2017年6月18日 下午6:58:58
 */
public class CommonDateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_WITH_PRECISION_ONLY_TO_SECOND = "HH:mm:ss";
    public static final String DATE_FORMAT_WITH_PRECISION_TO_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_WITH_PRECISION_TO_HOUR = "yyyy-MM-dd-HH";
    public static final String DATE_FORMAT_WITH_PRECISION_TO_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_INIT_DATE = "1970-01-01 00:00:00";

    /**
     * 将时间字符串转换为日期和秒一起组成的时间
     *
     * @param str 时间字符串
     * @return {@link Date}
     * @author chenwu on 2017.8.4
     */
    public static Date getDateByStringWithPrecisionToDayAndSeconds(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        str = str.trim();
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date result = null;
        try {
            result = dateFormat.parse(str);
        } catch (ParseException e) {
        }
        return result;
    }

    public static Date getDateByStringWithPrecisionOnlyToSecond(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        str = str.trim();
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_WITH_PRECISION_ONLY_TO_SECOND);
        try {
            Date result = dateFormat.parse(str);
            if (result != null) {
                Calendar currentDate = Calendar.getInstance();
                Calendar tempDate = Calendar.getInstance();
                tempDate.setTime(result);
                currentDate.set(11, tempDate.get(11));
                currentDate.set(12, tempDate.get(12));
                currentDate.set(13, tempDate.get(13));
                return currentDate.getTime();
            }
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 将{@link Date}转换为精确到秒数的时间字符串
     *
     * @param date 时间
     * @return {@link String}
     * @author chenwu on 2017.8.4
     */
    public static final String parseDateToStringWithPrecisionToMesc(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String formatDateStr = dateFormat.format(date);
        return formatDateStr;
    }

    public static final String parseDateToStringWithPrecisionToDay(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_WITH_PRECISION_TO_DAY);
        String formatDateStr = dateFormat.format(date);
        return formatDateStr;
    }

    /**
     * 将日期类型{@link Date}序列化为天和小时组成的日期字符串
     *
     * @param date                  日期对象
     * @param userDefinedDateFormat 用户自定义日期类型,如果为空,则使用{@link #DATE_FORMAT_WITH_PRECISION_TO_HOUR}
     * @return {@link String}
     * @author chenwu on 2017.7.7
     */
    public static final String parseDateToStringWithPrecisionToDayAndHour(Date date, String userDefinedDateFormat) {
        if (date == null) {
            return null;
        }
        DateFormat dateFormat = null;
        if (StringUtils.isNotBlank(userDefinedDateFormat)) {
            dateFormat = new SimpleDateFormat(userDefinedDateFormat);
        } else {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_WITH_PRECISION_TO_HOUR);
        }
        String formatDateStr = dateFormat.format(date);
        return formatDateStr;
    }

    /**
     * 检查日期是否在startTime与endTime之间的时间间隔内
     *
     * @param currentTime 当前时刻
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param timeFormat  时间格式
     * @return boolean true表示是,false表示否
     * @author chenwu on 2017.8.4
     */
    public static boolean checkDateIsInPeriod(String currentTime, String startTime, String endTime, String timeFormat) {
        DateFormat dateFormat = new SimpleDateFormat(timeFormat);
        try {
            Date currentDate = dateFormat.parse(currentTime);
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            if (currentDate.before(startDate)) {
                return false;
            }
            if (currentDate.after(endDate) || currentDate.equals(endDate)) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            // ignore the string parse exception
            return false;
        }
    }

    public static final Date getDateByStringWithPrecisionToDay(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_WITH_PRECISION_TO_DAY);
        str = str.trim();
        Date result = null;
        try {
            result = dateFormat.parse(str);
        } catch (ParseException e) {
        }
        return result;
    }

    public static Date getCurrentDate() {
        Calendar currentInstance = Calendar.getInstance();
        return currentInstance.getTime();
    }

    /**
     * 根据今天日期获取昨天日期
     *
     * @param today 今天日期
     * @return {@link Date}
     * @author chenwu on 2017.12.12
     */
    public static Date getYesterdayDate(Date today) {
        Calendar instance = Calendar.getInstance();
        if (today != null) {
            instance.setTime(today);
        }
        instance.add(6, -1);
        return instance.getTime();
    }

    public static int getMonths(Date date1, Date date2) {
        if ((date1 == null) || (date2 == null)) {
            return -1;
        }
        int monthRange = 0;

        int isNeedToSupply = 0;
        try {
            Calendar objCalendarDate1 = Calendar.getInstance();
            objCalendarDate1.setTime(date1);
            Calendar objCalendarDate2 = Calendar.getInstance();
            objCalendarDate2.setTime(date2);
            if (objCalendarDate2.equals(objCalendarDate1))
                return 0;
            if (objCalendarDate1.after(objCalendarDate2)) {
                Calendar temp = objCalendarDate1;
                objCalendarDate1 = objCalendarDate2;
                objCalendarDate2 = temp;
            }
            if (objCalendarDate2.get(5) < objCalendarDate1.get(5)) {
                isNeedToSupply = 1;
            }
            if (objCalendarDate2.get(1) > objCalendarDate1.get(1)) {
                monthRange = (objCalendarDate2.get(1) - objCalendarDate1.get(1)) * 12 + objCalendarDate2.get(2)
                        - isNeedToSupply - objCalendarDate1.get(2);
            } else {
                monthRange = objCalendarDate2.get(2) - objCalendarDate1.get(2) - isNeedToSupply;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return monthRange;
    }

    public static long calculateTimeDifference(Date toCompareDate, Date currentDate) {
        if ((toCompareDate == null) || (currentDate == null)) {
            return 0L;
        }
        return Math.abs(currentDate.getTime() - toCompareDate.getTime());
    }

    public static String getDateBefore(Date date, String dateFormat, int n) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(5, -n);
        date = cd.getTime();
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }

    public static String getDateBeforeByField(Date date, int field, int n, String outputDateFormat) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        if(n>0){
            cd.add(field, -n);
        }
        date = cd.getTime();
        SimpleDateFormat df = new SimpleDateFormat(outputDateFormat);
        return df.format(date);
    }

    /**
     * 根据当前时间、格式化日期的字符串等计算间隔period后的新时间,并按照dateFormat格式化后返回给前台
     *
     * @param currentTime 当前时间
     * @param dateFormat  格式化日期的字符串
     * @param period      时间间隔(当间隔为正值则表示当前时间以后的时间,当间隔为负值则表示当前时间以前的时间)
     * @param timeType    时间类型
     * @return {@link String}
     */
    public static String getDateToStringByPeriod(String currentTime, String dateFormat, int period, int timeType) {
        if (StringUtils.isBlank(currentTime) || StringUtils.isBlank(dateFormat)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        try {
            Date currentDate = df.parse(currentTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(timeType, period);
            Date newDate = calendar.getTime();
            String newDateString = df.format(newDate);
            return newDateString;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 根据当前时间计算一段时间差后的新时间
     *
     * @param currentTime 当前时间
     * @param period
     * @param timeType    时间类型
     * @return {@link Date}
     * @author chenwu on 2018.6.22
     */
    public static Date getDateByPeriod(Date currentTime, int period, int timeType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(timeType, period);
        Date newDate = calendar.getTime();
        return newDate;
    }

    public static boolean checkDateIsCurrentWeekIndex(Date date, List<Integer> specfiedWeekIndexList) {
        if ((date == null) || (CollectionUtils.isEmpty(specfiedWeekIndexList))) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int index = calendar.get(7);
        if (index == 1)
            index = 7;
        else {
            index -= 1;
        }
        return specfiedWeekIndexList.contains(Integer.valueOf(index));
    }

    /**
     * 将毫秒数转换为日期{@link Date}
     *
     * @param millisecond 毫秒数
     * @return {@link Date}
     * @author chenwu on 2017.7.7
     */
    public static Date getDateByMesc(Long millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        return calendar.getTime();
    }

    /**
     * 获取对应时间间隔(毫秒数)的分钟数
     *
     * @param timePeriod
     * @return {@link Long}
     * @author chenwu on 2017.11.3
     */
    public static long getMinutesByPeriod(long timePeriod) {
        return TimeUnit.MILLISECONDS.toMinutes(timePeriod);
    }

    /**
     * 检测日期字符串是否是工作日
     *
     * @param dateStr
     * @return boolean
     * @author chenwu on 2018.11.20
     */
    public static boolean checkDayIsWorkerDay(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return false;
        }
        Date date = getDateByStringWithPrecisionToDay(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == calendar.SUNDAY) {
            return false;
        }
        return true;
    }

    /**
     * 返回一个非空的日期字符串<br/>
     * 当date为null的时候返回DEFAULT_INIT_DATE
     *
     * @param date
     * @return {@link String}
     * @author chenwu on 2019.6.25
     */
    public static String getNonEmptyDate(String date) {
        if (StringUtils.isBlank(date)) {
            return DEFAULT_INIT_DATE;
        }
        return date;
    }

    /**
     * 使用java8的API获取当前日期的字符串形式,精确到秒级
     *
     * @return {@link String}
     * @author chenwu on 2019.12.24
     */
    public static String getNowTimeBySeconds(){
        LocalTime localTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return formatter.format(localTime);
    }
}
