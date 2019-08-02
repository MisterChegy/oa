package com.chegy.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.chegy.model.Operator;

public interface OperatorDao extends JpaRepository<Operator,Integer>{

	Page<Operator> findByMenuId(Integer menuId, PageRequest pageable);

	List<Operator> findByMenuId(Integer menuId);

}
