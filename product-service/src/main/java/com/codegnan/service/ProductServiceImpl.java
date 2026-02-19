package com.codegnan.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegnan.dto.ProductRequestDto;
import com.codegnan.dto.ProductResponseDto;
import com.codegnan.models.Product;
import com.codegnan.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
		
		log.info("ProductServiceImpl :: addProduct() started");
		log.debug("Received ProductRequestDto - Name: {}, Price: {}", 
				productRequestDto.getName(), 
				productRequestDto.getPrice());
		
		Product product = new Product();
		BeanUtils.copyProperties(productRequestDto, product);
		log.debug("Copied properties from DTO to Product entity");
		
		Product dbProduct = productRepository.save(product);
		log.info("Product saved successfully with ID: {}", dbProduct.getId());
		
		ProductResponseDto response = mapToDto(dbProduct);
		log.info("ProductServiceImpl :: addProduct() completed");
		
		return response;
	}
	
	
	public ProductResponseDto mapToDto(Product product) {
		
		log.debug("Mapping Product entity to ProductResponseDto for ID: {}", product.getId());
		
		ProductResponseDto response = new ProductResponseDto();
		BeanUtils.copyProperties(product, response);
		
		log.debug("Mapping completed for Product ID: {}", product.getId());
		return response;
	}

	@Override
	public boolean isProductExits(Long productId) {
		
		log.info("ProductServiceImpl :: isProductExits() called for ID: {}", productId);
		
		boolean exists = productRepository.existsById(productId);
		
		if (exists) {
			log.info("Product exists with ID: {}", productId);
		} else {
			log.warn("Product does NOT exist with ID: {}", productId);
		}
		
		return exists;
	}

	@Override
	public ProductResponseDto getProductById(Long productId) {
		
		log.info("ProductServiceImpl :: getProductById() started for ID: {}", productId);
		
		ProductResponseDto response = productRepository.findById(productId)
				.map(p -> {
					log.debug("Product found in DB with ID: {}", productId);
					return mapToDto(p);
				})
				.orElseThrow(() -> {
					log.error("Product Not Found in DB with ID: {}", productId);
					return new RuntimeException("Product Not Found in DB");
				});
		
		log.info("ProductServiceImpl :: getProductById() completed for ID: {}", productId);
		return response;
	}
}
