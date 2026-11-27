package com.clicKart.mail.service;

public interface MailService {

    void sendOrderConfirmationEmail(Long orderId);

}