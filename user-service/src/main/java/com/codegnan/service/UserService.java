package com.codegnan.service;

import com.codegnan.dto.UserDto;
import com.codegnan.models.User;

public interface UserService {

	UserDto save(UserDto userDto);
	UserDto findById(Integer userId);
	UserDto update(Integer userId,UserDto user);
}
// UserService userSErvice = new UserServiceImpl();