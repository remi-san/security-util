package net.remisan.security.permission;

import org.springframework.security.acls.domain.DefaultPermissionFactory;

public class PermissionFactory extends DefaultPermissionFactory {

    public PermissionFactory() {
        super();
        this.registerPublicPermissions(SecurityPermission.class);
    }
}
