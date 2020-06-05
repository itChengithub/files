package cn.itchen.config;

import cn.itchen.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Value("${file.dir}")
    private String filePath;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/file/**")//配置拦截的路径
                .excludePathPatterns("index","/user/**");//配置不拦截的路径
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")//代表以何种路径可以访问静态资源
        .addResourceLocations("classpath:/static/")//可以访问资源的静态目录
        .addResourceLocations("classpath:/templates")
        .addResourceLocations("file:"+filePath)
        ;
    }
}
