package com.codegnan.service;

import java.util.List;

import com.codegnan.dto.OrderResponseDTO;
import com.codegnan.dto.PlaceOrderRequestDTO;

public interface OrderService {
	OrderResponseDTO placeOrder(PlaceOrderRequestDTO request);

	void updateOrderStatus(Long orderId, String status);

	List<OrderResponseDTO> getOrdersByUser(Long userId);

	OrderResponseDTO getOrderById(Long orderId);
}
