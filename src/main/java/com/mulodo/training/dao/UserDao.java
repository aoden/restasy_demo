package com.mulodo.training.dao;

import java.util.List;

import com.mulodo.training.domain.User;

public interface UserDao extends PagingDao<User, Integer>{
	
	List<User> findAllUser();
	User findByEmail(String email);
	List<User> findByUserName(String userName,Integer page,Integer limit);
}
