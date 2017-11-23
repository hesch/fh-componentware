package de.randomerror.chat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import de.randomerror.chat.entities.User;
import de.randomerror.chat.interfaces.UserManagementLocal;
import de.randomerror.chat.interfaces.UserManagementRemote;

@Startup
@Singleton
public class UserManagementBean implements UserManagementLocal, UserManagementRemote {
	private Map<String, User> database = new HashMap<>();
	private List<User> loggedInUsers = new LinkedList<>();
	
	@Inject
	private HashBean pwEnc;
	
	public int getNumberOfRegisteredUser() {
		return database.size();
	}
	
	public void register(String username, String password) {
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setPasswordHash(pwEnc.hash(password));
		database.put(username, newUser);
	}
	
	public void login(String username) {
		User u = getUser(username);
		loggedInUsers.add(u);
	}

	@Override
	public void logout(String username) {
		User u = getUser(username);
		loggedInUsers.remove(u);
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
}
