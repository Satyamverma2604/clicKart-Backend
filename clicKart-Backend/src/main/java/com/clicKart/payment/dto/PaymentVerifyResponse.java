package com.clicKart.payment.dto;

import com.clicKart.common.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerifyResponse {

    private Long paymentId;
    private Long orderId;
    private String razorpayPaymentId;
    private PaymentStatus status;
    private String message;
}