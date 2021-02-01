package com.store.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationReview
{
	@Id
	private String id;

	@NotNull
	private String author;

	@NotNull
	@Min(value = 1)
	@Max(value = 5)
	private Integer rating;

	@NotNull
	private String reviewText;

	private OffsetDateTime createdOn = OffsetDateTime.now();
}
