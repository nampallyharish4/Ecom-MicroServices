package com.codegnan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codegnan.dto.UserDto;
import com.codegnan.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public UserDto createUser(@Valid @RequestBody UserDto userDto) {
		log.info("UserController :: createUser() started");
		log.debug("Creating user with email: {}", userDto.getEmailAddress());

		UserDto savedUser = userService.save(userDto);

		log.info("UserController :: createUser() completed successfully");
		return savedUser;
	}

	@GetMapping("/{userId}")
	public UserDto fetchUser(@PathVariable Integer userId) {
		log.info("UserController :: fetchUser() started for ID: {}", userId);

		UserDto userDto = userService.findById(userId);

		log.info("UserController :: fetchUser() completed for ID: {}", userId);
		return userDto;
	}

	// TODO : DELETE,PUT - STUDENT Assignment
}
