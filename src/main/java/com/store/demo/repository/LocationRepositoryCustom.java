package com.store.demo.repository;

import com.store.demo.model.Location;

import java.util.List;

public interface LocationRepositoryCustom
{
	List<Location> getNearbyLocations(double latitude, double longitude, int maxDistance);
}
