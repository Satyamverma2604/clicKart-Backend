
package com.clicKart.mail.service;

import java.nio.charset.StandardCharsets;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clicKart.exception.ResourceNotFoundException;
import com.clicKart.order.entity.Order;
import com.clicKart.order.entity.OrderItem;
import com.clicKart.order.repository.OrderRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public void sendOrderConfirmationEmail(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId));

        String customerName = escapeHtml(order.getUser().getName());

        String orderReference = escapeHtml(order.getOrderReference());

        String totalAmount = order.getTotalAmount().toPlainString();

        String paymentStatus = escapeHtml(
                order.getPaymentStatus().toString());

        String orderStatus = escapeHtml(
                order.getStatus().toString());

        // Generate dynamic product rows
        String orderItemsHtml = generateOrderItemsHtml(order);

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    StandardCharsets.UTF_8.name()
            );

            helper.setTo(order.getUser().getEmail());

            helper.setSubject(
                    "Order Confirmed! - " + orderReference + " | clicKart"
            );

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <body style="margin:0;padding:0;background:#f4f7fc;
                    font-family:Arial,Helvetica,sans-serif;">

                    <table width="100%" cellpadding="0" cellspacing="0"
                    style="padding:35px 12px;background:#f4f7fc;">
                    <tr><td align="center">

                    <table width="600" cellpadding="0" cellspacing="0"
                    style="max-width:600px;width:100%;background:#ffffff;
                    border-radius:12px;overflow:hidden;">

                    <!-- Header -->

                    <tr>
                    <td align="center" style="background:#263653;padding:28px;">
                    <h1 style="margin:0;color:#ffffff;font-size:30px;">
                    clic<span style="color:#8eb2ff;">Kart</span>
                    </h1>

                    <p style="color:#dce6fa;margin:8px 0 0;font-size:13px;">
                    Shopping made simple.
                    </p>
                    </td>
                    </tr>

                    <!-- Confirmation -->

                    <tr>
                    <td align="center" style="padding:35px 25px 20px;">

                    <div style="width:65px;height:65px;line-height:65px;
                    background:#e8f7ee;color:#239653;border-radius:50%;
                    font-size:34px;font-weight:bold;">
                    &#10003;
                    </div>

                    <h2 style="color:#263653;font-size:25px;margin:20px 0 10px;">
                    Order Confirmed!
                    </h2>

                    <p style="color:#6d7b94;font-size:15px;line-height:24px;">
                    Hello <strong>{{customerName}}</strong>,<br>
                    Your order has been successfully confirmed.
                    </p>

                    </td>
                    </tr>

                    <!-- Order Reference -->

                    <tr>
                    <td style="padding:15px 30px;">

                    <table width="100%" cellpadding="0" cellspacing="0"
                    style="background:#eaf0fc;border-radius:9px;">

                    <tr>
                    <td align="center" style="padding:20px;">

                    <p style="color:#6d7b94;font-size:12px;margin:0 0 8px;">
                    YOUR ORDER REFERENCE
                    </p>

                    <h2 style="color:#5278bd;font-size:23px;margin:0;">
                    {{orderReference}}
                    </h2>

                    </td>
                    </tr>

                    </table>
                    </td>
                    </tr>

                    <!-- Order Items -->

                    <tr>
                    <td style="padding:15px 30px 25px;">

                    <h3 style="color:#263653;font-size:17px;">
                    Order Items
                    </h3>

                    <table width="100%" cellpadding="0" cellspacing="0"
                    style="border:1px solid #e5eaf2;border-radius:8px;
                    border-collapse:collapse;">

                    <tr style="background:#eaf0fc;">

                    <th align="left" style="padding:12px;color:#263653;
                    font-size:12px;">
                    Product
                    </th>

                    <th align="center" style="padding:12px;color:#263653;
                    font-size:12px;">
                    Qty
                    </th>

                    <th align="right" style="padding:12px;color:#263653;
                    font-size:12px;">
                    Rate
                    </th>

                    <th align="right" style="padding:12px;color:#263653;
                    font-size:12px;">
                    Subtotal
                    </th>

                    </tr>

                    {{orderItems}}

                    </table>

                    <!-- Total Amount -->

                    <table width="100%" cellpadding="0" cellspacing="0"
                    style="margin-top:15px;">

                    <tr>
                    <td style="padding:10px;color:#6d7b94;">
                    Order Reference
                    </td>

                    <td align="right" style="padding:10px;color:#263653;
                    font-weight:bold;">
                    {{orderReference}}
                    </td>
                    </tr>

                    <tr>
                    <td style="padding:12px 10px;color:#263653;
                    font-size:16px;font-weight:bold;
                    border-top:1px solid #e5eaf2;">
                    Total Amount
                    </td>

                    <td align="right" style="padding:12px 10px;
                    color:#5278bd;font-size:18px;font-weight:bold;
                    border-top:1px solid #e5eaf2;">
                    ₹{{totalAmount}}
                    </td>
                    </tr>

                    </table>

                    </td>
                    </tr>

                    <!-- Payment Information -->

                    <tr>
                    <td style="padding:10px 30px 25px;">

                    <h3 style="color:#263653;font-size:17px;">
                    Payment Information
                    </h3>

                    <table width="100%" cellpadding="0" cellspacing="0"
                    style="border:1px solid #e5eaf2;border-radius:8px;">

                    <tr>
                    <td style="padding:15px;color:#6d7b94;">
                    Payment Status
                    </td>

                    <td align="right" style="padding:15px;color:#239653;
                    font-weight:bold;">
                    {{paymentStatus}}
                    </td>
                    </tr>

                    <tr>
                    <td style="padding:15px;color:#6d7b94;
                    border-top:1px solid #e5eaf2;">
                    Order Status
                    </td>

                    <td align="right" style="padding:15px;color:#239653;
                    font-weight:bold;border-top:1px solid #e5eaf2;">
                    {{orderStatus}}
                    </td>
                    </tr>

                    </table>
                    </td>
                    </tr>

                    <!-- Thank You -->

                    <tr>
                    <td align="center" style="padding:10px 30px 35px;">

                    <p style="color:#263653;font-size:17px;font-weight:bold;">
                    Thank you for shopping with clicKart!
                    </p>

                    <p style="color:#6d7b94;font-size:13px;line-height:22px;">
                    We appreciate your trust in us.<br>
                    We hope you enjoy your purchase!
                    </p>

                    </td>
                    </tr>

                    <!-- Footer -->

                    <tr>
                    <td align="center" style="background:#f8faff;padding:22px;
                    border-top:1px solid #e5eaf2;">

                    <p style="color:#263653;font-size:14px;font-weight:bold;">
                    Regards, clicKart Team
                    </p>

                    <p style="color:#9aa6b9;font-size:11px;">
                    This is an automated email. Please do not reply.
                    </p>

                    <p style="color:#9aa6b9;font-size:11px;">
                    © 2026 clicKart. All rights reserved.
                    </p>

                    </td>
                    </tr>

                    </table>
                    </td></tr>
                    </table>

                    </body>
                    </html>
                    """;

            // Replace dynamic placeholders

            htmlContent = htmlContent
                    .replace("{{customerName}}", customerName)
                    .replace("{{orderReference}}", orderReference)
                    .replace("{{totalAmount}}", totalAmount)
                    .replace("{{paymentStatus}}", paymentStatus)
                    .replace("{{orderStatus}}", orderStatus)
                    .replace("{{orderItems}}", orderItemsHtml);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException ex) {

            throw new RuntimeException(
                    "Unable to send order confirmation email", ex);
        }
    }

    // Generate dynamic order item rows

    private String generateOrderItemsHtml(Order order) {

        StringBuilder itemsHtml = new StringBuilder();

        for (OrderItem item : order.getItems()) {

            String productName = escapeHtml(
                    item.getProduct().getName());

            String quantity = item.getQuantity().toString();

            String rate = item.getPrice().toPlainString();

            String subtotal = item.getSubtotal().toPlainString();

            itemsHtml.append("""
                    <tr>

                    <td style="padding:15px;color:#263653;
                    border-top:1px solid #e5eaf2;font-size:13px;">
                    %s
                    </td>

                    <td align="center" style="padding:15px;
                    color:#263653;border-top:1px solid #e5eaf2;
                    font-size:13px;">
                    %s
                    </td>

                    <td align="right" style="padding:15px;
                    color:#263653;border-top:1px solid #e5eaf2;
                    font-size:13px;">
                    ₹%s
                    </td>

                    <td align="right" style="padding:15px;
                    color:#263653;font-weight:bold;
                    border-top:1px solid #e5eaf2;font-size:13px;">
                    ₹%s
                    </td>

                    </tr>
                    """.formatted(
                            productName,
                            quantity,
                            rate,
                            subtotal
                    ));
        }

        return itemsHtml.toString();
    }

    // Prevent HTML injection

    private String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
