package de.randomerror.chat;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.randomerror.chat.entities.User;
import de.randomerror.chat.interfaces.MessageProcessingLocal;
import de.randomerror.chat.interfaces.UserManagementLocal;
import de.randomerror.chat.interfaces.UserManagementRemote;

@Singleton
public class UserManagementBean implements UserManagementLocal, UserManagementRemote {
	private Map<String, User> database = new HashMap<>();
	private List<User> loggedInUsers = new LinkedList<>();
	
	@Inject
	private HashBean pwEnc;
	
	@Inject
	private MessageProcessingLocal messageProcessing;
	
	public int getNumberOfRegisteredUser() {
		return database.size();
	}
	
	public void register(String username, String password) {
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setPasswordHash(pwEnc.hash(password));
		database.put(username, newUser);
		
		messageProcessing.broadcastMessage(ChatMessage.register(username));
	}
	
	public User login(String username, String password) {
		User u = getUser(username);
		if (!checkPassword(username, password))
			throw new IllegalArgumentException();
		loggedInUsers.add(u);
		
		messageProcessing.broadcastMessage(ChatMessage.login(username));
		
		return u;
	}

	@Override
	public void logout(String username) {
		User u = getUser(username);
		loggedInUsers.remove(u);
		
		messageProcessing.broadcastMessage(ChatMessage.logout(username));
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
	public void changeUserPassword(String username, String newPassword) {
		getUser(username).setPasswordHash(pwEnc.hash(newPassword));
	}

	@Override
	public void deleteUser(String username) {
		User u = getUser(username);
		database.remove(u);
	}

	@Override
	public User getUser(String username) {
		return database.get(username);
	}

	@Override
	public boolean checkPassword(String username, String password) {
		return database.get(username).getPasswordHash().equals(pwEnc.hash(password));
	}
}
