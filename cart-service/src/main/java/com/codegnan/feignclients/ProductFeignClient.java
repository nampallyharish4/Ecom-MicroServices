package com.codegnan.feignclients;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
@LoadBalancerClient
public interface ProductFeignClient {
	@GetMapping("/products/exits/{productId}")
	public boolean isProductExits(@PathVariable Long productId);
}

//http://localhost:8051/products/exists/2