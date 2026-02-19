package com.codegnan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codegnan.models.Credential;

public interface CredentialRepository extends JpaRepository<Credential, Integer>{

	// Method Names
	
	Optional<Credential> findByUsername(String username);
	
	Optional<Credential> findByusernameAndPassword(String username,String password);
	

	//@Query(value = "select uname,password from user",nativeQuery = true)
	//Object[] fetchUserINformation(String username);
}
