package net.remisan.security.model;

import java.net.URISyntaxException;
import java.util.Set;

import net.remisan.security.model.mock.MockSecurityRole;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {

    private Long id = new Long(99);
    private String login = "foo";
    private String password = "bar";
    private String email = "foo@bar.com";

    @Test
    public void testConstructors() throws URISyntaxException {

        SecurityUser u = new BaseSecurityUser();
        Assert.assertEquals(null, u.getId());
        Assert.assertEquals(null, u.getLogin());
        Assert.assertEquals(null, u.getPassword());
        Assert.assertEquals(null, u.getEmail());
    }

    @Test
    public void testSettersGetters() {
        SecurityUser u = new BaseSecurityUser();
        u.setId(this.id);
        u.setLogin(this.login);
        u.setEmail(this.email);
        u.setPassword(this.password);

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
