package ctd.net.rpc.util;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @ClassName DateUtil
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/11
 * @Version V1.0
 **/
public class DateUtil {
    public static String dateFormatSort(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String dateFormat(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String dateFormatDT15(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String dateFormatDT15(Object date) {
        if ((date instanceof Date)) {
            return dateFormatDT15((Date)date);
        }
        if (date != null)
            return date.toString();
        return "";
    }

    public static Date stringToDate(String value) throws Exception
    {
        String df = "";
        if (value.length() == 15) {
            df = "yyyyMMdd'T'HHmmss";
        } else if (value.length() == 8) {
            df = "yyyyMMdd";
        } else {
            boolean isHasT = false;

            String[] di = null;
            if (value.contains("T")) {
                di = value.split("T");
                isHasT = true;
            } else {
                di = value.split(" ");
            }
            if (di.length >= 1) {
                int c = di[0].split("-").length;
                if (c >= 1)
                    df = df + "yyyy";
                if (c >= 2)
                    df = df + "-MM";
                if (c >= 3)
                    df = df + "-dd";
            }
            if (di.length >= 2) {
                if (isHasT)
                    df = df + "'T'";
                int c = di[1].split(":").length;
                if (c >= 1)
                    df = df + "HH";
                if (c >= 2)
                    df = df + ":mm";
                if (c >= 3)
                    df = df + ":ss";
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(df);
        return sdf.parse(value);
    }

    public static Date addDateToYear(Date date, int year) {
        Date da = (Date)date.clone();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(da);
        calendar.add(1, year);
        da = calendar.getTime();
        return da;
    }

    public static Date addDateToMonth(Date date, int month) {
        Date da = (Date)date.clone();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(da);
        calendar.add(2, month);
        da = calendar.getTime();
        return da;
    }

    public static Date addDateToDay(Date date, int day) {
        Date da = (Date)date.clone();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(da);
        calendar.add(5, day);
        da = calendar.getTime();
        return da;
    }

    public static Date addDateToHour(Date date, int hour) {
        Date da = (Date)date.clone();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(da);
        calendar.add(10, hour);
        da = calendar.getTime();
        return da;
    }

    public static String calcHour(Date startDate, Date endDate)
    {
        try {
            int minute = (int)((endDate.getTime() - startDate.getTime()) / 60000L);
            int hour = minute / 60;
            int min = minute % 60;
            String str = "";
            if (hour > 0) {
                str = str + String.valueOf(hour) + "小时";
            }
            if (min > 0) {
                str = str + String.valueOf(min) + "分钟";
            }
            return str; } catch (Exception localException) {
        }
        return "";
    }

    /**
     * 描述：将字符串格式yyyyMMTddHHmmss的字符串转为日期，格式"yyyy-MM-dd HH:mm:ss"
     * @param date 日期格式字符串
     * @param delPara 要删除的字符串，如：yyyyMMTddHHmmss 中多余的【T】，如果没有则传null
     * @throws Exception
     * @return String  返回 格式"yyyy-MM-dd HH:mm:ss" 的字符串日期格式
     */
    public static String strToDateFormat(String date, String delPara) throws Exception {
        StringBuffer date1 = new StringBuffer(date);
        if(!StringUtils.isEmpty(delPara)){
            int index = date1.indexOf(delPara);
            if(index != -1){
                date1.deleteCharAt(index);
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setLenient(false);
        Date newDate = formatter.parse(date1.toString());
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(newDate);
    }
}
