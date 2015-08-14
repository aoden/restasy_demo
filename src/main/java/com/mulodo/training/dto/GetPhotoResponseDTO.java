package com.mulodo.training.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class GetPhotoResponseDTO  extends ResponseDTO{

	
	protected List<String> photo = new ArrayList<String>();
	protected String comment;
	protected Integer imageId;
	
	
	
	public Integer getImageId() {
		return imageId;
	}
	@XmlElement
	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public List<String> getPhoto() {
		return photo;
	}
	
	@XmlElement
	public void setPhoto(List<String> photo) {
		this.photo = photo;
	}
	public String getComment() {
		return comment;
	}
	@XmlElement
	public void setComment(String comment) {
		this.comment = comment;
	}

	
	
}
