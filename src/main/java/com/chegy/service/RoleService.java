package com.chegy.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.chegy.model.Menu;
import com.chegy.model.Role;

public interface RoleService {

	List<Role> selectAll();

	Page<Role> selectAllPage(int page, int limit);

	Set<Menu> selectByRoleIdMenuTree(Integer id);

	void addRoleGrantMenu(Integer[] menuIds, Integer id);

	int[] selectOperatorIdsByRoleId(Integer id);

	void addGrantOperator(Set<Integer> operatorIds, Integer id);

	int[] selectByRoleIdMenuTreeIds(Integer id);

	boolean addNewRole(Role role);

	Set<Menu> selectByRoleIdFirstMenuTree(Integer id);

	Role findById(Integer id);

	void updateRole(Role role);

	void updateStatusById(Integer id, int i);

	void deleteRoleById(Integer id);

}
