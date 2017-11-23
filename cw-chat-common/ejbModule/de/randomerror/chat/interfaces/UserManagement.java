package de.randomerror.chat.interfaces;

import java.util.List;

import de.randomerror.chat.entities.User;

public interface UserManagement {
	User login(String username, String password);
	void logout(String username);
	void register(String username, String password);
	int getNumberOfRegisteredUsers();
	List<User> getOnlineUsers();
	void changeUserPassword(String username, String newPassword);
	void deleteUser(String username);
	User getUser(String username);
	
}
