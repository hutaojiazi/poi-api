package com.store.demo.controller;

import com.store.demo.config.WebSocketConfig;
import com.store.demo.dto.messaging.IncomingMessage;
import com.store.demo.dto.messaging.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MessageController
{
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/message")
	@SendTo("/topic/messages")
	public OutputMessage send(@Payload final IncomingMessage msg, final Principal principal)
	{
		return OutputMessage.builder()
				.from(msg.getFrom())
				.text(msg.getText())
				.time(new SimpleDateFormat("HH:mm").format(new Date()))
				.build();
	}

	@MessageMapping("/user-message")
	@SendToUser("/queue/reply")
	public OutputMessage sendToUser(@Payload IncomingMessage msg, Principal user, @Header("simpSessionId") String sessionId)
	{
		final OutputMessage out = OutputMessage.builder()
				.from(msg.getFrom())
				.to(msg.getTo())
				.text(msg.getText())
				.time(new SimpleDateFormat("HH:mm").format(new Date()))
				.build();
		return out;
		//messagingTemplate.convertAndSendToUser(msg.getTo(), "/queue/reply", out);
	}

	@MessageMapping("/registration-message")
	public void register(Message<Object> message, @Payload String payload, Principal principal)
	{
		messagingTemplate.convertAndSendToUser(principal.getName(), WebSocketConfig.SUBSCRIBE_USER_REPLY,
				"Welcome to this wonderful world.");
		messagingTemplate.convertAndSend(WebSocketConfig.SUBSCRIBE_QUEUE, "User " + principal.getName() + " registered: " + payload);
	}
}
