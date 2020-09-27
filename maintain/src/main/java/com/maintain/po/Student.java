package com.maintain.po;

import com.boyunmkt.mongodb.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户
 */
@Setter
@Getter
@Document
public class Student extends BaseEntity<String> {

    public static final Integer GENDER_UNKNOWN = 0;  //未知
    public static final Integer GENDER_MALE = 1;  //男
    public static final Integer GENDER_FEMALE = 2;  //女

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
     * 建筑号
     */
    private String buildNum;

    /**
     * 房间号
     */
    private String roomNum;

    /**
     * 手机号
     */
    private String phone;

}
