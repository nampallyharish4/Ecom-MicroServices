package com.codegnan.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PaymentRequestDTO {
private Long orderId;
private Long userId;
private BigDecimal amount;
}
