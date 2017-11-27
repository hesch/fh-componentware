package de.fh_dortmund.inf.cw.chat.server.shared;

import java.io.Serializable;
import java.util.Date;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;


public class ChatMessage implements Serializable {
	private String text;
	private String sender;
	private Date date;
	private ChatMessageType type;
	
	public static ChatMessage register(String sender) {
		ChatMessage message = new ChatMessage();
		
		message.setSender(sender);
		message.setDate(new Date());
		message.setText("");
		message.setType(ChatMessageType.REGISTER);
		
		return message;
	}
	
	public static ChatMessage login(String sender) {
		ChatMessage message = new ChatMessage();
		
		message.setSender(sender);
		message.setDate(new Date());
		message.setText("");
		message.setType(ChatMessageType.LOGIN);
		
		return message;
	}
	
	public static ChatMessage logout(String sender) {
		ChatMessage message = new ChatMessage();
		
		message.setSender(sender);
		message.setDate(new Date());
		message.setText("");
		message.setType(ChatMessageType.LOGOUT);
		
		return message;
	}
	
	public static ChatMessage disconnect(String sender) {
		ChatMessage message = new ChatMessage();
		
		message.setSender(sender);
		message.setDate(new Date());
		message.setText("");
		message.setType(ChatMessageType.DISCONNECT);
		
		return message;
	}
	
	public static ChatMessage statistic(CommonStatistic halfHourStatistic) {
		ChatMessage message = new ChatMessage();
		
		String chatText = String.format(
				"Statistik der letzten halben Stunde:\n" +
				"Anzahl der Anmeldungen: %d\n" +
				"Anzahl der Abmeldungen: %d\n" +
				"Anzahl der geschriebenen Nachrichten: %d",
				halfHourStatistic.getLogins(),
				halfHourStatistic.getLogouts(),
				halfHourStatistic.getMessages());
		
		message.setSender("Statistik");
		message.setDate(new Date());
		message.setText(chatText);
		message.setType(ChatMessageType.STATISTIC);
		
		return message;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public ChatMessageType getType() {
		return type;
	}
	public void setType(ChatMessageType type) {
		this.type = type;
	}

	
}
