package com.mulodo.training.dao_impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mulodo.training.dao.AbstractDao;
import com.mulodo.training.dao.PhotoDao;
import com.mulodo.training.domain.Photo;
import com.mulodo.training.domain.User;

@Repository
@Qualifier("photoDao")
public class PhotoDaoImpl extends AbstractDao<Photo, Integer> implements PhotoDao {
	
	@Override
	public Class<Photo> getEntityClass() {
		
		return Photo.class;
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Photo> getPhotoByUser(User user,Integer begin, Integer number) throws HibernateException{
		if(user != null){
			
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("user",user);
			return (List<Photo>) find("from Photo p where p.user = :user order by p.uploadDate desc", params, begin, number);
		}else {
			
			return (List<Photo>) find("from Photo p order by p.uploadDate", null, 0, 20);
		}
		
	}

	
}
