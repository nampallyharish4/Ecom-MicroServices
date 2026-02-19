package com.codegnan.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shipping_info")
public class ShippingInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long orderId;
	private String shippingMethod;// stanadard nextday,
	private LocalDateTime shippedAt;
	private LocalDateTime deliveryDate;
	private String status;// SHIPPED,IN-Transit,DEVLIVERED,
	private String carrier;// FEDEX,DHL,UPS...
}
