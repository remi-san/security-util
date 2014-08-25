package net.remisan.security.acl;

import javax.transaction.Transactional;

import net.remisan.security.model.BaseSecurityUser;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.permission.AclPermissionEvaluator;
import net.remisan.security.permission.PermissionUtil;
import net.remisan.security.permission.SecurityPermission;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@ContextConfiguration("file:src/test/resources/root-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AclSecurityUtilTest {

    @Autowired
    private AclSecurityUtil aclSecurityUtil;
    
    @Autowired
    private AclPermissionEvaluator permissionEvaluator;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    private Authentication authentication;
    
    private SecurityUser user;
    
    private Permission permission;
    
    private String strPermission;
    
    private Integer intPermission;
    
    @Before
    public void init() {
        this.authentication = new UsernamePasswordAuthenticationToken("user", "pwd", null);
        SecurityContextHolder.getContext().setAuthentication(this.authentication);
        
        this.user = new BaseSecurityUser();
        this.user.setId(1L);
        this.user.setLogin("john");
        
        this.intPermission = PermissionUtil.ADMINISTRATION;
        this.strPermission = "ADMINISTRATION";
        
        this.permission = new SecurityPermission(this.intPermission);
    }
    
    @Test
    public void testAcl() {
        
        TransactionTemplate tt = new TransactionTemplate(this.transactionManager);
        tt.execute(new TransactionCallbackWithoutResult() {
            
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                
                //Clean
                AclSecurityUtilTest.this.clean();
                
                // Create
                AclSecurityUtilTest.this.testNewPermission();
                
                // Update
                AclSecurityUtilTest.this.testUpdatePermission();
                
                // Delete
                AclSecurityUtilTest.this.testDeletePermission();
            }
        });
    }
    
    @Test
    public void testPermissionEvaluator() {
        this.permissionEvaluator.setObjectIdentityRetrievalStrategy(new ObjectIdentityRetrievalStrategyImpl());
        this.permissionEvaluator.setObjectIdentityGenerator(new ObjectIdentityRetrievalStrategyImpl());
        this.permissionEvaluator.setSidRetrievalStrategy(new SidRetrievalStrategyImpl());
        
        Assert.assertFalse(this.permissionEvaluator.hasPermission(this.authentication, null, this.permission));
        
        Assert.assertFalse(
            this.permissionEvaluator.hasPermission(
                this.authentication,
                -1L,
                this.user.getClass().getCanonicalName(),
                this.permission
            )
        );
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        this.permissionEvaluator.hasPermission(this.authentication, this.user, null);
    }
    
    private void clean() {
        this.aclSecurityUtil.deletePermissions(this.user);
    }
    
    private void testNewPermission() {
        
        Assert.assertFalse(this.permissionEvaluator.hasPermission(this.authentication, this.user, this.permission));
        
        this.aclSecurityUtil.addPermission(this.user, this.permission);
        
        Assert.assertTrue(this.permissionEvaluator.hasPermission(this.authentication, this.user, this.permission));
        Assert.assertTrue(
            this.permissionEvaluator.hasPermission(
                this.authentication,
                this.user.getId(),
                this.user.getClass().getCanonicalName(),
                this.permission
            )
        );
    }
    
    private void testUpdatePermission() {
        Assert.assertTrue(this.permissionEvaluator.hasPermission(this.authentication, this.user, this.permission));
        
        this.aclSecurityUtil.addPermission(this.user, this.permission);
        
        Assert.assertTrue(this.permissionEvaluator.hasPermission(this.authentication, this.user, this.permission));
        Assert.assertTrue(this.permissionEvaluator.hasPermission(this.authentication, this.user, this.intPermission));
        Assert.assertTrue(this.permissionEvaluator.hasPermission(this.authentication, this.user, this.strPermission));
        
        Permission[] permissions = new Permission[] {this.permission};
        Assert.assertTrue(this.permissionEvaluator.hasPermission(this.authentication, this.user, permissions));
    }
    
    private void testDeletePermission() {
        this.aclSecurityUtil.deletePermission(this.user, this.permission);
        
        Assert.assertFalse(this.permissionEvaluator.hasPermission(this.authentication, this.user, this.permission));
    }
}
