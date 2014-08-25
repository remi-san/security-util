package net.remisan.security.repository.mock;

import net.remisan.security.model.SecurityRole;
import net.remisan.security.model.mock.MockSecurityRole;
import net.remisan.security.repository.SecurityRoleRepository;

import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryMock
    extends RepositoryMock<SecurityRole, MockSecurityRole>
    implements SecurityRoleRepository {

    @Override
    public SecurityRole getNewInstance() {
        return new MockSecurityRole();
    }

    @Override
    public SecurityRole getByName(String alias) {
        SecurityRole byAlias = null;
        
        for (SecurityRole role : this.getObjects().values()) {
            if (role.getName() != null && role.getName().equals(alias)) {
                byAlias = role;
                break;
            }
        }
        
        return byAlias;
    }
}
