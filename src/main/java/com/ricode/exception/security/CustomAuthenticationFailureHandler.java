package com.ricode.exception.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //Settea el mensaje de error en la sesión
        request.getSession().setAttribute("error", "Credenciales incorrectas. Por favor, inténtelo nuevamente.");
        //Redirige al usuario a la página de login
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
