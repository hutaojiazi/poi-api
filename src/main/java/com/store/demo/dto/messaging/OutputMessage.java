package com.store.demo.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OutputMessage
{
	private String from;
	private String text;
	private String to;
	private String time;
}
