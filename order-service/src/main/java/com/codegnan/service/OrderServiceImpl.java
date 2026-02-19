package com.codegnan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codegnan.dto.CartItemResponseDTO;
import com.codegnan.dto.OrderItemResponseDTO;
import com.codegnan.dto.OrderResponseDTO;
import com.codegnan.dto.PlaceOrderRequestDTO;
import com.codegnan.dto.ProductResponseDto;
import com.codegnan.dto.UserDto;
import com.codegnan.exceptions.ResourceNotFoundException;
import com.codegnan.feignclients.CartFeignClient;
import com.codegnan.feignclients.ProductFeignClient;
import com.codegnan.feignclients.UserFeignClient;
import com.codegnan.model.Order;
import com.codegnan.model.OrderItem;
import com.codegnan.repository.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

	/*
	 * ============================================================ Dependencies
	 * Injection ============================================================
	 * OrderRepository → Talks to database
	 * UserFeignClient → Calls USER-SERVICE
	 * ProductFeignClient → Calls PRODUCT-SERVICE
	 * CartFeignClient → Calls CART-SERVICE
	 *
	 * This is Microservices communication using Feign Clients.
	 */

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserFeignClient userFeignClient;

	@Autowired
	private ProductFeignClient productFeignClient;

	@Autowired
	private CartFeignClient cartFeignClient;

	/*
	 * ============================================================ placeOrder()
	 * ============================================================ Complete Order
	 * Flow:
	 *
	 * 1. Validate User (USER-SERVICE)
	 * 2. Fetch Cart Items (CART-SERVICE)
	 * 3. Calculate Total Price (PRODUCT-SERVICE)
	 * 4. Convert Cart Items → Order Items
	 * 5. Save Order in DB
	 * 6. Clear Cart
	 * 7. Return OrderResponseDTO
	 */

	@Override
	public OrderResponseDTO placeOrder(PlaceOrderRequestDTO request) {

		log.info("OrderServiceImpl :: placeOrder() started for UserId: {}", request.getUserId());

		// Step 1: Validate User
		log.debug("Validating user with USER-SERVICE");
		UserDto userDto = validateUser(request.getUserId().longValue());
		if (userDto == null) {
			log.error("User not Found with Id: {}", request.getUserId());
			throw new ResourceNotFoundException("User not Found in DB");
		}

		// Step 2: Fetch Cart Items
		log.debug("Fetching cart items from CART-SERVICE");
		List<CartItemResponseDTO> cartItems = fetchCartItems(request.getUserId().longValue());
		if (cartItems == null || cartItems.isEmpty()) {
			log.error("Cart is empty for UserId: {}", request.getUserId());
			throw new ResourceNotFoundException("Cart is Empty");
		}
		log.info("Fetched {} cart items", cartItems.size());

		// Step 3: Calculate Total Price
		log.debug("Calculating total price");
		BigDecimal totalPrice = calculateTotalPrice(cartItems);
		log.info("Total price calculated: {}", totalPrice);

		// Step 4: Build Order Items
		log.debug("Building Order Items from Cart Items");
		List<OrderItem> orderItems = buildOrderItems(cartItems);

		// Step 5: Create Order Object
		log.debug("Creating Order entity");
		Order order = createOrder(request, totalPrice, orderItems);

		// Step 6: Save Order in Database
		log.debug("Saving order into database");
		Order dbOrder = orderRepository.save(order);
		log.info("Order saved successfully with Id: {}", dbOrder.getId());

		// Step 7: Clear Cart after successful order
		log.debug("Clearing cart after successful order placement");
		cartFeignClient.clearUserCart(request.getUserId().longValue());

		// Step 8: Convert to Response DTO
		log.info("OrderServiceImpl :: placeOrder() completed");
		return mapToOrderResponse(dbOrder, userDto);
	}

	/*
	 * Converts Order Entity → OrderResponseDTO
	 * We never expose entity directly to client.
	 */
	private OrderResponseDTO mapToOrderResponse(Order dbOrder, UserDto userDto) {

		log.debug("Mapping Order entity to OrderResponseDTO for OrderId: {}", dbOrder.getId());

		OrderResponseDTO dto = new OrderResponseDTO();

		// Copy simple properties except items
		BeanUtils.copyProperties(dbOrder, dto, "items");

		dto.setOrderId(dbOrder.getId());
		dto.setUserDto(userDto);

		// Manually convert OrderItem → OrderItemResponseDTO
		List<OrderItemResponseDTO> orderItemsResponse =
				dbOrder.getItems().stream().map(item -> {
					OrderItemResponseDTO itemDto = new OrderItemResponseDTO();
					BeanUtils.copyProperties(item, itemDto);
					return itemDto;
				}).collect(Collectors.toList());

		dto.setItems(orderItemsResponse);

		log.debug("OrderResponseDTO mapping completed");
		return dto;
	}

	/*
	 * Creates Order Entity Object
	 */
	private Order createOrder(PlaceOrderRequestDTO request,
							  BigDecimal totalPrice,
							  List<OrderItem> orderItems) {

		log.debug("Creating Order entity object");

		Order order = new Order();
		order.setUserId(request.getUserId().longValue());
		order.setTotalPrice(totalPrice);
		order.setStatus("PLACED");

		// Set parent reference inside each OrderItem
		for (OrderItem item : orderItems) {
			item.setOrder(order);
		}

		order.setItems(orderItems);

		return order;
	}

	/*
	 * Converts Cart Items → Order Items
	 * Fetches Product price from PRODUCT-SERVICE
	 */
	private List<OrderItem> buildOrderItems(List<CartItemResponseDTO> cartItems) {

		log.debug("Converting CartItems to OrderItems");

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItemResponseDTO item : cartItems) {

			ProductResponseDto product =
					productFeignClient.getProduct(item.getProductId());

			OrderItem orderItem = new OrderItem();
			orderItem.setProductId(item.getProductId());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setPrice(product.getPrice());

			orderItems.add(orderItem);
		}

		return orderItems;
	}

	/*
	 * Calculates total order price using BigDecimal
	 */
	private BigDecimal calculateTotalPrice(List<CartItemResponseDTO> cartItems) {

		log.debug("Calculating total order price");

		BigDecimal total = BigDecimal.ZERO;

		for (CartItemResponseDTO item : cartItems) {

			ProductResponseDto product =
					productFeignClient.getProduct(item.getProductId());

			Integer quantity = item.getQuantity();

			BigDecimal individualPrice =
					product.getPrice().multiply(BigDecimal.valueOf(quantity));

			total = total.add(individualPrice);
		}

		return total;
	}

	/*
	 * Fetch cart items using CART-SERVICE
	 */
	private List<CartItemResponseDTO> fetchCartItems(Long userId) {
		log.debug("Calling CART-SERVICE for userId: {}", userId);
		return cartFeignClient.getCartByUserId(userId);
	}

	/*
	 * Validate user using USER-SERVICE
	 */
	private UserDto validateUser(Long userId) {
		log.debug("Calling USER-SERVICE to validate userId: {}", userId);
		return userFeignClient.fetchUser(userId.intValue());
	}

	/*
	 * ============================================================
	 * updateOrderStatus()
	 * ============================================================
	 * Updates order status like:
	 * PLACED → SHIPPED → DELIVERED → CANCELLED
	 */
	@Override
	public void updateOrderStatus(Long orderId, String status) {

		log.info("Updating order status. OrderId: {}, Status: {}", orderId, status);

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> {
					log.error("Order Not found with Id: {}", orderId);
					return new ResourceNotFoundException("Order Not found with Id " + orderId);
				});

		order.setStatus(status);
		orderRepository.save(order);

		log.info("Order status updated successfully");
	}

	@Override
	public List<OrderResponseDTO> getOrdersByUser(Long userId) {

		log.info("Fetching orders for userId: {}", userId);

		UserDto userDto = validateUser(userId);
		if (userDto == null) {
			log.error("User not found for userId: {}", userId);
			throw new ResourceNotFoundException("User not found");
		}

		Optional<Order> orders = orderRepository.findById(userId);

		if (orders.isEmpty()) {
			log.warn("No orders found for user id: {}", userId);
			throw new ResourceNotFoundException("No orders found for user id: " + userId);
		}

		return orders.stream()
				.map(order -> mapToOrderResponse(order, userDto))
				.collect(Collectors.toList());
	}

	@Override
	public OrderResponseDTO getOrderById(Long orderId) {

		log.info("Fetching order by orderId: {}", orderId);

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> {
					log.error("Order Not Found With Id: {}", orderId);
					return new ResourceNotFoundException("Order Not Found With Id " + orderId);
				});

		UserDto userDto = validateUser(order.getUserId());

		return mapToOrderResponse(order, userDto);
	}
}
