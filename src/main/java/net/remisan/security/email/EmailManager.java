package net.remisan.security.email;

import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.Address;

public interface EmailManager {

    void setDefaultEmailSender(Map<String, String> params);
    
    Future<Boolean> send(
        final Address from,
        final Address to,
        String subject,
        String message
    );
    
    Future<Boolean> send(
        final Address from,
        final Address to,
        String subject,
        String message,
        Map<String, String> parameters
    );
    
    Future<Boolean> sendWithTemplate(
        final Address from,
        final Address to,
        String subject,
        String templateName,
        Map<String, String> parameters
    );

}