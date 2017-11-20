package de.randomerror.chat;

import java.util.List;

import de.fh_dortmund.inf.cw.chat.client.shared.ServiceHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.UserSessionHandler;

public class ServiceHandlerImpl extends ServiceHandler implements UserSessionHandler {

	@Override
	public void changePassword(String oldPassword, String newPassword) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String password) throws Exception {
		// TODO Auto-generated method stub-–
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumberOfOnlineUsers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfRegisteredUsers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getOnlineUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void login(String username, String password) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(String username, String password) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
