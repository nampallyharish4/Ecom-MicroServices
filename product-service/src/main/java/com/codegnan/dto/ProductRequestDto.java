package com.codegnan.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductRequestDto {

	private String name;
	private String description;
	private BigDecimal price;
	private Integer stock;
}
