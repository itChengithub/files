package cn.itchen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("cn.itchen.dao")
public class SpringbootDay09FilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDay09FilesApplication.class, args);
    }

//    /**
//     * 指定入口类
//     * @param builder
//     * @return
//     */
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(SpringbootDay09FilesApplication.class);
//    }
}
