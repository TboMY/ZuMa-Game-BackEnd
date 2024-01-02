package com.tbomy.canvas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tbomy.canvas.mapper")
public class CanvasApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CanvasApplication.class, args);
    }
    
}
