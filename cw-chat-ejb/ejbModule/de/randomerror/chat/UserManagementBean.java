package de.randomerror.chat;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;

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

@Singleton
public class UserManagementBean implements UserManagementLocal, UserManagementRemote {
	private Map<String, User> database = new HashMap<>();
	private List<User> loggedInUsers = new LinkedList<>();
	
	@Inject
	private HashBean pwEnc;
	
	@Inject
	private BroadcastingLocal broadcast;
	
	@Inject
	private StatisticManagementLocal statistics;
	
	public int getNumberOfRegisteredUser() {
		return database.size();
	}
	
	@Override
	public void register(String username, String password) throws UserAlreadyExistsException {
		if (database.containsKey(username))
			throw new UserAlreadyExistsException();
		
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setPasswordHash(pwEnc.hash(password));
		newUser.setStatistic(new UserStatistic());
		database.put(username, newUser);
		
		broadcast.broadcastMessage(ChatMessage.register(username));
	}
	
	@Override
	public User login(String username, String password) throws UserNotFoundException, AuthenticationFailedException {
		User u = getUser(username);
		if (!checkPassword(username, password))
			throw new AuthenticationFailedException();
		
		loggedInUsers.add(u);
		if(loggedInUsers.size() == 1)
			statistics.startStatisticTimer();
		UserStatistic stat = u.getStatistic();
		stat.setLastLogin(new Date());
		stat.setLogins(stat.getLogins() + 1);
		u.setStatistic(stat);
		
		statistics.newLogin();
		
		broadcast.broadcastMessage(ChatMessage.login(username));
		
		return u;
	}

	@Override
	public void logout(String username) throws UserNotFoundException {
		User u = getUser(username);
		loggedInUsers.remove(u);
		
		if(loggedInUsers.isEmpty())
			statistics.stopStatisticTimer();
		
		UserStatistic stat = u.getStatistic();
		stat.setLogouts(stat.getLogouts() + 1);
		u.setStatistic(stat);
		
		statistics.newLogout();
		
		broadcast.broadcastMessage(ChatMessage.logout(username));
	}

	@Override
	public int getNumberOfRegisteredUsers() {
		return database.size();
	}

	@Override
	public List<User> getOnlineUsers() {
		return loggedInUsers;
	}

	@Override
	public void changeUserPassword(String username, String newPassword) throws UserNotFoundException {
		getUser(username).setPasswordHash(pwEnc.hash(newPassword));
	}

	@Override
	public void deleteUser(String username) throws UserNotFoundException {
		User u = getUser(username);
		database.remove(u);
	}

	@Override
	public User getUser(String username) throws UserNotFoundException {
		if(!database.containsKey(username))
			throw new UserNotFoundException();
		return database.get(username);
	}

	@Override
	public boolean checkPassword(String username, String password) {
		return database.get(username).getPasswordHash().equals(pwEnc.hash(password));
	}

	@Override
	public void nuke() {
		database = new HashMap<>();
		loggedInUsers = new LinkedList<>();
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
