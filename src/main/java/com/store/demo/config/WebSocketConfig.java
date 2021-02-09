package com.store.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{
	public static final String ENDPOINT = "/chat";
	public static final String SUBSCRIBE_APP_PREFIX = "/app";
	public static final String SUBSCRIBE_USER_PREFIX = "/user";
	public static final String SUBSCRIBE_TOPIC = "/topic";
	public static final String SUBSCRIBE_QUEUE = "/queue";
	public static final String SUBSCRIBE_USER_REPLY = "/reply";

	@Override
	public void configureMessageBroker(final MessageBrokerRegistry config)
	{
		config.enableSimpleBroker(SUBSCRIBE_TOPIC, SUBSCRIBE_QUEUE, SUBSCRIBE_USER_REPLY);
		config.setApplicationDestinationPrefixes(SUBSCRIBE_APP_PREFIX);
		config.setUserDestinationPrefix(SUBSCRIBE_USER_PREFIX);
	}

	@Override
	public void registerStompEndpoints(final StompEndpointRegistry registry)
	{
		registry.addEndpoint(ENDPOINT).setAllowedOrigins("http://localhost:4200");
		registry.addEndpoint(ENDPOINT).setAllowedOrigins("http://localhost:4200").withSockJS();
	}

	@EventListener
	public void handleSubscribeEvent(SessionSubscribeEvent event)
	{
		log.info(
				"handleSubscribeEvent for user: " + (Objects.nonNull(event.getUser()) ? event.getUser().getName() : null) + ", event: "
						+ event);
	}

	@EventListener
	public void handleConnectEvent(SessionConnectEvent event)
	{
		log.info("handleConnectEvent for user: " + (Objects.nonNull(event.getUser()) ? event.getUser().getName() : null) + ", event: "
				+ event);
	}

	@EventListener
	public void handleDisconnectEvent(SessionDisconnectEvent event)
	{
		log.info("handleDisconnectEvent for user: " + (Objects.nonNull(event.getUser()) ? event.getUser().getName() : null)
				+ ", event: " + event);
	}
}
