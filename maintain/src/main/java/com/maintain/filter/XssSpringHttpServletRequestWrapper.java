package com.maintain.filter;

import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssSpringHttpServletRequestWrapper extends HttpServletRequestWrapper{

	public XssSpringHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if(values == null) return null;
		
		String[] newValues = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			newValues[i] = HtmlUtils.htmlEscape(values[i]);
		}
		return newValues;
	}
	
	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);  
		if(value == null) return null;
		
		return HtmlUtils.htmlEscape(value);
	}
	
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if(value == null)  return null;
		return HtmlUtils.htmlEscape(value);  
	}
	
}
