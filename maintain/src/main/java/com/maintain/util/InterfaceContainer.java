package com.maintain.util;

import com.maintain.service.AdminUserService;
import com.maintain.service.UserService;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Getter
public class InterfaceContainer {

    private static InterfaceContainer instance;

    @Resource
    private UserService userService;

    @Resource
    private AdminUserService adminUserService;

    @PostConstruct
    private void init(){
        instance = this;
    }

    public static InterfaceContainer getInstance(){
        return instance;
    }

}
