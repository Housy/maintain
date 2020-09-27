package com.maintain.util;

import javax.servlet.http.HttpServletResponse;

/**
 * 方便在service和其他地方获取响应对象
 * @author xujiantao
 *
 */
public class ResponseThreadLocal {
	
	private static ThreadLocal<HttpServletResponse> tlResponse = new ThreadLocal<>();
	
	public static HttpServletResponse get() {
		return tlResponse.get();
	}
	
	public static void set(HttpServletResponse request) {
		tlResponse.set(request);
	}
	
	public static void remove() {
		tlResponse.remove();
	}
}
