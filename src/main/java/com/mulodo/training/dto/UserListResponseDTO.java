package com.mulodo.training.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class UserListResponseDTO extends ResponseDTO {

	protected List<Integer> ids = new ArrayList<Integer>();
	protected List<String> userNames = new ArrayList<String>();
	protected List<String> avatars = new ArrayList<String>();
	protected List<String> emails = new ArrayList<String>();
	
	public List<Integer> getIds() {
		return ids;
	}
	@XmlElement(name = "id")
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	public List<String> getUserNames() {
		return userNames;
	}
	@XmlElement(name = "userName")
	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}
	public List<String> getAvatars() {
		return avatars;
	}
	@XmlElement(name = "avatar")
	public void setAvatars(List<String> avatars) {
		this.avatars = avatars;
	}
	public List<String> getEmails() {
		return emails;
	}
	@XmlElement(name = "email")
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	
	
	
}
