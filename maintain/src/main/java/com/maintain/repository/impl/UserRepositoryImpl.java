package com.maintain.repository.impl;

import com.boyunmkt.mongodb.core.BaseRepositoryCustomImpl;
import com.maintain.po.User;
import com.maintain.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl extends BaseRepositoryCustomImpl<User>
        implements UserRepositoryCustom {
    @Override
    protected List<Criteria> createCriterias(User query) {
        List<Criteria> list = new ArrayList<>();
        
        if(StringUtils.hasText(query.getId())) {
			list.add(Criteria.where("id").is(query.getId()));
		}

        if(StringUtils.hasText(query.getUsername())) {
            list.add(Criteria.where("username").is(query.getUsername()));
        }

        return list;
    }
}
