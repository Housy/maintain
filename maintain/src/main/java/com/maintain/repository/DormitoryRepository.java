package com.maintain.repository;

import com.boyunmkt.mongodb.core.BaseRepository;
import com.maintain.po.Dormitory;
import com.maintain.repository.custom.DormitoryRepositoryCustom;

public interface DormitoryRepository extends BaseRepository<Dormitory>,
        DormitoryRepositoryCustom {
    Dormitory findByBuildNum(String buildNum);
}