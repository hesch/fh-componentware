package de.randomerror.chat.interfaces;

import java.util.List;

import de.randomerror.chat.entities.User;
import de.randomerrror.chat.exceptions.AuthenticationFailedException;
import de.randomerrror.chat.exceptions.UserAlreadyExistsException;
import de.randomerrror.chat.exceptions.UserNotFoundException;

public interface UserManagement {
	void register(String username, String password) throws UserAlreadyExistsException;
	List<User> getOnlineUsers();
	int getNumberOfRegisteredUsers();
	
	void nuke();
}
