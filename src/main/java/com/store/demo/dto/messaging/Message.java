package com.store.demo.dto.messaging;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Message
{
	private String from;
	private String to;
	private String text;
}
