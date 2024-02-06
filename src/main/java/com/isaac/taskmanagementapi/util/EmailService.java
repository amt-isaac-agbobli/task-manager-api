package com.isaac.taskmanagementapi.util;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}