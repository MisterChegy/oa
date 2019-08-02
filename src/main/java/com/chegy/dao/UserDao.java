package com.chegy.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chegy.model.User;
@Repository
public interface UserDao extends JpaRepository<User,Integer>{

	User findByUsername(String username);
	
	@Transactional  
	@Modifying(clearAutomatically = true)
	@Query(nativeQuery=true,value="update user set status=?1 where id=?2")
	void save1(int status,int id);
;

}
