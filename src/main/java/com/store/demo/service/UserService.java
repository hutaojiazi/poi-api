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
	 * @return the user id if user found using the email and password.
	 */
	String validate(String email, String password);

	/**
	 * Creates a new user.
	 *
	 * @param userDto
	 * @return the id of the user created.
	 */
	String create(UserDto userDto);
}