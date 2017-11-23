package de.randomerror.chat;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.jms.Message;
import javax.naming.Context;
import javax.naming.InitialContext;

import de.fh_dortmund.inf.cw.chat.client.shared.ChatMessageHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.ServiceHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.UserSessionHandler;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.randomerror.chat.entities.User;
import de.randomerror.chat.interfaces.UserManagementRemote;
import de.randomerror.chat.interfaces.UserSessionRemote;

public class ServiceHandlerImpl extends ServiceHandler implements UserSessionHandler, ChatMessageHandler, MessageListener {

	private static ServiceHandler instance = new ServiceHandlerImpl();
	public static ServiceHandler getInstance() { return instance; }
	
	private Context ctx;
	private JmsContext jmsContext;
	
	private Queue messageQueue;
	private Topic messageTopic;
	private Topic disconnectTopic;
	
	private UserManagementRemote userManagement;
	private UserSessionRemote userSession;
	
	private ServiceHandlerImpl() {
		try {
			ctx = new InitialContext();
			
			userManagement = (UserManagementRemote) ctx.lookup("");
			userSession = (UserSessionRemote) ctx.lookup("");
			
			setupJms();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupJms() {
		try {
			ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("java:comp/DefaultJmsConnectionFactory");
			jmsContext = connectionFactory.createContext();
			
			messageQueue = (Queue) ctx.lookup("java:global/jms/MessageQueue");
			
			messageTopic = (Topic) ctx.lookup("java:global/jms/MessageTopic");
			disconnectTopic = (Topic) ctx.lookup("java:global/jms/DisconnectTopic");
			jmsContext.createConsumer(messageTopic).setMessageListener(this);
			jmsContext.createConsumer(disconnectTopic, "SENDER is " + getUserName()).setMessageListener(this);
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
		userSession.logout();
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

	@Override
	public void sendChatMessage(String message) {
		String username = getUserName();
		
		TextMessage jmsMessage = jmsContext.createTextMessage(message);
		jmsMessage.setStringProperty("USER", username);
		jmsContext.createProducer().send(messageQueue, jmsMessage);
	}
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		ChatMessage chatMessage = objectMessage.getObject(ChatMessage.class);
		
		notifyObservers(chatMessage);
	}

}
