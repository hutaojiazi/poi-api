package com.store.demo.controller;

import com.store.demo.dto.messaging.Message;
import com.store.demo.dto.messaging.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MessageController
{
	@MessageMapping("/message")
	@SendTo("/topic/messages")
	public OutputMessage send(@Payload final Message message, final Principal principal)
	{
		final String time = new SimpleDateFormat("HH:mm").format(new Date());
		return new OutputMessage(message.getFrom(), message.getText(), time);
	}
}
