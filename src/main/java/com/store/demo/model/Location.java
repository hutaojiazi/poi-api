package com.store.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "location")
public class Location
{
	@Id
	private String id;

	@NotNull
	private String name;

	private String address;

	@NotNull
	@Min(value = 1)
	@Max(value = 5)
	private Integer rating;

	private List<String> facilities;

	//@GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
	//private GeoJsonPoint coords;
	private double[] coords;

	private List<LocationOpeningTime> openingTimes;

	private List<LocationReview> reviews;
}
