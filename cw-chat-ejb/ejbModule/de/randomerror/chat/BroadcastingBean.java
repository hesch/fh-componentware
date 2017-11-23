package de.randomerror.chat;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.randomerror.chat.interfaces.BroadcastingLocal;
import de.randomerror.chat.interfaces.BroadcastingRemote;

@Stateless
public class BroadcastingBean implements BroadcastingLocal, BroadcastingRemote {

	@Inject
	private JMSContext context;
	
	@Resource(lookup="java:global/jms/MessageTopic")
	private Topic messageTopic;
	
	@Resource(lookup="java:global/jms/DisconnectTopic")
	private Topic disconnectTopic;
	
	
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
