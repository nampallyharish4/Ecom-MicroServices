package com.codegnan.service;

import com.codegnan.dto.ProductRequestDto;
import com.codegnan.dto.ProductResponseDto;

public interface ProductService {

	ProductResponseDto addProduct(ProductRequestDto productRequestDto);
	
	boolean isProductExits(Long productId);
	
	ProductResponseDto getProductById(Long productId);
}
