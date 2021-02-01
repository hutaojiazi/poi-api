package com.store.demo.controller;

import com.store.demo.dto.LocationDto;
import com.store.demo.dto.PageableCollection;
import com.store.demo.dto.ResourceIdDto;
import com.store.demo.exception.ResourceNotFoundException;
import com.store.demo.model.Location;
import com.store.demo.service.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/locations")
public class LocationController extends AbstractController
{

	private final LocationService locationService;

	public LocationController(final LocationService locationService)
	{
		this.locationService = locationService;
	}

	@GetMapping
	public HttpEntity<PageableCollection<Location>> getLocations(@PageableDefault(size = 20) Pageable pageable)
	{
		final Page<Location> locations = locationService.getAll(pageable);
		return ResponseEntity.ok(PageableCollection.of(locations));
	}

	@GetMapping(value = "/nearby")
	public HttpEntity<PageableCollection<Location>> getNearbyLocations(@RequestParam(name = "lat", required = true) double latitude,
			@RequestParam(name = "lng", required = true) double longitude,
			@RequestParam(name = "rad", required = false, defaultValue = "100000") int radius,
			@PageableDefault(size = 20) Pageable pageable)
	{
		final Page<Location> locations = locationService.getNearBy(pageable, latitude, longitude, radius);
		return ResponseEntity.ok(PageableCollection.of(locations));
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Location> get(@PathVariable final String id)
	{
		final Optional<Location> dto = locationService.get(id);
		return dto.map(body -> ResponseEntity.ok().body(body)).orElseThrow(() -> new ResourceNotFoundException(id));
	}

	@PostMapping
	public ResponseEntity<ResourceIdDto> create(@RequestBody @Valid final LocationDto dto)
	{
		final String id = locationService.create(dto);
		return ResponseEntity.ok(ResourceIdDto.of(id));
	}
}
