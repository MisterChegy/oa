package com.chegy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.chegy.model.Menu;
import com.chegy.model.vo.MenuVo;
import com.chegy.service.MenuService;
import com.chegy.util.PageResultBean;
import com.chegy.util.ResultBean;

@Controller
@RequestMapping("/menu")
public class MenuController {

	@Resource
	private MenuService menuService;

	// 菜单列表
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageResultBean<MenuVo> getList(@RequestParam(required = false) Integer parentId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {

//		Page<Menu> menus = menuService.selectAllWithMenu(page, limit);

		// 将mneus里面用到的信息放到vo中
//		List<MenuVo> vos = modelsToVos(menus.getContent());
//		return new PageResultBean<MenuVo>(menus.getTotalElements(), vos);
		if (parentId == 0) {
			parentId = null;
		}
		List<Menu> menus = menuService.findListByParentId(parentId);
		List<MenuVo> vos = modelsToVos(menus);
		return new PageResultBean<MenuVo>(menus.size(), vos);
	}

	private List<MenuVo> modelsToVos(List<Menu> menus) {
		List<MenuVo> vos = new ArrayList<>();
		for (Menu m : menus) {
			MenuVo v = new MenuVo();

			v.setIcon(m.getIcon());
			v.setId(m.getId());
			v.setName(m.getMenuName());
			v.setOrderNum(m.getId());
			v.setPerms(m.getPerms());
			v.setUrl(m.getUrl());
			vos.add(v);
		}
		return vos;
	}

	// 菜单树 /root/operator
	@GetMapping("/tree")
	@ResponseBody
	public ResultBean departTree() {
		// 完全菜单树--不带操作
		Set<Menu> menuTree = menuService.findAll(null);

		// menuTree 转换到 MenuVos里
//		List<MenuVo> vos = menusToVos(menuTree);

		return ResultBean.success("成功", menuTree);
	}

	// 菜单树 /root/operator
	@GetMapping("/tree/root/operator")
	@ResponseBody
	public ResultBean departTreeOperators() {
		// 完全菜单树
		Set<Menu> menuTree = menuService.findAll("operator");

		System.out.println("menuTree = " + menuTree);
		return ResultBean.success("成功", menuTree);
	}

	// 授权功能-展示的菜单-功能菜单 :operator/tree
	@GetMapping("/operator/tree")
	@ResponseBody
	public ResultBean menuAndOperatorTree() {
		// 完全菜单树
		Set<MenuVo> menuTree = menuService.findAllOperator();

		return ResultBean.success("成功", menuTree);
	}

	@GetMapping("/index")
	public String index() {
		return "menu/menu-list";
	}

	@GetMapping("/parent/{parentId}")
	public String addMenuPage(@PathVariable("parentId") Integer parentId,Model model) {
		if(parentId != 0) {
			model.addAttribute("parentId", parentId);
		}
		
		return "menu/menu-add";
	}

	// 添加菜单
	@PostMapping
	@ResponseBody
	public ResultBean addMenu(Menu menu, @RequestParam("parentId") Integer parentId) {
		if (menu == null) {
			return ResultBean.success("添加失败");
		}
		//非一级菜单
		Menu parent = null;
		if(parentId != null && !parentId.equals("")) {
			parent = menuService.findMenuById(parentId);
		}
		menu.setParent(parent);

		menuService.addMenu(menu);
		return ResultBean.success("添加成功");
	}

	// 修改部门
	@GetMapping("{id}")
	public String updateDepartPage(@PathVariable("id") Integer id, Model model) {

		Menu menu = menuService.findMenuById(id);
		model.addAttribute("menu", menu);
		
		if(menu != null && menu.getParent()!=null) {
			model.addAttribute("parentId", menu.getParent().getId());
		}
		return "menu/menu-add";
	}

	@PutMapping
	@ResponseBody
	public ResultBean updateMenu(Menu menu, @RequestParam("parentId") Integer parentId) {

		menuService.updateMenu(menu,parentId);
		return ResultBean.success("修改部门成功");
	}

	// 删除部门
	@DeleteMapping("{id}")
	@ResponseBody
	public ResultBean deleteDepart(@PathVariable("id") Integer id) {

		menuService.deleteMenuById(id);
		return ResultBean.success("删除成功");
	}

}
