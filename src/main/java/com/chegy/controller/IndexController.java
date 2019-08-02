package com.chegy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chegy.model.Menu;
import com.chegy.model.Operator;
import com.chegy.model.Role;
import com.chegy.model.User;
import com.chegy.service.MenuService;

@Controller
public class IndexController {
	
	@Resource
	private MenuService menuService;
	
	@RequestMapping({ "/", "/index" })
	public String index(Model model,HttpSession session) {

		User user = (User) session.getAttribute("user");
		System.out.println("user = "+user);
		
		if(user == null) {
			return "login";
		}
		//菜单列表
		List<Menu> menuTreeVOS = menuService.selectCurrentUserMenuTree(user);
        model.addAttribute("menus", menuTreeVOS);
        
        //功能权限
        Map<String,Boolean> oper = new HashMap<>();
        
        Set<Role> roles = user.getRoles();
		for(Role r : roles) {
			if(r.isStatus()) {
				Set<Operator> operators = r.getOperators();

				putPermsToMap(operators,oper);
			}
			
		}
		System.out.println("oper = "+oper);
        session.setAttribute("oper", oper);
		
		
        return "index";
	}

	private void putPermsToMap(Set<Operator> operators, Map<String, Boolean> oper) {
		// TODO Auto-generated method stub
		for(Operator o : operators) {
			permsToKey(o.getPerms(),oper);
			
		}
	}

	private void permsToKey(String perms, Map<String, Boolean> oper) {
		// TODO Auto-generated method stub
		//user:add
		String[] part = perms.split(":");
		String key = "";
		
		for(String i : part) {
			key += i;
		}
		//useradd
		oper.put(key, true);
	}
	
	
}
