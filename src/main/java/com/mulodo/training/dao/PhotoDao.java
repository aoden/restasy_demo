package com.mulodo.training.dao;

import java.util.List;

import com.mulodo.training.domain.Photo;
import com.mulodo.training.domain.User;

public interface PhotoDao extends PagingDao<Photo,Integer> {

	List<Photo> getPhotoByUser(User user,Integer begin,Integer number);
	//void removePhoto(Integer photoId,User user);
}
