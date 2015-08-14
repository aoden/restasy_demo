package com.mulodo.training.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mulodo.training.dao.TokenDao;
import com.mulodo.training.domain.Token;

/**
 * Validate token
 * @author Le Khoi
 *
 */
public class TokenFilter implements Filter {
	
	private ApplicationContext ctx;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException  {
		
		//get the application context to access spring beans
		ctx = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String token=null;
		
		try{
		
			//get the token from client
			token = httpRequest.getHeader("t");
			
			TokenDao tokenDao = (TokenDao) ctx.getBean("tokenDao");
			//check token
			Token tokenObj = tokenDao.findToken(token);
			if(tokenObj != null && !tokenObj.isReset()){ //is not a reset password token
				
				httpRequest.setAttribute("token",tokenObj); // attach the token object for further operations
				chain.doFilter(request, response); // let the request come through
				
			}else{
				
				System.out.println("invalid");
				httpResponse.setContentType("application/xml");
				PrintWriter out = httpResponse.getWriter();
				out.print(buildResponse(401,"Token invalid!"));
				response.flushBuffer(); //send response to client
			}
			
			
			
		}catch(NullPointerException ex){
			
			
			httpResponse.setContentType("application/xml");
			PrintWriter out = httpResponse.getWriter();
			out.print(buildResponse(401,"Token invalid!"));

			httpResponse.flushBuffer(); //send response to client
			ex.printStackTrace();
			
		}catch (NoSuchAlgorithmException e) {
			
			
			e.printStackTrace();
		} catch (HibernateException e) {
			
			httpResponse.setContentType("application/xml");
			PrintWriter out = httpResponse.getWriter();
			out.print(buildResponse(500,"Internal Error"));
			httpResponse.flushBuffer(); //send response to client
			e.printStackTrace();
		}finally{
			
			
		}
	}
	
	/**
	 * Build the response send back to client in xml format
	 * @param code This should be HTTP error code
	 * @param messege the messege attach to response
	 * @return The String that represent the xml content
	 * @throws IOException
	 */
	private String buildResponse(int code,String messege) throws IOException {
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>").append("<response>").append("<error><status>").
		append(code).append("</status>").append("<messege>").
		append(messege).append("</messege></error></response>");
		
		return sb.toString();
	}

	@Override
	public void destroy() {
		
		
	}

}
