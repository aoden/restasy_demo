package com.mulodo.training.dao;

import java.io.Serializable;

/**
 * 
 * @author Le Khoi
 *
 * @param <T> The entity type
 * @param <K> The entity's id type
 */
public interface PagingDao<T,K extends Serializable> extends Dao<T,K>{

	/**
	 * find total pages with given limit
	 * @param limit the number of items per page
	 * @return number of pages if input limit < 0 or exception the method returns null
	 */
	Integer findTotalPages(Integer limit);
}
