package com.example.workflow.service.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class EmailSender {

    @Value("${SENDGRID_API_KEY}")
    public String SENDGRID_API_KEY;

    @Value("${EMAIL_SENDER}")
    public String EMAIL_SENDER;

    @Value("${SEND_EMAIL}")
    public Boolean isSendMail;
    public void sendSimpleMessage(String to, String subject, String text) {
        Email emailFrom = new Email(EMAIL_SENDER);
        Email emailTo = new Email(to);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(emailFrom, subject, emailTo, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            if (isSendMail) {
                sg.api(request);
                log.info("Email sent successfully to '{}' with subject '{}'", to, subject);

            } else {
                log.info("Email property configuration is set to 'false'");
            }

        } catch (IOException ex) {
            log.error("Error sending email to '{}'", to, ex);
        }
    }
}
