package com.codegnan.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codegnan.dto.UserDto;
import com.codegnan.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(source = "email",target = "emailAddress")
	@Mapping(source = "phone",target = "contact")
	UserDto toDto(User user);
	
	@Mapping(source = "emailAddress",target = "email")
	@Mapping(source = "contact",target = "phone")
	User toEntity(UserDto userDto);
}
