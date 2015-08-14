package com.mulodo.training.service;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.mulodo.training.dto.SignupFormDTO;

/**
 * Handle service and resource related to user
 * @author Le Khoi
 *
 */
public interface UserService  {

	/**
	 * Handle login operations
	 * @param request
	 * @return
	 */
	Response login(HttpServletRequest request);
	/**
	 * Handle logout operation
	 * @param request
	 * @return
	 */
	Response logout(HttpServletRequest request);
	/**
	 * Handle forgot password
	 * @param email
	 * @return
	 */
	Response forgotPasswordHelp(String email);
	Response createAccount(@Valid SignupFormDTO form);
	Response doResetPwd(
			@Valid  @NotNull @Size(min = 6 , max = 100) String pwd, 
			@Valid  @NotNull @Size(min = 6 , max = 100) String cfm,
			@Context HttpServletRequest request);
	Response generateCaptcha(HttpServletRequest request);
	Response sendRecoveryMail(HttpServletRequest request);
	/**
	 * resource get user list api
	 * @param page current page number
	 * @param limit number of items per page
	 * @return
	 */
	Response getUserList(Integer page, Integer limit);
	
	/**
	 * find by email api
	 * @param email the input email
	 * @return
	 */
	Response findByEmail(String email);
	
	/**
	 * find by user name api
	 * @param userName the input user name
	 * @param page page current page number
	 * @param limit limit number of items per page
	 * @return
	 */
	Response findByUserName(String userName, Integer page, Integer limit);
}
