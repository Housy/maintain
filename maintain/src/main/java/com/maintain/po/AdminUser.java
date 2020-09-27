package com.maintain.po;

import com.boyunmkt.mongodb.core.BaseEntity;
import com.boyunmkt.utils.annotation.DTOField;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

/**
 * 管理员用户（api）
 */
@Setter
@Getter
@Document
public class AdminUser extends BaseEntity<String> {

    public static final String ROLE_ADMIN = "Admin";   //管理员
    public static final String ROLE_CITY_PARTNER = "CityPartner";  //城市合伙人

    @DTOField
    private String username;

    @DTOField
    private String password;

    private String salt;

    /**
     * 头像
     */
    @DTOField
    private String avatarUrl;

    /**
     * 手机号
     */
    @DTOField
    private String phone;

    /**
     * 性别
     */
    @DTOField
    private Integer gender;

    /**
     * 角色
     */
    @DTOField
    private String role;

    /**
     * 权限码集合
     */
    @DTOField
    private ArrayList<String> permissions = new ArrayList<>();

    /**
     * 出生日期
     */
    @DTOField
    private Long birthday;

    /**
     * 是否禁用
     */
    @DTOField
    private Integer isDisabled;

    /**
     * 是否是会员
     */
    @DTOField
    private Integer isVip;

    {
        permissions.add("add");
        permissions.add("update");
    }

    @Override
    @DTOField
    public String getId() {
        return super.getId();
    }

    @Override
    @DTOField
    public Integer getSortCount() {
        return super.getSortCount();
    }

    @Override
    @DTOField
    public String getShowDate() {
        return super.getShowDate();
    }

}
