package de.fh_dortmund.inf.cw.chat.server.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import de.randomerror.chat.entities.AbstractEntity;

@MappedSuperclass
public abstract class Statistic extends AbstractEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false)
	private long id;
	
	@Column(nullable=false)
	private int logins;
	@Column(nullable=false)
	private int logouts;
	@Column(nullable=false)
	private int messages;
	
	public int getLogins() {
		return logins;
	}
	public void setLogins(int logins) {
		this.logins = logins;
	}
	public int getLogouts() {
		return logouts;
	}
	public void setLogouts(int logouts) {
		this.logouts = logouts;
	}
	public int getMessages() {
		return messages;
	}
	public void setMessages(int messages) {
		this.messages = messages;
	}
}
