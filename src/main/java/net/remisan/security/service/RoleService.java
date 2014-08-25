package net.remisan.security.service;

import net.remisan.base.service.Service;
import net.remisan.security.model.SecurityRole;

public interface RoleService extends Service<SecurityRole> {

    SecurityRole getByName(String name);
}
