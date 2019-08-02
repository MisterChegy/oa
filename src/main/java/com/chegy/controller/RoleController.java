package com.chegy.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
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
import com.chegy.model.Operator;
import com.chegy.model.Role;
import com.chegy.model.vo.MenuVo;
import com.chegy.service.MenuService;
import com.chegy.service.RoleService;
import com.chegy.service.UserService;
import com.chegy.util.PageResultBean;
import com.chegy.util.ResultBean;

@Controller
@RequestMapping("/role")
public class RoleController {

	@Resource
	private RoleService roleService;

	@Resource
	private UserService userService;

	@Resource
	private MenuService menuService;

	@GetMapping("/index")
	public String index() {
		return "role/role-list";
	}

	// 用户列表
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public PageResultBean<Role> getList(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {

		Page<Role> roles = roleService.selectAllPage(page, limit);
		List<Role> lroles = roles.getContent();
		for (int i = 0; i < lroles.size(); i++) {
			lroles.get(i).setMenus(null);
			lroles.get(i).setUsers(null);
			lroles.get(i).setOperators(null);
		}

		return new PageResultBean<Role>(roles.getTotalElements(), lroles);
	}

	@GetMapping("{id}/own/menu")
	@ResponseBody
	public ResultBean findUserOwnMenu(@PathVariable("id") Integer id) {
		System.out.println("id = " + id);

		int[] menuIds = roleService.selectByRoleIdMenuTreeIds(id);

		return ResultBean.success("成功", menuIds);
	}

	@GetMapping("{id}/own/menu/tree")
	@ResponseBody
	public ResultBean findUserOwnMenuTree(@PathVariable("id") Integer id) {
		System.out.println("id = " + id);

		Set<Menu> menus = roleService.selectByRoleIdMenuTree(id);

		// 先存入list中
		List<Menu> list = new ArrayList<Menu>();
		for (Menu m : menus) {
			list.add(m);
		}
		// 消除重复menu
		for (Menu m : list) {
			for (Menu me : m.getChildren()) {
				if (menus.contains(me) == true) {
					menus.remove(me);
				}
			}
		}
		System.out.println("menus = " + menus);
		Set<MenuVo> menuVos = menuService.findAllOperator(menus);
		return ResultBean.success("成功", menuVos);
	}

	// 消除parent循环----懒加载不行
	public void clearParent(Menu m) {
		if (m == null) {
			return;
		}
		if (m.getChildren() == null) {
			return;
		}
		m.setParent(null);
		// 消除operator中menu循环 --- 懒加载
		for (Operator o : m.getOperators()) {
			o.setMenu(null);
		}
		for (Menu me : m.getChildren()) {
			clearParent(me);
		}
	}

	@GetMapping("{id}/own/operator")
	@ResponseBody
	public ResultBean findUserOwnOperator(@PathVariable("id") Integer id) {
		System.out.println("id = " + id);

		int[] operatorIds = roleService.selectOperatorIdsByRoleId(id);

		return ResultBean.success("成功", operatorIds);
	}

	@PostMapping("{id}/grant/menu")
	@ResponseBody
	public ResultBean addRoleGrantMenu(@PathVariable("id") Integer id, @RequestParam("menuIds[]") Integer[] menuIds) {

		System.out.println("id = " + id);
		roleService.addRoleGrantMenu(menuIds, id);

		return ResultBean.success("成功");
	}

	/// grant/operator---授权功能提交，并更新
	@PostMapping("{id}/grant/operator")
	@ResponseBody
	public ResultBean addGrantOperator(@PathVariable("id") Integer id,
			@RequestParam("operatorIds[]") Integer[] operatorIds) {
		System.out.println("============operatorIds=============");

		System.out.println("id = " + id);

		Set<Integer> oIds = new HashSet<>();
		for (Integer i : operatorIds) {
			oIds.add(i);
		}

		roleService.addGrantOperator(oIds, id);

		return ResultBean.success("成功");
	}

	@GetMapping
	public String toAddRole() {
		return "role/role-add";
	}

	@PostMapping
	@ResponseBody
	public ResultBean addAnRole(Role role) {
		System.out.println("role = " + role);

		boolean isSuccess = roleService.addNewRole(role);
		if (isSuccess == false) {
			return ResultBean.success("添加失败");
		}
		return ResultBean.success("添加成功");
	}

	// 修改角色页面
	@GetMapping("/{id}")
	public String updateRolePage(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("role", roleService.findById(id));
		return "role/role-add";
	}
	
	@PutMapping
	@ResponseBody
	public ResultBean updateRole(Role role) {
		
		roleService.updateRole(role);
		return ResultBean.success("修改成功");
	}
	
	//状态转换为正常
    @PostMapping("{id}/enable")
    @ResponseBody
    public ResultBean changeStatusToEnable(@PathVariable("id") Integer id) {
    	roleService.updateStatusById(id,1);
    	return ResultBean.success("正常");
    }
    
    //状态转换为锁死
    @PostMapping("{id}/disable")
    @ResponseBody
    public ResultBean changeStatusToDisable(@PathVariable("id") Integer id) {
    	
    	roleService.updateStatusById(id,0);
    	return ResultBean.success("锁死");
    }
    
    //删除角色
    @DeleteMapping("{id}")
    @ResponseBody
    public ResultBean deleteRole(@PathVariable("id") Integer id) {
    	
    	roleService.deleteRoleById(id);
    	return ResultBean.success("删除成功");
    }
}
