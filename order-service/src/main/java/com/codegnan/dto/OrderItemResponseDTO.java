package com.codegnan.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDTO {
private Long productId;
private Integer quantity;
private BigDecimal price;
}
