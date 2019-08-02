package com.chegy.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chegy.model.Depart;

public interface DepartDao extends JpaRepository<Depart,Integer>{

	Set<Depart> findByDepartIsNullOrderByIdDesc();

	List<Depart> findByDepartId(Integer parentId);

}
