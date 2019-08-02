package com.chegy.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chegy.model.Menu;

public interface MenuDao extends JpaRepository<Menu,Integer>{

//	List<Menu> findByUsername(String username);

	List<Menu> findAll();

	Set<Menu> findByParentIsNullOrderByIdDesc();

	List<Menu> findByParentId(Integer parentId);

}
