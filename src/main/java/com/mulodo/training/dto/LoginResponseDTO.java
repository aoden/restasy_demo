package com.mulodo.training.dto;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class LoginResponseDTO extends ResponseDTO{

	
	protected String token;
	protected String userName;
	protected String userId;
	
	
	
	public String getUserName() {
		return userName;
	}
	@XmlElement
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	@XmlElement
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@XmlElement
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LoginResponseDTO(ErrorDTO errorDTO, String token) {
		//super();
		this.errorDTO = errorDTO;
		this.token = token;
	}
	public LoginResponseDTO() {
		super();
	}
	
	
}
