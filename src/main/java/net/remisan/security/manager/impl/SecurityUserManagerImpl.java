package net.remisan.security.manager.impl;

import net.remisan.base.manager.AbstractManager;
import net.remisan.base.repository.Repository;
import net.remisan.security.manager.SecurityUserManager;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.repository.SecurityUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserManagerImpl
    extends AbstractManager<SecurityUser>
    implements SecurityUserManager {

    @Autowired
    protected SecurityUserRepository repository;
    
    @Override
    protected Repository<SecurityUser> getRepository() {
        return this.repository;
    }

    @Override
    public SecurityUser getByLogin(String login) {
        return this.repository.getByLogin(login);
    }

    @Override
    public SecurityUser getByEmail(String email) {
        return this.repository.getByEmail(email);
    }

    @Override
    public SecurityUser getByActivationToken(String token) {
        return this.repository.getByActivationToken(token);
    }
}