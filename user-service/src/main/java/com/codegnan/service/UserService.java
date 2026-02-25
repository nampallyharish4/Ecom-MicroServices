package com.codegnan.service;

import com.codegnan.dto.UserDto;


public interface UserService {

	UserDto save(UserDto userDto);
	UserDto findById(Integer userId);
	UserDto update(Integer userId,UserDto user);
}
// UserService userSErvice = new UserServiceImpl();