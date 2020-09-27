package com.maintain.service.impl;

import com.boyunmkt.mongodb.core.BaseServiceImpl;
import com.maintain.po.AdminUser;
import com.maintain.po.Maintain;
import com.maintain.po.Worker;
import com.maintain.repository.MaintainRepository;
import com.maintain.service.MaintainService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class MaintainServiceImpl extends
        BaseServiceImpl<Maintain> implements
        MaintainService {

    @Resource
    private MaintainRepository maintainRepository;

    @PostConstruct
    public void initSuper() {
        setBaseRepository(maintainRepository);
    }
    
    @Override
    public Maintain update(Maintain t) {
    	Maintain old = super.findById(t.getId());
    	old.setWorkerId(t.getWorkerId());
    	old.setStudentId(t.getStudentId());
    	old.setStatus(t.getStatus());
    	old.setReqTime(t.getReqTime());
    	old.setRespTime(t.getRespTime());
    	old.setType(t.getType());
    	old.setContent(t.getContent());
    	old.setBuildNum(t.getBuildNum());
    	return super.update(old);
    }
	@Override
	public Maintain findByStudentId(String studentId) {
		return maintainRepository.findByStudentId(studentId);
	}

}
