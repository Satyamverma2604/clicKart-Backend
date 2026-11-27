package com.clicKart.mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clicKart.mail.service.MailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test/mail")
@RequiredArgsConstructor
public class MailTestController {

    private final MailService mailService;

    @PostMapping("/{orderId}")
    public ResponseEntity<String> sendTestMail(
            @PathVariable Long orderId) {

        mailService.sendOrderConfirmationEmail(orderId);

        return ResponseEntity.ok(
                "Order confirmation email sent successfully");
    }
}