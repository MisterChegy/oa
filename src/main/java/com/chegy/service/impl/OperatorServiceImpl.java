package com.chegy.service.impl;

import java.util.Date;
import java.util.List;

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
import com.chegy.model.Menu;
import com.chegy.model.Operator;
import com.chegy.service.OperatorService;
@Service
@Transactional(readOnly = true)
public class OperatorServiceImpl implements OperatorService{

	@Resource
	private OperatorDao operatorDao;
	
	@Resource
	private MenuDao menuDao;
	
	@Override
	public Page<Operator> selectAllWithOperator(int page, int limit) {
		Sort sort = new Sort(Direction.DESC, "operatorId");
		PageRequest pageable = PageRequest.of(page-1, limit, sort);
	    Page<Operator> operators = operatorDao.findAll(pageable);
		
		return operators;
	}

	@Override
	public Page<Operator> selectAllWithOperator(int page, int limit, Integer menuId) {
		if(menuId == 0) {
			return selectAllWithOperator(page, limit);
		}
		Sort sort = new Sort(Direction.DESC, "operatorId");
		PageRequest pageable = PageRequest.of(page-1, limit, sort);
		Page<Operator> operators = operatorDao.findByMenuId(menuId,pageable);
		return operators;
	}

	@Override
	public List<Operator> findListByMenuId(Integer menuId) {
		// TODO Auto-generated method stub
		return operatorDao.findByMenuId(menuId);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void addOperator(Operator operator) {
		if(operator == null) {
			return ;
		}
		operator.setCreateTime(new Date());
		operator.setModifyTime(new Date());
		operatorDao.save(operator);
	}

	@Override
	public Operator findOperById(Integer id) {
		// TODO Auto-generated method stub
		return operatorDao.getOne(id);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void updateOperator(Operator operator, Integer menuId) {
		// TODO Auto-generated method stub
		if(operator == null || menuId == null) {
			return ;
		}
		Operator getOper = operatorDao.getOne(operator.getOperatorId());
		
		getOper.setOperatorName(operator.getOperatorName());
		getOper.setUrl(operator.getUrl());
		getOper.setHttpMethod(operator.getHttpMethod());
		getOper.setModifyTime(new Date());
		getOper.setPerms(operator.getPerms());
		
		Menu menu = menuDao.getOne(menuId);
		getOper.setMenu(menu);
		
		operatorDao.save(getOper);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void deleteOperatorById(Integer id) {
		if(id == null) {
			return ;
		}
		// TODO Auto-generated method stub
		Operator operator = operatorDao.getOne(id);
		
		operator.setMenu(null);
		operatorDao.save(operator);
		
		operatorDao.delete(operator);
	}

}
