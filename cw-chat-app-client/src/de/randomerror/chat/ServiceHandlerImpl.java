package de.randomerror.chat;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;

import de.fh_dortmund.inf.cw.chat.client.shared.ChatMessageHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.ServiceHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.StatisticHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.UserSessionHandler;
import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.randomerror.chat.entities.User;
import de.randomerror.chat.interfaces.StatisticManagementRemote;
import de.randomerror.chat.interfaces.UserManagementRemote;
import de.randomerror.chat.interfaces.UserSessionRemote;
import de.randomerrror.chat.exceptions.NotAuthenticatedException;
import de.randomerrror.chat.exceptions.UserNotFoundException;

public class ServiceHandlerImpl extends ServiceHandler
		implements UserSessionHandler, ChatMessageHandler, MessageListener, StatisticHandler {

	private static ServiceHandler instance = new ServiceHandlerImpl();

	public static ServiceHandler getInstance() {
		return instance;
	}
	
	public static void reinit() {
		instance = new ServiceHandlerImpl();
	}

	private Context ctx;
	private JMSContext jmsContext;

	private Queue messageQueue;
	private Topic messageTopic;
	private Topic disconnectTopic;

	private UserManagementRemote userManagement;
	private UserSessionRemote userSession;
	private StatisticManagementRemote statistics;

	private ServiceHandlerImpl() {
		try {
			ctx = new InitialContext();

			userManagement = (UserManagementRemote) ctx.lookup(
					"java:global/cw-chat-ear/cw-chat-ejb/UserManagementBean!de.randomerror.chat.interfaces.UserManagementRemote");
			userSession = (UserSessionRemote) ctx.lookup(
					"java:global/cw-chat-ear/cw-chat-ejb/UserSessionBean!de.randomerror.chat.interfaces.UserSessionRemote");
			statistics = (StatisticManagementRemote) ctx.lookup(
					"java:global/cw-chat-ear/cw-chat-ejb/StatisticManagementBean!de.randomerror.chat.interfaces.StatisticManagementRemote");

			setupJms();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setupJms() {
		try {
			ConnectionFactory connectionFactory = (ConnectionFactory) ctx
					.lookup("java:comp/DefaultJMSConnectionFactory");
			jmsContext = connectionFactory.createContext();

			messageQueue = (Queue) ctx.lookup("java:global/jms/MessageQueue");

			messageTopic = (Topic) ctx.lookup("java:global/jms/MessageTopic");
			disconnectTopic = (Topic) ctx.lookup("java:global/jms/DisconnectTopic");
			jmsContext.createConsumer(messageTopic).setMessageListener(this);
		} catch (Exception e) {
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
		try {
			userSession.logout();
		} catch (NotAuthenticatedException | UserNotFoundException e) {
			e.printStackTrace();
		}
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
		return userManagement.getOnlineUsers().stream().map(User::getUsername).collect(Collectors.toList());
	}

	@Override
	public String getUserName() {
		try {
			return userSession.getUserName();
		} catch (NotAuthenticatedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void login(String username, String password) throws Exception {
		userSession.login(username, password);
		jmsContext.createConsumer(disconnectTopic, "SENDER = '" + getUserName() + "'").setMessageListener(this);
	}

	@Override
	public void logout() throws Exception {
		userSession.logout();
	}

	@Override
	public void register(String username, String password) throws Exception {
		userManagement.register(username, password);
	}

	@Override
	public void sendChatMessage(String message) {
		try {
			String username = getUserName();

			TextMessage jmsMessage = jmsContext.createTextMessage(message);

			jmsMessage.setStringProperty("USER", username);

			jmsContext.createProducer().send(messageQueue, jmsMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Message message) {
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			ChatMessage chatMessage = (ChatMessage) objectMessage.getObject();
			
			System.out.println("message received: " + chatMessage.getType().getValue());
			
			setChanged();
			notifyObservers(chatMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void nuke() {
		userManagement.nuke();
	}

	@Override
	public List<CommonStatistic> getStatistics() {
		return statistics.getStatistics();
	}

	@Override
	public UserStatistic getUserStatistic() {
		try {
			return userSession.getUserStatistic();
		} catch (NotAuthenticatedException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
