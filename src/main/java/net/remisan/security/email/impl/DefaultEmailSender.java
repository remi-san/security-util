package net.remisan.security.email.impl;

import java.util.Map;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import net.remisan.security.email.AbstractEmailManager;
import net.remisan.security.email.EmailManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class DefaultEmailSender
    extends AbstractEmailManager
    implements EmailManager {

    @Autowired
    private JavaMailSender emailSender;
    
    protected void sendMessage(
        final Address from,
        final Address to,
        final String subject,
        final String message
    ) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            
            @Override
            public void prepare(MimeMessage mimeMessage) throws MessagingException {
                
                mimeMessage.setRecipient(Message.RecipientType.TO, to);
                mimeMessage.setFrom(from);
                mimeMessage.setSubject(subject);
                mimeMessage.setText(message);
            }
        };
        
        this.emailSender.send(preparator);
    }
    
    protected String getTemplateMessage(String templateName, Map<String, String> parameters) {
        throw new UnsupportedOperationException();
    }
}
