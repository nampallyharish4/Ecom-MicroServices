package com.codegnan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegnan.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	Optional<CartItem> findByUserIdAndProductId(Long userId,Long productId);
	
	List<CartItem> findByUserId(Long userId);
	
	void deleteByUserId(Long userId);
	
	void deleteByUserIdAndProductId(Long userId,Long productId);
}
