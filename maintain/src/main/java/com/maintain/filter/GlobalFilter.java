package com.maintain.filter;

import com.maintain.util.RequestThreadLocal;
import com.maintain.util.ResponseThreadLocal;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GlobalFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req =  (HttpServletRequest) request;
		HttpServletResponse resp =  (HttpServletResponse) response;
		RequestThreadLocal.set(req);
		ResponseThreadLocal.set((resp));

//		System.out.println("set req and resp");

		String uri = req.getRequestURI();

//		System.out.println("uri->" + uri);

		chain.doFilter(new XssSpringHttpServletRequestWrapper(req), response);
		RequestThreadLocal.remove();
		ResponseThreadLocal.remove();

//		System.out.println("remove req and resp");
	}

	@Override
	public void destroy() {
		
	}

}
