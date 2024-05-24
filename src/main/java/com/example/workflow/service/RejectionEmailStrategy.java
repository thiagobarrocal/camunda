package com.example.workflow.service;

public class RejectionEmailStrategy implements EmailStrategy {
    @Override
    public void sendEmail(String emailTo, String message) {

            // Implementar lógica de envio de e-mail de rejeição
            System.out.println("Sending rejection email to " + emailTo);
            System.out.println("Message: " + message);
            // Lógica de envio de e-mail

    }
}
