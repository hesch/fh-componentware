package de.randomerror.chat.test;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;

import de.fh_dortmund.inf.cw.chat.client.shared.ChatMessageHandler;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.randomerror.chat.ServiceHandlerImpl;

public class ChatMessageHandlerTest implements Observer {
	private ServiceHandlerImpl serviceHandler;
	
	private CountDownLatch observerLatch;
	
	@Before
	public void before() throws Exception {
		((ServiceHandlerImpl) ServiceHandlerImpl.getInstance()).nuke();
		ServiceHandlerImpl.reinit();
		serviceHandler = (ServiceHandlerImpl) ServiceHandlerImpl.getInstance();
		
		serviceHandler.addObserver(this);
		observerLatch = new CountDownLatch(1);
	}
	

	@Test(timeout=1000)
	public void testSendChatMessage() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test", "test");
		String theMessage = "message";
		serviceHandler.sendChatMessage(theMessage);
		observerLatch.await();
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o == serviceHandler && arg instanceof ChatMessage) {
			ChatMessage message = (ChatMessage) arg;
			if(message.getType() == ChatMessageType.TEXT) {
				observerLatch.countDown();
			}
		}
		observerLatch.countDown();
	}
}