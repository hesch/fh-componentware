package de.randomerror.chat.interfaces;

import javax.ejb.Local;

import de.randomerror.chat.entities.User;
import de.randomerrror.chat.exceptions.AuthenticationFailedException;
import de.randomerrror.chat.exceptions.UserNotFoundException;

@Local
public interface UserManagementLocal extends UserManagement {
	boolean checkPassword(String username, String password);
	User login(String username, String password) throws AuthenticationFailedException, UserNotFoundException;
	void logout(String username) throws UserNotFoundException;
	void changeUserPassword(String username, String newPassword) throws UserNotFoundException;
	User getUser(String username) throws UserNotFoundException;
	void deleteUser(String username) throws UserNotFoundException;
	void countMessage(String username);
}
