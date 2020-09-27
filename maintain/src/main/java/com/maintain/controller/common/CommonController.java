package com.maintain.controller.common;

import com.boyunmkt.utils.NetUtil;
import com.boyunmkt.utils.NewOSSUtil;
import com.boyunmkt.utils.Result;
import com.boyunmkt.utils.VerifyCodeUtil;
import com.maintain.common.Constant;
import com.maintain.util.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用的
 */
@Controller
public class CommonController {

	/**
	 * 文件上传
	 * @param file 文件的name
	 */
	@RequestMapping(value = "/uploadFile")
	@ResponseBody
	public Map<String, Object> uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(name = "isPrivate", defaultValue = "false") boolean isPrivate) {
		Map<String, Object> map = new HashMap<>();
		try {
			String mime = file.getContentType();
			String key;
			if(isPrivate) {
				key = NewOSSUtil.getPrivateInstance().put(file.getBytes(), mime, "/" + WebUtil.getLoginedUserId() + "/private/");
			}else {
				key = NewOSSUtil.getPublicReadInstance().put(file.getBytes(), mime);
			}
			map.put("code", 0);
			map.put("key", key);
			map.put("size", file.getSize());
			map.put("mime", file.getContentType());
		} catch (Exception e) {
			map.put("code", -1);
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 文件下载
	 * @param resp
	 */
	@RequestMapping(value = "/getFile/**")
	public void getFile(HttpServletResponse resp) {
		try {
			String uri = WebUtil.getCurrentRequest().getRequestURI();
//			if(uri.contains(WebUtil.getLoginedUserId())) {
			String key = uri.substring(uri.indexOf("getFile/") + 8);
			String url = NewOSSUtil.getPrivateInstance().getTempPrivateUrl(key);
			byte[] bs = NetUtil.getByteArrByUrl(url);
			resp.getOutputStream().write(bs);
//			}else {
//				resp.getOutputStream().write("have no right".getBytes());
//			}

		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	@RequestMapping(value = "/getVerifyCode")
	public void getVerificationCode(HttpServletResponse resp) {
		try {
			int verifySize = 4;
			String code = VerifyCodeUtil.generateVerifyCode(verifySize);
			WebUtil.getCurrentSession().setAttribute(Constant.SESSION_VALIDATE_CODE, code);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			VerifyCodeUtil.outputImage(116, 36, os, code);
			resp.getOutputStream().write(os.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证验证码是否正确
	 * @param code
	 * @return
	 */
	@RequestMapping("/checkVerifyCode")
	@ResponseBody
	public Result checkVerifyCode(String code){
		try {
			boolean bool = WebUtil.getCurrentVerifyCode().toLowerCase().equals(code.toLowerCase());
			return Result.success().setData(bool);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.fail(e.getMessage());
		}
	}

}
