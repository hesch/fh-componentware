package de.randomerror.chat.entities;

import java.io.Serializable;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;

public class User implements Serializable {
	private String username;
	private String passwordHash;
	
	private UserStatistic statistic;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public UserStatistic getStatistic() {
		return statistic;
	}
	public void setStatistic(UserStatistic statistic) {
		this.statistic = statistic;
	}
}
