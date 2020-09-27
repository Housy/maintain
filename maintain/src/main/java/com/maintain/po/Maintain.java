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
public class Maintain extends BaseEntity<String> {

    public static final Integer STATUS_WAITING = 0;  //等待维修
    public static final Integer STATUS_FINISHED = 1;  //维修完毕
    public static final Integer STATUS_CANNOT = 2;  //无法维修

    /**
     * 维修工人id
     */
    private String workerId;

    /**
     * 报修学生id
     */
    private String studentId;

    /**
     * 维修状态
     */
    private Integer status;

    /**
     * 发起维修时间
     */
    private Long reqTime;

    /**
     * 维修响应时间
     */
    private Long respTime;

    /**
     * 维修类型
     */
    private Integer type;

    /**
     * 维修内容
     */
    private String content;

    /**
     * 建筑号
     */
    private String buildNum;

    /**
     * 维修日期
     */
    private Long date;

}
