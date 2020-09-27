package com.maintain.repository.impl;

import com.boyunmkt.mongodb.core.BaseRepositoryCustomImpl;
import com.maintain.po.Student;
import com.maintain.repository.custom.StudentRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl extends BaseRepositoryCustomImpl<Student>
        implements StudentRepositoryCustom {
    @Override
    protected List<Criteria> createCriterias(Student query) {
        List<Criteria> list = new ArrayList<>();
        
        if(StringUtils.hasText(query.getId())) {
			list.add(Criteria.where("id").is(query.getId()));
		}
		
//		if(StringUtils.hasText(query.getAuthor())) {
//			list.add(Criteria.where("author").regex(".*" + query.getAuthor() + ".*"));
//		}
//		
//		if(query.getType() != null && query.getType() != -1) {
//			list.add(Criteria.where("type").is(query.getType()));
//		}
		
        return list;
    }
}
