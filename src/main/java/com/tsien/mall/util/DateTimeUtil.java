package com.tsien.mall.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/25 0025 16:28
 */

public class DateTimeUtil {

    /**
     * 标准化日期格式
     */
    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将字符串转换为日期
     *
     * @param dateTimeStr 时间字符串
     * @return LocalDateTime
     */
    public static LocalDateTime stringToDateTime(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_FORMAT);
        return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
    }

    /**
     * 将字符串转换为日期
     *
     * @param dateTimeStr 日期字符串
     * @param formatStr   日期格式
     * @return LocalDateTime
     */
    public static LocalDateTime stringToDateTime(String dateTimeStr, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatStr);
        return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
    }

    /**
     * 将日期转换为字符串
     *
     * @param dateTime LocalDateTime
     * @return 日期字符串
     */
    public static String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return StringUtils.EMPTY;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_FORMAT);
        return dateTime.format(dateTimeFormatter);
    }

    /**
     * 将日期转换为字符串
     *
     * @param dateTime  LocalDateTime
     * @param formatStr 格式化字符串
     * @return 时间日期字符串
     */
    public static String dateTimeToString(LocalDateTime dateTime, String formatStr) {
        if (dateTime == null) {
            return StringUtils.EMPTY;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatStr);
        return dateTime.format(dateTimeFormatter);
    }

}
