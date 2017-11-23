package de.randomerror.chat;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.randomerror.chat.interfaces.MessageProcessingLocal;
import de.randomerror.chat.interfaces.MessageProcessingRemote;

@MessageDriven(mappedName="MessageQueue")
public class MessageProcessingBean implements MessageProcessingLocal, MessageProcessingRemote, MessageListener {

	@Inject
	private JMSContext context;
	
	@Resource(lookup="java:global/jms/MessageTopic")
	private Topic messageTopic;
	
	@Resource(lookup="java:global/jms/DisconnectTopic")
	private Topic disconnectTopic;
	
	private List<String> blacklist = new LinkedList<String>() {{
		add("test");
	}};

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			String msg = textMessage.getText();
			blacklist.forEach(word -> {
				msg.replace(word, "***");
			});
			
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setText(msg);
			chatMessage.setDate(new Date());
			chatMessage.setSender(textMessage.getStringProperty("USER"));
			chatMessage.setType(ChatMessageType.TEXT);
			
			broadcastMessage(chatMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void broadcastMessage(ChatMessage msg) {
		try {
			ObjectMessage message = context.createObjectMessage();
			message.setObject(msg);
			context.createProducer().send(messageTopic, message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void broadcastDisconnectMessage(String sender) {
		try {
			ObjectMessage message = context.createObjectMessage();
			message.setObject(ChatMessage.disconnect(sender));
			message.setStringProperty("SENDER", sender);
			context.createProducer().send(disconnectTopic, message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
