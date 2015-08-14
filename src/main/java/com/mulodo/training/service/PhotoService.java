package com.mulodo.training.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * Handle service and resource related to photo
 * @author Le Khoi
 *
 */
public interface PhotoService {

	/**
	 * find total page of photos
	 * @return number of pages will be used for pagination in client of HTTP status 401 if an unauthorized user try to find out
	 */
	Response findTotalPage(Integer limit);
	
	/**
	 * Handle the upload process images will be saved in folder.The folder name is written in config.properties file
	 * @param request The request from client
	 * @return Appropriate response from server 
	 */
	Response saveImage(HttpServletRequest request);
	
	/**
	 * Get specific photo 
	 * @param request The request from client
	 * @param id the photo's id
	 * @return Appropriate response from server 
	 */
	Response getImage(HttpServletRequest request,Integer id);
	
	/**
	 * Handle like/unlike operation
	 * @param id the photo's id
	 * @param request request The request from client
	 * @return Appropriate response from server 
	 */
	Response likePhoto(Integer id,HttpServletRequest request);
	
	/**
	 * get photos of the current user
	 * @param request request The request from client
	 * @return Appropriate response from server 
	 */
	Response getMyImages(HttpServletRequest request);
	/**
	 * Get photos of other user
	 * @param request request request The request from client
	 * @param userId the other user's id
	 * @return Appropriate response from server 
	 */
	Response getOthersImages(HttpServletRequest request,Integer userId);
	/**
	 * remove photo
	 * @param photoId the photo's id
	 * @param request request request request The request from client
	 * @return Appropriate response from server 
	 */
	Response removePhoto(Integer photoId,HttpServletRequest request);
}
