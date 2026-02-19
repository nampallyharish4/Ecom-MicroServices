package com.codegnan.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.codegnan.dto.PaymentRequestDTO;
import com.codegnan.dto.PaymentResponseDTO;
import com.codegnan.dto.UserDto;
import com.codegnan.exceptions.ResourceNotFoundException;
import com.codegnan.feignclients.OrderFeignClient;
import com.codegnan.feignclients.UserFeignClient;
import com.codegnan.models.Payment;
import com.codegnan.repository.PaymentRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private PaymentRepository paymentRepository;
	private UserFeignClient userFeignClient;
	private OrderFeignClient orderFeignClient;

	@Override
	public PaymentResponseDTO processPayment(PaymentRequestDTO request) {

		log.info("PaymentServiceImpl :: processPayment() started for OrderId: {}", request.getOrderId());
		log.debug("Validating user with USER-SERVICE. UserId: {}", request.getUserId());

		UserDto userDto = userFeignClient.fetchUser(request.getUserId().intValue());

		if (userDto == null) {
			log.error("User not found with UserId: {}", request.getUserId());
			throw new ResourceNotFoundException("User Not Found In DB");
		}

		log.debug("User validated successfully. Creating Payment entity");

		Payment payment = new Payment();
		BeanUtils.copyProperties(request, payment);

		payment.setStatus("SUCCESS");

		log.debug("Saving payment with amount: {}", request.getAmount());
		paymentRepository.save(payment);

		log.info("Payment saved successfully for OrderId: {}", request.getOrderId());

		// Update Order Status
		log.debug("Updating order status to PAID via ORDER-SERVICE for OrderId: {}", request.getOrderId());
		orderFeignClient.updateOrderStatus(request.getOrderId(), "paid");

		log.info("Order status updated successfully for OrderId: {}", request.getOrderId());

		log.info("PaymentServiceImpl :: processPayment() completed");

		return mapToDto(payment, userDto);
	}

	private PaymentResponseDTO mapToDto(Payment payment, UserDto userDto) {

		log.debug("Mapping Payment entity to PaymentResponseDTO for PaymentId: {}", payment.getId());

		PaymentResponseDTO response = new PaymentResponseDTO();
		BeanUtils.copyProperties(payment, response);
		response.setUserDto(userDto);

		return response;
	}
}
