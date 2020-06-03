package cn.itchen.ssm.exceptionresolver.resolver;

import cn.itchen.ssm.exceptionresolver.exception.MyException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义异常拦截处理器
 */
public class MyExceptionResolver implements HandlerExceptionResolver {
    private ModelAndView modelAndView=new ModelAndView();
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        MyException exception=null;
        if(e instanceof MyException){
            exception=(MyException) e;
        }else {
            exception=new MyException("服务器过忙！请稍后重试！");
        }
        modelAndView.addObject("error",exception.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
