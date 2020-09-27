package com.maintain.service;

import com.boyunmkt.mongodb.core.BaseService;
import com.maintain.po.AdminUser;
import com.maintain.po.Dormitory;

public interface DormitoryService extends BaseService<Dormitory> {
    Dormitory findByBuildNum(String buildNum);
}
