package com.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 *
 * @author sunguiyong <sunguiong@kuaishou.com>
 * Created on 2020-06-12
 */
public class LocalTimeUtil {
    private static final long TIME_SUFFIX = 1000L;
    private static final long TIME_UTIL = 1000;
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String BASIC_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String[] STAR_ARR = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座",
            "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    private static final int[] DAY_ARR = {20, 19, 20, 20, 21, 21, 22, 23, 23, 23, 22, 21};  // 两个星座分割日
    private static final int HOUR_UNIT = 3600;
    private static final int MIN_UNIT = 60;
    private static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    public static String getStringDateByLong(Long d) {
        return DateFormatUtils.format(d * TIME_UTIL, DATE_PATTERN);
    }

    public static String getCurrentDate() {
        return DateFormatUtils.format(System.currentTimeMillis(), DATE_PATTERN);
    }

    public static String longToStr(Long timestamp) {
        if (timestamp.toString().length() < 11) {
            timestamp = timestamp * TIME_SUFFIX;
        }
        SimpleDateFormat format = new SimpleDateFormat(BASIC_DATE_PATTERN);
        return format.format(timestamp);
    }

    /**
     * @param date 格式为 2020-06-12
     */
    public static int getAge(String date) {
        Date birthDay = getDateByString(date);
        if (birthDay == null) {
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            return -1;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--; //当前日期在生日之前，年龄减一
                }
            } else {
                age--; //当前月份在生日之前，年龄减一
            }
        }
        return age;
    }

    /**
     * @param strDate 格式为 2020-06-12
     */
    public static Date getDateByString(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long string2Long(String strDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(strDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据生日日期获取星座
     *
     * @param date 格式为 2020-06-12
     */
    public static String getConstellation(String date) {
        Date birthDay = getDateByString(date);
        if (birthDay == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            return "";
        }
        cal.setTime(birthDay);
        int month = cal.get(Calendar.MONTH);
        int index = month;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (day < DAY_ARR[month]) {
            index = (index - 1) < 0 ? 11 : (index - 1);
        }
        return STAR_ARR[index % STAR_ARR.length];
    }

    /**
     * long类型时间转换，需要传目标类型
     */
    public static String getTimeStr(Long time, String pattern) {
        if (time.toString().length() < 11) {
            time = time * TIME_SUFFIX;
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(time);
    }

    //毫秒转时分秒
    //输入 100000 输出1min40s
    public static String millis2HourAndMin(long millis) {
        StringBuilder res = new StringBuilder();
        long second = millis / TIME_UTIL;
        if (second >= HOUR_UNIT) {
            res.append(second / HOUR_UNIT).append("h");
            second = second % HOUR_UNIT;
        }
        if (second > MIN_UNIT) {
            res.append(second / MIN_UNIT).append("min");
            second = second % MIN_UNIT;
        }
        if (second > 0) {
            res.append(second).append("s");
        }
        return StringUtils.isEmpty(res.toString()) ? "0" : res.toString();
    }

    //把map中指定字段，从timeStamp转string时间
    public static String time2StrInMap(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) {
            return "";
        }
        String time = map.getOrDefault(key, "0").toString();
        if (!StringUtils.isEmpty(time) && !"0".equals(time)) {
            return longToStr(Long.parseLong(time));
        }
        return "";
    }

    /**
     * 根据传入时间戳，获取一天的开始和结束时间
     * 例如传入的时间戳对应的时间是 2021-03-14 19:23:00
     *
     * @param type 0 原始时间
     * 1 获取开始时间，传出 2021-03-14 00:00:00
     * 2 获取结束时间，传出 2021-03-14 23:59:59
     */
    public static String dayStartAndEndStr(Long timestamp, int type) {
        if (timestamp.toString().length() < 11) {
            timestamp = timestamp * TIME_SUFFIX;
        }
        if (type == 0) {
            return longToStr(timestamp);
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        String date = format.format(timestamp);
        if (type == 1) {
            return date + " 00:00:00";
        } else {
            return date + " 23:59:59";
        }
    }

    /**
     * 根据传入时间戳，获取一天的开始和结束时间
     * 例如传入的时间戳对应的时间是 2021-03-14 19:23:00
     *
     * @param type 0 原始时间
     * 1 获取开始时间，传出 2021-03-14 00:00:00 对应的时间戳
     * 2 获取结束时间，传出 2021-03-14 23:59:59 对应的时间戳
     */
    public static long dayStartAndEndTimeStamp(Long timestamp, int type) {
        String date = dayStartAndEndStr(timestamp, type);
        return string2Long(date, BASIC_DATE_PATTERN);
    }

    /**
     * 数据查询时间太长的时候，可以通过这个方法将查询分片
     * 注意这个方法，只适用按天查询，不关注小时维度数据的场景
     *
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @param timeGap 一次查询查几天的数据
     */
    public static List<Map<String, Long>> getQueryTimeList(Long startTime, Long endTime, long timeGap) {
        List<Map<String, Long>> list = new ArrayList<>();
        Map<String, Long> tmp;
        long startTmp = startTime;
        long endTmp = 0L;
        if (endTime - startTime <= timeGap || timeGap == 0) { //如果查询时间不够设置的间隔，直接查
            tmp = new HashMap<>();
            tmp.put("start", startTime);
            tmp.put("end", endTime);
            list.add(tmp);
            return list;
        }
        boolean first = true;
        while (endTmp < endTime) {
            endTmp = startTmp + timeGap; //每次加 timeGap
            if (endTmp > endTime) {
                endTmp = endTime;
            }
            if (first) { //如果是第一次，开始时间使用用户选择的时间，结束时间设置为23:59:59
                tmp = new HashMap<>();
                tmp.put("start", dayStartAndEndTimeStamp(startTmp, 0));
                tmp.put("end", dayStartAndEndTimeStamp(endTmp, 2));
                list.add(tmp);
                first = false;
            } else { //不是第一次，开始时间和结束时间分别设置为 00:00:00,23:59:59
                tmp = new HashMap<>();
                tmp.put("start", dayStartAndEndTimeStamp(startTmp, 1));
                if (endTmp < startTmp) {
                    tmp.put("end", dayStartAndEndTimeStamp(startTmp, 2));
                } else {
                    tmp.put("end", dayStartAndEndTimeStamp(endTmp, 2));
                }
                list.add(tmp);
            }
            startTmp = endTmp + ONE_DAY; //开始时间在上一次的结束时间加一天，防止数据查重
        }
        if (!getTimeStr(endTmp, "yyyy-MM-dd").equals(getTimeStr(endTime, "yyyy-MM-dd"))
                && endTime - startTmp < timeGap) { //如果最后一次数据被查询的日期小于用户输入，说明还有数据需要被查询
            tmp = new HashMap<>();
            tmp.put("start", dayStartAndEndTimeStamp(startTmp, 1));
            tmp.put("end", dayStartAndEndTimeStamp(endTime, 0));
            list.add(tmp);
        }
        return list;
    }
}
