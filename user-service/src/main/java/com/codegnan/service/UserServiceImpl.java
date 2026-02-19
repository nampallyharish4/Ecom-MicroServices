package com.codegnan.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegnan.dto.UserDto;
import com.codegnan.mappers.UserMapper;
import com.codegnan.models.Credential;
import com.codegnan.models.User;
import com.codegnan.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional()
	public UserDto save(UserDto userDto) {
		
		log.info("UserServiceImpl :: save() method started");
		log.debug("Incoming UserDto email: {}", userDto.getEmailAddress());
		
		User user = userMapper.toEntity(userDto);
		log.debug("User entity mapped from DTO");

		Credential credential = user.getCredential();
		log.debug("Fetched credential object from user");

		//TODO : we are providing original password in db but we need to store in encoded password
		
		// BiDirectional
		credential.setUser(user); 
		log.debug("Set bidirectional mapping between User and Credential");

		User dbUser = userRepository.save(user);
		log.info("User saved successfully with ID: {}", dbUser.getUserId());
		
		UserDto responseDto = userMapper.toDto(dbUser);
		log.info("UserServiceImpl :: save() method completed");

		return responseDto;
	}

	@Override
	public UserDto findById(Integer userId) {
		log.info("UserServiceImpl :: findById() started for ID: {}", userId);

		UserDto userDto = userRepository.findById(userId)
			   .map(user -> {
				   log.debug("User found in DB with ID: {}", userId);
				   return userMapper.toDto(user);
			   })
			   .orElseThrow(() -> {
				   log.error("User not found in DB with ID: {}", userId);
				   return new RuntimeException("User Not found in Db");
			   });

		log.info("UserServiceImpl :: findById() completed for ID: {}", userId);
		return userDto;
	}

	@Override
	public UserDto update(Integer userId, UserDto user) {
		log.info("UserServiceImpl :: update() called for ID: {}", userId);
		log.warn("Update method is not implemented yet");
		return null;
	}
}
