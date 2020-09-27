package com.maintain.po;

import com.boyunmkt.mongodb.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 宿舍
 */
@Setter
@Getter
@Document
public class Dormitory extends BaseEntity<String> {

    /**
     * 宿舍建筑号
     */
    private String buildNum;

    /**
     * 宿舍房间号
     */
    private String roomNum;

    /**
     * 维修次数
     */
    private Integer times;

}
