package net.remisan.security.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.remisan.security.model.SecurityRole;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) {

        SecurityUser domainUser = this.userService.getUser(login);

        if (domainUser == null) {
            return null;
        }
        
        return new User(
            domainUser.getLogin(),
            domainUser.getPassword(),
            domainUser.isEnabled(),
            domainUser.getAccountExpirationDate() == null,
            domainUser.getCredentialsExpirationDate() == null,
            domainUser.getLockingDate() == null,
            this.getAuthorities(domainUser.getRoles())
        );
    }

    protected Collection<? extends GrantedAuthority> getAuthorities(Set<SecurityRole> roles) {
        List<GrantedAuthority> authList = getGrantedAuthorities(roles);
        return authList;
    }

    protected static List<GrantedAuthority> getGrantedAuthorities(Set<SecurityRole> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        if (roles != null) {
            for (SecurityRole role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        }

        return authorities;
    }

}
