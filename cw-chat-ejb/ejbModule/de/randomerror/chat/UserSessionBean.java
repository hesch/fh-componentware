package de.randomerror.chat;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.inject.Inject;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.randomerror.chat.entities.User;
import de.randomerror.chat.interfaces.MessageProcessingLocal;
import de.randomerror.chat.interfaces.UserManagementLocal;
import de.randomerror.chat.interfaces.UserSessionLocal;
import de.randomerror.chat.interfaces.UserSessionRemote;

@Stateful
public class UserSessionBean implements UserSessionLocal, UserSessionRemote {
	private User user;
	
	@Inject
	private UserManagementLocal userManagement;
	
	@Inject
	private MessageProcessingLocal messageProcessing;

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		if (user == null)
			throw new IllegalStateException();
		
		if(userManagement.checkPassword(user.getUsername(), oldPassword))
			userManagement.changeUserPassword(user.getUsername(), newPassword);
	}

	@Override
	public void delete(String password) {
		if(userManagement.checkPassword(user.getUsername(), password)) {
			userManagement.logout(user.getUsername());
			userManagement.deleteUser(user.getUsername());
		}
	}

	@Override
	public String getUserName() {
		return user.getUsername();
	}

	@Override
	public void login(String username, String password) {
		User u = userManagement.login(username, password);
		messageProcessing.broadcastDisconnectMessage(username);
		user = u;
	}

	@Override
	@Remove
	public void logout() {
		userManagement.logout(user.getUsername());
	}
	
}
