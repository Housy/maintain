package com.maintain.service;


import com.boyunmkt.mongodb.core.BaseService;
import com.maintain.po.User;

public interface UserService extends BaseService<User> {

    User findByUsername(String username);
    void updatePassword(String id, String password);
}
