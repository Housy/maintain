package com.maintain.controller;

import com.boyunmkt.mongodb.core.DTOUtil;
import com.boyunmkt.utils.PageInfo;
import com.boyunmkt.utils.PageResult;
import com.boyunmkt.utils.Params;
import com.boyunmkt.utils.Result;
import com.maintain.po.AdminUser;
import com.maintain.service.AdminUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/adminUser")
public class AdminUserController {
	
	/**
	 * 服务的变量命名统一为service
	 */
	@Resource
	private AdminUserService service;
	
	/**
	 * 分页查询
	 * @param query
	 * @param pi
	 * @return
	 */
	@RequestMapping("/getByPage")
	public Map<String, Object> getByPage(AdminUser query, PageInfo<AdminUser> pi){
		PageResult<AdminUser> pr = pi.toPageResult();
		pr = service.findByCustom(query, pr);
		return DTOUtil.toDTOTableData(pr);
	}
	
	
	/**
	 * 根据id查询
	 * @param p
	 * @return
	 */
	@RequestMapping("/getById")
	public Result getById(Params p){
		return Result.success().setData(service.findById(p.getId()).toDTO());
	}
	
	/**
	 * 添加
	 * @param obj
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(AdminUser obj){
		service.save(obj);
		return Result.success();
	}
	
	/**
	 * 修改
	 * @param obj
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(AdminUser obj){
		service.update(obj);
		return Result.success();
	}
	
	/**
	 * 修改排序字段
	 * 
	 * 命名规则为：update + 你要修改的字段名(首字母大写)
	 * @param p 公共参数
	 * @param value 你要修改的字段的值
	 * @return
	 */
	@RequestMapping("/updateSortCount")
	public Result update(Params p, Integer value){
		AdminUser old = service.findById(p.getId());
		old.setSortCount(value);
		service.save(old);
		return Result.success();
	}

	@RequestMapping("/updatePhone")
	public Result updatePhone(Params p, String value){
		AdminUser old = service.findById(p.getId());
		old.setPhone(value);
		service.save(old);
		return Result.success();
	}

	@RequestMapping("/updateIsDisabled")
	public Result updateIsDisabled(Params p, Integer isDisabled){
		AdminUser old = service.findById(p.getId());
		old.setIsDisabled(isDisabled);
		service.save(old);
		return Result.success();
	}

	@RequestMapping("/updateIsVip")
	public Result updateIsVip(Params p, Integer isVip){
		AdminUser old = service.findById(p.getId());
		old.setIsVip(isVip);
		service.save(old);
		return Result.success();
	}

	@RequestMapping("/enable")
	public Result enable(Params p){
		service.enable(p.getId());
		return Result.success();
	}

	@RequestMapping("/disable")
	public Result disable(Params p){
		service.disable(p.getId());
		return Result.success();
	}
	
	/**
	 * 删除单条
	 * @param p
	 * @return
	 */
	@RequestMapping("/deleteById")
	public Result deleteById(Params p){
		service.delete(p.getId());
		return Result.success();
	}
	
	/**
	 * 批量删除
	 * @param p
	 * @return
	 */
	@RequestMapping("/deleteByIds")
	public Result deleteByIds(Params p){
		service.deleteAll(p.getIds());
		return Result.success();
	}

}
