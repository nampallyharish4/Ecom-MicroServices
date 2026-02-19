package com.codegnan.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.codegnan.dto.CartItemRequestDto;
import com.codegnan.dto.CartItemResponseDTO;
import com.codegnan.dto.UserDto;
import com.codegnan.exceptions.ResourceNotFoundException;
import com.codegnan.feignclients.ProductFeignClient;
import com.codegnan.feignclients.UserFeignClient;
import com.codegnan.model.CartItem;
import com.codegnan.repository.CartItemRepository;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private CartItemRepository cartItemRepository;
    private UserFeignClient userFeignClient;
    private ProductFeignClient productFeignClient;
    
    public CartServiceImpl(CartItemRepository cartItemRepository, 
            UserFeignClient userFeignClient,
            ProductFeignClient productFeignClient) {
        this.cartItemRepository = cartItemRepository;
        this.userFeignClient = userFeignClient;
        this.productFeignClient = productFeignClient;
    }

    @Override
    public CartItemResponseDTO addToCart(CartItemRequestDto request) {

        log.info("CartServiceImpl :: addToCart() started");
        log.debug("Request received - UserId: {}, ProductId: {}, Quantity: {}", 
                request.getUserId(), request.getProductId(), request.getQuantity());

        boolean productExists = productFeignClient.isProductExits(request.getProductId());
        log.debug("Product existence check result: {}", productExists);

        if (!productExists) {
            log.error("Product does not exist with ID: {}", request.getProductId());
            throw new ResourceNotFoundException("Product Doesn't exists in Db");
        }

        UserDto userDto = userFeignClient.fetchUser(request.getUserId().intValue());
        log.debug("User fetched from User Service: {}", request.getUserId());

        if (userDto == null) {
            log.error("User does not exist with ID: {}", request.getUserId());
            throw new ResourceNotFoundException("User Doesn't exists in Db");
        }

        CartItem cartItem = new CartItem();
        BeanUtils.copyProperties(request, cartItem);
        log.debug("Copied properties from DTO to CartItem entity");

        CartItem dbCartItem = cartItemRepository.save(cartItem);
        log.info("Cart item saved successfully with ID: {}", dbCartItem.getId());

        log.info("CartServiceImpl :: addToCart() completed");
        return mapToResponseDto(dbCartItem);
    }

    private CartItemResponseDTO mapToResponseDto(CartItem dbCartItem) {

        log.debug("Mapping CartItem entity to CartItemResponseDTO for ID: {}", dbCartItem.getId());

        CartItemResponseDTO response = new CartItemResponseDTO();
        BeanUtils.copyProperties(dbCartItem, response);

        return response;
    }

    @Override
    public List<CartItemResponseDTO> getUserCart(Long userId) {

        log.info("CartServiceImpl :: getUserCart() called for UserId: {}", userId);

        List<CartItemResponseDTO> cartItems = cartItemRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());

        log.info("Fetched {} cart items for UserId: {}", cartItems.size(), userId);

        return cartItems;
    }

    @Override
    @Transactional
    public void removeItem(Long userId, Long productId) {

        log.info("CartServiceImpl :: removeItem() called for UserId: {}, ProductId: {}", 
                userId, productId);

        cartItemRepository.deleteByUserIdAndProductId(userId, productId);

        log.info("Cart item removed successfully for UserId: {}, ProductId: {}", 
                userId, productId);
    }

    @Override
    public CartItemResponseDTO updateQuanity(CartItemRequestDto request) {

        log.info("CartServiceImpl :: updateQuanity() started");
        log.debug("Updating quantity for UserId: {}, ProductId: {}", 
                request.getUserId(), request.getProductId());

        CartItem cartItem = cartItemRepository
                .findByUserIdAndProductId(request.getUserId(), request.getProductId())
                .orElseThrow(() -> {
                    log.error("Item not found in cart for UserId: {}, ProductId: {}", 
                            request.getUserId(), request.getProductId());
                    return new RuntimeException("Item not in the cart");
                });

        cartItem.setQuantity(request.getQuantity());
        log.debug("Quantity updated to {}", request.getQuantity());

        CartItem dbCart = cartItemRepository.save(cartItem);
        log.info("Cart item updated successfully with ID: {}", dbCart.getId());

        return mapToResponseDto(dbCart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {

        log.info("CartServiceImpl :: clearCart() called for UserId: {}", userId);

        cartItemRepository.deleteByUserId(userId);

        log.info("Cart cleared successfully for UserId: {}", userId);
    }
}