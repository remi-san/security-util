package net.remisan.security.util;

import javax.transaction.Transactional;

import net.remisan.security.model.BaseSecurityUser;
import net.remisan.security.model.SecurityRole;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.model.mock.MockSecurityRole;
import net.remisan.security.permission.AclPermissionEvaluator;
import net.remisan.security.permission.PermissionUtil;
import net.remisan.security.repository.SecurityRoleRepository;
import net.remisan.security.repository.SecurityUserRepository;

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
public class UserUtilTest {
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private AclPermissionEvaluator permissionEvaluator;
    
    @Autowired
    private UserUtil util;
    
    @Autowired
    private SecurityUserRepository userRepository;
    
    @Autowired
    private SecurityRoleRepository roleRepository;
    
    private Authentication authentication;
    
    private SecurityUser user;
    
    @Before
    public void init() throws BindException {
        
        this.authentication = new UsernamePasswordAuthenticationToken("user", "pwd", null);
        SecurityContextHolder.getContext().setAuthentication(this.authentication);
        
        this.user = new BaseSecurityUser();
        this.user.setEmail("john@doe.com");
        this.user.setLogin("john.doe");
        this.user.setPlainPassword("pwd");
        
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
                UserUtilTest.this.testInTransaction();
            }
        });
    }
    
    public void testInTransaction() {
        
        Assert.assertNull(this.user.getId());
        Assert.assertNull(this.user.getPassword());
        Assert.assertNull(this.user.getActivationToken());
        Assert.assertNull(this.user.getCreationDate());
        Assert.assertNull(this.user.getRoles());
        
        this.util.preSave(this.user);
        
        Assert.assertNull(this.user.getId());
        Assert.assertNotNull(this.user.getPassword());
        Assert.assertNotNull(this.user.getActivationToken());
        Assert.assertNotNull(this.user.getCreationDate());
        Assert.assertEquals(1, this.user.getRoles().size());
        
        this.userRepository.save(this.user);
        
        Assert.assertNotNull(this.user.getId());
        
        this.util.postSave(this.user, true);
        
        Assert.assertTrue(
            this.permissionEvaluator.hasPermission(
                this.authentication, this.user, PermissionUtil.ADMINISTRATION
            )
        );
    }
}
