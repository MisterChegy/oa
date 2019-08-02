package com.chegy.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chegy.model.User;
import com.chegy.service.UserService;
import com.chegy.util.ResultBean;


@Controller
public class LoginController {
	
	@Resource
	private UserService userService;
	
//	@RequestMapping({ "/", "/index" })
//	public String index(Model model) {
//		
//		return "index";
//	}

	@RequestMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@PostMapping("/login2")
	@ResponseBody
	public ResultBean login(User user,HttpSession session) throws Exception {
		System.out.println("/login2------user = "+user);
		
		if(user == null) {
			return ResultBean.success("用户未登录");
		}
		//判断账号密码是否正确
		User findUser = userService.findByUsername(user.getUsername());
		System.out.println("findUser = "+findUser);
		if(findUser == null) {
			return ResultBean.success("用户名不存在");
		}else {
			if(findUser.getPassword().equals(user.getPassword())==true) {
				if(findUser.isStatus()) {
					session.setAttribute("user", findUser);
					
					return ResultBean.success("登录成功");
				}else {
					return ResultBean.success("该账户已经被锁死");
				}
				
			}else {
				return ResultBean.success("密码错误");
			}
		}
		
	}

	@RequestMapping("/403")
	public String unauthorizedRole() {
		System.out.println("------没有权限-------");
		return "403";
	}

	//退出登录
    @GetMapping("/logout")
    public String logout(HttpSession session) {
//    	session.setAttribute("user", null);
    	//销毁session
    	session.removeAttribute("user");
		session.invalidate();
    	return "login";
    }
    
    //注册页面
    @GetMapping("/register")
    public String signUp() {
    	return "register";
    }
    
    //注册
    @PostMapping("/register")
    @ResponseBody
    public ResultBean register(User user) {
    	
    	userService.addUser(user, null, null);
		return ResultBean.success("注册成功");
    }
}