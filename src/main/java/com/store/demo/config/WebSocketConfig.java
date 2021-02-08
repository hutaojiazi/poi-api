package com.store.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{
	@Override
	public void configureMessageBroker(final MessageBrokerRegistry config)
	{
		config.enableSimpleBroker("/topic/", "/queue/");
		config.setApplicationDestinationPrefixes("/app");
		config.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(final StompEndpointRegistry registry)
	{
		registry.addEndpoint("/chat").setAllowedOrigins("http://localhost:4200");
		registry.addEndpoint("/chat").setAllowedOrigins("http://localhost:4200").withSockJS();
	}
}