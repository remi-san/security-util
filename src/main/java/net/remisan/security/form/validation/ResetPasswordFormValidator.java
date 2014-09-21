package net.remisan.security.form.validation;

import net.remisan.base.model.validation.util.UserValidationHelper;
import net.remisan.security.form.PasswordForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ResetPasswordFormValidator implements Validator {

    @Autowired
    private UserValidationHelper userValidationHelper;

    @Override
    public boolean supports(Class<?> clazz) {
        return PasswordForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        PasswordForm form = (PasswordForm) obj;
        this.userValidationHelper.validatePassword(
            errors, form.getPlainPassword(), form.getPlainPasswordConfirmation());
    }
}