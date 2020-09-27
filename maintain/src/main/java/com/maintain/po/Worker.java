package com.maintain.po;

import com.boyunmkt.mongodb.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户
 */
@Setter
@Getter
@Document
public class Worker extends BaseEntity<String> {

    /**
     * 关联用户表
     */
    private String userId;

    /**
     * 名字
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 工号
     */
    private String workNum;

}
