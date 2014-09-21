package net.remisan.security.validation;

import javax.transaction.Transactional;

import org.junit.Assert;

import net.remisan.base.model.validation.UserValidator;
import net.remisan.security.model.BaseSecurityUser;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.model.mock.MockSecurityRole;
import net.remisan.security.service.SecurityUserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindException;

@ContextConfiguration("file:src/test/resources/root-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UserValidatorTest {

    @Autowired
    private UserValidator validator;
    
    @Autowired
    private SecurityUserService service;
    
    private SecurityUser user;
    
    @Before
    public void init() throws BindException {
        this.user = new BaseSecurityUser();
        this.user.setId(1L);
        this.user.setEmail("johnny@doe.com");
        this.user.setLogin("johnny.doe");
        
        this.service.save(this.user);
    }
    
    @Test
    public void testSupport() {
        Assert.assertTrue(this.validator.supports(SecurityUser.class));
        Assert.assertTrue(this.validator.supports(BaseSecurityUser.class));
        Assert.assertFalse(this.validator.supports(MockSecurityRole.class));
    }
    
    @Test
    public void testValidation() {
        
        BindException errors = new BindException(this.user, this.user.getClass().getCanonicalName());
        this.validator.validate(this.user, errors);
        
        Assert.assertFalse(errors.hasErrors());
        
        SecurityUser doppleganger = new BaseSecurityUser();
        doppleganger.setId(2L);
        doppleganger.setEmail("johnny@doe.com");
        doppleganger.setLogin("johnny.doe");
        
        this.validator.validate(doppleganger, errors);
        
        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(2, errors.getErrorCount());
    }
}
