package de.randomerror.chat.interfaces;

import javax.ejb.Local;

@Local
public interface UserManagementLocal extends UserManagement {
	boolean checkPassword(String username, String password);
}
