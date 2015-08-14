package com.mulodo.training.dao_impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mulodo.training.dao.AbstractDao;
import com.mulodo.training.dao.LikeInforDao;
import com.mulodo.training.domain.LikeInfor;
import com.mulodo.training.domain.Photo;
import com.mulodo.training.domain.User;

@SuppressWarnings("unchecked")
@Repository
@Qualifier("likeInforDao")
public class LikeInforDaoImpl extends AbstractDao<LikeInfor, Integer> implements LikeInforDao{

	@Override
	public Class<LikeInfor> getEntityClass() throws HibernateException{
		
		return LikeInfor.class;
	}

	@Override
	public List<User> findAllLikeGivers(Photo photo) throws HibernateException{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("photo", photo);
		return (List<User>) find("from LikeInfor likeInfor where likeInfor.photo = :photo and likeInfor.status = true", params, 0, Integer.MAX_VALUE);
	}

	@Override
	public Integer countLikes(Photo photo) throws HibernateException{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("photo", photo);
		return (Integer) find("select count(*) from LikeInfor likeInfor where likeInfor.photo = :photo",params,0,1);
	}

	@Override
	public LikeInfor findLikeInfor(User user, Photo photo) throws HibernateException{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("photo", photo);
		params.put("user", user);
		return  (LikeInfor) find("from LikeInfor likeInfor where likeInfor.user = :user and likeInfor.photo = :photo", params, 0, 1);
	}

}
