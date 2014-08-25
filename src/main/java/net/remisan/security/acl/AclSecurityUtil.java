/* Copyright (c) 2008, DENKSOFT SRL. All rights reserved.
     This software is licensed under the BSD license available at
     http://www.opensource.org/licenses/bsd-license.php, with these parameters:
     <OWNER> = DENKSOFT SRL <ORGANIZATION> = DENKSOFT SRL <YEAR> = 2008
 */

package net.remisan.security.acl;

import net.remisan.security.model.SecuredPersistable;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

public interface AclSecurityUtil {

    void addPermission(SecuredPersistable securedObject,
            Permission permission);

    void addPermission(SecuredPersistable securedObject, Sid recipient,
            Permission permission);

    void deletePermissions(SecuredPersistable securedObject);
    
    void deletePermissions(SecuredPersistable securedObject, Sid recipient);
    
    void deletePermission(SecuredPersistable securedObject, Permission permission);
    
    void deletePermission(SecuredPersistable securedObject,
            Sid recipient, Permission permission);
}
