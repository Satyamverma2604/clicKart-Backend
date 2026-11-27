package com.clicKart.payment.gateway;

import java.math.BigDecimal;

import com.clicKart.payment.dto.PaymentCreateResponse;

public interface PaymentGateway {

    PaymentCreateResponse createOrder(Long orderId, BigDecimal amount);

    boolean verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    );
}