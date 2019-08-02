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

import com.chegy.model.Menu;
import com.chegy.model.Operator;
import com.chegy.model.vo.OperatorVo;
import com.chegy.service.MenuService;
import com.chegy.service.OperatorService;
import com.chegy.util.PageResultBean;
import com.chegy.util.ResultBean;

@Controller
@RequestMapping("/operator")
public class OperatorController {

	@Resource
	private OperatorService operatorService;

	@Resource
	private MenuService menuService;

	@GetMapping("/index")
	public String index() {
		return "operator/operator-list";
	}

	// 操作列表
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageResultBean<OperatorVo> getList(@RequestParam(required = false) Integer menuId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {

//		Page<Operator> operators = operatorService.selectAllWithOperator(page, limit,menuId);
//
//		// 将mneus里面用到的信息放到vo中
//		List<OperatorVo> vos = modelsToVos(operators.getContent());
//		return new PageResultBean<OperatorVo>(operators.getTotalElements(), vos);
		if (menuId == null || menuId == 0) {
			return new PageResultBean<OperatorVo>();
		}
		List<Operator> operators = operatorService.findListByMenuId(menuId);
		List<OperatorVo> vos = modelsToVos(operators);

		return new PageResultBean<OperatorVo>(vos.size(), vos);
	}

	private List<OperatorVo> modelsToVos(List<Operator> operators) {
		List<OperatorVo> vos = new ArrayList<>();
		for (Operator o : operators) {
			OperatorVo v = new OperatorVo();
			v.setCreateTime(o.getCreateTime());
			v.setHttpMethod(o.getHttpMethod());
			v.setModifyTime(o.getModifyTime());
			v.setOperatorId(o.getOperatorId());
			v.setOperatorName(o.getOperatorName());
			v.setUrl(o.getUrl());
			v.setPerms(o.getPerms());
			vos.add(v);
		}

		return vos;
	}

	@GetMapping("add/{menuId}")
	public String addOperatorPage(@PathVariable("menuId") Integer menuId, Model model) {
		if (menuId != 0) {
			model.addAttribute("menuId", menuId);
		}

		return "operator/operator-add";
	}

	// 添加功能
	@PostMapping
	@ResponseBody
	public ResultBean addOperator(Operator operator, @RequestParam("menuId") Integer menuId) {
		if (operator == null || menuId == null) {
			return ResultBean.success("添加失败");
		}

		Menu parent = menuService.findMenuById(menuId);
		operator.setMenu(parent);

		operatorService.addOperator(operator);

		return ResultBean.success("添加成功");
	}

	// 修改功能
	@GetMapping("{id}")
	public String updateOperPage(@PathVariable("id") Integer id, Model model) {

		Operator operator = operatorService.findOperById(id);
		if(operator != null) {
			model.addAttribute("operator", operator);
		}
		

		if (operator != null && operator.getMenu()!=null) {
			model.addAttribute("menuId", operator.getMenu().getId());
		}
		return "operator/operator-add";
	}

	@PutMapping
	@ResponseBody
	public ResultBean updateOperator(Operator operator, @RequestParam("menuId") Integer menuId) {

		operatorService.updateOperator(operator, menuId);
		return ResultBean.success("修改成功");
	}

	// 删除部门
	@DeleteMapping("{id}")
	@ResponseBody
	public ResultBean deleteOperator(@PathVariable("id") Integer id) {

		operatorService.deleteOperatorById(id);
		return ResultBean.success("删除成功");
	}

}
