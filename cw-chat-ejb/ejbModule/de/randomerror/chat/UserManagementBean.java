package de.randomerror.chat;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.randomerror.chat.entities.User;
import de.randomerror.chat.interfaces.BroadcastingLocal;
import de.randomerror.chat.interfaces.StatisticManagementLocal;
import de.randomerror.chat.interfaces.UserManagementLocal;
import de.randomerror.chat.interfaces.UserManagementRemote;
import de.randomerrror.chat.exceptions.AuthenticationFailedException;
import de.randomerrror.chat.exceptions.UserAlreadyExistsException;
import de.randomerrror.chat.exceptions.UserNotFoundException;

@Stateless
public class UserManagementBean implements UserManagementLocal, UserManagementRemote {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject
	private HashBean pwEnc;
	
	@Inject
	private BroadcastingLocal broadcast;
	
	@Inject
	private StatisticManagementLocal statistics;
	
	@Override
	public void register(String username, String password) throws UserAlreadyExistsException {
		TypedQuery<User> q = entityManager.createNamedQuery("User.findByUsername", User.class);
		q.setParameter("username", username);
		if (!q.getResultList().isEmpty())
			throw new UserAlreadyExistsException();
		
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setPasswordHash(pwEnc.hash(password));
		newUser.setStatistic(new UserStatistic());
		
		entityManager.persist(newUser);
		
		broadcast.broadcastMessage(ChatMessage.register(username));
	}
	
	@Override
	public User login(String username, String password) throws UserNotFoundException, AuthenticationFailedException {
		User u = getUser(username);
		if (!checkPassword(username, password))
			throw new AuthenticationFailedException();
		
		u.setLoggedIn(true);
		
		if(getOnlineUsers().size() == 1)
			statistics.startStatisticTimer();
		
		UserStatistic stat = u.getStatistic();
		stat.setLastLogin(new Date());
		stat.setLogins(stat.getLogins() + 1);
		u.setStatistic(stat);
		
		statistics.newLogin();
		
		entityManager.merge(u);
		
		broadcast.broadcastMessage(ChatMessage.login(username));
		
		return u;
	}

	@Override
	public void logout(String username) throws UserNotFoundException {
		User u = getUser(username);
		u.setLoggedIn(false);
		
		if(getOnlineUsers().isEmpty())
			statistics.stopStatisticTimer();
		
		UserStatistic stat = u.getStatistic();
		stat.setLogouts(stat.getLogouts() + 1);
		u.setStatistic(stat);
		
		statistics.newLogout();
		
		entityManager.merge(u);
		
		broadcast.broadcastMessage(ChatMessage.logout(username));
	}

	@Override
	public int getNumberOfRegisteredUsers() {
		TypedQuery<Long> q = entityManager.createNamedQuery("User.count", Long.class);
		return q.getSingleResult().intValue();
	}

	@Override
	public List<User> getOnlineUsers() {
		TypedQuery<User> q = entityManager.createNamedQuery("User.findByLoggedIn", User.class);
		return q.getResultList();
	}

	@Override
	public void changeUserPassword(String username, String newPassword) throws UserNotFoundException {
		getUser(username).setPasswordHash(pwEnc.hash(newPassword));
	}

	@Override
	public void deleteUser(String username) throws UserNotFoundException {
		User u = getUser(username);
		entityManager.remove(u);
	}

	@Override
	public User getUser(String username) throws UserNotFoundException {
		TypedQuery<User> q = entityManager.createNamedQuery("User.findByUsername", User.class);
		q.setParameter("username", username);
		try {
			return q.getSingleResult();
		} catch(NoResultException e) {
			throw new UserNotFoundException();
		}
	}

	@Override
	public boolean checkPassword(String username, String password) throws UserNotFoundException {
		return getUser(username).getPasswordHash().equals(pwEnc.hash(password));
	}

	@Override
	public void nuke() {
		//TODO
	}

	@Override
	public void countMessage(String username) {
		try {
			User u = getUser(username);
			u.getStatistic().setMessages(u.getStatistic().getMessages() + 1);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}
}
