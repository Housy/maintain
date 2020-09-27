package com.maintain.service.impl;

import com.boyunmkt.mongodb.core.BaseServiceImpl;
import com.maintain.po.AdminUser;
import com.maintain.po.Dormitory;
import com.maintain.repository.DormitoryRepository;
import com.maintain.service.DormitoryService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class DormitoryServiceImpl extends
        BaseServiceImpl<Dormitory> implements
        DormitoryService {

    @Resource
    private DormitoryRepository dormitoryRepository;

    @PostConstruct
    public void initSuper() {
        setBaseRepository(dormitoryRepository);
    }
    
    @Override
    public Dormitory update(Dormitory t) {
    	Dormitory old = super.findById(t.getId());
//    	old.setTitle(t.getTitle());
//    	old.setAuthor(t.getAuthor());
//    	old.setContent(t.getContent());
//    	old.setLabel(t.getLabel());
    	return super.update(old);
    }
    @Override
    public Dormitory findByBuildNum(String buildNum) {
        return dormitoryRepository.findByBuildNum(buildNum);
    }
}
