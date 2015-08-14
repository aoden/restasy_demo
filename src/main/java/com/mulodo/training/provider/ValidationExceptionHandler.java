package com.mulodo.training.provider;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;



public class ValidationExceptionHandler implements ExceptionMapper<ValidationException> 
{
   @Override
   public Response toResponse(ValidationException exception) 
   {
	   exception.printStackTrace();
       return Response.status(Status.UNAUTHORIZED).entity("Fill all fields").build();
   }
}