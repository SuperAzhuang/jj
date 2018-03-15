package com.feihua.jjcb.phone.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author wcj
 */
/**
 * @author wcj
 *
 */

/**
 * @author wcj
 *
 */
public class DateUtil
{

    /**
     * 将Date类型转换为字符串
     *
     * @param date
     * @return
     */
    public static String format(Date date)
    {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将Date类型转换为字符串
     *
     * @param date
     * @return
     */
    public static String format2(Date date)
    {
        return format(date, "HH:mm:ss");
    }

    /**
     * 将Date类型转换为字符串
     *
     * @param date
     * @return
     */
    public static String format3(Date date)
    {
        return format(date, "yyyy-MM-dd");
    }

    /**
     * 将Date类型转换为字符串
     *
     * @param date
     * @return
     */
    public static String format4(Date date)
    {
        return format(date, "MM-dd HH:mm:ss");
    }

    /**
     * 将Date类型转换为字符串
     *
     * @param date
     * @param pattern
     *            字符串格式
     * @return
     */
    public static String format(Date date, String pattern)
    {
        if (date == null)
        {
            return "null";
        }
        if (pattern == null || pattern.equals("") || pattern.equals("null"))
        {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return new java.text.SimpleDateFormat(pattern).format(date);
    }

    /**
     * 将字符串转换为Date类型
     *
     * @param date
     * @return
     */
    public static Date format(String date)
    {
        return format(date, null);
    }

    /**
     * 将字符串转换为Date类型
     *
     * @param date
     * @param pattern
     *            格式
     * @return
     */
    public static Date format(String date, String pattern)
    {
        if (pattern == null || pattern.equals("") || pattern.equals("null"))
        {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (date == null || date.equals("") || date.equals("null"))
        {
            return new Date();
        }
        Date d = null;
        try
        {
            d = new java.text.SimpleDateFormat(pattern).parse(date);
        }
        catch (ParseException pe)
        {
        }
        return d;
    }

    /**
     * 获得当前日期(年月日)
     *
     * @return yyyy-MM-dd
     */
    public static String getCurrentDate()
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 获得当前日期(时分秒)
     *
     * @return HH:mm:ss
     */
    public static String getCurrentTime()
    {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    /**
     * 获得当前日期(时分秒)
     *
     * @return HH:mm:ss
     */
    public static String getCurrentHour()
    {
        return new SimpleDateFormat("HH").format(new Date());
    }

    /**
     * 获得当前日期
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDate2()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 获得当前日期
     *
     * @return yyyy-MM-dd HH:mm
     */
    public static String getCurrentDate3()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }

    /**
     * 获得当前日期
     *
     * @return yyyy-MM
     */
    public static String getCurrentDate4()
    {
        return new SimpleDateFormat("yyyy-M").format(new Date());
    }

    /**
     * 获得当前日期
     *
     * @return yyyy-MM-dd
     */
    public static Date getCurrentDateBegin()
    {
        return format(format3(new Date()) + " 00:00:00", null);
    }

    /**
     * 获得当前日期
     *
     * @return yyyy-MM-dd
     */
    public static Date getCurrentDateEnd()
    {
        return format(format3(new Date()) + " 23:59:59", null);
    }

    /**
     * 根据年月 获取月份天数
     *
     * @return int
     */
    public static int calDayByYearAndMonth(String dyear, String dmouth)
    {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");
        Calendar rightNow = Calendar.getInstance();
        try
        {
            rightNow.setTime(simpleDate.parse(dyear + "/" + dmouth));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     *
     * @param d1
     * @param d2
     * @return 计算日期直接相隔的天数
     * @throws Exception
     */
    public static int dateDiff(Date d1, Date d2) throws Exception
    {
        long n1 = d1.getTime();
        long n2 = d2.getTime();
        long diff = Math.abs(n1 - n2);
        diff /= 3600 * 1000 * 24;
        return (int) diff;
    }

    /**
     * yyyy-MM
     *
     * @param currentDate
     * @return 根据当月的yyyy-MM获得下个月的yyy-MM
     */
    public static String getNextMonthDate(String currentDate)
    {
        if (currentDate == null)
        {
            return "";
        }
        String[] stringdate = currentDate.split("-");
        int year = Integer.valueOf(stringdate[0]);
        int month = Integer.valueOf(stringdate[1]) + 1;
        String monthString = month + "";
        if (month > 12)
        {
            year = year + 1;
            month = 1;
        }
        if (month < 10)
        {
            monthString = "0" + month;
        }
        return year + "-" + monthString;
    }

    public static String getDateOfLastMonth(Calendar date, int i)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = (Calendar) date.clone();
        lastDate.add(Calendar.MONTH, i);
        return sdf.format(lastDate.getTime());
    }

    /**
     * 上个月的今天日期(i=-1) 上两个月的今天日期(i=-2) ...
     *
     * @param dateStr
     * @return
     */
    public static String getDateOfLastMonth(String dateStr, int i)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date date = sdf.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return getDateOfLastMonth(c, i);
        }
        catch (ParseException e)
        {
            throw new IllegalArgumentException("Invalid date format(yyyyMMdd): " + dateStr);
        }
    }

    public static Date getDateFromString3(String str)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            return sdf.parse(str);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 昨日
     * @param dateStr
     * @return
     */
    public static String getYesDayStr(String dateStr)
    {
        Date afterDate = null;
        Date toEndDate = getDateFromString3(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toEndDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        afterDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String afterDate2 = sdf.format(afterDate);
        return afterDate2;
    }

    /**
     * 上个月的今日
     * @param dateStr
     * @return
     */
    public static String getYesMonthStr(String dateStr)
    {//
        Date afterDate = null;
        Date toEndDate = getDateFromString3(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toEndDate);
        calendar.add(Calendar.MONTH, -1);
        afterDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String afterDate2 = sdf.format(afterDate);
        return afterDate2;
    }

    /**
     * 去年今日
     * @param dateStr
     * @return
     */
    public static String getYesYearStr(String dateStr)
    {
        Date afterDate = null;
        Date toEndDate = getDateFromString3(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toEndDate);
        calendar.add(Calendar.YEAR, -1);
        afterDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String afterDate2 = sdf.format(afterDate);
        return afterDate2;
    }

    // 字符串类型日期转化成date类型
    public static Date strToDate(String style, String date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try
        {
            return formatter.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String dateToStr(String style, Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }

    //time="hh:mm"转换成分钟
    public static long HHmmToMm(String time)
    {
        long m = 0;
        try
        {
            String[] times = time.split(":");
            long h = Long.valueOf(times[0]);
            m = h*60 + Long.valueOf(times[1]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return m;
    }

}
