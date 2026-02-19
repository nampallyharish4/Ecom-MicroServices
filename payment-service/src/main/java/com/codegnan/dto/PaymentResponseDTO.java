package com.codegnan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PaymentResponseDTO {
	private Long id;
	private Long orderId;
	private BigDecimal amount;
	private String status;// success,failure,pending
	private LocalDateTime paymentTime;
	private UserDto userDto;
}
