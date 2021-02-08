package com.store.demo.controller;

import com.store.demo.POIApplication;
import com.store.demo.dto.messaging.Message;
import com.store.demo.dto.messaging.OutputMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { POIApplication.class })
public class MessageControllerIT
{
	private static final String SEND_CHAT_ENDPOINT = "/app/message";
	private static final String SUBSCRIBE_MESSAGE_ENDPOINT = "/topic/messages";

	private CompletableFuture<OutputMessage> completableFuture;

	@BeforeEach
	public void setup()
	{
		completableFuture = new CompletableFuture<>();
	}

	@Test
	@Disabled
	public void testMessageEndpoint() throws InterruptedException, ExecutionException, TimeoutException
	{
		final WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		final StompSession stompSession = stompClient.connect("ws://localhost:8099/chat", new StompSessionHandlerAdapter()
		{
		}).get(1, SECONDS);

		final String from = "someone";
		final String text = "well done.";

		stompSession.subscribe(SUBSCRIBE_MESSAGE_ENDPOINT, new CreateOutputMessageFrameHandler());
		stompSession.send(SEND_CHAT_ENDPOINT, Message.builder().from(from).text(text).build());

		final OutputMessage message = completableFuture.get(10, SECONDS);
		assertEquals(from, message.getFrom());
		assertEquals(text, message.getText());
		assertNotNull(message.getTime());

	}

	private List<Transport> createTransportClient()
	{
		return List.of(new WebSocketTransport(new StandardWebSocketClient()));
	}

	private class CreateOutputMessageFrameHandler implements StompFrameHandler
	{
		@Override
		public Type getPayloadType(StompHeaders stompHeaders)
		{
			//System.out.println(stompHeaders.toString());
			return OutputMessage.class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object o)
		{
			//System.out.println((OutputMessage) o);
			completableFuture.complete((OutputMessage) o);
		}
	}

}
