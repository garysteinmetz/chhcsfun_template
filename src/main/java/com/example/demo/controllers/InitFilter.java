package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
//@WebFilter(urlPatterns = "/happy.html")
public class InitFilter implements Filter {
    @Value("${aws.cognito.oauth2.authorize.url}")
    String loginUrl;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Cookie loginUrlCookie = new Cookie("loginUrl", loginUrl);
        ((HttpServletResponse)servletResponse).addCookie(loginUrlCookie);
        Optional<UserSession> userSessionOptional =
                UserSession.getSession(((HttpServletRequest)servletRequest).getSession(false));
        //if (userSessionOptional.isPresent()) {
        //    Cookie userDisplayNameCookie = new Cookie("userDisplayName", userSessionOptional.get().getName());
        //    ((HttpServletResponse)servletResponse).addCookie(userDisplayNameCookie);
        //    //Cookie loginUrlCookie = new Cookie("loginUrl", loginUrl);
        //    //((HttpServletResponse)servletResponse).addCookie(loginUrlCookie);
        //}
        //Note - It seems like Cookies must be set Before 'doFilter()' is called, otherwise they can't be set
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
