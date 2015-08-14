package com.mulodo.training.service_impl;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import util.HashUtil;
import util.ImageUtil;
import util.MailUtil;
import util.PagingUtil;

import com.mulodo.training.dao.PhotoDao;
import com.mulodo.training.dao.TokenDao;
import com.mulodo.training.dao.UserDao;
import com.mulodo.training.domain.Token;
import com.mulodo.training.domain.User;
import com.mulodo.training.dto.ErrorDTO;
import com.mulodo.training.dto.LoginResponseDTO;
import com.mulodo.training.dto.ResponseDTO;
import com.mulodo.training.dto.SignupFormDTO;
import com.mulodo.training.dto.UserListResponseDTO;
import com.mulodo.training.service.UserService;

@Path("/user")
@Component
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private PhotoDao photoDao;
	@Autowired
	private TokenDao tokenDao;
	@Autowired
	private ServletContext servletContext;
	
	
	
	@Override
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/find_by_email")
	public Response findByEmail(String email) {
		
		try{
			
			User user = userDao.findByEmail(email);
			UserListResponseDTO dto = new UserListResponseDTO();
			dto.getIds().add(user.getId());
			dto.getEmails().add(user.getEmail());
			dto.getUserNames().add(user.getUserName());
			dto.getAvatars().add(Base64.encodeBase64String(user.getAvatar()));
			dto.getErrorDTO().setStatus(200);
			return Response.status(200).entity(dto).build();
			
		}catch(HibernateException e){
			
			ResponseDTO dto = new ResponseDTO();
			dto.getErrorDTO().setStatus(500);
			dto.getErrorDTO().setMessege("Internal error");
			e.printStackTrace();
			return Response.status(500).entity(dto).build();
		}
		
	}

	@Override
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/find_by_username")
	public Response findByUserName(String userName,Integer page, Integer limit) {
		
		try{
			
			List<User> userList = userDao.findByUserName(userName, page, limit);
			//build response dto
			UserListResponseDTO dto = new UserListResponseDTO();
			for(User user: userList){
				
				dto.getIds().add(user.getId());
				dto.getEmails().add(user.getEmail());
				dto.getUserNames().add(user.getUserName());
				dto.getAvatars().add(Base64.encodeBase64String(user.getAvatar()));
			}
			dto.getErrorDTO().setMessege("OK");
			dto.getErrorDTO().setStatus(200);
			return Response.status(200).entity(dto).build();
			
		}catch(Exception e){
			
			ResponseDTO dto = new ResponseDTO();
			dto.getErrorDTO().setStatus(500);
			dto.getErrorDTO().setMessege("Internal error");
			return Response.status(500).entity(dto).build();
		}
	}

	@Override
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/list")
	public Response getUserList(@QueryParam("page") Integer page,
			@QueryParam("page") Integer limit) {
		
		try{
			
			@SuppressWarnings("unchecked")
			List<User> userList = (List<User>) userDao.find("from user", null, PagingUtil.doPaging(page, limit), limit);
			//build response dto
			UserListResponseDTO dto = new UserListResponseDTO();
			for(User user: userList){
				
				dto.getIds().add(user.getId());
				dto.getEmails().add(user.getEmail());
				dto.getUserNames().add(user.getUserName());
				dto.getAvatars().add(Base64.encodeBase64String(user.getAvatar()));
			}
			dto.getErrorDTO().setMessege("OK");
			dto.getErrorDTO().setStatus(200);
			return Response.status(200).entity(dto).build();
			
		}catch(Exception e){
			
			ResponseDTO dto = new ResponseDTO();
			dto.getErrorDTO().setStatus(500);
			dto.getErrorDTO().setMessege("Internal error");
			return Response.status(500).entity(dto).build();
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED)
	@Path("/send_mail")
	public Response sendRecoveryMail(@Context HttpServletRequest request) {
		
		try{
			
			String tokenStr = request.getHeader("t");
			Token tokenObj = tokenDao.findToken(tokenStr);
			User user = tokenObj.getUser();
			MailUtil.send("le.khoi992@gmail.com", user.getEmail(), "[Photo Share] Reset your password",request.getParameter("This is reset email"));
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return null;
	}

	//captcha api
	@Override
	@GET
	@Path("/captcha")
	@Produces("image/jpeg")
	public Response generateCaptcha(@Context HttpServletRequest request) {
		
		try{
			
			//generate random string
			String s = RandomStringUtils.randomAlphabetic(7);
			System.out.println(s);
			request.getSession().setAttribute("captchaString", s);
			
			//create image
			int w = 200, h = 70;
			BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, w, h);
			
			g.setColor(Color.cyan);
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.drawString(s, 20, 50);
			g.dispose();
			
			//build response
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", out);
			byte[] bytes = out.toByteArray();
			
			return Response.status(200).entity(bytes).build();
			
		}catch(IOException e){
					
			e.printStackTrace();
			return Response.status(500).build();
		}
		
	}

	//reset password api
	@Override
	@PUT
	@Path("/reset")
	@Produces(MediaType.APPLICATION_XML)
	@ValidateRequest
	@Transactional(propagation = Propagation.NESTED)
	public Response doResetPwd(
			@FormParam("pwd") String pwd, 
			@FormParam("cfm") String cfm,
			@Context HttpServletRequest request) {
		
		try{
			
			Token tokenObj = (Token) request.getAttribute("token");
			User user = tokenObj.getUser();
			if(pwd.equals(cfm)){
				
				user.setPassword(HashUtil.hash("MD5", pwd));
			}
			tokenDao.delete(tokenObj);
			return Response.status(200).entity(buildDTO(200, null)).build();
			
		}catch(HibernateException e){
			
			e.printStackTrace();
			return Response.serverError().entity(buildDTO(500, null)).build();
		}catch(NoSuchAlgorithmException e){
			
			e.printStackTrace();
			return Response.serverError().entity(buildDTO(500, null)).build();
		}
		
	}

	//reset password api
	@Override
	@GET
	@Path("/reset_demand")
	@Produces(MediaType.APPLICATION_XML)
	public Response forgotPasswordHelp(String email) {
		
		try {
			User user = userDao.findByEmail(email);
			String token = buildToken(email, new Date().toString());
			//save the token
			tokenDao.saveToken(token, user,true);
			
			return Response.ok().entity(buildDTO(200, token)).build();
			
		}catch(HibernateException e){
			
			e.printStackTrace();
			return Response.serverError().entity(buildDTO(500, null)).build();
		}
		catch (InvalidKeyException e) {
			
			e.printStackTrace();
			return Response.serverError().entity(buildDTO(500, null)).build();
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
			return Response.serverError().entity(buildDTO(500, null)).build();
		}
	}

	//create account api
	@Override
	@POST
	@Path("/signup")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@ValidateRequest
	public Response createAccount(@MultipartForm SignupFormDTO form) {
		try{
			
			if(form.getPwd().equals(form.getCfm())){
				
				User user = new User();
				user.setEmail(form.getEmail());
				user.setUserName(form.getUserName());
				user.setPassword(HashUtil.hash("MD5", form.getPwd()));
				user.setAvatar(createAvatar(form.getAvatar()));
				userDao.save(user);
				return Response.ok().entity(buildDTO(200, "null")).build();
			}else {
				
				return Response.status(400).entity(buildDTO(400, "")).build();
			}
		}catch(HibernateException e){
			
			e.printStackTrace();
			return Response.serverError().entity(buildDTO(500, null)).build();
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
			return Response.serverError().entity(buildDTO(500, null)).build();
		} catch (IOException e) {
			
			e.printStackTrace();
			return Response.serverError().entity(buildDTO(500, null)).build();
		}
	}

	/**
	 * create avatar from input, the input image will be resized to 200*200 px and convert to byte array
	 * @param avatar the input stream contains user submitted image
	 * @return byte array contains the resized avatar
	 * @throws IOException
	 */
	private byte[] createAvatar(InputStream avatar) throws IOException {
		
		BufferedImage image = ImageIO.read(avatar);
		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
		BufferedImage resizedAtatar = ImageUtil.resizeImage(image, type, 200, 200);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(resizedAtatar, "jpg", bos);
		
		return bos.toByteArray();
	}


	//log out api
	@Override
	@PUT
	@Path("/logout")
	public Response logout(@Context HttpServletRequest request) {
		
		Token tokenObj = (Token) request.getAttribute("token");
		tokenDao.delete(tokenObj);
		return Response.status(200).entity(buildDTO(200, "")).build();
	}


	
	//login api
	@POST
	@Path("/login")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_XML)
	public Response login(@Context HttpServletRequest request){
				
		try{
			
			String email = null;
			String password = null; 
			
			//get the request payload
			ServletFileUpload servletFileUpload = new ServletFileUpload();
			FileItemIterator iterator = servletFileUpload.getItemIterator(request);
			
			while (iterator.hasNext()) {
				FileItemStream fileItemStream = iterator.next();
				if("email".equals(fileItemStream.getFieldName())){
					
					StringWriter stringWriter = new StringWriter();
					IOUtils.copy(fileItemStream.openStream(), stringWriter, "utf-8");
					email = stringWriter.toString();
				} else if("password".equals(fileItemStream.getFieldName())){
					
					StringWriter stringWriter = new StringWriter();
					IOUtils.copy(fileItemStream.openStream(), stringWriter, "utf-8");
					password = stringWriter.toString();
				}
			}

			Map<String,Object> params = new HashMap<String, Object>();
			params.put("email", email);
			
			//find user form DB
			User user = (User) userDao.find("from User u where u.email = :email", params, 0, 1);
			
			//hash the input with MD5 Algorithm     
	        String hashString = HashUtil.hash("MD5", password);
	        
	        //check the password
	        if(hashString.equals(user.getPassword())){
	        	
	        	//generate token
	        	String token = buildToken(String.valueOf(user.getId()),new Date().toString());
	        	LoginResponseDTO loginResponseDTO = buildDTO(200, token);
	        	loginResponseDTO.setUserId(String.valueOf(user.getId()));
	        	loginResponseDTO.setUserName(user.getUserName());
	        	
	        	//save token in database
	        	tokenDao.saveToken(token, user);	        	
	        	
	        	return Response.status(200).entity(loginResponseDTO).build();
	        }else throw new NullPointerException();//error 401,password incorrect
			
		}catch(JDBCException ex1){//database error
			
			ex1.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(500,null);
			
			return Response.status(500).entity(loginResponseDTO).build();
		}
		catch (NoSuchAlgorithmException e) {//encrypt error
			
			e.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(500,null);
			
			return Response.status(500).entity(loginResponseDTO).build();
			
		}catch(NullPointerException ex){//not exist user or wrong password
			
			ex.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(401,null);
			
			return Response.status(401).entity(loginResponseDTO).build();
		} catch (InvalidKeyException e) {//encrypt error
			
			e.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(500,null);
			
			return Response.status(500).entity(loginResponseDTO).build();
		} catch (FileUploadException e2) {//upload error
			
			e2.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(500,null);
			
			return Response.status(500).entity(loginResponseDTO).build();
		} catch (IOException e3) { 
			
			e3.printStackTrace();
			LoginResponseDTO loginResponseDTO = buildDTO(500,null);
			
			return Response.status(500).entity(loginResponseDTO).build();
		} 
		
	}
	
	private String buildToken(String id, String date) throws NoSuchAlgorithmException, InvalidKeyException {
		
		SecretKey secretKey = null;

	    byte[] keyBytes = date.getBytes();
	    secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

	    Mac mac = null;
	    mac = Mac.getInstance("HmacSHA1");
		mac.init(secretKey);
	   

	    byte[] text = id.getBytes();

	    return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
		
	}


	private LoginResponseDTO buildDTO(Integer b, String token) {
		
		ErrorDTO error=null;
		switch (b) {
		case 200:
			
			error = new ErrorDTO(b, "OK");
			break;
		case 401:
			
			error = new ErrorDTO(b, "User name or password incorrect!");break;
		case 500:
			
			error = new ErrorDTO(b, "Internal Error");break;
		case 400:
			
			error = new ErrorDTO(b, "Bad request!");break;
		default:
			break;
		}
		return new LoginResponseDTO(error,token);
	}

 
	
}
