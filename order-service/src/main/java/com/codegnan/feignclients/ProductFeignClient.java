package com.codegnan.feignclients;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codegnan.dto.ProductResponseDto;

@FeignClient(name = "product-service")
@LoadBalancerClient
public interface ProductFeignClient {
	@GetMapping("/products/{productId}")
	public ProductResponseDto getProduct(@PathVariable Long productId);

	@GetMapping("/products/exits/{productId}")
	public boolean isProductExits(@PathVariable Long productId);
}
