package com.maintain.service;

import com.boyunmkt.mongodb.core.BaseService;
import com.maintain.po.AdminUser;
import com.maintain.po.Maintain;

public interface MaintainService extends BaseService<Maintain> {
    Maintain findByStudentId(String studentId);
}
