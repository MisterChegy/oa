package com.chegy.service;

import org.springframework.data.domain.Page;

import com.chegy.model.User;

public interface UserService {

	User findByUsername(String username);

	Page<User> selectAllWithDept(int page, int limit);

	User findById(Integer id);

	void updatePassword(Integer id, String password);

	void addUser(User user, Integer[] role, Integer[] departIds);

	void deleteUserById(Integer id);

	void updateStatusById(Integer id, int i);

}
