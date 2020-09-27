package com.maintain.service.impl;

import com.boyunmkt.mongodb.core.BaseServiceImpl;
import com.maintain.po.AdminUser;
import com.maintain.repository.AdminUserRepository;
import com.maintain.service.AdminUserService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class AdminUserServiceImpl extends
        BaseServiceImpl<AdminUser> implements
        AdminUserService {

    @Resource
    private AdminUserRepository adminUserRepository;

    @PostConstruct
    public void initSuper() {
        setBaseRepository(adminUserRepository);
    }

    @Override
    public AdminUser add(AdminUser adminUser) {
        return super.add(adminUser);
    }

    @Override
    public AdminUser update(AdminUser t) {
    	AdminUser old = super.findById(t.getId());
        old.setRole(t.getRole());
        old.setPermissions(t.getPermissions());
        old.setBirthday(t.getBirthday());
        old.setPassword(t.getPassword());
        old.setGender(t.getGender());
        old.setPhone(t.getPhone());
        old.setAvatarUrl(t.getAvatarUrl());
    	return super.update(old);
    }

    @Override
    public void enable(String id) {
        AdminUser old = getById(id);
        old.setIsDisabled(AdminUser.NO);
        super.update(old);
    }

    @Override
    public void disable(String id) {
        AdminUser old = getById(id);
        old.setIsDisabled(AdminUser.YES);
        super.update(old);
    }

    @Override
    public AdminUser findByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

}
