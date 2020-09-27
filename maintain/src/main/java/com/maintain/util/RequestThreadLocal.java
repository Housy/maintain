package com.maintain.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 方便在service和其他地方获取请求对象
 * @author xujiantao
 *
 */
public class RequestThreadLocal {
	
	private static ThreadLocal<HttpServletRequest> tlRequest = new ThreadLocal<>();
	
	public static HttpServletRequest get() {
		return tlRequest.get();
	}
	
	public static void set(HttpServletRequest request) {
		tlRequest.set(request);
	}
	
	public static void remove() {
		tlRequest.remove();
	}
	
}
