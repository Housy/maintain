package com.maintain.repository.impl;

import com.boyunmkt.mongodb.core.BaseRepositoryCustomImpl;
import com.maintain.po.AdminUser;
import com.maintain.repository.custom.AdminUserRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AdminUserRepositoryImpl extends BaseRepositoryCustomImpl<AdminUser>
        implements AdminUserRepositoryCustom {
    @Override
    protected List<Criteria> createCriterias(AdminUser query) {
        List<Criteria> list = new ArrayList<>();
        
        if(StringUtils.hasText(query.getId())) {
			list.add(Criteria.where("id").is(query.getId()));
		}
		
		if(StringUtils.hasText(query.getPhone())) {
			list.add(Criteria.where("phone").regex(".*" + query.getPhone() + ".*"));
		}

        if(StringUtils.hasText(query.getRole())) {
            list.add(Criteria.where("role").is(query.getRole()));
        }

        if(query.getIsDisabled() != null) {
            list.add(Criteria.where("isDisabled").is(query.getIsDisabled()));
        }

        return list;
    }
}
