package net.remisan.security.controllers.security;

import java.security.InvalidKeyException;
import java.util.concurrent.ExecutionException;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.remisan.base.controller.BaseController;
import net.remisan.security.form.BaseInscriptionForm;
import net.remisan.security.form.BasePasswordForm;
import net.remisan.security.form.InscriptionForm;
import net.remisan.security.model.SecurityUser;
import net.remisan.security.service.UserService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

public class SecurityNavigation extends BaseController {

    @Autowired
    protected UserService userService;

    @Autowired
    @Qualifier("formValidator")
    protected Validator validator;

    /**
     * DataBinder is only used for @ModelAttribute
     * 
     * @param binder WebDataBinder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(this.validator);
    }

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) {
        return this.getModelAndView("login/login-form", request, response);
    }

    @RequestMapping(value = "/retry-login.html", method = RequestMethod.GET)
    public ModelAndView invalidLogin(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = this.getModelAndView("login/login-form", request, response);
        modelAndView.addObject("error", true);
        return modelAndView;
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @RequestMapping(value = "/forbidden.html", method = RequestMethod.GET)
    public ModelAndView forbidden(HttpServletRequest request, HttpServletResponse response) {
        return this.getModelAndView("error/forbidden", request, response);
    }

    @RequestMapping(value = "/subscribe.html", method = RequestMethod.GET)
    public ModelAndView inscription(HttpServletRequest request, HttpServletResponse response)
        throws InstantiationException, IllegalAccessException {
        BaseInscriptionForm user = new BaseInscriptionForm();

        ModelAndView modelAndView = this.getModelAndView("login/inscription", request, response);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    public ModelAndView inscriptionValidation(
        InscriptionForm user,
        BindingResult result,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws BindException, InstantiationException, IllegalAccessException {
        
        if (result.hasErrors()) {
            ModelAndView modelAndView = this.getModelAndView("login/inscription", request, response);
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        
        SecurityUser persistableUser = this.userService.getNewInstance();
        user.updateObject(persistableUser);
        this.userService.save(persistableUser);

        return this.getModelAndView("redirect:/", request, response);
    }
    
    @RequestMapping(value = "/lost-password.html", method = RequestMethod.GET)
    public ModelAndView lostPassword(HttpServletRequest request, HttpServletResponse response) {
        return this.getModelAndView("login/lost", request, response);
    }
    
    @Transactional
    @RequestMapping(value = "/lost-password.html", method = RequestMethod.POST)
    public ModelAndView sendPassword(
        @RequestParam("email") String email,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AddressException, InterruptedException, ExecutionException, BindException {
        
        SecurityUser user = this.userService.getUserByEmail(email);
        
        if (user != null) {
            this.userService.sendResetPasswordMessage(user);
            
            return this.getModelAndView("login/password-sent", request, response);
        } else {
            ModelAndView modelAndView = this.getModelAndView("login/lost", request, response);
            modelAndView.addObject("error", true);
            return modelAndView;
        }
    }

    @RequestMapping(
        value = "/validate.html", method = RequestMethod.GET, params = { "token" })
    public ModelAndView validate(
        @RequestParam(value = "token") String token,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws BindException {
        
        this.userService.validateUser(token);
        
        ModelAndView modelAndView = this.getModelAndView("login/login-form", request, response);
        modelAndView.addObject("ok", true);
        modelAndView.addObject("message", "Your account is now active! Please login.");
        return modelAndView;
    }
    
    @RequestMapping(
        value = "/reset-password.html", method = RequestMethod.GET, params = { "reset" })
    public ModelAndView resetForm(
        @RequestParam(value = "reset") String token,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws InvalidKeyException {
        SecurityUser user = this.userService.getUserByToken(token);
        
        if (user == null) {
            throw new InvalidKeyException();
        }

        ModelAndView modelAndView = this.getModelAndView("login/reset-form", request, response);
        modelAndView.addObject("user", user);
        return modelAndView;
    }
    
    @Transactional
    @RequestMapping(value = "/reset-password.html", method = RequestMethod.POST)
    public ModelAndView resetPassword(
        @ModelAttribute(value = "user") @Valid BasePasswordForm form,
        BindingResult result,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws BindException, InstantiationException, IllegalAccessException {
        
        if (result.hasErrors()) {
            ModelAndView modelAndView = this.getModelAndView("login/reset-form", request, response);
            modelAndView.addObject("user", form);
            return modelAndView;
        }
        
        Long id = form.getId();
        SecurityUser user = this.userService.getByIdEager(id);
        BeanUtils.copyProperties(form, user);
        user.setActivationToken(null);
        this.userService.save(user);

        ModelAndView modelAndView = this.getModelAndView("login/login-form", request, response);
        modelAndView.addObject("ok", true);
        modelAndView.addObject("message", "Password changed successfully! Please login.");
        return modelAndView;
    }
}
