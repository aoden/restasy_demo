package com.mulodo.training.dao;

import java.security.NoSuchAlgorithmException;

import org.hibernate.HibernateException;

import com.mulodo.training.domain.Token;
import com.mulodo.training.domain.User;

public interface TokenDao extends PagingDao<Token,Integer>{

	void saveToken(String token, User owner) throws NoSuchAlgorithmException, HibernateException;
	Token findToken(String tokenString) throws NoSuchAlgorithmException;
	void saveToken(String token,User owner, boolean reset) throws NoSuchAlgorithmException;
}
