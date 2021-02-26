package com.bjf;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.util.unit.DataSize;

@SpringBootApplication
@ComponentScan(basePackages = "com.bjf.*")
@MapperScan("com.bjf.mapper")
@Import(FdfsClientConfig.class)//文件上传
public class MallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
        System.err.println("\t项目已启动！");

    }
    @Bean
    public MultipartConfigFactory multipartConfigElement(){
        MultipartConfigFactory factory=new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("1024000KB"));
        factory.setMaxRequestSize(DataSize.parse("10240000KB"));
        return factory;
    }

}
