package com.mulodo.training.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="error")
public class ErrorDTO {
	
	
	protected Integer status;
	
	protected String messege;
	
	
	public ErrorDTO(Integer status, String messege) {
		super();
		this.status = status;
		this.messege = messege;
	}
	public Integer getStatus() {
		return status;
	}
	@XmlElement
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessege() {
		return messege;
	}
	@XmlElement
	public void setMessege(String messege) {
		this.messege = messege;
	}
	public ErrorDTO() {
		super();
	}
	
	
}
