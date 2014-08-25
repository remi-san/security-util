package net.remisan.security.controllers.errors;

import javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.remisan.base.controller.BaseController;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Controller
public class ErrorController extends BaseController {

    @ResponseStatus(value = HttpStatus.FORBIDDEN) 
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView accessDeniedException(HttpServletRequest request, HttpServletResponse response) {
        return this.getModelAndView("error/forbidden", request, response);
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND) 
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFoundException(HttpServletRequest request, HttpServletResponse response) {
        return this.getModelAndView("error/404", request, response);
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @RequestMapping(value = "/404.html", headers = "Accept=text/html")
    public ModelAndView pageNotFoundException(HttpServletRequest request, HttpServletResponse response) {
        return this.getModelAndView("error/404", request, response);
    }

    // Total control - setup a model and return the view name yourself. Or consider
    // subclassing ExceptionHandlerExceptionResolver (see below).
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) 
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(
        HttpServletRequest request,
        HttpServletResponse response,
        Exception exception
    ) {
        exception.printStackTrace();
        
        ModelAndView mav = this.getModelAndView("error/default", request, response);
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        return mav;
    }
}
