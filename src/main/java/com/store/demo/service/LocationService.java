package com.store.demo.service;

import com.store.demo.dto.LocationDto;
import com.store.demo.model.Location;
import com.store.demo.model.LocationReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.List;
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
	List<Location> getNearBy(Pageable pageable, double latitude, double longitude, int maxDistance);

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

	/**
	 * Returns the requested list of location reviews
	 *
	 * @param pageable   the page request criteria.
	 * @param locationId the location id
	 * @return the requested location reviews
	 */
	List<LocationReview> getLocationReviews(Pageable pageable, String locationId);

	/**
	 * Retrieves a location with provided id and location id.
	 *
	 * @param locationId the location id
	 * @param reviewId   the review id
	 * @return the requested location review, or {@link Optional#empty()} if the resource is not found.
	 */
	Optional<LocationReview> getLocationReviewById(String locationId, String reviewId);

	/**
	 * Creates a new location review.
	 *
	 * @param review
	 * @param locationId
	 * @return the id of the location review created.
	 */
	LocationReview createLocationReview(LocationReview review, String locationId);
}