package com.codegnan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegnan.models.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
