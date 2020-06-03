package cn.itchen.ssm.version;

import cn.itchen.ssm.exceptionresolver.exception.MyException;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateVersion implements Converter<String, Date> {
    @Override
    public Date convert(String source) {

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            return dateFormat.parse(source);
        } catch (Exception e) {
            throw new MyException("日期格式异常！");
        }
    }
}
