package net.remisan.security.controllers.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.remisan.base.controller.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/admin", headers = "Accept=text/html")
public class AdminBaseController extends BaseController {
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView indexAdminPage(HttpServletRequest request, HttpServletResponse response) {
        return this.getModelAndView("admin/pages/home", request, response);
    }
}
