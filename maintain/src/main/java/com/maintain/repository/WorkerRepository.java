package com.maintain.repository;

import com.boyunmkt.mongodb.core.BaseRepository;
import com.maintain.po.Student;
import com.maintain.po.Worker;
import com.maintain.repository.custom.WorkerRepositoryCustom;

public interface WorkerRepository extends BaseRepository<Worker>,
        WorkerRepositoryCustom {

    Worker findByUserId(String userId);

}