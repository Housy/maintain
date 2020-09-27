package com.maintain.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截除静态资源外的所有请求
 * @author xujiantao
 *
 */
public class GlobalInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
//		RequestThreadLocal.set((HttpServletRequest) request);
//		ResponseThreadLocal.set((HttpServletResponse) response);
//		String uri = request.getRequestURI();
		//System.out.println("uri-->" + uri);
//		System.out.println("GlobalInterceptor");
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
//		String uri = request.getRequestURI();
//		//不进行缓存
//		if(uri.contains("views")){
//			response.setHeader("Cache-Control","no-cache");
//			response.setHeader("Pragma","no-cache");
//			response.setDateHeader("Expires",0);
//		}
//		RequestThreadLocal.remove();
//		ResponseThreadLocal.remove();
	}
	
}
