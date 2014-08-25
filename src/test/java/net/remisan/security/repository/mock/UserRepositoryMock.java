package net.remisan.security.repository.mock;

import net.remisan.security.model.BaseSecurityUser;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.repository.SecurityUserRepository;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryMock
    extends RepositoryMock<SecurityUser, BaseSecurityUser>
    implements SecurityUserRepository {

    @Override
    public SecurityUser getNewInstance() {
        return new BaseSecurityUser();
    }

    @Override
    public SecurityUser getByLogin(String login) {
        
        SecurityUser byLogin = null;
        
        for (SecurityUser user : this.getObjects().values()) {
            if (user.getLogin() != null && user.getLogin().equals(login)) {
                byLogin = user;
                break;
            }
        }
        
        return byLogin;
    }
    
    public SecurityUser getPersistable(SecurityUser user) {
        
        SecurityUser persistable = this.getNewInstance();
        persistable.setId(user.getId());
        persistable.setEmail(user.getEmail());
        persistable.setLogin(user.getLogin());
        persistable.setPlainPassword(user.getPlainPassword());
        
        persistable.setActivationToken(user.getActivationToken());
        persistable.setCreationDate(user.getCreationDate());
        persistable.setEnabled(user.isEnabled());
        
        persistable.setAccountExpirationDate(user.getAccountExpirationDate());
        persistable.setCredentialsExpirationDate(user.getCredentialsExpirationDate());
        persistable.setLockingDate(user.getLockingDate());
        
        persistable.setRoles(user.getRoles());
        
        return persistable;
    }

    @Override
    public SecurityUser getByActivationToken(String token) {
        
        SecurityUser byToken = null;
        
        for (SecurityUser user : this.getObjects().values()) {
            if (user.getActivationToken() != null && user.getActivationToken().equals(token)) {
                byToken = user;
                break;
            }
        }
        
        return byToken;
    }

    @Override
    public SecurityUser getByEmail(String email) {
        
        SecurityUser byEmail = null;
        
        for (SecurityUser user : this.getObjects().values()) {
            if (user.getEmail() != null && user.getEmail().equals(email)) {
                byEmail = user;
                break;
            }
        }
        
        return byEmail;
    }

}
