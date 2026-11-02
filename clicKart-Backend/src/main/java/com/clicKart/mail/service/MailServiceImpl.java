package com.clicKart.mail.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.clicKart.exception.ResourceNotFoundException;
import com.clicKart.order.entity.Order;
import com.clicKart.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final OrderRepository orderRepository;

    @Override
    public void sendOrderConfirmationEmail(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId));

        String customerEmail = order.getUser().getEmail();

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(customerEmail);
        message.setSubject(
                "clicKart Order Confirmation - Order #" + order.getId());

        message.setText(
                "Hello " + order.getUser().getName() + ",\n\n"
                + "Your order has been successfully confirmed.\n\n"
                + "Order ID: " + order.getId() + "\n"
                + "Total Amount: ₹" + order.getTotalAmount() + "\n"
                + "Payment Status: " + order.getPaymentStatus() + "\n"
                + "Order Status: " + order.getStatus() + "\n\n"
                + "Thank you for shopping with clicKart!\n\n"
                + "Regards,\n"
                + "clicKart Team"
        );

        mailSender.send(message);
    }
}