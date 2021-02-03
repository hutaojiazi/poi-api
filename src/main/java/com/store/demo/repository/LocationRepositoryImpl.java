package com.store.demo.repository;

import com.store.demo.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.NearQuery;

import java.util.List;

public class LocationRepositoryImpl implements LocationRepositoryCustom
{
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public List<Location> getNearbyLocations(final Pageable pageable, final double latitude, final double longitude,
			final int maxDistance)
	{
		BasicQuery query = new BasicQuery(
				"{coords:{ $near: { $geometry: { type: 'Point', coordinates: [" + latitude + "," + longitude + " ] }, $maxDistance: "
						+ maxDistance + "}}}");

		mongoTemplate.indexOps(Location.class).ensureIndex(new GeospatialIndex("coords").typed(GeoSpatialIndexType.GEO_2DSPHERE));
		return mongoTemplate.find(query, Location.class);

//		final Point point = new Point(latitude, longitude);
//		final GeoResults<Location> result = mongoTemplate.query(Location.class)
//			.as(Location.class)
//			.near(NearQuery.near(new GeoJsonPoint(latitude, longitude)).maxDistance(maxDistance, Metrics.KILOMETERS))
//			.all();
//
//		return result;
	}
}
