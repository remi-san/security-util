package net.remisan.security.repository;

import java.util.List;

import net.remisan.security.model.SecurityRole;
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
public class RoleRepositoryTest {
    
    @Autowired
    private SecurityRoleRepository roleRepository;
    
    @Test
    public void testNotFound() {
        SecurityRole role = this.roleRepository.findOne(99L);
        Assert.assertNull(role);
    }
    
    @Test
    public void testInstance() throws InstantiationException, IllegalAccessException {
        SecurityRole role = this.roleRepository.getNewInstance();
        Assert.assertEquals(MockSecurityRole.class, role.getClass());
    }
    
    @Test
    public void testInsertSelectUpdateDelete() throws Exception {
        
        String name = "ROLE_1";
        
        // Insert
        SecurityRole role = new MockSecurityRole();
        role.setName(name);

        this.roleRepository.save(role);
        Long roleId = role.getId();

        SecurityRole role2 = new MockSecurityRole();
        role2.setName("ROLE_2");
        this.roleRepository.save(role2);

        SecurityRole paris = this.roleRepository.findOne(roleId);
        Assert.assertEquals(roleId, paris.getId());

        // Select
        SecurityRole roleByName = this.roleRepository.getByName(name);
        Assert.assertEquals(role, roleByName);
        
        SecurityRole roleByNameNull = this.roleRepository.getByName("toto");
        Assert.assertNull(roleByNameNull);

        // Update
        String roleBis = "ROLE_R";
        Long role2Id = role2.getId();
        role2.setName(roleBis);
        this.roleRepository.merge(role2);
        Assert.assertEquals(role2Id, role2.getId());
        
        SecurityRole r2 = this.roleRepository.findOne(role2.getId());
        Assert.assertEquals(roleBis, r2.getName());
        Assert.assertEquals(role2, r2);

        // Delete
        this.roleRepository.delete(role2);
        Assert.assertNull(this.roleRepository.findOne(role2Id));
        
        List<SecurityRole> roles = (List<SecurityRole>) this.roleRepository.findAll();
        Assert.assertEquals(1, roles.size());
        
        this.roleRepository.flush();
    }
}
