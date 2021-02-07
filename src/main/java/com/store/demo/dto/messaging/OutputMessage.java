package com.store.demo.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OutputMessage
{
	private String from;
	private String text;
	private String time;
}
