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

import com.chegy.dao.MenuDao;
import com.chegy.dao.RoleDao;
import com.chegy.model.Menu;
import com.chegy.model.Operator;
import com.chegy.model.Role;
import com.chegy.model.User;
import com.chegy.model.vo.MenuVo;
import com.chegy.service.MenuService;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

	@Resource
	private MenuDao menuDao;

	@Resource
	private RoleDao roleDao;

	/**
	 * 获取当前登陆用户拥有的树形菜单 (admin 账户拥有所有权限.)
	 */
	public List<Menu> selectCurrentUserMenuTree(User user) {
		List<Menu> menus = new ArrayList<>();

		System.out.println("user = " + user);

		if (user.getRoles() == null) {
			return null;
		}
		
		List<Role> roles = new ArrayList<>();
		for(Role r : user.getRoles()) {
			//正常用户被加入
			if(r.isStatus()) {
				roles.add(r);
			}
			
		}
		selectMenuTreeByRole(roles, menus);
		return menus;
	}

	public void selectMenuTreeByRole(List<Role> roles, List<Menu> menus) {

		// 菜单树----根据角色来查询出该角色下的菜单树
		for (Role r : roles) {
			// 权限-菜单

			for (Menu m : r.getMenus()) {
				if (m.getParent() == null) {
					m.setParent(null);
					menus.add(m);
				}
			}

		}

	}

	@Override
	public Set<Menu> findAll(String operator) {

		Set<Menu> menus = menuDao.findByParentIsNullOrderByIdDesc();

//		if (operator == null) {
//			return menus;
//		}
		Set<Menu> menuls = new HashSet<>();

		// 遍历Menus，设置Menu为空
		for (Menu d : menus) {
			setMenuIsNull(d, operator, null);

			menuls.add(d);
		}

		return menuls;
	}

	@Override
	public Set<MenuVo> findAllOperator() {

		return findAllOperator(menuDao.findByParentIsNullOrderByIdDesc());
	}

	@Override
	public Set<MenuVo> findAllOperator(Set<Menu> menus) {
		Set<MenuVo> vos = new HashSet<>();

		// 遍历Menus，设置Menu为空
		for (Menu d : menus) {
			MenuVo v = new MenuVo();
			setMenuIsNull(d, v);

			vos.add(v);
		}

		return vos;
	}

	// 遍历Menus，设置Menu为空
	public void setMenuIsNull(Menu menu, MenuVo v) {
		if (menu == null) {
			return;
		}
		menu.setParent(null);

		// MenuVo赋值
		// 先添加菜单
		v.setIcon(menu.getIcon());
		v.setId(menu.getId());
		v.setName(menu.getMenuName());
		v.setPerms(menu.getPerms());
		v.setUrl(menu.getUrl());

		// 再添加操作按钮
		if(menu.getOperators().isEmpty() == false) {
			Set<MenuVo> menuVos2 = new HashSet<>();
			for (Operator o : menu.getOperators()) {
				o.setMenu(null);

				MenuVo vo = new MenuVo();
				vo.setId(o.getOperatorId());
				vo.setName(o.getOperatorName());
				vo.setUrl(o.getUrl());
				menuVos2.add(vo);

			}
			v.setChildren(menuVos2);
		}
		
		Set<MenuVo> vos = new HashSet<>();
		
		// 进入children循环---菜单
		for (Menu d : menu.getChildren()) {
			MenuVo vo = new MenuVo();
			setMenuIsNull(d, vo);
			vos.add(vo);
			v.setChildren(vos);
		}

	}

	// 遍历Menus，设置Menu为空
	public void setMenuIsNull(Menu menu, String operator, MenuVo v) {
		if (menu == null) {
			return;
		}
//		if (menu.getChildren() == null) {
//			return;
//		}

		// MenuVo赋值
		if (v != null) {
			v.setIcon(menu.getIcon());
			v.setId(menu.getId());
			v.setName(menu.getMenuName());
			v.setPerms(menu.getPerms());
			v.setUrl(menu.getUrl());
		}

		menu.setParent(null);
		if (operator == null) {
			menu.setOperators(null);

		}

		if (v != null) {
			Set<MenuVo> menuVos = new HashSet<>();
			for (Menu d : menu.getChildren()) {
				MenuVo vo = new MenuVo();

				setMenuIsNull(d, operator, vo);
				menuVos.add(vo);
			}
			v.setChildren(menuVos);

		} else {
			for (Menu d : menu.getChildren()) {
				setMenuIsNull(d, operator, null);
			}
		}

		if (operator != null && "operator".equals(operator) == true && menu.getOperators().size() != 0) {
			menu.setMenuName(menu.getMenuName() + "(" + menu.getOperators().size() + ")");

			if (v != null) {
				Set<MenuVo> menuVos = new HashSet<>();
				for (Operator o : menu.getOperators()) {
					o.setMenu(null);

					MenuVo vo = new MenuVo();
					vo.setId(o.getOperatorId());
					vo.setName(o.getOperatorName());
					vo.setUrl(o.getUrl());
//					vo.setCheckArr("1");
					menuVos.add(vo);

				}
				v.setChildren(menuVos);
			} else {
				for (Operator o : menu.getOperators()) {
					o.setMenu(null);
				}
			}
		}

	}

	@Override
	public Page<Menu> selectAllWithMenu(int page, int limit) {

		Sort sort = new Sort(Direction.DESC, "id");
		PageRequest pageable = PageRequest.of(page - 1, limit, sort);
		Page<Menu> menus = menuDao.findAll(pageable);

		return menus;
	}

	// 根据parentId查询出列表
	@Override
	public List<Menu> findListByParentId(Integer parentId) {
		// TODO Auto-generated method stub
		return menuDao.findByParentId(parentId);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void addMenu(Menu menu) {
		// TODO Auto-generated method stub
		if(menu == null) {
			return ;
		}
		menuDao.save(menu);
	}

	@Override
	public Menu findMenuById(Integer parentId) {
		if(parentId == null) {
			return null;
		}
		return menuDao.getOne(parentId);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void updateMenu(Menu menu, Integer parentId) {
		// TODO Auto-generated method stub
		if(menu == null || parentId==null) {
			return ;
		}
		Menu parent = null;
		if(!parentId.equals("")) {
			parent = menuDao.getOne(parentId);
		}
		
		Menu getMenu = menuDao.getOne(menu.getId());
		getMenu.setParent(parent);
		
		getMenu.setMenuName(menu.getMenuName());
		getMenu.setUrl(menu.getUrl());
		getMenu.setIcon(menu.getIcon());
		getMenu.setPerms(menu.getPerms());
		
		menuDao.save(getMenu);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void deleteMenuById(Integer id) {
		if(id==null) {
			return ;
		}
		Menu getMenu = menuDao.getOne(id);
		getMenu.setOperators(null);
		if(getMenu != null) {
			Menu menu = menuDao.save(getMenu);
			
			menuDao.delete(menu);
		}
		
	}
}
