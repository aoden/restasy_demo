package com.mulodo.training.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="users")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2634252615490409523L;

	@Id @GeneratedValue
	protected int id;
	
	protected String email;
	@Column(name="user_name")
	protected String userName;
	@DateTimeFormat
	protected Date created;
	@DateTimeFormat
	protected Date modified;
	@Column(name="created_gmt")
	protected int createdGMT;
	@Column(name="modified_gmt")
	protected int modifiedGMT;
	protected String password;
	protected byte[] avatar;
	
	@OneToMany(mappedBy="user")
	protected Set<LikeInfor> likeInfors  = new HashSet<LikeInfor>();
	@OneToMany(mappedBy="user")
	protected Set<Photo> photos = new HashSet<Photo>();
	@OneToMany(mappedBy="user")
	protected Set<Token> tokens = new HashSet<Token>();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public int getCreatedGMT() {
		return createdGMT;
	}
	public void setCreatedGMT(int createdGMT) {
		this.createdGMT = createdGMT;
	}
	public int getModifiedGMT() {
		return modifiedGMT;
	}
	public void setModifiedGMT(int modifiedGMT) {
		this.modifiedGMT = modifiedGMT;
	}
	public Set<LikeInfor> getLikeInfors() {
		return likeInfors;
	}
	public void setLikeInfors(Set<LikeInfor> likeInfors) {
		this.likeInfors = likeInfors;
	}
	public Set<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}
	public Set<Token> getTokens() {
		return tokens;
	}
	public void setTokens(Set<Token> tokens) {
		this.tokens = tokens;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public byte[] getAvatar() {
		return avatar;
	}
	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}
	
	
	
	
}
