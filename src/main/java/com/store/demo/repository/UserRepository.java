package com.store.demo.repository;

import com.store.demo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>
{
	User findByEmail(String email);
}
