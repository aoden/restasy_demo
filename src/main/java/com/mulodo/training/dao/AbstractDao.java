package com.mulodo.training.dao;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED)
public abstract class AbstractDao<T,K extends Serializable> implements PagingDao<T, K> {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	protected Session getCurrentSession() throws HibernateException{
		
		return this.sessionFactory.getCurrentSession();
	}
	
	public abstract Class<T> getEntityClass();

	
	
	@Override
	public void merge(T entity) throws HibernateException {
		
		Session session = getCurrentSession();
		session.merge(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED,readOnly = true)
	public T get(Serializable id) {
		
		return (T) sessionFactory.getCurrentSession().get(getEntityClass(), id);
	}



	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED,readOnly = true)
	public T load(Serializable id) throws HibernateException {
		
		return (T) sessionFactory.getCurrentSession().load(getEntityClass(), id);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public void save(T entity) throws HibernateException{
		
		Session session = sessionFactory.getCurrentSession();
		session.save(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public void delete(T entity) throws HibernateException{
		
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public void update(T entity) throws HibernateException{
		
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
	}


	@Override
	@Transactional(readOnly = true,propagation = Propagation.REQUIRED)
	public Object find(String queryString,Map<String, Object> params, int begin, int end) throws HibernateException{
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery(queryString);
		if(params != null)
			//iterate through the map and set query parameters
			for(Map.Entry<String, Object> entry: params.entrySet()){
				
				query.setParameter(entry.getKey(), entry.getValue());
			}
		try {
				
			return query.uniqueResult();
		} catch (NonUniqueResultException e) {
				
			return query.setFirstResult(begin).setMaxResults(end).list();
		}
		
	}

	@Override
	public Integer findTotalPages(Integer limit) {
		
		try{
			
			if(limit<0) return null;
			Session session = getCurrentSession();;
			Criteria criteria = session.createCriteria(getEntityClass()).setProjection(Projections.rowCount());
			Integer total = (Integer) criteria.uniqueResult();
			
			return total/limit + 1;
		}catch(Exception e){
			
			e.printStackTrace();
			return null;
		}
		
	}

	
}
