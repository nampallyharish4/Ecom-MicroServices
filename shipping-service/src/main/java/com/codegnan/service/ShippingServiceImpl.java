package com.codegnan.service;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.codegnan.dto.ShippingRequestDTO;
import com.codegnan.dto.ShippingResponseDTO;
import com.codegnan.exceptions.ResourceNotFoundException;
import com.codegnan.feignclients.OrderFeignClient;
import com.codegnan.model.ShippingInfo;
import com.codegnan.repository.ShippingRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ShippingServiceImpl implements ShippingService {

	private ShippingRepository shippingRepository;
	private OrderFeignClient orderFeignClient;

	@Override
	public ShippingResponseDTO shipOrder(ShippingRequestDTO request) {

		log.info("ShippingServiceImpl :: shipOrder() started for OrderId: {}", request.getOrderId());
		log.debug("Shipping method: {}, Carrier: {}", request.getShippingMethod(), request.getCarrier());

		ShippingInfo shippingInfo = new ShippingInfo();
		BeanUtils.copyProperties(request, shippingInfo);

		shippingInfo.setStatus("Shipped");
		shippingInfo.setShippedAt(LocalDateTime.now());

		log.debug("Saving shipping information to database");
		ShippingInfo dbShipping = shippingRepository.save(shippingInfo);

		log.info("Shipping info saved successfully for OrderId: {}", request.getOrderId());

		log.debug("Updating order status to SHIPPED via ORDER-SERVICE");
		orderFeignClient.updateOrderStatus(request.getOrderId(), "SHIPPED");

		log.info("Order status updated to SHIPPED for OrderId: {}", request.getOrderId());

		log.info("ShippingServiceImpl :: shipOrder() completed");

		return mapToDto(dbShipping);
	}

	@Override
	public void updateShippingStatus(Long orderId, String status) {

		log.info("ShippingServiceImpl :: updateShippingStatus() called for OrderId: {}, Status: {}", orderId, status);

		ShippingInfo shippingInfo = shippingRepository.findByOrderId(orderId);

		if (shippingInfo == null) {
			log.error("Shipping info not found for OrderId: {}", orderId);
			throw new ResourceNotFoundException("Order Not Found ");
		}

		shippingInfo.setStatus(status);
		shippingInfo.setDeliveryDate("DELIVERED".equals(status) ? LocalDateTime.now() : null);

		log.debug("Saving updated shipping status to database");
		shippingRepository.save(shippingInfo);

		if ("DELIVERED".equals(status)) {
			log.info("Order delivered. Updating order status to DELIVERED via ORDER-SERVICE");
			orderFeignClient.updateOrderStatus(orderId, "DELIVERED");
		}

		log.info("ShippingServiceImpl :: updateShippingStatus() completed for OrderId: {}", orderId);
	}

	private ShippingResponseDTO mapToDto(ShippingInfo dbShipping) {

		log.debug("Mapping ShippingInfo entity to ShippingResponseDTO for OrderId: {}", dbShipping.getOrderId());

		ShippingResponseDTO response = new ShippingResponseDTO();
		BeanUtils.copyProperties(dbShipping, response);

		response.setShippedAt(response.getShippedAt());

		return response;
	}
}
