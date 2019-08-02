package com.chegy.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.chegy.dao.DepartDao;
import com.chegy.dao.RoleDao;
import com.chegy.dao.UserDao;
import com.chegy.model.Depart;
import com.chegy.model.Role;
import com.chegy.model.User;
import com.chegy.service.UserService;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{
	
	@Resource
    private UserDao userDao;
	
	@Resource
	private RoleDao roleDao;
	
	@Resource
	private DepartDao departDao;
	
    @Override
    public User findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername()");
        return userDao.findByUsername(username);
    }

	@Override
	public Page<User> selectAllWithDept(int page, int limit) {
		Sort sort = new Sort(Direction.DESC, "id");
		PageRequest pageable = PageRequest.of(page-1, limit, sort);
	    Page<User> pusers = userDao.findAll(pageable);
		
	    System.out.println("pusers = "+pusers.getContent());
	    return pusers;
	}

	@Override
	public User findById(Integer id) {
		
//		Optional<User> user = userDao.findById(id);
		User user = userDao.getOne(id);
		System.out.println("user = "+ user);
//		if(user.isPresent() == false) {
//			return null;
//		}
//
//		return user.get();
		return user;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void updatePassword(Integer id, String password) {
		// TODO Auto-generated method stub
		User user = userDao.getOne(id);
		if(user == null) {
			return ;
		}
		user.setPassword(password);
		
		userDao.save(user);
	}

	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void addUser(User user, Integer[] role, Integer[] departIds) {
		if(user == null) {
			return ;
		}
		//修改
		if(user.getId()!=null) {
			User formUser = user;
			user = userDao.getOne(user.getId());
			
			user.setEmail(formUser.getEmail());
			user.setPassword(formUser.getPassword());
			user.setStatus(formUser.isStatus());
			user.setUsername(formUser.getUsername());
		}
		//role->List<Integer>
		if(role != null) {
			Set<Role> sroles = new HashSet<>();
			List<Integer> list=new ArrayList<Integer>();
			for(Integer i : role) {
				
				list.add(i);
			}
			//查找roles
			List<Role> roles = roleDao.findAllById(list);
			
			sroles.addAll(roles);
			user.setRoles(sroles);
		}
		
		//departs
		if(departIds != null) {
			Set<Depart> sdeparts = new HashSet<>();
			List<Integer> list = new ArrayList<Integer>();
			for(Integer i : departIds) {
				
				list.add(i);
			}
			
			List<Depart> departs = departDao.findAllById(list);
			
			sdeparts.addAll(departs);
			user.setDeparts(sdeparts);
		}
		
		//加入到user
//		user.setDeparts(sdeparts);
//		user.setRoles(sroles);
		if(user != null) {
			userDao.save(user);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void deleteUserById(Integer id) {
		// TODO Auto-generated method stub
		User user = userDao.getOne(id);
		if(user != null) {
			userDao.delete(user);
		}
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void updateStatusById(Integer id, int i) {
		// TODO Auto-generated method stub
		User user = userDao.getOne(id);
		if(user == null) {
			return ;
		}
		user.setStatus((i == 1) ? false : true);
		System.out.println("user = "+ user);
		userDao.save1(i,id);
	}

}