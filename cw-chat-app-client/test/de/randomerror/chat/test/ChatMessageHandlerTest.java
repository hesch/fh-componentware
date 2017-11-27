package de.randomerror.chat.test;

import org.junit.Before;
import org.junit.Test;

import de.fh_dortmund.inf.cw.chat.client.shared.ChatMessageHandler;
import de.randomerror.chat.ServiceHandlerImpl;

public class ChatMessageHandlerTest {
	private ChatMessageHandler serviceHandler;
	
	@Before
	public void before() throws Exception {
		((ServiceHandlerImpl) ServiceHandlerImpl.getInstance()).nuke();
		ServiceHandlerImpl.reinit();
		serviceHandler = (ChatMessageHandler) ServiceHandlerImpl.getInstance();
	}

	@Test
	public void testSendChatMessage() throws Exception {
		//TODO
	}
}