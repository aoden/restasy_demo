package com.mulodo.training.dao_impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import util.PagingUtil;

import com.mulodo.training.dao.AbstractDao;
import com.mulodo.training.dao.UserDao;
import com.mulodo.training.domain.User;

@Repository
@Qualifier("userDao")
public class UserDaoImpl extends AbstractDao<User, Integer> implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findByUserName(String userName,Integer page,Integer limit) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", userName);
		return (List<User>) find("from User u where u.userName like concat(%,concat(:userName,%))", params, PagingUtil.doPaging(page, limit), limit);
	}

	@Override
	public User findByEmail(String email) throws HibernateException{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		return (User) find("from User u where u.email = :email", params, 0, Integer.MAX_VALUE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllUser() throws HibernateException{
		
		Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(getEntityClass()).list();
	}

	@Override
	public Class<User> getEntityClass() {
		
		return User.class;
	}

}
