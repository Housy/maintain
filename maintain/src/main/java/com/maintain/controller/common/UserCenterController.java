package com.maintain.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.boyunmkt.utils.KeyUtil;
import com.boyunmkt.utils.Result;
import com.maintain.po.Student;
import com.maintain.po.User;
import com.maintain.po.Worker;
import com.maintain.repository.StudentRepository;
import com.maintain.repository.WorkerRepository;
import com.maintain.service.StudentService;
import com.maintain.service.UserService;
import com.maintain.service.WorkerService;
import com.maintain.util.DigestUtils;
import com.maintain.util.MenuUtil;
import com.maintain.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 *  用户中心和权限控制的controller，
 *  通用的
 */
@Controller
@Slf4j
public class UserCenterController {

    @Resource
    private UserService userService;


    @Resource
    private StudentRepository studentRepository;

    @Resource
    private WorkerRepository workerRepository;

    @Resource
    private WorkerService workerService;

    /**
     * 登录页面重定向
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        return "redirect:/index.html#/userCenter/login";
    }



    /**
     * 登录请求
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public Result doLogin(String username, String password){
        //登录后存放进shiro token
        UsernamePasswordToken token = new UsernamePasswordToken(
                username, password);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        WebUtil.setLoginedUser(user);
        return Result.success();
    }

    @RequestMapping("/register")
    @ResponseBody
    public Result register(String username, String password, String role){
        User user = new User();
        user.setUsername(username);
        user.setRole(role);
        user.setSalt(KeyUtil.getUUIDKey());
        user.setPassword(DigestUtils.Md5(user.getSalt(), password));
        userService.save(user);
        return Result.success();
    }

    /**
     * 初始化账号
     * @return
     */
    @RequestMapping("/initUser")
    @ResponseBody
    public Result initUser(String token){
        String curToken = "123456";
        Assert.state(curToken.equals(token), "token error");
        User user = new User();
        user.setUsername("user");
        user.setRole(User.ROLE_ADMIN);
        user.setSalt(KeyUtil.getUUIDKey());
        user.setPassword(DigestUtils.Md5(user.getSalt(), curToken));
        userService.save(user);
        return Result.success();
    }

    /**
     * 获取登录的用户信息
     * @return
     */
    @RequestMapping(value = "/api/getLoginUserInfo")
    @ResponseBody
    public Result getLoginUserInfo() {
        User user = WebUtil.getLoginedUser();
        return Result.success().setData(user);
    }

    /**
     * 获取登录的用户信息
     * @return
     */
    @RequestMapping(value = "/api/getMyInfo")
    @ResponseBody
    public Result getMyInfo() {
        User user = WebUtil.getLoginedUser();
        String role = user.getRole();
        if(role.equals(User.ROLE_STUDENT)) {
            Student stu = studentRepository.findByUserId(user.getId());
            return Result.success().setData(stu);
        }else if(role.equals(User.ROLE_WORKER)){
            Worker w = workerRepository.findByUserId(user.getId());
            return Result.success().setData(w);
        }
        return Result.fail();

    }

    @RequestMapping(value = "/api/updateStudent")
    @ResponseBody
    public Result updateStudent(Student stu) {
        User user = WebUtil.getLoginedUser();
        Student w = studentRepository.findByUserId(user.getId());
        if(w != null) {
            w.setName(stu.getName());
            w.setGender(stu.getGender());
            w.setBuildNum(stu.getBuildNum());
            w.setRoomNum(stu.getRoomNum());
            w.setPhone(stu.getPhone());
        }else {
            w = new Student();
            w.setUserId(user.getId());
            w.setName(stu.getName());
            w.setGender(stu.getGender());
            w.setBuildNum(stu.getBuildNum());
            w.setRoomNum(stu.getRoomNum());
            w.setPhone(stu.getPhone());
        }
        studentRepository.save(w);
        return Result.success();
    }

    @RequestMapping(value = "/api/updateWorker")
    @ResponseBody
    public Result updateWorker(Worker t) {
        User user = WebUtil.getLoginedUser();
        Worker w = workerRepository.findByUserId(user.getId());
        if(w != null) {
            w.setName(t.getName());
            w.setGender(t.getGender());
            w.setWorkNum(t.getWorkNum());
        }else {
            w = new Worker();
            w.setUserId(user.getId());
            w.setName(t.getName());
            w.setGender(t.getGender());
            w.setWorkNum(t.getWorkNum());
        }
        workerRepository.save(w);
        return Result.success();
    }

    /**
     * 获取登录用户对应的菜单栏
     * @return
     */
    @RequestMapping(value = "/api/getMenus")
    @ResponseBody
    public JSONObject getMenus() {
        return MenuUtil.getMenus(WebUtil.getLoginedUser().getRole());
    }

    /**
     * 用户身份认证
     * @path 页面路径，如：/game/list
     * @return 0：成功 -1：未登录 -2：没有权限
     */
    @RequestMapping(value = "/auth")
    @ResponseBody
    public Result auth(String path) {
        User user = WebUtil.getLoginedUser();
        if(user == null) {
            return Result.fail(-1, "未登录");  //-1表示未登录
        }
        return Result.success();
    }

    @RequestMapping(value = "api/updatePwd")
    @ResponseBody
    public Result updatePwd(String oldPwd, String newPwd){
        validatePwd(oldPwd);
        String password = DigestUtils.Md5(WebUtil.getLoginedUser().getSalt(), newPwd);
        userService.updatePassword(WebUtil.getLoginedUser().getId(), password);
        return Result.success();
    }

    private Result validatePwd(String pwd) {
        User user = userService.findById(WebUtil.getLoginedUserId());
        String password = DigestUtils.Md5(user.getSalt(), pwd);
        Assert.state(password.equals(user.getPassword()), "原密码错误");
        return Result.success();
    }
    /**
     * 退出登录
     * @return
     */
    @RequestMapping("/logout")
    public String logout(){
        WebUtil.getCurrentSession().invalidate();
        SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }

}
