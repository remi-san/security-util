package net.remisan.security.util;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.mail.internet.AddressException;
import javax.persistence.EntityNotFoundException;

import net.remisan.security.acl.AclSecurityUtil;
import net.remisan.security.model.SecuredPersistable;
import net.remisan.security.model.SecurityRole;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.permission.PermissionUtil;
import net.remisan.security.permission.SecurityPermission;
import net.remisan.security.service.RoleService;
import net.remisan.security.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;

@SuppressWarnings("deprecation")
@Component
public class UserUtil implements ModelUtil {

    @Autowired
    private AclSecurityUtil aclSecurityUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void preSave(SecuredPersistable obj) {

        SecurityUser user = (SecurityUser) obj;

        if (user.getPlainPassword() != null) {
            user.setPassword(
                this.passwordEncoder.encodePassword(
                    user.getPlainPassword(),
                    user.getLogin()
                )
            );
        }

        if (user.isNew()) {
            SecurityRole userRole = this.roleService.getByName("ROLE_USER");
            
            if (userRole == null) {
                throw new EntityNotFoundException("Role not found");
            }
            
            user.addRole(userRole);
            user.setActivationToken(UUID.randomUUID().toString());
            user.setCreationDate(new Date());
        }
    }

    @Override
    public void postSave(SecuredPersistable obj, boolean newObject) {

        SecurityUser user = (SecurityUser) obj;

        if (newObject) {
            
            Future<Boolean> result = null;
            try {
                result = this.userService.sendInscriptionMessage(user);
            } catch (AddressException e1) {
                e1.printStackTrace();
            }
            
            this.aclSecurityUtil.addPermission(
                user,
                new PrincipalSid(user.getLogin()),
                new SecurityPermission(PermissionUtil.COMPLETE_ADMIN)
            );

            if (result != null) {
                try {
                    result.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
