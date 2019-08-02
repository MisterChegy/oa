package com.chegy.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.chegy.model.Menu;
import com.chegy.model.Operator;
import com.chegy.model.User;
import com.chegy.model.vo.MenuVo;

public interface MenuService {

	List<Menu> selectCurrentUserMenuTree(User user);

	Set<Menu> findAll(String operator);

	Page<Menu> selectAllWithMenu(int page, int limit);

	List<Menu> findListByParentId(Integer parentId);

	Set<MenuVo> findAllOperator();

	Set<MenuVo> findAllOperator(Set<Menu> menus);

	void addMenu(Menu menu);

	Menu findMenuById(Integer parentId);

	void updateMenu(Menu menu, Integer parentId);

	void deleteMenuById(Integer id);

}
