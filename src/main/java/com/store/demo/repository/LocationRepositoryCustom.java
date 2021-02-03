package com.store.demo.repository;

import com.store.demo.model.Location;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationRepositoryCustom
{
	List<Location> getNearbyLocations(Pageable pageable, double latitude, double longitude, int maxDistance);
}
