package net.remisan.security.permission;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;

public class SecurityPermission extends BasePermission {

    static final Permission REPORT = new SecurityPermission(1 << 5, 'O');
    static final Permission AUDIT = new SecurityPermission(1 << 6, 'T');
    
    private static final long serialVersionUID = 6850410861048011498L;

    public SecurityPermission(int mask) {
        super(mask);
    }

    public SecurityPermission(int mask, char code) {
        super(mask, code);
    }
}
