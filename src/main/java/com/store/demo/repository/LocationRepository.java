package com.store.demo.repository;

import com.store.demo.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, String>
{
}
