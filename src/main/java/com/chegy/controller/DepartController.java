package com.chegy.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chegy.model.Depart;
import com.chegy.model.vo.DepartVo;
import com.chegy.service.DepartService;
import com.chegy.util.PageResultBean;
import com.chegy.util.ResultBean;

@Controller
@RequestMapping("/depart")
public class DepartController {
	
	@Resource
	private DepartService departService;
	
	@GetMapping("/tree")
	@ResponseBody
	public ResultBean departTree() {
		
		//部门列表
		List<Depart> departTree = departService.findAll();
		
		return ResultBean.success("成功",departTree);
	}
	
	@GetMapping("/index")
	public String idnex() {
		return "dept/dept-list";
	}
	
	//部门列表
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageResultBean<DepartVo> getList(@RequestParam(required = false) Integer parentId,@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "limit", defaultValue = "10")int limit) {
    
//    	Page<Depart> departs = departService.selectAllWithDept(page, limit);
//        
//    	//将departs里面用到的信息放到vo中
//    	List<DepartVo> vos = departsToVos(departs.getContent());
//    	return new PageResultBean<DepartVo>(departs.getTotalElements(), vos);
    
    	//不需要分页
    	if(parentId == 0) {
			parentId = null;
		}
    	List<Depart> departs = departService.findListByParentId(parentId);
    	List<DepartVo> vos = departsToVos(departs);
    	
    	return new PageResultBean<DepartVo>(vos.size(),vos);
    }

	private List<DepartVo> departsToVos(List<Depart> departs) {
		
		List<DepartVo> vos = new ArrayList<DepartVo>();
		for(Depart d : departs) {
			DepartVo v = new DepartVo();
			v.setId(d.getId());
			v.setOname(d.getOname());
			v.setOrderNum(d.getId());
			v.setDescription(d.getDescription());
			v.setStatus(d.getStatus());
			vos.add(v);
		}
		return vos;
	}
	
	@GetMapping
	public String addDepartPage() {
		return "dept/dept-add";
	}
	
	//添加部门
	@PostMapping
	@ResponseBody
	public ResultBean addDepart(Depart depart,@RequestParam("parentId") Integer parentId) {
		
		System.out.println("depart = "+depart);
		System.out.println("parentId = "+parentId);
		
		departService.addDepart(depart,parentId);
		return ResultBean.success("添加部门成功");
	}
	
	//修改部门
	@GetMapping("/{id}")
	public String updateDepartPage(@PathVariable("id") Integer id,Model model) {
		
		model.addAttribute("depart", departService.findById(id));
		return "dept/dept-add";
	}
	
	@PutMapping
	@ResponseBody
	public ResultBean updateDepart(Depart depart,@RequestParam("parentId") Integer parentId) {
		
		System.out.println("updateDepart---------depart = "+depart);
		departService.addDepart(depart,parentId);
		return ResultBean.success("修改部门成功");
	}
	
	//删除部门
    @DeleteMapping("{id}")
    @ResponseBody
    public ResultBean deleteDepart(@PathVariable("id") Integer id) {
    	
    	departService.deleteDepartById(id);
    	return ResultBean.success("删除成功");
    }
    
}
