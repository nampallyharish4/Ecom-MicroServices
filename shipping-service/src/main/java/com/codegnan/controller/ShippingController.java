package com.codegnan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.codegnan.dto.ShippingRequestDTO;
import com.codegnan.dto.ShippingResponseDTO;
import com.codegnan.service.ShippingService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/shipping")
@AllArgsConstructor
@Slf4j
public class ShippingController {

    private ShippingService shippingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShippingResponseDTO shipOrder(@RequestBody ShippingRequestDTO request) {

        log.info("ShippingController :: shipOrder() called for OrderId: {}", request.getOrderId());

        ShippingResponseDTO response = shippingService.shipOrder(request);

        log.info("ShippingController :: shipOrder() completed successfully");

        return response;
    }

    @PutMapping("/{orderId}/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public void updateShippingStatus(
            @PathVariable Long orderId,
            @PathVariable String status) {

        log.info("ShippingController :: updateShippingStatus() called for OrderId: {}, Status: {}", orderId, status);

        shippingService.updateShippingStatus(orderId, status);

        log.info("ShippingController :: updateShippingStatus() completed for OrderId: {}", orderId);
    }
}
