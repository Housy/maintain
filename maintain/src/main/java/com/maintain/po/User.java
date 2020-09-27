package com.maintain.po;

import com.boyunmkt.mongodb.core.BaseEntity;
import com.boyunmkt.utils.annotation.DTOField;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户
 */
@Setter
@Getter
@Document
public class User extends BaseEntity<String> {

    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_STUDENT = "Student";
    public static final String ROLE_WORKER = "Worker";

    public static final List<String> Roles = new ArrayList<>();
    static {
        Roles.add(ROLE_ADMIN);
        Roles.add(ROLE_STUDENT);
        Roles.add(ROLE_WORKER);
    }

    /**
     * 用户名
     */
    private String username;

    /**
     * 加盐加密后的密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 角色
     */
    private String role;

}
