package net.remisan.security.service;

import javax.transaction.Transactional;

import net.remisan.security.model.BaseSecurityUser;
import net.remisan.security.model.SecurityRole;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.model.mock.MockSecurityRole;
import net.remisan.security.repository.mock.UserRepositoryMock;
import net.remisan.security.service.impl.CustomUserDetailsService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("file:src/test/resources/root-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService service;
    
    @Autowired
    private UserRepositoryMock repository;
    
    private String login = "user";
    
    @Before
    public void init() {
        SecurityUser user = new BaseSecurityUser();
        user.setLogin(this.login);
        user.setPassword("pwd");
        
        SecurityRole role = new MockSecurityRole();
        role.setId(1L);
        role.setName("ROLE_TEST");
        
        user.addRole(role);
        
        this.repository.save(user);
    }
    
    @Test
    public void testLoadByUsername() {
        UserDetails user = this.service.loadUserByUsername(this.login);
        Assert.assertEquals(this.login, user.getUsername());
        
        UserDetails user2 = this.service.loadUserByUsername("u");
        Assert.assertNull(user2);
    }
}
