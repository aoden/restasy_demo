package com.mulodo.training.interceptor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mulodo.training.domain.LikeInfor;
import com.mulodo.training.domain.Photo;
import com.mulodo.training.domain.Token;
import com.mulodo.training.domain.User;

/**
 * auto save date time created or modify when save/update entity
 * @author Le Khoi
 *
 */
@Component
@Scope(value = "prototype")
public class HibernateInterceptor extends EmptyInterceptor{

	
	private static final long serialVersionUID = 8275284339668214390L;

	@SuppressWarnings("deprecation")
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		
		if(entity instanceof User){
			
			setProperties(state, propertyNames, "modified", new Date());
			setProperties(state, propertyNames, "modifiedGMT", new Date().getHours());
			setProperties(state, propertyNames, "created", new Date());
			setProperties(state, propertyNames, "createdGMT", new Date().getHours());
		}
		else if(entity instanceof Photo){
			
			setProperties(state, propertyNames, "uploadDate", new Date());
		}else if(entity instanceof Token){
			
			setProperties(state, propertyNames, "createdDate", new Date());
		}else if(entity instanceof LikeInfor){
			
			setProperties(state, propertyNames, "created", new Date());
			setProperties(state, propertyNames, "modified", new Date());
		}
		
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		
		if(entity instanceof User){
			
			setProperties(currentState, propertyNames, "modified", new Date());
			setProperties(currentState, propertyNames, "modifiedGMT", new Date().getHours());
		} else if(entity instanceof LikeInfor){
			
			setProperties(currentState, propertyNames, "modified", new Date());
		}
		
		return true;
	}

	private void setProperties(Object[] state,String[] propertyNames,String propertyToSet,Object value){
		
		int index = Arrays.asList(propertyNames).indexOf(propertyToSet);
		if(index >= 0){
			
			state[index] = value;
		}
	}

}
