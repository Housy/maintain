package com.maintain.service.impl;

import com.boyunmkt.mongodb.core.BaseServiceImpl;
import com.maintain.po.Student;
import com.maintain.repository.StudentRepository;
import com.maintain.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class StudentServiceImpl extends
        BaseServiceImpl<Student> implements
        StudentService {

    @Resource
    private StudentRepository studentRepository;

    @PostConstruct
    public void initSuper() {
        setBaseRepository(studentRepository);
    }
    
    @Override
    public Student update(Student t) {
    	Student old = super.findById(t.getId());
//    	old.setTitle(t.getTitle());
//    	old.setAuthor(t.getAuthor());
//    	old.setContent(t.getContent());
//    	old.setLabel(t.getLabel());
    	return super.update(old);
    }
}
