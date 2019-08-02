package com.chegy.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.chegy.model.Depart;

public interface DepartService {

	List<Depart> findAll();

	Page<Depart> selectAllWithDept(int page, int limit);

	List<Depart> findListByParentId(Integer parentId);

	void addDepart(Depart depart, Integer parentId);

	Depart findById(Integer id);

	void deleteDepartById(Integer id);

}
