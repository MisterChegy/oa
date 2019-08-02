package com.chegy.service.impl;

import java.util.ArrayList;
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
import com.chegy.model.Depart;
import com.chegy.service.DepartService;

@Service
@Transactional(readOnly = true)
public class DepartServiceImpl implements DepartService{

	@Resource
	private DepartDao departDao;
	
	@Override
	public List<Depart> findAll() {
		Set<Depart> departs = (Set<Depart>) departDao.findByDepartIsNullOrderByIdDesc();
//		Set<Depart> result = new HashSet<>();
//		for(Depart d : departs) {
//			if(d.getDepart() == null) {
//				//父节点为空，说明是一级目录
//				result.add(d);
//			}
//		}
		List<Depart> departls = new ArrayList<>();
		//遍历departs，设置depart为空
		for(Depart d : departs) {
			setDepartIsNull(d);
			departls.add(d);
		}
		
		return departls;
	}

	//遍历departs，设置depart为空
	public void setDepartIsNull(Depart depart){
		if(depart == null) {
			return ;
		}
		if(depart.getChildren() == null) {
			return ;
		}
		depart.setDepart(null);
		depart.setUsers(null);
		for(Depart d : depart.getChildren()) {
			setDepartIsNull(d);
		}
	}

	@Override
	public Page<Depart> selectAllWithDept(int page, int limit) {
		Sort sort = new Sort(Direction.DESC, "id");
		PageRequest pageable = PageRequest.of(page-1, limit, sort);
	    Page<Depart> departs = departDao.findAll(pageable);
		
		return departs;
	}

	@Override
	public List<Depart> findListByParentId(Integer parentId) {
		// TODO Auto-generated method stub
		return departDao.findByDepartId(parentId);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void addDepart(Depart depart, Integer parentId) {
		if(depart == null || parentId==null) {
			return ;
		}
		Depart parent = departDao.getOne(parentId);
		
		depart.setDepart(parent);
		depart.setStatus(1);
		
		departDao.save(depart);
	}

	@Override
	public Depart findById(Integer id) {
		// TODO Auto-generated method stub
		return departDao.getOne(id);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public void deleteDepartById(Integer id) {
		// TODO Auto-generated method stub
		//先删除中间表数据（null,update），再删除部门表中该数据，级联子节点，不级联父节点。
		
		if(id != null) {
			Depart depart = departDao.getOne(id);
			depart.setUsers(null);
			depart.setPositions(null);
			departDao.save(depart);
			
			if(depart != null) {
				departDao.delete(depart);
			}
			
		}
	}
}
