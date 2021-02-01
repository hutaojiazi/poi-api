package com.store.demo.controller;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Base class for web controller providing shared functionality.
 */
@SuppressWarnings("squid:S1075")
public class AbstractController
{
	/**
	 * Generate the location header for a given resource.
	 *
	 * @param id     the newly created resource id
	 * @param params array of path parameters before the resource id
	 * @return a location URI.
	 */
	protected URI buildLocationHeader(final String id, final String... params)
	{
		return buildResourceURI(generateLinkBuilder(this.getClass(), params), id);
	}

	protected URI buildResourceURI(final WebMvcLinkBuilder controllerLinkBuilder, final String id)
	{
		return controllerLinkBuilder.toUriComponentsBuilder().pathSegment(id).build().toUri();
	}

	protected WebMvcLinkBuilder generateLinkBuilder(final Class<?> clazz, final String... params)
	{
		return linkTo(clazz, (Object[]) params);
	}
}
