package net.remisan.security.model;

import java.net.URISyntaxException;

import net.remisan.security.model.mock.MockSecurityRole;

import org.junit.Assert;
import org.junit.Test;

public class RoleTest {

    private Long id = new Long(99);
    private String name = "ROLE_1";

    @Test
    public void testConstructors() throws URISyntaxException {

        SecurityRole r = new MockSecurityRole();
        Assert.assertEquals(null, r.getId());
        Assert.assertEquals(null, r.getName());

    }

    @Test
    public void testSettersGetters() {
        SecurityRole r = new MockSecurityRole();
        r.setId(this.id);
        r.setName(this.name);

        Assert.assertEquals(this.id, r.getId());
        Assert.assertEquals(this.name, r.getName());
    }
}
