package com.maintain.repository.impl;

import com.boyunmkt.mongodb.core.BaseRepositoryCustomImpl;
import com.maintain.po.Maintain;
import com.maintain.repository.custom.MaintainRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MaintainRepositoryImpl extends BaseRepositoryCustomImpl<Maintain>
        implements MaintainRepositoryCustom {
    @Override
    protected List<Criteria> createCriterias(Maintain query) {
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
