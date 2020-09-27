package com.maintain.service.impl;

import com.boyunmkt.mongodb.core.BaseServiceImpl;
import com.maintain.po.User;
import com.maintain.repository.UserRepository;
import com.maintain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
@Slf4j
public class UserServiceImpl extends
        BaseServiceImpl<User> implements
        UserService {

    @Resource
    private UserRepository userRepository;

    @PostConstruct
    public void initSuper() {
        setBaseRepository(userRepository);
    }

    @Override
    public User getById(String id) {
        Assert.hasText(id, "参数非法");
        User user = findById(id);
        Assert.notNull(user, "用户不存在");
        return user;
    }

    @Override
    public User update(User t) {
        User old = getById(t.getId());
        return super.update(old);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updatePassword(String id, String password) {
        User user = getById(id);
        user.setPassword(password);
        save(user);
    }
}
