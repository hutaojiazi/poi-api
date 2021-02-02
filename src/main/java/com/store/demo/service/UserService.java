package com.store.demo.service;

import com.store.demo.dto.UserDto;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService
{
	/**
	 * Validates the password of an user.
	 *
	 * @param email    the user email.
	 * @param password the user password
	 * @return true if the password is valid. otherwise return false.
	 */
	Boolean validate(String email, String password);

	/**
	 * Creates a new user.
	 *
	 * @param userDto
	 * @return the id of the user created.
	 */
	String create(UserDto userDto);
}