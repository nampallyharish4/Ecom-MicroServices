package com.codegnan.service;

import com.codegnan.dto.PaymentRequestDTO;
import com.codegnan.dto.PaymentResponseDTO;

public interface PaymentService {
	PaymentResponseDTO processPayment(PaymentRequestDTO request);
}
