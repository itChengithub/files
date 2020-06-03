package com.changgou.goods.entity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
// */
//@ControllerAdvice//捕获所有@GEetMapping路径下的异常
//public class BaseExceptionHandler {
//    @ExceptionHandler(value = Exception.class)//全局的异常处理对象
//    @ResponseBody
//    public Result error(Exception e){
//        System.out.println(e.getMessage());return new Result(false, StatusCode.ERROR,e.getMessage());
//    }
//}
