package com.mulodo.training.service_impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import util.HashUtil;
import util.PagingUtil;

import com.mulodo.training.dao.LikeInforDao;
import com.mulodo.training.dao.PhotoDao;
import com.mulodo.training.dao.UserDao;
import com.mulodo.training.domain.LikeInfor;
import com.mulodo.training.domain.Photo;
import com.mulodo.training.domain.Token;
import com.mulodo.training.domain.User;
import com.mulodo.training.dto.ErrorDTO;
import com.mulodo.training.dto.GetPhotoResponseDTO;
import com.mulodo.training.service.PhotoService;

@Path("/photo")
@Component
public class PhotoServiceImpl implements PhotoService {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private PhotoDao photoDao;
	@Autowired
	private LikeInforDao likeInforDao;
	@Autowired
	private ServletContext servletContext;
	
	
	
	//find total pages api
	@Override
	@GET
	@Path("/total_pages")
	public Response findTotalPage(@QueryParam("limit") Integer limit) {
		
		Integer count = photoDao.findTotalPages(limit);
		return Response.status(200).entity(count).build();
	}
	
	//remove photo api
	@Override
	@DELETE
	@Path("/remove")
	@Produces(MediaType.APPLICATION_XML)
	@Transactional(propagation = Propagation.NESTED)
	public Response removePhoto(Integer photoId,@Context HttpServletRequest request) {
		
		try{
			
			Photo photo = photoDao.load(photoId);
			Token tokenObj = (Token) request.getAttribute("token");
			User user = tokenObj.getUser();
			if(user.equals(photo.getUser())){
				
				new File(photo.getPath()).delete(); //delete the file
				photoDao.delete(photo);
			}
			GetPhotoResponseDTO dto = buildDTO(200, null);
			dto.setImageId(photoId);
			return Response.status(200).entity(dto).build();
		}catch(HibernateException e){
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		}
	}
	//find uploaded photo api
	@Override
	@GET
	@Path("/my_photos")
	@Produces(MediaType.APPLICATION_XML)
	@Transactional(propagation = Propagation.NESTED)
	public Response getMyImages(HttpServletRequest request) {
		
		try{
			
			Token tokenObj = (Token) request.getAttribute("token");
			Integer page  = Integer.parseInt(request.getParameter("page"));
			Integer limit  = Integer.parseInt(request.getParameter("limit"));
			//if(tokenObj == null) return Response.status(401).entity(buildDTO(401, null)).build();
			User user = tokenObj.getUser();
			List<Photo> photos = photoDao.getPhotoByUser(user, PagingUtil.doPaging(page, limit),limit);
			List<String> imageIds = buildImageStringList(photos);
			
			return Response.status(200).entity(buildDTO(200,imageIds)).build();
		}catch(HibernateException e){
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		} catch (IOException e) {
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		}
	}

	/**
	 * build the list of photo id to send back to client
	 * @param photos the photos retrieved from database
	 * @return list contains ids of the photos
	 * @throws IOException
	 */
	private List<String> buildImageStringList(List<Photo> photos) throws IOException {
		
		List<String> URIs = new ArrayList<String>();
		for(Photo photo : photos){

			URIs.add(String.valueOf(photo.getId()));
		}
		return URIs;
	}
	//find other photo api
	@Override
	@Transactional(propagation = Propagation.NESTED)
	@GET
	@Path("/photo_list/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getOthersImages(@Context HttpServletRequest request,@PathParam("id") Integer userId) {
		
		try{
			//if(request.getAttribute("token") == null)
				//return Response.status(401).entity(buildDTO(401, null)).build();
			Integer page  = Integer.parseInt(request.getParameter("page"));
			Integer limit  = Integer.parseInt(request.getParameter("limit"));
			
			User user = userDao.load(userId);
			List<Photo> photos = photoDao.getPhotoByUser(user, PagingUtil.doPaging(page, limit),limit);
			List<String> imageIds = buildImageStringList(photos);
			
			return Response.status(200).entity(buildDTO(200,imageIds)).build();
		}catch(HibernateException e){
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		} catch (IOException e) {
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		}
	}
	//uplaod api
	@Override
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_XML)
	@Transactional(propagation = Propagation.NESTED)
	public Response saveImage(@Context HttpServletRequest request) {
		
		String comment = null;
		//Token tokenObj = (Token) request.getAttribute("token");
		//byte[] image = null;
		InputStream is = null;
		BufferedOutputStream out = null;
		String path = null;
		
		try{
					
			ServletFileUpload servletFileUpload = new ServletFileUpload();
			FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(request);
			while(fileItemIterator.hasNext()){
				
				FileItemStream  fileItemStream = fileItemIterator.next();
				if("comment".equals(fileItemStream.getFieldName())){
					
					StringWriter stringWriter = new StringWriter();
					IOUtils.copy(fileItemStream.openStream(), stringWriter, "utf-8");
					comment = stringWriter.toString();
				}else if("content".equals(fileItemStream.getFieldName())){
					
					path = createPath();
					
					//save image content
					//path = ("/Users/aoden/Desktop/aaaaaa.jpg");
					is = fileItemStream.openStream();
					out = new BufferedOutputStream(new FileOutputStream(path));
					int data = -1;
				    while ((data = is.read()) != -1) {
				    	
				        out.write(data);
				    }
				}
			}
			
			Token tokenObj = (Token) request.getAttribute("token");
			Photo photo = new Photo();
			photo.setComment(comment);
			photo.setPath(path);
			photo.setUser(tokenObj.getUser());
			photoDao.save(photo);
			return Response.status(500).entity(buildDTO(200,null)).build();
			//User user = tokenObj.getUser();
			
		}catch(HibernateException e){
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		} catch (FileUploadException e) {
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		} catch (IOException e) {
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		} catch (NoSuchAlgorithmException e) {
			
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		}finally{
			//close the streams
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		}
		
	}

	/**
	 * create the path to save image, first check the root folder if there is no sub folder then 
	 * create a new folder named '1', each folder only contains maximum of 10000 image if exceeded then
	 * a new folder will be created which name is follow the sequence e.g 2,3,4,5...
	 * ; the photo will be renamed with random string
	 * @return the full path for the image
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private String createPath() throws IOException, NoSuchAlgorithmException {
		
		//load the path from properties file
		InputStream is = new FileInputStream(servletContext.getRealPath("/WEB-INF/config.properties"));
		Properties properties = new Properties();
		properties.load(is);
		String rootPath = properties.getProperty("path");
		
		//scan the path 
		File file  = new File(rootPath);
		String[] dirs = file.list();
		if(dirs.length == 0) { //no subfolder
			
			new File(rootPath + "/" + 1).mkdir(); //make new folder named '1'
			return rootPath + "/" + 1;
		}
		String latest  = Collections.max(Arrays.asList(dirs));
		
		File file2 = new File(rootPath + "/" + latest);
		
		if(file2.list().length <= 10000){
			
			
			return rootPath + "/" + latest + HashUtil.hash("MD5", String.valueOf(System.currentTimeMillis())) + ".jpg";
		}else {
			
			Integer newDir = Integer.parseInt(latest) + 1;
			new File(rootPath + "/" + newDir).mkdir(); //make new folder
			return rootPath + "/" + newDir + HashUtil.hash("MD5", String.valueOf(System.currentTimeMillis())) + ".jpg";
		}
		
	}
	//find one photo api
	@Override
	@GET
	@Path("/{id}")
	@Produces("image/jpeg")
	@Transactional(propagation = Propagation.NESTED)
	public Response getImage(@Context HttpServletRequest request, @PathParam("id") Integer id) {
				
		try {
			//load photo and initialize the path
			//Token tokenObj = (Token) request.getAttribute("token");
			Photo photo = photoDao.load(id);
			String path = getPath(photo);
			//encode the image to base 64 string
			FileInputStream fis = new FileInputStream(path);
			byte[] image = IOUtils.toByteArray(fis);        
			//response and exception handlings
			
			return Response.ok().entity(image).build();
				
		}catch (HibernateException e) {
			
			e.printStackTrace();
			return Response.status(500).build();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return Response.status(500).build();
		} catch (IOException e) {
			
			e.printStackTrace();
			return Response.status(500).build();
		} 
		
	}

	private String getPath(Photo photo) throws IOException {
		
		InputStream is = new FileInputStream(servletContext.getRealPath("/WEB_INF/config.properties"));
		Properties properties = new Properties();
		properties.load(is);
		String rootPath = properties.getProperty("path");
		is.close();
		return rootPath+photo.getPath();
	}

	@Override
	@PUT
	@Path("/like/{id}")
	@Transactional(propagation  = Propagation.NESTED)
	public Response likePhoto(@PathParam("id") Integer id , @Context HttpServletRequest request) {
		
		try{
			
			Token tokenObj = (Token) request.getAttribute("token");
			Photo photo = photoDao.load(id);
			User likeGiver = tokenObj.getUser();
			LikeInfor likeInfor = likeInforDao.findLikeInfor(likeGiver, photo);
			if(likeInfor != null){
				
				if(likeInfor.isStatus()){
					
					likeInfor.setStatus(false);					
				}else {
					
					likeInfor.setStatus(true);
				}
			}else{
				
				likeInfor = new LikeInfor();
				likeInfor.setPhoto(photo);
				likeInfor.setUser(likeGiver);
				likeInforDao.save(likeInfor);
			}
			
			return Response.status(200).entity(buildDTO(200,null)).build();
		}catch(HibernateException e){
			
			e.printStackTrace();
			return Response.status(500).entity(buildDTO(500,null)).build();
		}
		
	}
	
	private GetPhotoResponseDTO buildDTO(int b , List<String> idList){
		
		GetPhotoResponseDTO dto = new GetPhotoResponseDTO();
		ErrorDTO error = dto.getErrorDTO();
		switch (b) {
		case 200:
			
			error = new ErrorDTO(b, "OK");
			break;
		case 500:
			
			error = new ErrorDTO(b, "Internal Error");break;
		
		default:
			break;
		}
		
		dto.setErrorDTO(error);
		if (idList != null) 
			for(String s : idList){
				
				dto.getPhoto().add(s);
			}
		return dto;
	}
	
}
