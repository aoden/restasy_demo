package com.mulodo.training.dao_impl;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import util.HashUtil;

import com.mulodo.training.dao.AbstractDao;
import com.mulodo.training.dao.TokenDao;
import com.mulodo.training.domain.Token;
import com.mulodo.training.domain.User;

@Repository
@Qualifier("tokenDao")
public class TokenDaoImpl extends AbstractDao<Token, Integer> implements TokenDao {

	
	@Override
	public Class<Token> getEntityClass() {
		
		return Token.class;
	}
	
	

	@Override
	public void saveToken(String token, User owner, boolean reset) throws NoSuchAlgorithmException,HibernateException {
		
		Session session = getCurrentSession();
		session.refresh(owner);
		
		Token tokenObj = new Token();
		tokenObj.setContent(HashUtil.hash("MD5", token));
		tokenObj.setUser(owner);
		tokenObj.setReset(true);
		
		session.save(tokenObj);
	}



	@Override
	public void saveToken(String token, User owner) throws NoSuchAlgorithmException,HibernateException {
		
		Session session = getCurrentSession();
		session.refresh(owner);

		Token tokenObj = new Token();
		tokenObj.setContent(HashUtil.hash("MD5", token));
		tokenObj.setUser(owner);
		
		session.save(tokenObj);
		
	}

	@Override
	public Token findToken(String tokenString) throws NoSuchAlgorithmException,HibernateException {
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("tokenString", HashUtil.hash("MD5", tokenString));
		Token token = (Token) find("from Token t where t.content = :tokenString", params, 0, 1);
		return token;
	}

	
	

}
