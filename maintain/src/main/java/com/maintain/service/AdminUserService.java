package com.maintain.service;

import com.boyunmkt.mongodb.core.BaseService;
import com.maintain.po.AdminUser;

public interface AdminUserService extends BaseService<AdminUser> {

    AdminUser findByUsername(String username);

    void disable(String id);

    void enable(String id);

}
