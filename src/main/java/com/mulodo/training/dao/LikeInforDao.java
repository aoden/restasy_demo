package com.mulodo.training.dao;


import java.util.List;

import com.mulodo.training.domain.LikeInfor;
import com.mulodo.training.domain.Photo;
import com.mulodo.training.domain.User;

public interface LikeInforDao extends PagingDao<LikeInfor,Integer>{

	List<User> findAllLikeGivers(Photo photo);
	LikeInfor findLikeInfor(User user,Photo photo);
	Integer countLikes(Photo photo);
}
