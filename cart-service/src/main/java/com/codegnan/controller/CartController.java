package com.codegnan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.codegnan.dto.CartItemRequestDto;
import com.codegnan.dto.CartItemResponseDTO;
import com.codegnan.service.CartService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/cart")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

	@Autowired
	private CartService cartService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public CartItemResponseDTO addItemToCart(@RequestBody CartItemRequestDto request) {

		log.info("CartController :: addItemToCart() called");
		log.debug("Request - UserId: {}, ProductId: {}", 
				request.getUserId(), request.getProductId());

		CartItemResponseDTO response = cartService.addToCart(request);

		log.info("CartController :: addItemToCart() completed successfully");
		return response;
	}
	
	@PutMapping
	public CartItemResponseDTO updateQuanity(@RequestBody CartItemRequestDto request) {

		log.info("CartController :: updateQuanity() called");

		CartItemResponseDTO response = cartService.updateQuanity(request);

		log.info("CartController :: updateQuanity() completed successfully");
		return response;
	}
	
	@GetMapping("/{userId}")
	public List<CartItemResponseDTO> getCartByUserId(@PathVariable Long userId) {

		log.info("CartController :: getCartByUserId() called for UserId: {}", userId);

		List<CartItemResponseDTO> response = cartService.getUserCart(userId);

		log.info("CartController :: getCartByUserId() completed for UserId: {}", userId);
		return response;
	}
	
	@DeleteMapping("/clear/{userId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void clearUserCart(@PathVariable Long userId) {

		log.info("CartController :: clearUserCart() called for UserId: {}", userId);

		cartService.clearCart(userId);

		log.info("CartController :: clearUserCart() completed for UserId: {}", userId);
	}
	
	@DeleteMapping("/remove/{productId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeItemFromCart(@PathVariable Long productId,
			@RequestParam Long userId) {

		log.info("CartController :: removeItemFromCart() called for UserId: {}, ProductId: {}", 
				userId, productId);

		cartService.removeItem(userId, productId);

		log.info("CartController :: removeItemFromCart() completed");
	}
}