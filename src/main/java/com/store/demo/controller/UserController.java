package com.store.demo.controller;

import com.store.demo.dto.ResourceIdDto;
import com.store.demo.dto.UserDto;
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
	public ResponseEntity<ResourceIdDto> create(@RequestBody @Valid final UserDto dto)
	{
		final String id = userService.create(dto);
		return ResponseEntity.ok(ResourceIdDto.of(id));
	}

	@PostMapping(value = "/validate")
	public ResponseEntity<ResourceIdDto> validate(@RequestBody @Valid final UserDto dto)
	{
		final String id = userService.validate(dto.getEmail(), dto.getPassword());
		return ResponseEntity.ok(ResourceIdDto.of(id));
	}
}
