package com.maintain.filter;

import com.boyunmkt.utils.FileUtil;
import com.boyunmkt.utils.others.CodeAutoCreater;
import com.maintain.util.WebUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 页面权限验证过滤器，只有静态页面会走这里
 */
public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void initFilterBean() throws ServletException {
    }


    @Override
    public void destroy() {

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException{
        String html = FileUtil.inputStreamToStr(CodeAutoCreater.class.getClassLoader().getResourceAsStream("static" + request.getRequestURI()));
        List<String> roleList = getAllowRoles(html);
        //不为空，表示配置了角色
        if(!CollectionUtils.isEmpty(roleList)) {
            //没有权限
            if(!roleList.contains(WebUtil.getLoginedUserRole())){
                //没有权限直接跳转到deny页面
                request.getRequestDispatcher("/deny").forward(request, response);
            }else {  //有权限
                response.setHeader("Cache-Control","no-cache");
                response.setHeader("Pragma","no-cache");
                response.setDateHeader("Expires",0);
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().print(removeMarkContent(html));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private final String MarkRegex = "\\{\\[\\([\\w _,-]*\\)\\]\\}";

    /**
     * 处理内容
     * @param content
     * @return
     */
    private List<String> getAllowRoles(String content){
        String realCon = findMarkContent(content);
        if(realCon == null) {
            return null;
        }
        realCon = realCon.substring(realCon.indexOf("{[(") + 3, realCon.indexOf(")]}")).trim();
        if(!StringUtils.hasText(realCon)) {
            return new ArrayList<>();
        }
        List<String> list = Arrays.asList(realCon.split(","));
        List<String> roles = new ArrayList<>();
        list.forEach(r -> {
            roles.add(r.trim());
        });
        return roles;
    }

    /**
     * 解析模板，拿到模板中的数据
     * @param html
     * @return
     */
    private String findMarkContent(String html){
        Pattern p = Pattern.compile(MarkRegex);
        Matcher m = p.matcher(html);
        boolean isFound = m.find();
        if(isFound) {
            return m.group();
        }
        return null;
    }

    /**
     * 移除模板
     */
    private String removeMarkContent(String str){
        return str.replaceAll(MarkRegex, "");
    }


    public static void main(String[] args) {
        AuthFilter f = new AuthFilter();
        String content = "123213{[( Admi-n, Svi-p )]}213123";
        System.out.println(f.getAllowRoles(content));
    }
}
