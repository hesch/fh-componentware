package de.randomerror.chat.entities;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import de.randomerror.chat.entities.AbstractEntity;

public class CreateUpdateListener {
	@PrePersist
	public void newEntity(Object entity) {
		if(entity instanceof AbstractEntity) {
			AbstractEntity e = (AbstractEntity) entity;
			
			e.setCreatedAt(new Date());
		}
	}
	
	@PreUpdate
	public void updateEntity(Object entity) {
		if(entity instanceof AbstractEntity) {
			AbstractEntity e = (AbstractEntity) entity;
			
			e.setUpdatedAt(new Date());
		}
	}
}
