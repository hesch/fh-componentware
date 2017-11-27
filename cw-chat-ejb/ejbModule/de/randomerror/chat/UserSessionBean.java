package de.randomerror.chat;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.inject.Inject;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.randomerror.chat.entities.User;
import de.randomerror.chat.interfaces.BroadcastingLocal;
import de.randomerror.chat.interfaces.UserManagementLocal;
import de.randomerror.chat.interfaces.UserSessionLocal;
import de.randomerror.chat.interfaces.UserSessionRemote;
import de.randomerrror.chat.exceptions.AuthenticationFailedException;
import de.randomerrror.chat.exceptions.NotAuthenticatedException;
import de.randomerrror.chat.exceptions.UserNotFoundException;

@Stateful
public class UserSessionBean implements UserSessionLocal, UserSessionRemote {
	private User user;
	
	@Inject
	private UserManagementLocal userManagement;
	
	@Inject
	private BroadcastingLocal broadcast;

	@Override
	public void changePassword(String oldPassword, String newPassword) throws NotAuthenticatedException, UserNotFoundException, AuthenticationFailedException {
		if(!userManagement.checkPassword(getUserName(), oldPassword))
			throw new AuthenticationFailedException();
		userManagement.changeUserPassword(getUserName(), newPassword);
	}

	@Override
	@Remove
	public void delete(String password) throws UserNotFoundException, NotAuthenticatedException {
		if(userManagement.checkPassword(getUserName(), password)) {
			this.logout();
			userManagement.deleteUser(getUserName());
		}
	}

	@Override
	public String getUserName() throws NotAuthenticatedException {
		if (user == null)
			throw new NotAuthenticatedException();
		return user.getUsername();
	}

	@Override
	public void login(String username, String password) throws AuthenticationFailedException, UserNotFoundException {
		User u = userManagement.login(username, password);
		broadcast.broadcastDisconnectMessage(username);
		user = u;
	}

	@Override
	@Remove
	public void logout() throws NotAuthenticatedException, UserNotFoundException {
		userManagement.logout(getUserName());
		broadcast.broadcastMessage(ChatMessage.logout(getUserName()));
	}

	@Override
	public UserStatistic getUserStatistic() throws NotAuthenticatedException {
		if (user == null)
			throw new NotAuthenticatedException();
		return user.getStatistic();
	}
	
}
