package com.mulodo.training.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class ResponseDTO {

	protected ErrorDTO errorDTO = new ErrorDTO();

	public ErrorDTO getErrorDTO() {
		return errorDTO;
	}
	
	@XmlElement(name="error")
	public void setErrorDTO(ErrorDTO errorDTO) {
		this.errorDTO = errorDTO;
	}
	
	
}
