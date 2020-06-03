package cn.itchen.ssm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static SimpleDateFormat dateFormat;
    public static String dateToString(Date date,String formatStr){
        if(date!=null&&formatStr!=null){
            dateFormat=new SimpleDateFormat(formatStr);
            return dateFormat.format(date);
        }
        return null;
    }
    public static Date stringToDate(String dateStr,String parseStr){
        if(dateStr!=null&&parseStr!=null){
            dateFormat=new SimpleDateFormat(parseStr);
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
