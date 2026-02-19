package com.codegnan.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codegnan.dto.ProductResponseDto;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

	@GetMapping("products/{productId}")
	public ProductResponseDto getProduct(@PathVariable Long productId);
}
