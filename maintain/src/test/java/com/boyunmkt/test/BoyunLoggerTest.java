package com.boyunmkt.test;

import com.maintain.MaintainApplication;
import com.maintain.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MaintainApplication.class)
public class BoyunLoggerTest {


    @Resource
    private UserService userService;


    @Test
    public void test() {
//        CustomerOrderService.CustomerSearchReq req = new CustomerOrderService.CustomerSearchReq();
//        req.setBuyerId("5df1e4b56fdaed1ffecde19b");
//        req.setStatus(-1);
//        PageResult<CustomerOrder > pr = new PageResult<>();
//        pr = customerOrderService.findByPageForCustomer(req, pr);
//        System.out.println(pr);
    }
}
