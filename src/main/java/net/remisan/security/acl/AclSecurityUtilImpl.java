package net.remisan.security.acl;

import java.util.ArrayList;
import java.util.List;

import net.remisan.security.model.SecuredPersistable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AclSecurityUtilImpl implements AclSecurityUtil {

    private static Logger logger = Logger.getLogger(AclSecurityUtil.class);

    @Autowired
    private MutableAclService mutableAclService;

    @Override
    public void addPermission(SecuredPersistable secureObject, Permission permission) {
        this.addPermission(secureObject, new PrincipalSid(this.getUsername()),  permission);
    }

    @Override
    public void addPermission(SecuredPersistable securedObject, Sid recipient, Permission permission) {
        MutableAcl acl;
        ObjectIdentity oid = new ObjectIdentityImpl(securedObject.getClass().getCanonicalName(), securedObject.getId());

        logger.debug(securedObject.getClass().getCanonicalName() + " : " + securedObject.getId());

        try {
            List<Sid> sids = new ArrayList<Sid>();
            sids.add(recipient);
            acl = (MutableAcl) this.mutableAclService.readAclById(oid, sids);
        } catch (NotFoundException nfe) {
            acl = this.mutableAclService.createAcl(oid);
        }

        List<AccessControlEntry> aces = acl.getEntries();
        int aceIndex = -1;
        for (int i = 0; i < aces.size(); i++) {
            AccessControlEntry ace = aces.get(i);
            if (ace.getSid().equals(recipient)) {
                aceIndex = i;
                break;
            }
        }

        if (aceIndex >= 0) {
            acl.updateAce(aceIndex, permission);
        } else {
            acl.insertAce(acl.getEntries().size(), permission, recipient, true);
        }

        this.mutableAclService.updateAcl(acl);

        logger.debug("Added permission " + permission + " for Sid " + recipient + " securedObject " + securedObject);
    }
    
    @Override
    public void deletePermissions(SecuredPersistable securedObject) {
        this.deletePermissions(securedObject, new PrincipalSid(this.getUsername()));
    }
    
    @Override
    public void deletePermissions(SecuredPersistable securedObject, Sid recipient) {
        this.deletePermission(securedObject, recipient, null);
    }
    
    @Override
    public void deletePermission(SecuredPersistable securedObject, Permission permission) {
        this.deletePermission(securedObject, new PrincipalSid(this.getUsername()),  permission);
    }
    
    @Override
    public void deletePermission(SecuredPersistable securedObject,
            Sid recipient, Permission permission) {
        ObjectIdentity oid = new ObjectIdentityImpl(securedObject.getClass()
                .getCanonicalName(), securedObject.getId());
        MutableAcl acl = (MutableAcl) this.mutableAclService.readAclById(oid);

        // Remove all permissions associated with this particular recipient
        // (string equality to KISS)
        List<AccessControlEntry> entries = acl.getEntries();

        for (int i = 0; i < entries.size(); i++) {
            if (
                permission == null
                    || (entries.get(i).getSid().equals(recipient)  && entries.get(i).getPermission().equals(permission))
            ) {
                acl.deleteAce(i);
            }
        }

        this.mutableAclService.updateAcl(acl);

        logger.debug("Deleted securedObject " + securedObject + " ACL permissions for recipient " + recipient);
    }

    protected String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        } else {
            return auth.getPrincipal().toString();
        }
    }
}
