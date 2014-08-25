package net.remisan.security.manager.impl;

import net.remisan.base.manager.AbstractManager;
import net.remisan.base.repository.Repository;
import net.remisan.security.manager.SecurityRoleManager;
import net.remisan.security.model.SecurityRole;
import net.remisan.security.repository.SecurityRoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityRoleManagerImpl
    extends AbstractManager<SecurityRole>
    implements SecurityRoleManager {

    @Autowired
    protected SecurityRoleRepository repository;
    
    @Override
    protected Repository<SecurityRole> getRepository() {
        return this.repository;
    }

    @Override
    public SecurityRole getByName(String name) {
        return this.repository.getByName(name);
    }
}