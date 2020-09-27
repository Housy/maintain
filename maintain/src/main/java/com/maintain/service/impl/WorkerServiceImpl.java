package com.maintain.service.impl;

import com.boyunmkt.mongodb.core.BaseServiceImpl;
import com.maintain.po.Worker;
import com.maintain.repository.WorkerRepository;
import com.maintain.service.WorkerService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class WorkerServiceImpl extends
        BaseServiceImpl<Worker> implements
        WorkerService {

    @Resource
    private WorkerRepository workerRepository;

    @PostConstruct
    public void initSuper() {
        setBaseRepository(workerRepository);
    }
    
    @Override
    public Worker update(Worker t) {
    	Worker old = super.findById(t.getId());
//    	old.setTitle(t.getTitle());
//    	old.setAuthor(t.getAuthor());
//    	old.setContent(t.getContent());
//    	old.setLabel(t.getLabel());
        old.setName(t.getName());
        old.setGender(t.getGender());
        old.setWorkNum(t.getWorkNum());
    	return super.update(old);
    }
}
