package com.chegy.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.chegy.model.Role;
import com.chegy.model.User;
import com.chegy.model.vo.UserDisplayVo;
import com.chegy.service.RoleService;
import com.chegy.service.UserService;
import com.chegy.util.PageResultBean;
import com.chegy.util.ResultBean;

@Controller
@RequestMapping("/user")
public class UserController {
	@Resource
    private UserService userService;
	
	@Resource
	private RoleService roleService;
	
	@GetMapping("/index")
    public String index() {
        return "user/user-list";
    }

	//用户列表
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public PageResultBean<UserDisplayVo> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "limit", defaultValue = "10")int limit) {
        Page<User> users = userService.selectAllWithDept(page, limit);
        List<User> lusers = users.getContent();
        
        //将lusers转换为userDisplayVos
        List<UserDisplayVo> userDisplayVos = toChangVos(lusers);
//        for(int i=0;i<lusers.size();i++) {
//        	lusers.get(i).setRoles(null);
//        	lusers.get(i).getUserinfo().setUsers(null);
//        }
        return new PageResultBean<UserDisplayVo>(users.getTotalElements(), userDisplayVos);

    }
    //将lusers转换为userDisplayVos
    public List<UserDisplayVo> toChangVos(List<User> lusers){
    	List<UserDisplayVo> vos = new ArrayList<>();
    	
    	for(User u : lusers) {
    		UserDisplayVo user = new UserDisplayVo();
    		user.setId(u.getId());
    		user.setEmail(u.getEmail());
    		user.setStatus(u.isStatus());
    		user.setUsername(u.getUsername());
    		//部门集
    		StringBuffer sb = new StringBuffer();
			
    		for(Depart d : u.getDeparts()) {
    			sb.append("["+ d.getOname() +"],");
    		}
    		if(sb!=null && sb.length()>0) {
    			sb.deleteCharAt(sb.length()-1);
    		}
    		//角色集
    		StringBuffer rb = new StringBuffer();
			
    		for(Role d : u.getRoles()) {
    			rb.append("["+ d.getName() +"],");
    		}
    		if(rb!=null && rb.length()>0) {
    			rb.deleteCharAt(rb.length()-1);
    		}
    		user.setRoleNames(rb.toString());
    		user.setDeptNames(sb.toString());
    		vos.add(user);
    	}
    	System.out.println("vos = "+vos);
    	return vos;
    }
    
    //用户添加或编辑页面
    @GetMapping("{id}")
    public String update(@PathVariable("id") Integer id,Model model) {
    	
    	System.out.println("roles = "+roleService.selectAll());
        model.addAttribute("roles", roleService.selectAll());
        
        //编辑页面
        if(id != null) {
        	 User user = userService.findById(id);
        	 System.out.println("user = "+user);
        	 
        	 model.addAttribute("user", user);
        }
        return "user/user-add";
    }
    
    //用户修改
    @PutMapping
    @ResponseBody
    public ResultBean updateUser(User user,@RequestParam("role[]") Integer[] role,@RequestParam("departIds[]") Integer[] departIds) {
    	
    	userService.addUser(user,role,departIds);
    	return ResultBean.success("修改成功");
    }
    
    //用户添加
    @GetMapping
    public String add(Model model) {
    	
        model.addAttribute("roles", roleService.selectAll());
        
        return "user/user-add";
    }
    
    //添加用户
    @PostMapping
    @ResponseBody
    public ResultBean addUser(User user,@RequestParam("role[]") Integer[] role,@RequestParam("departIds[]") Integer[] departIds) {
    	//departIds
    	
    	userService.addUser(user,role,departIds);
    	return ResultBean.success("添加用户成功");
    }
    
    //重置密码页面
    @GetMapping("{id}/reset")
    public String updatePasswordPage(@PathVariable("id") Integer id) {
    	
    	return "user/user-reset-pwd";
    }
    
    //重置密码
    @PostMapping("{id}/reset")
    @ResponseBody
    public ResultBean updatePassword(@PathVariable("id") Integer id,@RequestParam String password) {
    	
    	System.out.println("updatePassword----------id = "+id);
    	System.out.println("password = "+password);
    	
    	userService.updatePassword(id,password);
    	
    	return ResultBean.success("重置密码成功");
    }
    
    //状态转换为正常
    @PostMapping("{id}/enable")
    @ResponseBody
    public ResultBean changeStatusToEnable(@PathVariable("id") Integer id) {
    	userService.updateStatusById(id,1);
    	return ResultBean.success("正常");
    }
    
    //状态转换为锁死
    @PostMapping("{id}/disable")
    @ResponseBody
    public ResultBean changeStatusToDisable(@PathVariable("id") Integer id) {
    	
    	userService.updateStatusById(id,0);
    	return ResultBean.success("锁死");
    }
    
    //删除用户
    @DeleteMapping("{id}")
    @ResponseBody
    public ResultBean deleteUser(@PathVariable("id") Integer id) {
    	
    	userService.deleteUserById(id);
    	return ResultBean.success("删除成功");
    }
    
}
