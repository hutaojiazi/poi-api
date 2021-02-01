package com.store.demo.service;

import com.store.demo.dto.LocationDto;
import com.store.demo.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface LocationService
{

	/**
	 * Returns the requested page of locations
	 *
	 * @param pageable the page request criteria.
	 * @return the requested locations page
	 */
	Page<Location> getAll(Pageable pageable);

	/**
	 * Returns the requested page of locations
	 *
	 * @param pageable the page request criteria.
	 * @return the requested locations page
	 */
	Page<Location> getNearBy(Pageable pageable, double latitude, double longitude, int radius);

	/**
	 * Retrieves a location with provided id.
	 *
	 * @param id the resource identifier.
	 * @return the requested location, or {@link Optional#empty()} if the resource is not found.
	 */
	Optional<Location> get(String id);

	/**
	 * Creates a new location.
	 *
	 * @param location
	 * @return the id of the location created.
	 */
	String create(LocationDto location);
}