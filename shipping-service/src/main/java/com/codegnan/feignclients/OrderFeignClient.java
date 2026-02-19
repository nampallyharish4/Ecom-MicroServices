package com.codegnan.feignclients;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="order-service")
@LoadBalancerClient
public interface OrderFeignClient {
	 @PutMapping("/orders/{orderId}/status/{status}")
	    void updateOrderStatus(@PathVariable Long orderId,
	                           @PathVariable String status);
}
