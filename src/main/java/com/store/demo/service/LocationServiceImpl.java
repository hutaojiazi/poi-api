package com.store.demo.service;

import com.store.demo.dto.LocationDto;
import com.store.demo.exception.ResourceNotFoundException;
import com.store.demo.model.Location;
import com.store.demo.model.LocationReview;
import com.store.demo.repository.LocationRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService
{

	private LocationRepository locationRepository;

	public LocationServiceImpl(LocationRepository locationRepository)
	{
		this.locationRepository = locationRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Location> getAll(final Pageable pageable)
	{
		return locationRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Location> getNearBy(final Pageable pageable, final double latitude, final double longitude, final int maxDistance)
	{
		return locationRepository.getNearbyLocations(pageable, latitude, longitude, maxDistance);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Location> get(String id)
	{
		return locationRepository.findById(id);
	}

	@Override
	@Transactional
	public String create(LocationDto dto)
	{
		final List<LocationReview> reviews = Optional.ofNullable(dto.getReviews()).orElse(List.of()).stream().map(review -> {
			review.setId(new ObjectId().toString());
			return review;
		}).collect(Collectors.toList());
		final Location entity = Location.builder()
				.name(dto.getName())
				.address(dto.getAddress())
				.rating(dto.getRating())
				//.coords(new GeoJsonPoint(dto.getCoords()[0], dto.getCoords()[1]))
				.coords(dto.getCoords())
				.facilities(dto.getFacilities())
				.openingTimes(dto.getOpeningTimes())
				.reviews(reviews)
				.build();

		return locationRepository.save(entity).getId();
	}

	@Override
	public List<LocationReview> getLocationReviews(final Pageable pageable, final String locationId)
	{
		final Location location = locationRepository.findById(locationId).orElseThrow(() -> new ResourceNotFoundException(locationId));
		return location.getReviews();
	}

	@Override
	public Optional<LocationReview> getLocationReviewById(final String locationId, final String reviewId)
	{
		final Location location = locationRepository.findById(locationId).orElseThrow(() -> new ResourceNotFoundException(locationId));
		return Optional.ofNullable(location.getReviews().get(0));
	}

	@Override
	@Transactional
	public String createLocationReview(final LocationReview dto, final String locationId)
	{
		final Location location = locationRepository.findById(locationId).orElseThrow(() -> new ResourceNotFoundException(locationId));
		final List<LocationReview> reviews = Optional.ofNullable(location.getReviews()).orElse(List.of());

		final ObjectId id = new ObjectId();
		dto.setId(id.toString());
		reviews.add(dto);
		location.setReviews(reviews);
		locationRepository.save(location);

		return id.toString();
	}
}