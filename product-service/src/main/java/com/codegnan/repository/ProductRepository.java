package com.codegnan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegnan.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	// 
	
}
