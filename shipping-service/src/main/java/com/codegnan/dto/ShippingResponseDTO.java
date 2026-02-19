package com.codegnan.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ShippingResponseDTO {
	private Long orderId;
	private String shippingMethod;// stanadard nextday,
	private LocalDateTime shippedAt;
	private LocalDateTime deliveryDate;
	private String status;// SHIPPED,IN-Transit,DEVLIVERED,
	private String carrier;// FEDEX,DHL,UPS...
}
