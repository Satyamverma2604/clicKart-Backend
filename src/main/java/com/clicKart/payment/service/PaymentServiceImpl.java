package com.clicKart.payment.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clicKart.common.OrderStatus;
import com.clicKart.common.PaymentStatus;
import com.clicKart.exception.BadRequestException;
import com.clicKart.exception.InvalidOperationException;
import com.clicKart.exception.ResourceNotFoundException;
import com.clicKart.mail.service.MailService;
import com.clicKart.order.entity.Order;
import com.clicKart.order.repository.OrderRepository;
import com.clicKart.payment.dto.PaymentCreateRequest;
import com.clicKart.payment.dto.PaymentCreateResponse;
import com.clicKart.payment.dto.PaymentVerifyRequest;
import com.clicKart.payment.dto.PaymentVerifyResponse;
import com.clicKart.payment.entity.Payment;
import com.clicKart.payment.gateway.PaymentGateway;
import com.clicKart.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;
    private final MailService mailService;

    @Override
    @Transactional
    public PaymentCreateResponse createPayment(
            PaymentCreateRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: "
                                        + request.getOrderId()));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOperationException(
                    "Cannot make payment for a cancelled order");
        }

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new InvalidOperationException(
                    "Order is already paid");
        }

        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new InvalidOperationException(
                    "Payment already exists for order: "
                            + order.getId());
        }

        BigDecimal amount = order.getTotalAmount();

        PaymentCreateResponse gatewayResponse =
                paymentGateway.createOrder(
                        order.getId(),
                        amount);

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setRazorpayOrderId(
                gatewayResponse.getRazorpayOrderId());
        payment.setAmount(amount);
        payment.setCurrency(
                gatewayResponse.getCurrency());
        payment.setStatus(PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);

        gatewayResponse.setPaymentId(savedPayment.getId());

        return gatewayResponse;
    }

@Override
@Transactional
public PaymentVerifyResponse verifyPayment(
        PaymentVerifyRequest request) {

    Payment payment = paymentRepository
            .findByRazorpayOrderId(
                    request.getRazorpayOrderId())
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Payment not found for Razorpay order: "
                                    + request.getRazorpayOrderId()));

    if (!payment.getOrder().getId()
            .equals(request.getOrderId())) {

        throw new BadRequestException(
                "Payment does not belong to this order");
    }

    boolean verified = paymentGateway.verifyPayment(
            request.getRazorpayOrderId(),
            request.getRazorpayPaymentId(),
            request.getRazorpaySignature());

    if (!verified) {

        payment.setStatus(PaymentStatus.FAILED);

        paymentRepository.save(payment);

        throw new BadRequestException(
                "Payment verification failed");
    }

    payment.setRazorpayPaymentId(
            request.getRazorpayPaymentId());

    payment.setStatus(PaymentStatus.PAID);

    Order order = payment.getOrder();

    order.setPaymentStatus(PaymentStatus.PAID);
    order.setStatus(OrderStatus.CONFIRMED);

    Payment savedPayment =
            paymentRepository.save(payment);

    orderRepository.save(order);

    // Send payment success email
    mailService.sendOrderConfirmationEmail(order.getId());

    PaymentVerifyResponse response =
            new PaymentVerifyResponse();

    response.setPaymentId(savedPayment.getId());
    response.setOrderId(order.getId());
    response.setRazorpayPaymentId(
            savedPayment.getRazorpayPaymentId());
    response.setStatus(savedPayment.getStatus());
    response.setMessage(
            "Payment verified successfully");

    return response;
}
}