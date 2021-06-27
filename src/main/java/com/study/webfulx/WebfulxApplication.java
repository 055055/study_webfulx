package com.study.webfulx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebfulxApplication {

    public static void main(String[] args) {
        try {
         SpringApplication.run(WebfulxApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
