package com.codegnan.mappers;

import org.mapstruct.Mapper;

import com.codegnan.dto.CredentialDto;
import com.codegnan.models.Credential;

@Mapper(componentModel = "spring")
public interface CredentialMapper {

	CredentialDto toDto(Credential credential);
}
