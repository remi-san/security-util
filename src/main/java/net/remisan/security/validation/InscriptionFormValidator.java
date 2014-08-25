package net.remisan.security.validation;

import net.remisan.security.form.InscriptionForm;
import net.remisan.security.validation.util.UserValidationHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class InscriptionFormValidator implements Validator {

    @Autowired
    private UserValidationHelper userValidationHelper;

    @Override
    public boolean supports(Class<?> clazz) {
        return InscriptionForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        InscriptionForm form = (InscriptionForm) obj;
        this.userValidationHelper.validateEmail(errors, form.getId(), form.getEmail());
        this.userValidationHelper.validateLogin(errors, form.getId(), form.getLogin());
    }
}