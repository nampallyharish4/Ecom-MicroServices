package com.codegnan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codegnan.dto.CredentialDto;
import com.codegnan.service.CredentialService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/credentials")
@Slf4j
public class CredentialController {

	@Autowired
	private CredentialService credentialService;

	@GetMapping("username/{uname}")
	public CredentialDto findByUsername(@PathVariable("uname") String username) {
		log.info("CredentialController :: findByUsername() called");
		log.debug("Fetching credential for username: {}", username);

		CredentialDto credentialDto = credentialService.findByUsername(username);

		log.info("Credential fetched successfully for username: {}", username);
		return credentialDto;
	}
}
