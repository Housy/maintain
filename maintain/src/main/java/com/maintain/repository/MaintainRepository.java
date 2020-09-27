package com.maintain.repository;

import com.boyunmkt.mongodb.core.BaseRepository;
import com.maintain.po.Maintain;
import com.maintain.repository.custom.MaintainRepositoryCustom;

public interface MaintainRepository extends BaseRepository<Maintain>,
        MaintainRepositoryCustom {
    Maintain findByStudentId(String studentId);
}