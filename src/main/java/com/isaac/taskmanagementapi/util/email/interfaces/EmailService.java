package com.isaac.taskmanagementapi.util.email.interfaces;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}