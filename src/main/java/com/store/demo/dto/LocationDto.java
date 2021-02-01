package com.store.demo.dto;

import com.store.demo.model.LocationOpeningTime;
import com.store.demo.model.LocationReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto
{
	@NotNull
	private String name;

	private String address;

	@NotNull
	@Min(value = 1)
	@Max(value = 5)
	private Integer rating;

	private List<String> facilities;

	private double[] coords;

	private List<LocationOpeningTime> openingTimes;

	private List<LocationReview> reviews;
}
