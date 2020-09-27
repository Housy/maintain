package com.maintain.util;

import com.boyunmkt.utils.JsonUtil;
import com.maintain.common.Constant;
import com.maintain.po.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;


public class WebUtil {

	/**
	 * 向页面输出指定内容
	 * 
	 * @param response
	 * @param result
	 */
	public static void printJsonToView(HttpServletResponse response,
			Object result) {
		response.setContentType("application/json; charset=UTF-8");
		try {
			response.getWriter().print(JsonUtil.toJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printJsonToView(Object result) {
		printJsonToView(getCurrentResponse(), result);
	}
	
	public static void setAttribute(String name, Object o){
		getCurrentRequest().setAttribute(name, o);
	}
	
	public static Object getAttribute(String name){
		return getCurrentRequest().getAttribute(name);
	}
	
	public static void printToView(HttpServletResponse resp, Object result) {
		try {
			resp.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printToView(Object result) {
		printToView(getCurrentResponse());
	}
	
	public static HttpSession getCurrentSession() {
		return RequestThreadLocal.get().getSession();
	};
	
	public static HttpServletRequest getCurrentRequest() {
		return RequestThreadLocal.get();
	};
	
	public static HttpServletResponse getCurrentResponse() {
		return ResponseThreadLocal.get();
	};
	
	public static String getCurrentVerifyCode() {
		Object obj = getCurrentSession().getAttribute(Constant.SESSION_VALIDATE_CODE);
		return obj != null ? obj.toString() : "";
	}
	
	public static User getLoginedUser() {
		try {
			Object obj = getCurrentSession().getAttribute(Constant.SESSION_LOGINED_USER);
			return obj != null ? (User) obj : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 清空当前session所有的值
	 */
	public static void clearSession(){
		HttpSession session = getCurrentSession();
		Enumeration em = session.getAttributeNames();  //得到session中所有的属性名
		while (em.hasMoreElements()) {
			String name = em.nextElement().toString();
			System.out.println(name);
			session.removeAttribute(name); //遍历删除session中的值
		}
	}

	public static String getLoginedUserRole() {
		return getLoginedUser().getRole();
	}
	
	public static String getLoginedUserId() {
		return getLoginedUser().getId();
	}
	
	public static String getLoginedUsername() {
		return getLoginedUser().getUsername();
	}
	
	public static void setLoginedUser(User user) {
		getCurrentSession().setAttribute(Constant.SESSION_LOGINED_USER, user);
	}
	
	public static void forward(String path) {
		try {
			HttpServletRequest req = getCurrentRequest();
			HttpServletResponse resp = getCurrentResponse();
			req.getRequestDispatcher(path).forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendRedirect(String location) {
		HttpServletResponse resp = getCurrentResponse();
		try {
			resp.sendRedirect(location);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
