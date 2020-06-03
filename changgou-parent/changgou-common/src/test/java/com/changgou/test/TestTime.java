package com.changgou.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestTime {
    public static void main(String[] args) throws ParseException {
        String str="202006010535231";
        String strNum="20200601053523";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(dateFormat.parse(str));
        System.out.println(dateFormat.parse(strNum));
        System.out.println("-------------------------------发送给了Listener--------------------------------"+"------------------------");

    }
}
