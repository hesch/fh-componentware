package de.randomerror.chat;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import de.randomerror.chat.entities.User;
import de.randomerror.chat.interfaces.UserManagementLocal;
import de.randomerror.chat.interfaces.UserManagementRemote;

@Startup
@Singleton
public class UserManagementBean implements UserManagementLocal, UserManagementRemote {
	private List<User> database = new LinkedList<>();
	
	@Inject
	private HashBean pwEnc;
	
	public int getNumberOfRegisteredUser() {
		return database.size();
	}
	
	public void register(String username, String password) {
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setPasswordHash(pwEnc.hash(password));
		database.add(newUser);
	}
	
	public void login(String username, String password) {
		
	}

	@Override
	public void logout(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumberOfRegisteredUsers() {
		return database.size();
	}

	@Override
	public List<User> getOnlineUsers() {
		return null;
	}

	@Override
	public void changeUserPassword(String username, String newPassword) {
		getUser(username).setPasswordHash(pwEnc.hash(newPassword));
	}

	@Override
	public void deleteUser(String username) {
		
	}

	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}
}
