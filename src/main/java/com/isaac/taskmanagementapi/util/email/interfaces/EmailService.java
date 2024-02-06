package com.isaac.taskmanagementapi.util.email;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}