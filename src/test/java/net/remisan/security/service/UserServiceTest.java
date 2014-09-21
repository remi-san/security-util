package net.remisan.security.service;

import java.util.UUID;

import javax.transaction.Transactional;

import net.remisan.security.model.BaseSecurityUser;
import net.remisan.security.model.SecurityRole;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.model.mock.MockSecurityRole;
import net.remisan.security.repository.SecurityRoleRepository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.BindException;

@ContextConfiguration("file:src/test/resources/root-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UserServiceTest {

    @Autowired
    private SecurityUserService service;
    
    @Autowired
    private SecurityRoleRepository roleRepository;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Before
    public void init() throws BindException {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "pwd", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        SecurityRole role = new MockSecurityRole();
        role.setName("ROLE_USER");
        this.roleRepository.save(role);
    }
    
    @After
    public void tearDown() {
        this.roleRepository.deleteAll();
    }
    
    @Test
    public void test() {
        TransactionTemplate tt = new TransactionTemplate(this.transactionManager);
        tt.execute(new TransactionCallbackWithoutResult() {
            
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                
                try {
                    UserServiceTest.this.testInTransaction();
                } catch (BindException e) {
                    Assert.assertTrue(false);
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    Assert.assertTrue(false);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Assert.assertTrue(false);
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void testInTransaction() throws BindException, InstantiationException, IllegalAccessException {
        
        // Instantiate
        SecurityUser user = this.service.createNewUser();
        Assert.assertNotNull(user);
        Assert.assertEquals(BaseSecurityUser.class, user.getClass());
        
        user = this.service.createNewUser("john@doe.com", "john", "pwd");
        Assert.assertNotNull(user);
        Assert.assertNull(user.getId());
        Assert.assertEquals(BaseSecurityUser.class, user.getClass());
        Assert.assertEquals("john@doe.com", user.getEmail());
        Assert.assertEquals("john", user.getLogin());
        Assert.assertEquals("pwd", user.getPassword());
        
        SecurityUser persistableUser = this.service.getPersistableUser(user);
        Assert.assertEquals(user.getLogin(), persistableUser.getLogin());
        
        user.setActivationToken(UUID.randomUUID().toString());
        
        // Create
        this.service.save(user);
        Assert.assertNotNull(user.getId());
        
        // Select
        SecurityUser userGet = this.service.getById(user.getId());
        Assert.assertEquals(user, userGet);
        
        SecurityUser userEmail = this.service.getUserByEmail(user.getEmail());
        Assert.assertEquals(user, userEmail);
        
        SecurityUser userLogin = this.service.getUser(user.getLogin());
        Assert.assertEquals(user, userLogin);
        
        // Validation
        Assert.assertFalse(user.isEnabled());
        
        this.service.validateUser(user.getActivationToken());
        Assert.assertTrue(user.isEnabled());
    }
    
}
