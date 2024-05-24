package com.example.workflow.service;

public class ApprovalEmailStrategy implements EmailStrategy {
    @Override
    public void sendEmail(String emailTo, String message) {

        // Implementar lógica de envio de e-mail de aprovação
        System.out.println("Sending approval email to " + emailTo);
        System.out.println("Message: " + message);
        // Lógica de envio de e-mail

    }
}
