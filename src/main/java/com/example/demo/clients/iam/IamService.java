package com.example.demo.clients.iam;

import com.example.demo.controllers.UserSession;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface IamService {
    void logout();
    String processLoginRequest(String url, HttpSession httpSession);
    String processOauthTwoCallback(String code, String redirectPath,
            HttpServletRequest request, HttpServletResponse httpResponse, HttpSession httpSession);
    UserSession getUserSession();
}
