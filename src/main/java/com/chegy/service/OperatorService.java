package com.chegy.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.chegy.model.Operator;

public interface OperatorService {

	Page<Operator> selectAllWithOperator(int page, int limit);

	Page<Operator> selectAllWithOperator(int page, int limit, Integer menuId);

	List<Operator> findListByMenuId(Integer menuId);

	void addOperator(Operator operator);

	Operator findOperById(Integer id);

	void updateOperator(Operator operator, Integer menuId);

	void deleteOperatorById(Integer id);

//	List<Operator> findAll();

}
