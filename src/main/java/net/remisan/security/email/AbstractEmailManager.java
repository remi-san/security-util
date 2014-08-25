package net.remisan.security.email;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import javax.mail.Address;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

public abstract class AbstractEmailManager implements EmailManager {
    
    private Map<String, String> defaultParams;
    
    public void setDefaultEmailSender(Map<String, String> params) {
        this.defaultParams = params;
    }
    
    @Async
    @Override
    public Future<Boolean> send(
        final Address from,
        final Address to,
        final String subject,
        final String message
    ) {
        return this.send(from, to, subject, message, null);
    }
    
    @Async
    @Override
    public Future<Boolean> send(
        final Address from,
        final Address to,
        final String subject,
        final String message,
        final Map<String, String> parameters
    ) {
       
        final String finalMessage = this.getParameteredMessage(message, parameters);
        
        this.sendMessage(from, to, subject, finalMessage);
        
        return new AsyncResult<Boolean>(true);
    }

    @Async
    @Override
    public Future<Boolean> sendWithTemplate(
        final Address from,
        final Address to,
        final String subject,
        final String templateName,
        final Map<String, String> parameters
    ) {
        String message = this.getTemplateMessage(templateName, parameters);
        return this.send(from, to, subject, message, null);
    }

    protected abstract void sendMessage(
        final Address from,
        final Address to,
        final String subject,
        final String message
    );
    
    protected abstract String getTemplateMessage(String templateName, Map<String, String> parameters);
    
    protected String getParameteredMessage(String message, Map<String, String> parameters) {
        
        String parameteredMessage = message;
        Map<String, String> finalParameters = parameters;
        
        if (finalParameters == null) {
            finalParameters = new HashMap<String, String>();
        }
            
        if (this.defaultParams != null) {
            for (Entry<String, String> entry : this.defaultParams.entrySet()) {
                if (!finalParameters.containsKey(entry.getKey())) {
                    finalParameters.put(entry.getKey(), entry.getValue());
                }
            }
        }
        
        for (Entry<String, String> entry : finalParameters.entrySet()) {
            String key = "\\{" + entry.getKey() + "\\}";
            String value = entry.getValue();
            
            parameteredMessage = parameteredMessage.replaceAll(key, value);
        }
        
        return parameteredMessage;
    }
}
