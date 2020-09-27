package com.maintain.controller.common;

import com.boyunmkt.utils.Result;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 */
@Controller
@ControllerAdvice
public class SimpleErrorController implements ErrorController {

	@RequestMapping("/error")
	public String error() {
		return "redirect:/index.html#/common/error";
	}

	@RequestMapping("/404")
	public String to404(){
		return "redirect:/index.html#/common/404";
	}

	@RequestMapping("/deny")
	public String deny(){
		return "forward:/views/common/deny.html";
	}

	/**
	 * Exception 统一处理
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Result handleException(Exception e) {
		e.printStackTrace();
		//Shiro的授权异常
		if(e instanceof UnauthorizedException) {
			return Result.fail("您没有权限");
		}else if(e instanceof IncorrectCredentialsException){
			return Result.fail("用户名或密码错误");
		}{ //其他业务异常
			return Result.fail(e.getMessage());
		}
	}

	@Override
	public String getErrorPath() {
		return null;
	}
}
