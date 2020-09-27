package com.maintain.repository.impl;

import com.boyunmkt.mongodb.core.BaseRepositoryCustomImpl;
import com.maintain.po.Dormitory;
import com.maintain.repository.custom.DormitoryRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DormitoryRepositoryImpl extends BaseRepositoryCustomImpl<Dormitory>
        implements DormitoryRepositoryCustom {
    @Override
    protected List<Criteria> createCriterias(Dormitory query) {
        List<Criteria> list = new ArrayList<>();
        
        if(StringUtils.hasText(query.getBuildNum())) {
			list.add(Criteria.where("buildNum").is(query.getBuildNum()));
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
