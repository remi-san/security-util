package net.remisan.security.form;

import net.remisan.base.app.form.PersistableEntityForm;
import net.remisan.security.model.SecurityUser;

public interface PasswordForm extends PersistableEntityForm<SecurityUser> {
    
    String getPlainPassword();
    String getPlainPasswordConfirmation();
}
