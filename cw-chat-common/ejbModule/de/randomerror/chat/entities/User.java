package de.randomerror.chat.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;

@NamedQueries({
	@NamedQuery(
			name="User.findAll",
			query="SELECT u FROM User u"),
	@NamedQuery(
			name="User.findByUsername",
			query="SELECT u FROM User u WHERE u.username = :username"),
	@NamedQuery(
			name="User.findByLoggedIn",
			query="SELECT u FROM User u WHERE u.loggedIn = true"),
	@NamedQuery(
			name="User.count",
			query="SELECT COUNT(1) FROM User u"),
	@NamedQuery(
			name="User.countByLoggedIn",
			query="SELECT COUNT(1) FROM User u WHERE u.loggedIn = true")
})
@Table(name="CHAT_USER")
@Entity
public class User extends AbstractEntity implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false)
	private long id;
	
	@Column(nullable=false)
	@Basic(optional=false)
	private String username;
	
	@Column(nullable=false)
	@Basic(optional=false)
	private String passwordHash;
	
	@Column(nullable=false)
	private boolean loggedIn;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="statistic_id", unique=true, nullable=false)
	private UserStatistic statistic;
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
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
