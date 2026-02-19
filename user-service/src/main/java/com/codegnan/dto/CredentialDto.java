package com.codegnan.dto;

import com.codegnan.models.RoleBasedAuthority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CredentialDto {

	private String username;
	private String password;
	private RoleBasedAuthority roleBasedAuthority;
}
