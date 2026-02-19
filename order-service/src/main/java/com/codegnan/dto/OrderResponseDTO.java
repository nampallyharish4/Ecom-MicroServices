package com.codegnan.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDTO {
private Long orderId;
private Long userId;
private BigDecimal totalPrice;
private String status;
private List<OrderItemResponseDTO> items;
private UserDto userDto;
}
