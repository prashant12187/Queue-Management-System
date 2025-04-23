package com.hospital_management_system.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
