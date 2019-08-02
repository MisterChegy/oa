package com.chegy.service.impl;

import java.util.ArrayList;
import java.util.Date;
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

import com.chegy.dao.MenuDao;
import com.chegy.dao.OperatorDao;
import com.chegy.dao.RoleDao;
import com.chegy.model.Menu;
import com.chegy.model.Operator;
import com.chegy.model.Role;
import com.chegy.service.RoleService;
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService{

	@Resource
	private RoleDao roleDao;
	
	@Resource
	private MenuDao menuDao;
	
	@Resource
	private OperatorDao operatorDao;
	@Override
	public List<Role> selectAll() {
		// TODO Auto-generated method stub
		return roleDao.findAll();
	}

	@Override
	public Page<Role> selectAllPage(int page, int limit) {
		Sort sort = new Sort(Direction.DESC, "id");
		PageRequest pageable = PageRequest.of(page-1, limit, sort);
		
		return roleDao.findAll(pageable);
	}

	@Override
	public Set<Menu> selectByRoleIdMenuTree(Integer id) {
		Role role = roleDao.getOne(id);
		Set<Menu> menus = role.getMenus();
		
		//消除parent循环----懒加载不行
		for(Menu m : menus) {
			clearParent(m);
			
		}
		
		return menus;
		
	}

	//消除parent循环----懒加载不行
	public void clearParent(Menu m) {
		if(m == null) {
			return ;
		}
//		if(m.getChildren() == null) {
//			return ;
//		}
		m.setParent(null);
		//消除operator中menu循环 --- 懒加载
		for(Operator o : m.getOperators()) {
			o.setMenu(null);
		}
		for(Menu me : m.getChildren()) {
			clearParent(me);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void addRoleGrantMenu(Integer[] menuIds,Integer id) {
		// TODO Auto-generated method stub
		if(menuIds == null || id == null) {
			return ;
		}
		Role role = roleDao.getOne(id);
		if(role == null) {
			return ;
		}
		//根据menuIds查询出要更新的权限菜单
		List<Integer> list=new ArrayList<Integer>();
		for(Integer i : menuIds) {
			
			list.add(i);
		}
		System.out.println("list = "+list);
		List<Menu> menus = menuDao.findAllById(list);
		
		//去掉重复的menu---一级菜单经过set会去掉重复，二级菜单看他的一级菜单是否在集合中
		Set<Menu> setMenus = new HashSet<Menu>();
		for(Menu m : menus) {
			setMenus.add(m);
		}
		//role更新菜单权限
		role.setMenus(setMenus);
		
		//更新数据库的role
		if(role != null) {
			roleDao.save(role);
		}
	}

	@Override
	public int[] selectOperatorIdsByRoleId(Integer id) {
		Role role = roleDao.getOne(id);
		
		Set<Operator> opers = new HashSet<>();
		for(Operator o : role.getOperators()) {
			opers.add(o);
		}
		int[] results = new int[opers.size()];
		int i = 0;
		for(Operator o : opers) {
			results[i] = o.getOperatorId();
			i++;
		}
		return results;
	}

	//根据id查询出Role,然后根据Ids查询出要更新的该用户下的操作列表，合并，插入数据库
	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void addGrantOperator(Set<Integer> operatorIds, Integer id) {
		
		if(operatorIds == null || id == null) {
			return ;
		}
		Role role = roleDao.getOne(id);
		if(role == null) {
			return ;
		}
		//根据operatorIds查询出要更新的权限菜单
		List<Integer> list = new ArrayList<Integer>();
		for(Integer i : operatorIds) {
			list.add(i);
		}
		
		List<Operator> operators = operatorDao.findAllById(list);
		
		Set<Operator> opers = new HashSet<>();
		for(Operator o : operators) {
			opers.add(o);
		}
		role.setOperators(opers);
		
		//更新数据库的role
		if(role != null) {
			roleDao.save(role);
		}
	}

	@Override
	public int[] selectByRoleIdMenuTreeIds(Integer id) {
		
		Set<Menu> setMenus = selectByRoleIdMenuTree(id);
//		setMenus-->menuTreeVos
		//消除parent循环----懒加载不行
		for(Menu m : setMenus) {
			clearParent(m);
			
		}
		
		int[] menuIds = new int[setMenus.size()];
		
		//获得列表id
		int i=0;
		for(Menu m : setMenus) {
			menuIds[i] = m.getId();
			i++;
		}
		System.out.println("menuIds = "+menuIds);
		return menuIds;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public boolean addNewRole(Role role) {
		if(role == null) {
			return false;
		}
		Date date = new Date();// 获取当前时间 
		
		role.setCreatedTime(date);
		role.setUpdatedTime(date);
		role.setStatus(true);
		
		Role result = roleDao.save(role);
		if(result == null) {
			return false;
		}
		return true;
	}

	@Override
	public Set<Menu> selectByRoleIdFirstMenuTree(Integer id) {
//		Role role = roleDao.getOne(id);
//		Set<Menu> menus = role.getMenus();
//		
//		//消除parent循环----懒加载不行
//		for(Menu m : menus) {
//			
//			if(m.getParent() != null) {
//				menus.remove(m);
//			}else {
//				clearParent(m);
//				
//			}
//			
//		}
//		
		return null;
		
	}

	@Override
	public Role findById(Integer id) {
		if(id == null) {
			return null;
		}
		return roleDao.getOne(id);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void updateRole(Role role) {
		// TODO Auto-generated method stub
		if(role == null || role.getId() == null) {
			return ;
		}
		Role getRole = roleDao.getOne(role.getId());
		getRole.setUpdatedTime(new Date());
		getRole.setName(role.getName());
		getRole.setRemark(role.getRemark());
		
		roleDao.save(getRole);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void updateStatusById(Integer id, int i) {
		roleDao.save1(id,i);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void deleteRoleById(Integer id) {
		if(id == null) {
			return ;
		}
		Role role = roleDao.getOne(id);
		role.setDeparts(null);
		role.setMenus(null);
		role.setOperators(null);
		role.setUsers(null);
		if(role != null) {
			role = roleDao.save(role);
			
			roleDao.delete(role);
		}
		
	}

}
