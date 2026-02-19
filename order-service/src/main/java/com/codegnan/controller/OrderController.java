package com.codegnan.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codegnan.dto.OrderResponseDTO;
import com.codegnan.dto.PlaceOrderRequestDTO;
import com.codegnan.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

	private OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public OrderResponseDTO placeOrder(@RequestBody PlaceOrderRequestDTO request) {

		log.info("OrderController :: placeOrder() called for UserId: {}", request.getUserId());

		OrderResponseDTO response = orderService.placeOrder(request);

		log.info("OrderController :: placeOrder() completed");
		return response;
	}

	@PutMapping("/{orderId}/status/{status}")
	public void updateOrderStatus(@PathVariable Long orderId, @PathVariable String status) {

		log.info("OrderController :: updateOrderStatus() called. OrderId: {}, Status: {}", orderId, status);

		orderService.updateOrderStatus(orderId, status);

		log.info("OrderController :: updateOrderStatus() completed");
	}

	@GetMapping("/{orderId}")
	public OrderResponseDTO getOrder(@PathVariable Long orderId) {

		log.info("OrderController :: getOrder() called for OrderId: {}", orderId);

		return orderService.getOrderById(orderId);
	}

	@GetMapping("/user/{userId}")
	public List<OrderResponseDTO> getOrdersByUser(@PathVariable Long userId) {

		log.info("OrderController :: getOrdersByUser() called for UserId: {}", userId);

		return orderService.getOrdersByUser(userId);
	}
}
