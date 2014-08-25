package net.remisan.security.manager;

import net.remisan.base.manager.Manager;
import net.remisan.security.model.SecurityUser;

public interface SecurityUserManager extends Manager<SecurityUser> {

    SecurityUser getByLogin(String login);
    
    SecurityUser getByEmail(String email);
    
    SecurityUser getByActivationToken(String token);
}