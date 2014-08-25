package net.remisan.security.repository;

import java.util.List;
import java.util.Set;

import net.remisan.security.model.BaseSecurityUser;
import net.remisan.security.model.SecurityRole;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.model.mock.MockSecurityRole;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("file:src/test/resources/root-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UserRepositoryTest {
    
    @Autowired
    private SecurityUserRepository userRepository;
    
    @Test
    public void testNotFound() {
        SecurityUser user = this.userRepository.findOne(99L);
        Assert.assertNull(user);
    }
    
    @Test
    public void testInstance() throws InstantiationException, IllegalAccessException {
        SecurityUser user = this.userRepository.getNewInstance();
        Assert.assertEquals(BaseSecurityUser.class, user.getClass());
    }
    
    @Test
    public void testInsertSelectUpdateDelete() throws Exception {
        
        String login = "user1";
        String email = "user1@dummy.com";
        String password = "pwd";
        String token = "azertyuiop";
        
        // Insert
        SecurityUser user = new BaseSecurityUser();
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);
        user.setActivationToken(token);

        this.userRepository.save(user);
        Long userId = user.getId();

        SecurityUser user2 = new BaseSecurityUser();
        user2.setLogin("user2");
        user2.setEmail("user2@dummy.com");
        user2.setPassword(password);
        
        this.userRepository.save(user2);

        SecurityUser paris = this.userRepository.findOne(userId);
        Assert.assertEquals(userId, paris.getId());

        // Select
        SecurityUser userCopy = this.userRepository.findOne(user.getId());
        Assert.assertEquals(user, userCopy);
        
        SecurityUser userByName = this.userRepository.getByLogin(login);
        Assert.assertEquals(user, userByName);
        
        SecurityUser userByNameNull = this.userRepository.getByLogin("toto");
        Assert.assertNull(userByNameNull);
        
        SecurityUser userByEmail = this.userRepository.getByEmail(email);
        Assert.assertEquals(user, userByEmail);
        
        SecurityUser userByToken = this.userRepository.getByActivationToken(token);
        Assert.assertEquals(user, userByToken);

        // Update
        String userBis = "userBis";
        Long user2Id = user2.getId();
        user2.setLogin(userBis);
        this.userRepository.merge(user2);
        Assert.assertEquals(user2Id, user2.getId());
        
        SecurityUser r2 = this.userRepository.findOne(user2.getId());
        Assert.assertEquals(userBis, r2.getLogin());
        Assert.assertEquals(user2, r2);

        // Delete
        this.userRepository.delete(user2);
        Assert.assertNull(this.userRepository.findOne(user2Id));
        
        List<SecurityUser> users = (List<SecurityUser>) this.userRepository.findAll();
        Assert.assertEquals(1, users.size());
        
        this.userRepository.flush();
    }
    
    @Test
    public void testSettersGetters() {
        
        Long id = new Long(99);
        String login = "foo";
        String password = "bar";
        String email = "foo@bar.com";
        
        SecurityUser u = new BaseSecurityUser();
        u.setId(id);
        u.setLogin(login);
        u.setEmail(email);
        u.setPassword(password);
        
        Assert.assertEquals(id, u.getId());
        Assert.assertEquals(login, u.getLogin());
        Assert.assertEquals(email, u.getEmail());
        Assert.assertEquals(password, u.getPassword());

        java.util.Date expirationDate = new java.util.Date();
        u.setAccountExpirationDate(expirationDate);
        Assert.assertEquals(expirationDate, u.getAccountExpirationDate());

        String token = "token";
        u.setActivationToken(token);
        Assert.assertEquals(token, u.getActivationToken());

        java.util.Date creationDate = new java.util.Date();
        u.setCreationDate(creationDate);
        Assert.assertEquals(creationDate, u.getCreationDate());

        u.setCredentialsExpirationDate(expirationDate);
        Assert.assertEquals(expirationDate, u.getCredentialsExpirationDate());

        u.setEnabled(true);
        Assert.assertEquals(true, u.isEnabled());

        java.util.Date lockingDate = new java.util.Date();
        u.setLockingDate(lockingDate);
        Assert.assertEquals(lockingDate, u.getLockingDate());

        String plainPassword = "plain pwd";
        u.setPlainPassword(plainPassword);
        Assert.assertEquals(plainPassword, u.getPlainPassword());

        SecurityRole r = new MockSecurityRole();
        r.setName("ROLE_1");
        u.addRole(r);
        Set<SecurityRole> roles = u.getRoles();
        Assert.assertEquals(1, roles.size());
        Assert.assertTrue(roles.contains(r));

        SecurityRole r2 = new MockSecurityRole();
        r2.setName("ROLE_2");
        roles.add(r2);
        u.setRoles(roles);
        Assert.assertEquals(2, u.getRoles().size());
        Assert.assertTrue(u.getRoles().contains(r));
        Assert.assertTrue(u.getRoles().contains(r2));
    }
}
