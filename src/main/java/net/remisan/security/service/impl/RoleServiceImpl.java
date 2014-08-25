package net.remisan.security.service.impl;

import net.remisan.base.manager.Manager;
import net.remisan.base.service.AbstractService;
import net.remisan.security.manager.SecurityRoleManager;
import net.remisan.security.model.SecurityRole;
import net.remisan.security.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl
    extends AbstractService<SecurityRole>
    implements RoleService {

    @Autowired
    private PermissionEvaluator permissionEvaluator;
    
    @Autowired
    private SecurityRoleManager manager;

    @Override
    protected Manager<SecurityRole> getManager() {
        return this.manager;
    }
    
    @Override
    //@PostAuthorize("hasPermission(returnObject, 'WRITE') or hasRole('ROLE_ADMIN')")
    public SecurityRole getById(Long id) {
        return super.getById(id);
    }

    @Override
    public SecurityRole getByName(String name) {
        return this.manager.getByName(name);
    }
}
