package de.randomerror.chat.interfaces;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.randomerrror.chat.exceptions.AuthenticationFailedException;
import de.randomerrror.chat.exceptions.NotAuthenticatedException;
import de.randomerrror.chat.exceptions.UserNotFoundException;

public interface UserSession {
	void changePassword(String oldPassword, String newPassword) throws UserNotFoundException, NotAuthenticatedException, AuthenticationFailedException;
	void delete(String password) throws UserNotFoundException, NotAuthenticatedException;
	String getUserName() throws NotAuthenticatedException;
	UserStatistic getUserStatistic() throws NotAuthenticatedException;
	void login(String username, String password) throws AuthenticationFailedException, UserNotFoundException;
	void logout() throws NotAuthenticatedException, UserNotFoundException;
}
