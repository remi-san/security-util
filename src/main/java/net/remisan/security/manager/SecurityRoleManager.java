package net.remisan.security.manager;

import net.remisan.base.manager.Manager;
import net.remisan.security.model.SecurityRole;

public interface SecurityRoleManager extends Manager<SecurityRole> {

    SecurityRole getByName(String name);
}