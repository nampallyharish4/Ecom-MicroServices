package com.codegnan.service;

import com.codegnan.dto.ShippingRequestDTO;
import com.codegnan.dto.ShippingResponseDTO;

public interface ShippingService {
	ShippingResponseDTO shipOrder(ShippingRequestDTO request);

	void updateShippingStatus(Long orderId, String status);
}
