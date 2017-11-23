package de.randomerror.chat.interfaces;



public interface UserSession {
	void changePassword(String oldPassword, String newPassword);
	void delete(String password);
	String getUserName();
	void login(String username, String password);
	void logout();
}
