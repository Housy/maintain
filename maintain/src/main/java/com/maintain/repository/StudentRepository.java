package com.maintain.repository;

import com.boyunmkt.mongodb.core.BaseRepository;
import com.maintain.po.Student;
import com.maintain.repository.custom.StudentRepositoryCustom;

public interface StudentRepository extends BaseRepository<Student>,
        StudentRepositoryCustom {

    Student findByUserId(String userId);

}