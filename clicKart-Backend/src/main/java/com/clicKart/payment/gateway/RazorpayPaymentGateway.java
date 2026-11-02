package com.clicKart.payment.gateway;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.clicKart.exception.InvalidOperationException;
import com.clicKart.payment.dto.PaymentCreateResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@Component
public class RazorpayPaymentGateway implements PaymentGateway {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Override
    public PaymentCreateResponse createOrder(
            Long orderId,
            BigDecimal amount) {

        try {

            RazorpayClient razorpayClient =
                    new RazorpayClient(keyId, keySecret);

            JSONObject orderRequest = new JSONObject();

            orderRequest.put(
                    "amount",
                    amount
                            .multiply(BigDecimal.valueOf(100))
                            .longValue()
            );

            orderRequest.put(
                    "currency",
                    "INR"
            );

            orderRequest.put(
                    "receipt",
                    "order_" + orderId
            );

            Order razorpayOrder =
                    razorpayClient.orders.create(orderRequest);

            PaymentCreateResponse response =
                    new PaymentCreateResponse();

            response.setOrderId(orderId);

            response.setRazorpayOrderId(
                    razorpayOrder.get("id")
            );

            response.setRazorpayKeyId(keyId);

            response.setAmount(amount);

            response.setCurrency("INR");

            return response;

        } catch (Exception e) {

            throw new InvalidOperationException(
                    "Unable to create Razorpay order"
            );
        }
    }

    @Override
    public boolean verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        try {

            JSONObject attributes = new JSONObject();

            attributes.put(
                    "razorpay_order_id",
                    razorpayOrderId
            );

            attributes.put(
                    "razorpay_payment_id",
                    razorpayPaymentId
            );

            attributes.put(
                    "razorpay_signature",
                    razorpaySignature
            );

            return Utils.verifyPaymentSignature(
                    attributes,
                    keySecret
            );

        } catch (Exception e) {

            throw new InvalidOperationException(
                    "Unable to verify Razorpay payment"
            );
        }
    }
}