package com.store.demo.exception;

import java.util.Arrays;

public class ResourceNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Resource not found with identifier(s): %s";
	private static final String GENERIC_MESSAGE = "Resource not found.";

	public ResourceNotFoundException(final String... ids)
	{
		super(String.format(MESSAGE, Arrays.asList(ids)));
	}

	public ResourceNotFoundException()
	{
		super(GENERIC_MESSAGE);
	}
}
