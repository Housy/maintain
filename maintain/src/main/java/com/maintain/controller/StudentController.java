package com.maintain.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maintain.po.Student;
import com.maintain.service.StudentService;
import com.boyunmkt.utils.PageInfo;
import com.boyunmkt.utils.PageResult;
import com.boyunmkt.utils.Params;
import com.boyunmkt.utils.Result;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	
	/**
	 * 服务的变量命名统一为service
	 */
	@Resource
	private StudentService service;
	
	/**
	 * 分页查询
	 * @param query
	 * @param pi
	 * @return
	 */
	@RequestMapping("/getByPage")
	public Map<String, Object> getByPage(Student query, PageInfo<Student> pi){
		PageResult<Student> pr = pi.toPageResult();
		pr = service.findByCustom(query, pr);
		return pr.toTableData();
	}
	
	
	/**
	 * 根据id查询
	 * @param p
	 * @return
	 */
	@RequestMapping("/getById")
	public Result getById(Params p){
		return Result.success().setData(service.findById(p.getId()));
	}
	
	/**
	 * 添加
	 * @param obj
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(Student obj){
		service.add(obj);
		return Result.success();
	}
	
	/**
	 * 修改
	 * @param obj
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(Student obj){
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
		Student old = service.findById(p.getId());
		old.setSortCount(value);
		service.update(old);
		return Result.success();
	}
	
	/**
	 * 删除单条
	 * @param p
	 * @return
	 */
//	@RequestMapping("/deleteById")
	public Result deleteById(Params p){
		service.delete(p.getId());
		return Result.success();
	}
	
	/**
	 * 批量删除
	 * @param p
	 * @return
	 */
//	@RequestMapping("/deleteByIds")
	public Result deleteByIds(Params p){
		service.deleteAll(p.getIds());
		return Result.success();
	}
	
}
