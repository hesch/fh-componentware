package de.randomerror.chat.interfaces;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;

@Local
public interface BroadcastingLocal extends Broadcasting {
	void broadcastMessage(ChatMessage message);

	void broadcastDisconnectMessage(String sender);
}
