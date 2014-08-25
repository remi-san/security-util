package net.remisan.security.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


public class BaseInscriptionForm extends BasePasswordForm implements InscriptionForm {
    
    @NotNull
    @NotEmpty
    private String login;
    
    @NotNull
    @NotEmpty
    @Email
    private String email;
    
    @Override
    public Long getId() {
        return null;
    }
    
    @Override
    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
