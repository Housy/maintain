package com.maintain.repository;

import com.boyunmkt.mongodb.core.BaseRepository;
import com.maintain.po.User;
import com.maintain.repository.custom.UserRepositoryCustom;

public interface UserRepository extends BaseRepository<User>,
        UserRepositoryCustom {
    User findByUsername(String username);
}