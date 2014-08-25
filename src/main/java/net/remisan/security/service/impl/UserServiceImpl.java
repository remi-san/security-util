package net.remisan.security.service.impl;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import net.remisan.base.manager.Manager;
import net.remisan.base.service.AbstractService;
import net.remisan.security.email.EmailManager;
import net.remisan.security.manager.SecurityUserManager;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.service.RoleService;
import net.remisan.security.service.UserService;
import net.remisan.security.util.UserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

@Service
@Transactional
public class UserServiceImpl
    extends AbstractService<SecurityUser>
    implements UserService {

    @Autowired
    private SecurityUserManager manager;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailManager mailSender;

    @Autowired
    private UserUtil userUtil;
    
    @Autowired
    private Address from;
    
    @Override
    protected Manager<SecurityUser> getManager() {
        return this.manager;
    }

    @Override
    public SecurityUser getUser(String login) {
        return this.manager.getByLogin(login);
    }

    @Override
    public SecurityUser getPersistableUser(SecurityUser user) throws InstantiationException, IllegalAccessException {
        return this.manager.getPersistable(user);
    }

    @Override
    public SecurityUser getUserByEmail(String email) {
        return this.manager.getByEmail(email);
    }
    
    public SecurityUser getUserByToken(String token) {
        return this.manager.getByActivationToken(token);
    }

    @Override
    public SecurityUser save(SecurityUser user) throws BindException {

        this.userUtil.preSave(user);
        boolean isNew = user.isNew();

        SecurityUser u = this.manager.save(user);
        
        if (isNew) {
            this.userUtil.postSave(u, true);
        }

        return u;
    }
    
    @Override
    public SecurityUser createNewUser() throws InstantiationException, IllegalAccessException {
        return this.getNewInstance();
    }

    @Override
    public SecurityUser createNewUser(String email, String login, String password)
        throws InstantiationException, IllegalAccessException {
        SecurityUser user = this.createNewUser();
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    @Override
    public SecurityUser validateUser(String token) throws BindException {
        SecurityUser user = this.manager.getByActivationToken(token);
        if (user != null) {
            user.setEnabled(true);
            user.setActivationToken(null);
            this.save(user);
        }
        return user;
    }
    
    @Override
    public Future<Boolean> sendInscriptionMessage(final SecurityUser user) throws AddressException {
        String validateUriBase = "{baseUrl}/validate.html";
        Future<Boolean> result = this.mailSender.send(
            this.from,
            new InternetAddress(user.getEmail()),
            "Inscription",
            validateUriBase + "?token=" + user.getActivationToken()
        );
        
        return result;
    }

    @Override
    public void sendResetPasswordMessage(SecurityUser user)
        throws AddressException, InterruptedException, ExecutionException, BindException {
        String token = UUID.randomUUID().toString();
        user.setActivationToken(token);
        
        String validateUriBase = "{baseUrl}/reset-password.html";
        
        Future<Boolean> result = this.mailSender.send(
            this.from,
            new InternetAddress(user.getEmail()),
            "Inscription",
            validateUriBase + "?reset=" + token
        );
        
        this.save(user);
        
        result.get();
        
    }
}
