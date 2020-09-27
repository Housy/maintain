package com.maintain.util;

import com.alibaba.fastjson.JSONObject;
import com.boyunmkt.utils.FileUtil;
import com.boyunmkt.utils.JsonUtil;
import com.maintain.po.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 菜单工具
 */
public class MenuUtil {

    private final static Map<String, JSONObject> Menus = new HashMap<>();

    /**
     * 根据User中定义的角色去加载对应的菜单的json文件，
     * 登录成功获取菜单的时候要用
     */
    static {
        User.Roles.forEach(role ->{
            loadMenuJson(role);
        });
    }

    public static void loadMenuJson(String role){
        try{
            System.out.println("loadMenuJson[role:"+role+"]");
            String str = FileUtil.inputStreamToStr(MenuUtil.class.getClassLoader().getResourceAsStream("file/roles/"+ role +".json"));
            JSONObject navJson = JsonUtil.fromJson(str, JSONObject.class);
            Menus.put(role, navJson);
            System.out.println("加载" + role + "权限文件成功");
        }catch (Exception e) {
            System.err.println("加载" + role + "权限文件失败");
        }

    }

    public static JSONObject getMenus(String role){
        return Menus.get(role);
    }

}
