package com.chegy.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chegy.model.Role;
@Repository
public interface RoleDao extends JpaRepository<Role,Integer>{

	@Transactional  
	@Modifying(clearAutomatically = true)
	@Query(nativeQuery=true,value="update role set status=?2 where id=?1")
	void save1(Integer id, int i);
	
}
