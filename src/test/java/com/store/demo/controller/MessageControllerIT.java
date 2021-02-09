package com.store.demo.controller;

import com.store.demo.MessagingConstants;
import com.store.demo.POIApplication;
import com.store.demo.config.WebSocketConfig;
import com.store.demo.dto.messaging.IncomingMessage;
import com.store.demo.dto.messaging.OutputMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = { POIApplication.class })
class MessageControllerIT
{
	private static final String BASE_URL = "ws://localhost:8099/chat";
	private static final String SEND_CHAT_ENDPOINT = "/app/message";
	private static final String SEND_CHAT_TO_USER_ENDPOINT = "/app/user-message";
	private static final String SEND_REGISTRATION_MSG_ENDPOINT = "/app/registration-message";

	private static final String SUBSCRIBE_MESSAGE_ENDPOINT = "/topic/messages";
	private static final String SUBSCRIBE_USER_MESSAGE_ENDPOINT = WebSocketConfig.SUBSCRIBE_USER_PREFIX + "/queue/reply";
	private static final String SUBSCRIBE_REGISTRATION_MESSAGE_QUEUE = WebSocketConfig.SUBSCRIBE_QUEUE;
	private static final String SUBSCRIBE_REGISTRATION_MESSAGE_USER_REPLY =
			WebSocketConfig.SUBSCRIBE_USER_PREFIX + WebSocketConfig.SUBSCRIBE_USER_REPLY;

	private WebSocketStompClient stompClient1;
	private WebSocketStompClient stompClient2;
	private StompSession stompSession1;
	private StompSession stompSession2;

	@BeforeEach
	public void setup() throws ExecutionException, InterruptedException
	{
		stompClient1 = createWebSocketClient();
		stompSession1 = stompClient1.connect(BASE_URL, new ClientSessionHandlerAdapter()).get();
		stompClient2 = createWebSocketClient();
		stompSession2 = stompClient2.connect(BASE_URL, new ClientSessionHandlerAdapter()).get();
	}

	@AfterEach
	public void tearDown()
	{
		stompSession1.disconnect();
		stompClient1.stop();
		stompSession2.disconnect();
		stompClient2.stop();
	}

	@Test
	void testMessageEndpoint() throws InterruptedException, ExecutionException, TimeoutException
	{
		final CompletableFuture<OutputMessage> completableFuture = new CompletableFuture<>();
		final String from = "someone";
		final String text = "well done.";

		stompSession1.subscribe(SUBSCRIBE_MESSAGE_ENDPOINT, new ClientFrameHandlerWithCompletableFuture(completableFuture));
		stompSession1.send(SEND_CHAT_ENDPOINT, IncomingMessage.builder().from(from).text(text).build());

		final OutputMessage message = completableFuture.get(2, SECONDS);
		assertEquals(from, message.getFrom());
		assertEquals(text, message.getText());
		assertNotNull(message.getTime());
	}

	@Test
	void testUserMessageEndpoint() throws InterruptedException, ExecutionException, TimeoutException
	{
		final CompletableFuture<OutputMessage> completableFuture = new CompletableFuture<>();
		final String from = "unknown";
		final String to = "me";
		final String text = "nice work.";

		stompSession1.subscribe(SUBSCRIBE_USER_MESSAGE_ENDPOINT, new ClientFrameHandlerWithCompletableFuture(completableFuture));
		stompSession1.send(SEND_CHAT_TO_USER_ENDPOINT, IncomingMessage.builder().from(from).to(to).text(text).build());

		final OutputMessage message = completableFuture.get(2, SECONDS);
		assertEquals(from, message.getFrom());
		assertEquals(to, message.getTo());
		assertEquals(text, message.getText());
		assertNotNull(message.getTime());
	}

	@Test
	void testRegistrationMessageEndpoint() throws InterruptedException
	{
		// subscribe first client
		final BlockingQueue<OutputMessage> queue1 = new LinkedBlockingDeque<>();
		final BlockingQueue<OutputMessage> userQueue1 = new LinkedBlockingDeque<>();
		stompSession1.subscribe(SUBSCRIBE_REGISTRATION_MESSAGE_QUEUE,
				new ClientFrameHandlerWithConsumer((payload) -> queue1.offer(payload)));
		stompSession1.subscribe(SUBSCRIBE_REGISTRATION_MESSAGE_USER_REPLY,
				new ClientFrameHandlerWithConsumer((payload) -> userQueue1.offer(payload)));

		// subscribe second client
		final BlockingQueue<OutputMessage> queue2 = new LinkedBlockingDeque<>();
		final BlockingQueue<OutputMessage> userQueue2 = new LinkedBlockingDeque<>();
		stompSession2.subscribe(SUBSCRIBE_REGISTRATION_MESSAGE_QUEUE,
				new ClientFrameHandlerWithConsumer((payload) -> queue2.offer(payload)));
		stompSession2.subscribe(SUBSCRIBE_REGISTRATION_MESSAGE_USER_REPLY,
				new ClientFrameHandlerWithConsumer((payload) -> userQueue2.offer(payload)));

		Thread.currentThread().sleep(100);

		// call register endpoint for first client
		final String text = "hello team";
		stompSession1.send(SEND_REGISTRATION_MSG_ENDPOINT, text);
		Thread.currentThread().sleep(100);
		assertEquals(MessagingConstants.DEFAULT_USER_REGISTRATION_REPLY_MESSAGE, userQueue1.poll().getText());
		assertEquals(MessagingConstants.DEFAULT_USER_REGISTRATION_QUEUE_MESSAGE + text, queue1.poll().getText());
		assertEquals(MessagingConstants.DEFAULT_USER_REGISTRATION_QUEUE_MESSAGE + text, queue2.poll().getText());

		// call register endpoint for second client
		final String anotherText = "hola";
		stompSession2.send(SEND_REGISTRATION_MSG_ENDPOINT, anotherText);
		Thread.currentThread().sleep(100);
		assertEquals(MessagingConstants.DEFAULT_USER_REGISTRATION_REPLY_MESSAGE, userQueue2.poll().getText());
		assertEquals(MessagingConstants.DEFAULT_USER_REGISTRATION_QUEUE_MESSAGE + anotherText, queue1.poll().getText());
		assertEquals(MessagingConstants.DEFAULT_USER_REGISTRATION_QUEUE_MESSAGE + anotherText, queue2.poll().getText());
	}

	private WebSocketStompClient createWebSocketClient()
	{
		final WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		return stompClient;
	}

	private class ClientFrameHandlerWithCompletableFuture implements StompFrameHandler
	{
		private final CompletableFuture<OutputMessage> frameHandler;

		public ClientFrameHandlerWithCompletableFuture(CompletableFuture<OutputMessage> frameHandler)
		{
			this.frameHandler = frameHandler;
		}

		@Override
		public Type getPayloadType(final StompHeaders stompHeaders)
		{
			return OutputMessage.class;
		}

		@Override
		public void handleFrame(final StompHeaders stompHeaders, final Object payload)
		{
			frameHandler.complete((OutputMessage) payload);
		}
	}

	private class ClientFrameHandlerWithConsumer implements StompFrameHandler
	{
		private final Consumer<OutputMessage> frameHandler;

		public ClientFrameHandlerWithConsumer(Consumer<OutputMessage> frameHandler)
		{
			this.frameHandler = frameHandler;
		}

		@Override
		public Type getPayloadType(final StompHeaders stompHeaders)
		{
			return OutputMessage.class;
		}

		@Override
		public void handleFrame(final StompHeaders stompHeaders, final Object payload)
		{
			frameHandler.accept((OutputMessage) payload);
		}
	}

	private class ClientSessionHandlerAdapter extends StompSessionHandlerAdapter
	{
		@Override
		public void afterConnected(final StompSession session, final StompHeaders connectedHeaders)
		{
			final String username = connectedHeaders.get("user-name").iterator().next();
			log.info("afterConnected: username=" + username);
			super.afterConnected(session, connectedHeaders);
		}

		@Override
		public void handleException(final StompSession session, final StompCommand command, final StompHeaders headers,
				final byte[] payload, final Throwable exception)
		{
			log.info("handleException: exception=" + exception);
			super.handleException(session, command, headers, payload, exception);
		}
	}

}
