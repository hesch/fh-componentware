package de.randomerror.chat;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
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
import de.randomerror.chat.interfaces.BroadcastingLocal;
import de.randomerror.chat.interfaces.BroadcastingRemote;

@MessageDriven(mappedName="java:global/jms/MessageQueue", messageListenerInterface=MessageListener.class, activationConfig= {
		@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue")
})
public class MessageProcessingBean implements MessageListener {

	@Inject
	private BroadcastingLocal broadcast;
	
	private List<String> blacklist = new LinkedList<String>() {{
		add("test");
	}};

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("message received");
			TextMessage textMessage = (TextMessage) message;
			String msg = textMessage.getText();
			
			for(String word: blacklist) {
				msg = msg.replace(word, "***");
			}
			
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setText(msg);
			chatMessage.setDate(new Date());
			chatMessage.setSender(textMessage.getStringProperty("USER"));
			chatMessage.setType(ChatMessageType.TEXT);
			
			broadcast.broadcastMessage(chatMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
