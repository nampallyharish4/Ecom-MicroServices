package com.codegnan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingRequestDTO {

	private Long orderId;
	private String shippingMethod;
	private String carrier;
}
