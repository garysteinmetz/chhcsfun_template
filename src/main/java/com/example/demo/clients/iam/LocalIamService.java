package com.example.demo.clients.iam;

import com.example.demo.controllers.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LocalIamService implements IamService {
    @Override
    public void logout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String processLoginRequest(String url) {
        StringBuilder sb = new StringBuilder("redirect:");
        sb.append(url);
        return sb.toString();
    }

    @Override
    public String processOauthTwoCallback(String code, String redirectPath,
                                          HttpServletRequest request, HttpServletResponse httpResponse,
                                          HttpSession httpSession) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserSession getUserSession() {
        throw new UnsupportedOperationException();
    }
}
