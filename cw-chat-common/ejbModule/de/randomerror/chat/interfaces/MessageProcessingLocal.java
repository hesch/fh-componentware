package de.randomerror.chat.interfaces;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;

@Local
public interface MessageProcessingLocal extends MessageProcessing {
	void broadcastMessage(ChatMessage message);

	void broadcastDisconnectMessage(String sender);
}
