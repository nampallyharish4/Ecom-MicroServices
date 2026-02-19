package com.codegnan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codegnan.dto.ProductRequestDto;
import com.codegnan.dto.ProductResponseDto;
import com.codegnan.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponseDto addProduct(@RequestBody ProductRequestDto product) {
		
		log.info("ProductController :: addProduct() called");
		log.debug("Creating product with Name: {}", product.getName());
		
		ProductResponseDto response = productService.addProduct(product);
		
		log.info("ProductController :: addProduct() completed successfully");
		return response;
	}
	
	@GetMapping("/{productId}")
	public ProductResponseDto getProduct(@PathVariable Long productId) {
		
		log.info("ProductController :: getProduct() called for ID: {}", productId);
		
		ProductResponseDto response = productService.getProductById(productId);
		
		log.info("ProductController :: getProduct() completed for ID: {}", productId);
		return response;
	}
	
	@GetMapping("/exits/{productId}")
	public boolean isProductExits(@PathVariable Long productId) {
		
		log.info("ProductController :: isProductExits() called for ID: {}", productId);
		
		boolean exists = productService.isProductExits(productId);
		
		log.info("ProductController :: isProductExits() result for ID {} : {}", productId, exists);
		
		return exists;
	}
}
