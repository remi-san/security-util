package net.remisan.security.permission;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityGenerator;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;

public class AclPermissionEvaluator implements PermissionEvaluator {

    private final Log logger = LogFactory.getLog(this.getClass());

    private final AclService aclService;
    private ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy = new ObjectIdentityRetrievalStrategyImpl();
    private ObjectIdentityGenerator objectIdentityGenerator = new ObjectIdentityRetrievalStrategyImpl();
    private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();
    private PermissionFactory permissionFactory = new DefaultPermissionFactory();

    public AclPermissionEvaluator(AclService aclService) {
        this.aclService = aclService;
    }

    /**
     * Determines whether the user has the given permission(s) on the domain
     * object using the ACL configuration. If the domain object is null, returns
     * false (this can always be overridden using a null check in the expression
     * itself).
     */
    @Override
    public boolean hasPermission(Authentication authentication,
            Object domainObject, Object permission) {
        if (domainObject == null) {
            return false;
        }

        ObjectIdentity objectIdentity = this.objectIdentityRetrievalStrategy
                .getObjectIdentity(domainObject);

        return this.checkPermission(authentication, objectIdentity, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication,
            Serializable targetId, String targetType, Object permission) {
        ObjectIdentity objectIdentity = this.objectIdentityGenerator
                .createObjectIdentity(targetId, targetType);

        return this.checkPermission(authentication, objectIdentity, permission);
    }

    private boolean checkPermission(Authentication authentication,
            ObjectIdentity oid, Object permission) {
        // Obtain the SIDs applicable to the principal
        List<Sid> sids = this.sidRetrievalStrategy.getSids(authentication);
        List<Permission> requiredPermission = this
                .resolvePermission(permission);

        final boolean debug = this.logger.isDebugEnabled();

        if (debug) {
            this.logger.debug("Checking permission '" + permission
                    + "' for object '" + oid + "'");
        }

        try {
            // Lookup only ACLs for SIDs we're interested in
            Acl acl = this.aclService.readAclById(oid, sids);

            return this.isGranted(acl, requiredPermission, debug);

        } catch (NotFoundException nfe) {
            if (debug) {
                this.logger
                        .debug("Returning false - no ACLs apply for this principal");
            }
        }

        return false;

    }

    List<Permission> resolvePermission(Object permission) {
        if (permission instanceof Integer) {
            return Arrays.asList(this.permissionFactory
                    .buildFromMask(((Integer) permission).intValue()));
        }

        if (permission instanceof Permission) {
            return Arrays.asList((Permission) permission);
        }

        if (permission instanceof Permission[]) {
            return Arrays.asList((Permission[]) permission);
        }

        if (permission instanceof String) {
            String permString = (String) permission;
            Permission p;

            try {
                p = this.permissionFactory.buildFromName(permString);
            } catch (IllegalArgumentException notfound) {
                p = this.permissionFactory.buildFromName(permString.toUpperCase());
            }

            if (p != null) {
                return Arrays.asList(p);
            }

        }
        throw new IllegalArgumentException("Unsupported permission: " + permission);
    }

    public void setObjectIdentityRetrievalStrategy(
            ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy) {
        this.objectIdentityRetrievalStrategy = objectIdentityRetrievalStrategy;
    }

    public void setObjectIdentityGenerator(
            ObjectIdentityGenerator objectIdentityGenerator) {
        this.objectIdentityGenerator = objectIdentityGenerator;
    }

    public void setSidRetrievalStrategy(
            SidRetrievalStrategy sidRetrievalStrategy) {
        this.sidRetrievalStrategy = sidRetrievalStrategy;
    }

    public void setPermissionFactory(PermissionFactory permissionFactory) {
        this.permissionFactory = permissionFactory;
    }

    protected boolean isGranted(Acl acl, List<Permission> requiredPermission, boolean debug) {

        if (acl == null) {
            return false;
        }

        if (requiredPermission == null || requiredPermission.size() == 0) {
            return true;
        }

        List<AccessControlEntry> entries = acl.getEntries();
        for (AccessControlEntry entry : entries) {
            for (Permission perm : requiredPermission) {
                Permission localPermission = entry.getPermission();
                int permissionMask = localPermission.getMask();
                if ((permissionMask | perm.getMask()) == permissionMask) {
                    if (debug) {
                        this.logger.debug("Access is granted");
                    }

                    return true;
                }
            }
        }

        // If access hasn't been granted, we check if the acl has a parent we
        // can check
        return this.isGranted(acl.getParentAcl(), requiredPermission, debug);
    }
}
