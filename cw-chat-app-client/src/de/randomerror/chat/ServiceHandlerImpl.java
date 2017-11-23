package de.randomerror.chat;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;

import de.fh_dortmund.inf.cw.chat.client.shared.ServiceHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.UserSessionHandler;
import de.randomerror.chat.entities.User;
import de.randomerror.chat.interfaces.UserManagementRemote;
import de.randomerror.chat.interfaces.UserSessionRemote;

public class ServiceHandlerImpl extends ServiceHandler implements UserSessionHandler {

	private Context ctx;
	
	private UserManagementRemote userManagement;
	private UserSessionRemote userSession;
	
	@PostConstruct
	public void init() {
		try {
			ctx = new InitialContext();
			
			userManagement = (UserManagementRemote) ctx.lookup("");
			userSession = (UserSessionRemote) ctx.lookup("");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void changePassword(String oldPassword, String newPassword) throws Exception {
		userSession.changePassword(oldPassword, newPassword);
	}

	@Override
	public void delete(String password) throws Exception {
		userSession.delete(password);
	}

	@Override
	public void disconnect() {
		//TODO what does this?!
	}

	@Override
	public int getNumberOfOnlineUsers() {
		return userManagement.getOnlineUsers().size();
	}

	@Override
	public int getNumberOfRegisteredUsers() {
		return userManagement.getNumberOfRegisteredUsers();
	}

	@Override
	public List<String> getOnlineUsers() {
		return userManagement
				.getOnlineUsers()
				.stream()
				.map(User::getUsername)
				.collect(Collectors.toList());
	}

	@Override
	public String getUserName() {
		return userSession.getUserName();
	}

	@Override
	public void login(String username, String password) throws Exception {
		userSession.login(username, password);
	}

	@Override
	public void logout() throws Exception {
		userSession.logout();
	}

	@Override
	public void register(String username, String password) throws Exception {
		userManagement.register(username, password);
	}

}
