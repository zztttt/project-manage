package org.fields.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.fields.project.mapper")
public class AiPlatformMetadataApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiPlatformMetadataApplication.class, args);
    }

}
