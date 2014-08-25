package net.remisan.security.form;

import javax.validation.constraints.NotNull;

import net.remisan.base.app.form.AbstractForm;
import net.remisan.security.model.SecurityUser;

import org.hibernate.validator.constraints.NotEmpty;


public class BasePasswordForm extends AbstractForm<SecurityUser> implements PasswordForm {
    
    private Long id;
    
    @NotNull
    @NotEmpty
    private String plainPassword;
    
    @NotNull
    @NotEmpty
    private String plainPasswordConfirmation;

    /**
     * Constructor
     */
    public BasePasswordForm() {
        super(SecurityUser.class);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getPlainPassword() {
        return this.plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }
    
    @Override
    public String getPlainPasswordConfirmation() {
        return this.plainPasswordConfirmation;
    }

    public void setPlainPasswordConfirmation(String plainPasswordConfirmation) {
        this.plainPasswordConfirmation = plainPasswordConfirmation;
    }
}
