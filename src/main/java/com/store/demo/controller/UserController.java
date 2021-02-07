package com.store.demo.controller;

import com.store.demo.dto.UserDto;
import com.store.demo.dto.UserResponseDto;
import com.store.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController extends AbstractController
{

	private final UserService userService;

	public UserController(final UserService userService)
	{
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<UserResponseDto> create(@RequestBody @Valid final UserDto dto)
	{
		final UserResponseDto user = userService.create(dto);
		return ResponseEntity.ok(user);
	}

	@PostMapping(value = "/validate")
	public ResponseEntity<UserResponseDto> validate(@RequestBody @Valid final UserDto dto)
	{
		final UserResponseDto user = userService.validate(dto.getEmail(), dto.getPassword());
		return ResponseEntity.ok(user);
	}
}
