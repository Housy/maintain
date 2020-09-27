package com.maintain.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

/**
 * 绑定application.properties的数到属性上面
 * @author xujiantao
 * @date 2018/11/22
 */
@Getter
public class ApplicationConfigBean {


    @Value("${env}")
    private int env;

}
