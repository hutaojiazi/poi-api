package com.store.demo.service;

import com.store.demo.dto.LocationDto;
import com.store.demo.model.Location;
import com.store.demo.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
	public List<Location> getNearBy(final Pageable pageable, final double latitude, final double longitude, final int radius)
	{
		return locationRepository.getNearbyLocations(latitude, longitude, radius);
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
		final Location entity = Location.builder()
				.name(dto.getName())
				.address(dto.getAddress())
				.rating(dto.getRating())
				.coords(new GeoJsonPoint(dto.getCoords()[0], dto.getCoords()[1]))
				.facilities(dto.getFacilities())
				.openingTimes(dto.getOpeningTimes())
				.reviews(dto.getReviews())
				.build();

		return locationRepository.save(entity).getId();
	}
}