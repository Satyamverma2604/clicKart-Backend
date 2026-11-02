package com.clicKart.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clicKart.payment.dto.PaymentCreateRequest;
import com.clicKart.payment.dto.PaymentCreateResponse;
import com.clicKart.payment.dto.PaymentVerifyRequest;
import com.clicKart.payment.dto.PaymentVerifyResponse;
import com.clicKart.payment.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentCreateResponse> createPayment(
            @Valid @RequestBody PaymentCreateRequest request) {

        PaymentCreateResponse response =
                paymentService.createPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentVerifyResponse> verifyPayment(
            @Valid @RequestBody PaymentVerifyRequest request) {

        PaymentVerifyResponse response =
                paymentService.verifyPayment(request);

        return ResponseEntity.ok(response);
    }
}