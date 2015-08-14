package com.mulodo.training.dto;

import java.io.InputStream;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.Email;
import org.jboss.resteasy.annotations.providers.multipart.PartType;


public class SignupFormDTO {
	
	@FormParam("email")
	@PartType("text/plain")
	protected String email;
	@FormParam("userName")
	@PartType("text/plain")
	protected String userName;
	@FormParam("pwd")
	@PartType("text/plain")
	protected String pwd;
	@FormParam("cfm")
	@PartType("text/plain")
	protected String cfm;
	
	@FormParam("avatar")
	@PartType("image/jpeg")
	protected InputStream avatar;
	
	@Valid @NotNull @Email @Size(min = 6)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Valid @NotNull @Size(min = 6, max = 50)
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Valid @NotNull @Size(min = 6, max = 50)
	public String getPwd() {
		return pwd;
	}
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	@Valid @NotNull @Size(min = 6, max = 50)
	public String getCfm() {
		return cfm;
	}
	public void setCfm(String cfm) {
		this.cfm = cfm;
	}
	
	public InputStream getAvatar() {
		return avatar;
	}
	public void setAvatar(InputStream avatar) {
		this.avatar = avatar;
	}
	
	
	
}
