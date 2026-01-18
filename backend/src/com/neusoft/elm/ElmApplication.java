package com.neusoft.elm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 饿了么商家后台管理系统 - Spring Boot 主启动类
 *
 * @author Neusoft ELM Team
 * @version 2.0 (Spring Boot Edition)
 */
@SpringBootApplication
public class ElmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElmApplication.class, args);
        System.out.println("========================================");
        System.out.println("   饿了么商家后台管理系统启动成功！");
        System.out.println("   访问地址: http://localhost:8080");
        System.out.println("========================================");
    }
}
