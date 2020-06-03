package cn.itchen.ssm.controller;

import cn.itchen.ssm.domain.SysLog;
import cn.itchen.ssm.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

@Component
//@Aspect
public class SysLogAOP {
    @Autowired
    private HttpServletRequest request;
    private Date visitTime;//访问时间
    private Class clazz;//访问的类
    private Method method;//访问的方法
    @Autowired
    private SysLogService sysLogService;
//    @Before("execution(* cn.itchen.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint joinPoint) throws NoSuchMethodException {
        visitTime=new Date();
        clazz=joinPoint.getTarget().getClass();//获取class类属性
        String methodName = joinPoint.getSignature().getName();//获取方法名称
        Object[] args = joinPoint.getArgs();//参数
        if(args==null||args.length==0){
            method = clazz.getMethod(methodName);//如果是无参方法，那么创建
        }else {
            Class[] cs=new Class[args.length];
            for(int i=0;i<args.length;i++){
                if(args[i]!=null){
                    cs[i]=args[i].getClass();
                }
            }
            method=clazz.getMethod(methodName,cs);//如果方法有参数，那么获取每个参数的class反射创建方法
        }
    }
//    @After("execution(* cn.itchen.ssm.controller.*.*(..))")
    public void doAfter(JoinPoint jp){
        long executionTime=new Date().getTime()-visitTime.getTime();
        if(clazz.getName().contains("SysLogController") ){
            return;
        }
        String url="";
        if(clazz!=null&&method!=null&clazz!= SysLog.class){
            RequestMapping annotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if(annotation!=null){
                String[] classValue = annotation.value();
                RequestMapping annotation1 = method.getAnnotation(RequestMapping.class);
                if(annotation1!=null){
                    String[] methodValue = annotation1.value();
                    url=classValue[0]+methodValue[0];
                }
            }
        }
        String ip = request.getRemoteAddr();//获取访问的IP地址
        SecurityContext context = SecurityContextHolder.getContext();//获取SecurityContext对象
        User user = (User)context.getAuthentication().getPrincipal();//获取User对象
        String username = user.getUsername();//获取对象的username
        SysLog sysLog = new SysLog();
        sysLog.setVisitTime(visitTime);
        sysLog.setUsername(username);
        sysLog.setIp(ip);
        sysLog.setUrl(url);
        sysLog.setExecutionTime((int)executionTime);
        sysLog.setMethod("类名"+clazz.getName()+",方法名"+method.getName());
        sysLogService.saveLog(sysLog);


    }
}
