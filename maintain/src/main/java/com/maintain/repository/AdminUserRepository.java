package com.maintain.repository;

import com.boyunmkt.mongodb.core.BaseRepository;
import com.maintain.po.AdminUser;
import com.maintain.repository.custom.AdminUserRepositoryCustom;

public interface AdminUserRepository extends BaseRepository<AdminUser>,
        AdminUserRepositoryCustom {

    AdminUser findByUsername(String username);

}