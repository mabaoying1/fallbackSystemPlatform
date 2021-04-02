package ctd.net.rpc.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 1.8 日期操作类
 * LocalDate, LocalTime, LocalDateTime, Instant, Period, Duration, ChronoUnit
 * LocalDate：默认格式(yyyy-MM-dd)
 * LocalTime：默认格式是hh:mm:ss.zzz
 * LocalDateTime：默认格式是yyyy-MM-dd-HH-mm-ss.zzz
 * Instant：以Unix时间戳的形式存储日期时间。
 * Period：getYears（），getMonths（）和getDays（）来计算
 * Duration：提供了使用基于时间的值（如秒，纳秒）测量时间量的方法。 
 * ChronoUnit：ChronoUnit类可用于在单个时间单位内测量一段时间，例如天数或秒。
 * @ClassName: DateTimeFormatterUtil 
 * @Description: TODO
 * @author: caidao
 * @date: 2020年6月9日 下午2:28:52
 */
public class DateTimeFormatterUtil {

	 /**
     * Date转换为格式化时间
     * @param date date
     * @param pattern 格式
     * @return
     */
	public static String format(Date date, String pattern){
       return format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()), pattern);
	}
	
	
	 /**
     * localDateTime转换为格式化时间
     * @param localDateTime localDateTime
     * @param pattern 格式
     * @return
     */
    public static String format(LocalDateTime localDateTime, String pattern){
    	if(pattern == "" || pattern == "null" || pattern == null) {
    		pattern = "yyyy-MM-dd HH:mm:ss";
    	}
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }
 
    
    /**
     * 格式化字符串转为Date
     * @param time 格式化时间
     * @param pattern 格式
     * @return
     */
    public static Date parseDate(String time, String pattern){
        return Date.from(parseLocalDateTime(time, pattern).atZone(ZoneId.systemDefault()).toInstant());

    }

    /**
     * 格式化字符串转为LocalDateTime
     * @param time 格式化时间
     * @param pattern 格式
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String time, String pattern){
    	if(pattern == "" || pattern == "null" || pattern == null) {
    		pattern = "yyyy-MM-dd HH:mm:ss";
    	}
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(time, formatter);
    }
 
	
    public static LocalDate parseLocalDate(String time, String pattern){
    	if(pattern == "" || pattern == "null" || pattern == null) {
    		pattern = "yyyy-MM-dd";
    	}
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(time, formatter);
    }

    /**
     * 偏移offset后，天数
     * @Title: getNextDay
     * @Description: TODO
     * @param date
     * @param offset 偏移  负数向前 正数向后
     * @return Date
     * @author caidao
     * @date 2020年4月13日下午4:15:51
     */
    public static Date getNextDay(Date date,int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        date = calendar.getTime();
        return date;
    }
}
