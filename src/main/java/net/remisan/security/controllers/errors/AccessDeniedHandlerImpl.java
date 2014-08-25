package net.remisan.security.controllers.errors;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    
    private String errorPage;

    public AccessDeniedHandlerImpl() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.web.access.AccessDeniedHandler#handle(
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse,
     * org.springframework.security.access.AccessDeniedException)
     */
    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException,
            ServletException {

        RequestDispatcher rd = request.getRequestDispatcher(this.errorPage);
        rd.forward(request, response);
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public String getErrorPage() {
        return this.errorPage;
    }

}
