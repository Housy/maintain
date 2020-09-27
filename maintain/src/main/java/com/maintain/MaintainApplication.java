package com.maintain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MaintainApplication {

    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis();
        SpringApplication.run(MaintainApplication.class, args);
        System.out.println("------Maintain Started[耗时："+ (System.currentTimeMillis() - start) +"ms]------");
    }
}
