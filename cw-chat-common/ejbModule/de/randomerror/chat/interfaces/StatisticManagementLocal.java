package de.randomerror.chat.interfaces;

import javax.ejb.Local;

@Local
public interface StatisticManagementLocal extends StatisticManagement {
	void newLogin();
	void newLogout();
	void newMessage();
	void startStatisticTimer();
	void stopStatisticTimer();
}
