package net.remisan.security.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.mail.internet.AddressException;

import org.springframework.validation.BindException;

import net.remisan.base.service.Service;
import net.remisan.security.model.SecurityUser;

public interface UserService extends Service<SecurityUser> {

    SecurityUser getUser(String login);

    SecurityUser getPersistableUser(SecurityUser user) throws InstantiationException, IllegalAccessException;

    SecurityUser getUserByEmail(String email);
    
    SecurityUser getUserByToken(String token);

    SecurityUser createNewUser() throws InstantiationException, IllegalAccessException;

    SecurityUser createNewUser(String email, String login, String password)
        throws InstantiationException, IllegalAccessException;

    SecurityUser validateUser(String token) throws BindException;
    
    void sendResetPasswordMessage(final SecurityUser user)
        throws AddressException, InterruptedException, ExecutionException, BindException;

    Future<Boolean> sendInscriptionMessage(final SecurityUser user) throws AddressException;

}
