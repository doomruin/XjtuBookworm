/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bookworm.utls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 * @author bigstone
 */
public class DateUtil {

    public static final String[] months = {"一月", "二月", "三月", "四月", "五月", "六月",
        "七月", "八月", "九月", "十月", "十一月", "十二月",};
    public static final String[] quarters = {"一季度", "二季度", "三季度", "四季度"};
    private static String timePattern = "yyyyMM";

    public DateUtil() {
    }

    /**
     * 获取日期字符串。
     * 
     * <pre>
     *  日期字符串格式： yyyyMMdd
     *  其中：
     *      yyyy   表示4位年。
     *      MM     表示2位月。
     *      dd     表示2位日。
     * </pre>
     * 
     * @return String "yyyyMMdd"格式的日期字符串。
     */
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(new Date());
    }

    /**
     * 获取当前年度字符串。
     * 
     * <pre>
     *  日期字符串格式： yyyy
     *  其中：
     *      yyyy   表示4位年。
     * </pre>
     * 
     * @return String "yyyy"格式的当前年度字符串。
     */
    public static String getNowYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        return formatter.format(new Date());
    }

    /**
     * 获取当前月度字符串。
     * 
     * <pre>
     *  日期字符串格式： MM
     *  其中：
     *      MM   表示4位年。
     * </pre>
     * 
     * @return String "yyyy"格式的当前月度字符串。
     */
    public static String getNowMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        return formatter.format(new Date());
    }

    /**
     * 获取当前月度字符串。
     * 
     * <pre>
     *  日期字符串格式： dd
     *  其中：
     *      dd   表示4位年。
     * </pre>
     * 
     * @return String "yyyy"格式的当前月度字符串。
     */
    public static String getNowDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        return formatter.format(new Date());
    }

    /**
     * 获取日期字符串。
     * 
     * <pre>
     *  日期字符串格式： yyyyMMdd
     *  其中：
     *      yyyy   表示4位年。
     *      MM     表示2位月。
     *      dd     表示2位日。
     * </pre>
     * 
     * @param date
     *                需要转化的日期。
     * @return String "yyyyMMdd"格式的日期字符串。
     */
    public static String getDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 获取日期字符串。
     * 
     * <pre>
     *  日期字符串格式： yyyy-MM-dd
     *  其中：
     *      yyyy   表示4位年。
     *      MM     表示2位月。
     *      dd     表示2位日。
     * </pre>
     * 
     * @return String "yyyy-MM-dd"格式的日期字符串。
     */
    public static String getHyphenDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    public static int getMonthDateCount(String monthTime) {
        Calendar cal = new GregorianCalendar();
        //或者用Calendar   cal   =   Calendar.getInstance();  

        /**设置date**/
        SimpleDateFormat dateFormater = new SimpleDateFormat("", Locale.ENGLISH);
        dateFormater.applyPattern(timePattern);

        try {
            cal.setTime(dateFormater.parse(monthTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /**或者设置月份，注意月是从0开始计数的，所以用实际的月份-1才是你要的月份**/
        //一月份: cal.set(   2009,   1-1,   1   );  
        /**如果要获取上个月的**/
        //cal.set(Calendar.DAY_OF_MONTH, 1);  
        //日期减一,取得上月最后一天时间对象  
        //cal.add(Calendar.DAY_OF_MONTH, -1);  
        //输出上月最后一天日期  
        //System.out.println(cal.get(Calendar.DAY_OF_MONTH));  
        /**开始用的这个方法获取月的最大天数，总是得到是31天**/
        //int num = cal.getMaximum(Calendar.DAY_OF_MONTH);  
        /**开始用的这个方法获取实际月的最大天数**/
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前日期和时间
     * 
     * @return String
     */
    public static String getCurrentDateStr() {
        Date date = new Date();
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        str = df.format(date);
        return str;
    }

    /**
     * 日期相加
     *
     * @param day
     *             天数
     * @return 返回相加后的日期
     */
    public static String addDate(int day) {
        java.util.Calendar c = java.util.Calendar.getInstance();

        c.setTimeInMillis(System.currentTimeMillis() + ((long) day) * 24 * 3600
                * 1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return df.format(c.getTime());
    }

    /**
     * 返回毫秒
     * 
     * @param date
     *             日期
     * @return 返回毫秒
     */
    public static long getMillis(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 获取当前日期和时间
     * @param format 日期格式 例：yyyy-MM-dd hh:mm
     * @return String
     */
    public static String getNowDate(String format) {
        Date date = new Date();
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat(format);
        str = df.format(date);
        return str;
    }

    public static String getTimeStr() {
        String str = "";
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        str = df.format(new Date());
        return str;
    }

    public static float nhoursBetweenTwoDate(String firstString, String secondString) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse(firstString);
            secondDate = df.parse(secondString);
        } catch (Exception e) {
            System.out.println("bad struct");
        }

        int nHours = (int) ((secondDate.getTime() - firstDate.getTime()) / (60 * 60 * 1000));
        int minutes = (int) ((secondDate.getTime() - firstDate.getTime()) / (60 * 1000)) % 60;
        float hourResult = nHours;
        if(minutes >= 30){
            hourResult += 0.5;
        }
        return hourResult;
    }
}
