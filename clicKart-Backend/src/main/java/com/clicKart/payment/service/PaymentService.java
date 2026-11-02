package com.clicKart.payment.service;

import com.clicKart.payment.dto.PaymentCreateRequest;
import com.clicKart.payment.dto.PaymentCreateResponse;
import com.clicKart.payment.dto.PaymentVerifyRequest;
import com.clicKart.payment.dto.PaymentVerifyResponse;

public interface PaymentService {

    PaymentCreateResponse createPayment(PaymentCreateRequest request);

    PaymentVerifyResponse verifyPayment(PaymentVerifyRequest request);
}