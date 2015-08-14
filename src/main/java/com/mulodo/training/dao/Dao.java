package com.mulodo.training.dao;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Le Dinh Minh Khoi
 *	
 * @param <T> The entity's class
 * @param <K> The entity's Id class
 */
public interface Dao<T,K extends Serializable> {
	/**
	 * save the entity
	 * @param entity 
	 */
	void save(T entity);
	/**
	 * delete the entity
	 * @param entity
	 */
	void delete(T entity);
	/**
	 * update the entity
	 * @param entity
	 */
	void update (T entity);
	/**
	 * merge the entity
	 * @param entity
	 */
	void merge(T entity); 
	/**
	 * load the entity from database
	 * @param id
	 * @return loaded entity or null if non-exist
	 */
	T load(Serializable id);
	/**
	 * get the entity from session
	 * @param id
	 * @return
	 */
	T get(Serializable id);
	/**
	 * find the object(s)
	 * @param queryString HQL query string (with parameters if any)
	 * @param params A map that each entry contains parameter name and parameter value
	 * @param begin begin offset 
	 * @param end total object to load
	 * @return an unique result or a list depends on the HQL query
	 */
	Object find(String queryString,Map<String,Object> params,int begin,int end);
}
