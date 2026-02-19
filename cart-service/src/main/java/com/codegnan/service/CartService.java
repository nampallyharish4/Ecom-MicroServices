package com.codegnan.service;

import java.util.List;

import com.codegnan.dto.CartItemRequestDto;
import com.codegnan.dto.CartItemResponseDTO;

public interface CartService {

	CartItemResponseDTO addToCart(CartItemRequestDto request);
	
	List<CartItemResponseDTO> getUserCart(Long userId);
	
	void removeItem(Long userId,Long productId);
	
	CartItemResponseDTO updateQuanity(CartItemRequestDto request);
	
	void clearCart(Long userId);
}
